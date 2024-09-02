import com.sun.net.httpserver.*;


import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static HashMap<String , Integer> IPRequests = new HashMap<>();
    public static HashMap<String , String> CheckIP = new HashMap<>();
    public static String DBURL = null;
    public static String DBUserName = null;
    public static String DBPassword = null;

    public static void main(String[] args) throws Exception {
        // 加载数据库驱动
        Class.forName("org.postgresql.Driver");
        // 加载配置文件
        JsonObject jsonObject = new JsonObject(JsonObject.getFileString("config.json"));
        DBURL = (String) jsonObject.get("db_url");
        DBUserName = (String) jsonObject.get("db_user");
        DBPassword = (String) jsonObject.get("db_pwd");

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(11111) , 0);
        httpServer.setExecutor(executorService);
        httpServer.createContext("/reg" , new Reg());
        httpServer.createContext("/login" , new Login());
        httpServer.createContext("/user", new GetUserConfig());
        httpServer.createContext("/search_user/" , new search_user());
        httpServer.createContext("/get_check_code" , new GetCheckCode());
        httpServer.createContext("/check_ip_check" , new CheckIPCheck());
        System.out.println("[INFO] BOOT USER_SERVER.");
        Thread requests = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 60);
                        IPRequests.clear();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        requests.start();
        httpServer.start();
    }

    public static boolean UserLogin(String username , String password) throws Exception {
        if (!AllowString(username) || !AllowString(password)) {
            return false;
        }

        String url = DBURL+"/accounts";

        // 建立连接
        Connection conn = DriverManager.getConnection(url, DBUserName, DBPassword);
        Statement stmt = conn.createStatement();
        String sqlQuery = "SELECT username,password FROM accounts WHERE username='"+username+"' AND password='"+password+"';";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        return rs.next();
    }

    public static boolean IP_Requests(String ip) {
        // 判断一个IP在规定1分钟时间内是否访问了超过 5 次
        if (IPRequests.containsKey(ip)) {
            int re = IPRequests.get(ip) + 1;
            IPRequests.put(ip , re);
            return !(re > 10);
        } else {
            IPRequests.put(ip , 1);
            return true;
        }
    }
    public static void IPBaned(String ip , HttpExchange httpExchange){
        //System.out.println(IPRequests);
        if (!IP_Requests(ip)) {
            LinwinHackerKiller.ToHacker(httpExchange);
        }
    }

    public static String generateRandomString(int length) {
        // 创建一个包含所有大小写字母的字符串
        String characterLibrary = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(); // 用于拼接最终的字符串
        Random random = new Random(); // 用于生成随机数

        for (int i = 0; i < length; i++) {
            // 从characterLibrary中随机选择一个字符
            int index = random.nextInt(characterLibrary.length());
            char randomChar = characterLibrary.charAt(index);
            // 将选中的字符拼接到结果字符串中
            sb.append(randomChar);
        }

        return sb.toString();
    }

    static class search_user implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            sendCORS(httpExchange);
            String response = "";
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String search = split[2].trim();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{\n   \"message\" : [\n");
                File[] files = new File("db").listFiles();
                for (File f : files) {
                    try {
                        String username = f.getName().substring(0 , f.getName().indexOf("-"));
                        if (username.contains(search)) {
                            stringBuilder.append("  \"");
                            stringBuilder.append(username);
                            stringBuilder.append("\",");
                        }
                    }catch (Exception exception) {
                        continue;
                    }
                }
                response = stringBuilder.substring(0 , stringBuilder.toString().length()-1);
                response += "]\n}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e) {
                static_code = 400;
                response = "{\"message\" : \"" + e.getMessage() + "\"}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    public static void sendCORS(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add("Content-Type","application/json");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
    }
    public static String getHttpBody(HttpExchange httpExchange) throws Exception {
        InputStream inputStream = httpExchange.getRequestBody();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        inputStream.close();
        bufferedReader.close();
        return stringBuilder.toString();
    }
    public static boolean AllowString(String str) {
        return !str.contains(" ") &&
                !str.contains("|") &&
                !str.contains("\\") &&
                !str.contains("/") &&
                !str.contains("'") &&
                !str.contains("\""); // not allowed
    }


}
