import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class Activation implements HttpHandler {
    public static String send_mail(String json,String IP) throws Exception {

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://send.linwinsoft.top/api/v1/send_mail").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();

        JsonObject jsonObject = new JsonObject(json);
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(65500 - 10000 + 1);
        jsonObject.objectObjectTreeMap.put("content","验证码: "+String.valueOf(randomNumber)+" ,请妥善保存，不要泄露给他人.");
        jsonObject.objectObjectTreeMap.put("subject","验证你的Linwinsoft账户注册");
        Main.CheckMail.put(IP,String.valueOf(randomNumber));
        outputStream.write(jsonObject.TreeMapToJson(jsonObject.objectObjectTreeMap).getBytes());
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = httpURLConnection.getInputStream();
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
        return stringBuilder.toString();
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.sendCORS(httpExchange);
        try {
            String ip = httpExchange.getRemoteAddress().getAddress().toString();
            // 判断一个IP在规定1分钟时间内是否访问了超过 5 次
            if (Main.IPRequests.containsKey(ip)) {
                int re = Main.IPRequests.get(ip) + 1;
                Main.IPRequests.put(ip , re);
                throw new Exception("Too many requests");
            } else {
                Main.IPRequests.put(ip , 1);
            }
            String json = Main.getHttpBody(httpExchange);
            System.out.println(json);
            httpExchange.sendResponseHeaders(200,0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(send_mail(json,httpExchange.getRemoteAddress().getAddress().toString()).getBytes());
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            OutputStream out = httpExchange.getResponseBody();
            out.write("{\"message\":\"send error.\"}".getBytes());
            out.flush();
            out.close();
        }
    }
}
