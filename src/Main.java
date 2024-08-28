import com.sun.net.httpserver.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static HashMap<String , Integer> IPRequests = new HashMap<>();
    public static HashMap<String , String> CheckIP = new HashMap<>();



    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(11111) , 0);
        httpServer.setExecutor(executorService);
        httpServer.createContext("/reg" , new Reg());
        httpServer.createContext("/login" , new Login());
        httpServer.createContext("/search_pdf" , new SearchPDF());
        httpServer.createContext("/writer_update_pdf",new writer_update_pdf());
        httpServer.createContext("/user", new get_user_config());
        httpServer.createContext("/search_user/" , new search_user());
        httpServer.createContext("/get_check_code" , new GetCheckCode());
        httpServer.createContext("/check_ip_check" , new CheckIPCheck());
        httpServer.createContext("/create_apikey" , new CreateLinwinSoftAPIKey());
        httpServer.createContext("/get_user_apikey" , new GetUserApiKey());
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
    static class LoginWithToken implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
            String response = "";
            int static_code = 200;
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            try {
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
                String[] split = stringBuilder.toString().split("\n");
                String username = split[0];
                String password = split[1];
                if (EasyDB.existsDB(username ,password)) {
                    response = "{\"message\" : \"login successful.\"}";
                    static_code = 200;
                } else {
                    static_code = 400;
                    response = "{\"message\" : \"login failed.\"}";
                }
            }catch (Exception e) {
                e.printStackTrace();
                static_code = 500;
                response = "{\"message\" : \"login failed.\"}";
            }
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    static class GetUserApiKey implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String username = split[2].trim();
                String password = split[3].trim();
                if (!EasyDB.existsDB(username,  password)) {
                    static_code = 400;
                    response = "{\"message\" : \"Username or password error.\"}";
                    httpExchange.sendResponseHeaders(static_code , 0);
                    OutputStream outputStream = httpExchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
                TreeMap<String , String> apikey_db = EasyDB.getTreeMap("apikey" , "#114514+1919@810");
                System.out.println(apikey_db);
                for (String i : apikey_db.keySet()) {
                    if (apikey_db.get(i).equals(username)) {
                        response = "{\"message\" : \""+i+"\"}";
                        httpExchange.sendResponseHeaders(static_code , 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        return;
                    }
                }
                throw new Exception("No User ApiKey Found.");
            }catch (Exception e) {
                static_code = 500;
                response = "{\"message\" : \"" + e.getMessage() + "\"}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    static class CreateLinwinSoftAPIKey implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String username = split[2].trim();
                String password = split[3].trim();
                TreeMap<String , String> apikey_db = EasyDB.getTreeMap("apikey" , "#114514+1919@810");
                System.out.println(apikey_db);
                for (String i : apikey_db.values()) {
                    if (i.equals(username)) {
                        static_code = 500;
                        response = "{\"message\" : \"The user only create one apiKey.\"}";
                        httpExchange.sendResponseHeaders(static_code , 0);
                        OutputStream outputStream = httpExchange.getResponseBody();
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        return;
                    }
                }
                if (!EasyDB.existsDB(username , password)) {
                    static_code = 400;
                    response = "{\"message\" : \"Login Failed.\"}";
                } else {
                    String apikey = generateRandomString(16);
                    EasyDB.insertDataToDatabase(apikey , username , "apikey" , "#114514+1919@810");
                    EasyDB.insertDataToDatabase(apikey , "0" , "apikey_type" , "#114514+1919@810");
                    EasyDB.insertDataToDatabase(apikey , "50" , "apikey_tokens" , "#114514+1919@810");
                    response = "{\"message\" : \"Create Successful.\" , \"apikey\" : \""+apikey+"\"}";
                }
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e) {
                response = "{\"message\" : \"" + e.getMessage() + "\"}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    public static void StringToPng(String text, String outputPath) {
        // 设置图片的宽度和高度
        int width = 200;
        int height = 100;

        // 创建一个带有白色背景的图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色为白色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 设置字体颜色为黑色
        g2d.setColor(Color.BLACK);

        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);

        // 计算文本的边界，用于居中显示
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        // 绘制文本
        g2d.drawString(text, x, y);

        // 释放资源
        g2d.dispose();

        // 将图片写入文件
        try {
            ImageIO.write(image, "png", new File(outputPath));
            System.out.println("Image created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class CheckIPCheck implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            boolean isApiKey = false;

            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String checkcode = split[2];
                System.out.println(CheckIP);
                boolean isIPOK = CheckIP.containsKey(httpExchange.getRemoteAddress().getAddress().toString());
                if (isIPOK) {
                    if (!CheckIP.get(httpExchange.getRemoteAddress().getAddress().toString()).equals(checkcode)) {
                        throw new Exception("Check Code Error.");
                    }
                    else {
                        CheckIP.remove(httpExchange.getRemoteAddress().getAddress().toString());
                        throw new Exception("ok");
                    }
                }
                else if (EasyDB.getTreeMap("apikey" , "#114514+1919@810").containsKey(checkcode)) {
                    isApiKey = true;
                    throw new Exception("ok");
                }
                else {
                    throw new Exception("No Check Code.");
                }
            }catch (Exception e) {
                if (isApiKey) {
                    response = "{\"message\" : \"" + e.getMessage() + "\" , \"is_api\" : true}";
                } else {
                    response = "{\"message\" : \"" + e.getMessage() + "\"}";
                }
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    static class GetCheckCode implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
            String response = "";
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            Random random = new Random();
            int randomNumber = 10000 + random.nextInt(65500 - 10000 + 1);
            StringToPng(String.valueOf(randomNumber) , "check_code/"+String.valueOf(randomNumber)+".png");
            CheckIP.put(httpExchange.getRemoteAddress().getAddress().toString() , String.valueOf(randomNumber));
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            InputStream inputStream = new FileInputStream("check_code/"+String.valueOf(randomNumber)+".png");
            int length = -1;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes , 0 , length);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
            new File("check_code/"+String.valueOf(randomNumber)+".png").delete();
        }
    }
    static class search_user implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
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
                response = stringBuilder.toString().substring(0 , stringBuilder.toString().length()-1);
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
    static class get_user_config implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            String response = "";
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String username = split[2].trim();
                String password = split[3].trim();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{\n  \"message\": \"ok\" , \n");
                if (EasyDB.existsDB(username,password)) {
                    TreeMap<String,String> treeMap = EasyDB.getTreeMap(username,password);
                    for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                        stringBuilder.append("  \"");
                        stringBuilder.append(entry.getKey());
                        stringBuilder.append("\":");
                        stringBuilder.append("\"");
                        stringBuilder.append(entry.getValue());
                        stringBuilder.append("\"");
                        stringBuilder.append(",");
                        stringBuilder.append("\n");
                    }
                    response = stringBuilder.toString().substring(0 , stringBuilder.toString().length()-2);
                    response += "\n}";
                } else {
                    static_code = 400;
                    response = "{\"message\" : \"username or password error.\"}";
                }
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
    static class writer_update_pdf implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String username = split[2];
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{\n\"message\" : {");
                TreeMap<String , String> treeMap = EasyDB.getTreeMap("PDF_USER","#114514+1919@810");
                TreeMap<String , String> file_name=EasyDB.getTreeMap("PDF","#114514+1919@810");
                for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals(username)) {
                        stringBuilder.append("  \"");
                        stringBuilder.append(value);
                        stringBuilder.append("\":");
                        stringBuilder.append("\"");
                        stringBuilder.append(file_name.get(value));
                        stringBuilder.append("\"");
                        stringBuilder.append(",");
                    }
                }
                String s = stringBuilder.substring(0 , stringBuilder.toString().length() - 1);
                s += "}\n}";
                response = s;
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
    static class SearchPDF implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            try {
                String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
                String filename = split[2];
                ArrayList<String> pdf_list = EasyDB.searchDataFromDB("PDF","#114514+1919@810" , filename);
                response = "{\"message\" : {\n";
                StringBuilder stringBuilder = new StringBuilder();
                TreeMap<String , String> treeMap = EasyDB.getTreeMap("PDF","#114514+1919@810");
                int j = 0;
                for (String i : pdf_list) {
                    stringBuilder.append("\"");
                    stringBuilder.append("  ");
                    stringBuilder.append(i);
                    stringBuilder.append("\": ");
                    stringBuilder.append("\"");
                    stringBuilder.append(treeMap.get(i));
                    stringBuilder.append("\"");
                    stringBuilder.append("\n");
                    if (!(j + 1 == pdf_list.size())) {
                        stringBuilder.append(",");
                    }
                    j++;
                }
                response = response + stringBuilder.toString() + "\n}\n}";
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
    static class Reg implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
            String response = "";
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            int static_code = 200;
            //System.out.println(httpExchange.getRemoteAddress().toString()+" : "+ httpExchange.getRequestURI().toString());
            try {
                IPBaned(httpExchange.getRemoteAddress().getAddress().toString() , httpExchange);
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
                String[] split = stringBuilder.toString().split("\n");
                String username = split[0].trim();
                String password = split[1].trim();
                String checkcode= split[2].trim();
                if (username.length() > 15 || password.length() > 20 || checkcode.length() > 15) {
                    throw new Exception("username or password's length mustn't > 15.");
                }

                if (username.contains("-") || password.contains("-")
                || username.contains("/") || password.contains("/")
                ) {
                    response = "Can not contain Charset '-' or '/'";
                    httpExchange.sendResponseHeaders(static_code , 0);
                    OutputStream outputStream = httpExchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
                boolean isIPOK = CheckIP.containsKey(httpExchange.getRemoteAddress().getAddress().toString());
                if (isIPOK) {
                    if (!CheckIP.get(httpExchange.getRemoteAddress().getAddress().toString()).equals(checkcode)) {
                        throw new Exception("Check Code Error.");
                    }
                }
                else {
                    throw new Exception("No Check Code.");
                }
                CheckIP.remove(httpExchange.getRemoteAddress().getAddress().toString());
                EasyDB.createDB(username, password);
                EasyDB.insertDataToDatabase("is_vip" , "false" , username , password);
                EasyDB.insertDataToDatabase("user_type","ordinary",username,password);
                EasyDB.insertDataToDatabase("about","这个用户很懒，什么都没有写",username,password);
                EasyDB.insertDataToDatabase("level" , "1" , username,password);
                response = "{\"message\" : \"create successful\"}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e) {
                static_code = 500;
                response = "{\"message\" : \"" + e.getMessage() + "\"}";
                httpExchange.sendResponseHeaders(static_code , 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    static class Login implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
            String response = "";
            int static_code = 200;
            httpExchange.getResponseHeaders().add("Content-Type","application/json");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST");
            httpExchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "*");
            try {
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
                String[] split = stringBuilder.toString().split("\n");
                String username = split[0];
                String password = split[1];
                if (EasyDB.existsDB(username ,password)) {
                    response = "{\"message\" : \"login successful.\"}";
                    static_code = 200;
                } else {
                    static_code = 400;
                    response = "{\"message\" : \"login failed.\"}";
                }
            }catch (Exception e) {
                e.printStackTrace();
                static_code = 500;
                response = "{\"message\" : \"login failed.\"}";
            }
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
