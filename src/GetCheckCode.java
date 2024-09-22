import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Random;

public class GetCheckCode implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(Main.GetRealIP(httpExchange)+" "+this.getClass().getName());
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
        ToolKits.StringToPng(String.valueOf(randomNumber) , "check_code/"+String.valueOf(randomNumber)+".png");
        Main.CheckIP.put(Main.GetRealIP(httpExchange) , String.valueOf(randomNumber));
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