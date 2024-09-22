import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static HashMap<String , Integer> IPRequests = new HashMap<>();
    public static HashMap<String , String> CheckIP = new HashMap<>();
    public static String DBURL = null;
    public static String DBUserName = null;
    public static String DBPassword = null;
    public static HashMap<String , String> CheckMail = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // 加载数据库驱动
        Class.forName("org.postgresql.Driver");
        // 加载配置文件
        JsonObject jsonObject = new JsonObject(JsonObject.getFileString("config.json"));
        DBURL = (String) jsonObject.get("db_url");
        DBUserName = (String) jsonObject.get("db_user");
        DBPassword = (String) jsonObject.get("db_pwd");
        System.out.println(jsonObject.objectObjectTreeMap.toString());

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(11111) , 0);
        httpServer.setExecutor(executorService);
        httpServer.createContext("/login" , new Login());
        httpServer.createContext("/user", new GetUserConfig());
        httpServer.createContext("/search_user/" , new SearchUser());
        httpServer.createContext("/get_check_code" , new GetCheckCode());
        httpServer.createContext("/check_ip_check" , new CheckIPCheck());
        // 以上是老旧的接口，下面是新的接口
        httpServer.createContext("/api/v2/login" ,new LoginV2());
        httpServer.createContext("/api/v2/reg" , new RegV2());
        httpServer.createContext("/api/v2/change_password" , new ChangePassword());
        httpServer.createContext("/api/v2/user_reg_page" , new UserRegPage());
        httpServer.createContext("/api/v1/activation" , new Activation());
        httpServer.createContext("/static" , new StaticResource());
        httpServer.createContext("/api/v2/bind_mail" , new BindMail());
        httpServer.createContext("/api/v2/mail_login", new MailLogin());

        System.out.println("[INFO] BOOT USER_SERVER.");
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 60);
                    IPRequests.clear();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        httpServer.start();
    }

    public static long getFileLastFixTimeAsLong(File file) {
        // 获取文件的最后修改时间
        long lastModifiedMillis = file.lastModified();

        // 将毫秒时间戳转换为 LocalDateTime
        LocalDateTime lastModifiedDateTime = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(lastModifiedMillis),
                ZoneId.systemDefault()
        );

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 将 LocalDateTime 格式化为字符串
        String formattedDateTime = lastModifiedDateTime.format(formatter);

        // 将格式化后的字符串转换为长整数
        return Long.parseLong(formattedDateTime);
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
            return !(re > 5);
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

    public static void sendCORS(HttpExchange httpExchange) {
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
        if (
                str.contains(" ") ||
                str.contains("&") ||
                        str.contains("/") ||
                        str.contains("\\") ||
                        str.contains(";") ||
                        str.contains(",") ||
                        str.contains("'") ||
                        str.contains("\"") ||
                        str.contains("-") ||
                        str.contains("*") ||
                        str.contains("%") ||
                        str.contains("#")
        )
        {
            return false;
        }
        return true;
    }
    public static String GetRealIP(HttpExchange httpExchange) {
        String ip = httpExchange.getRequestHeaders().getFirst("X-Forwarded-For");
        if (ip == null) {
            ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
}