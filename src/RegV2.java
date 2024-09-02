import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegV2 implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
        String response = "";
        Main.sendCORS(httpExchange);
        int static_code = 200;
        try {
            Main.IPBaned(httpExchange.getRemoteAddress().getAddress().toString() , httpExchange);
            JsonObject jsonObject = new JsonObject(Main.getHttpBody(httpExchange));
            String username = jsonObject.get("user").toString();
            String password = jsonObject.get("pwd").toString();
            String checkcode= jsonObject.get("check_code").toString();

            if (username.length() > 20 || password.length() > 20 || checkcode.length() > 15) {
                throw new Exception("username or password's length mustn't > 15.");
            }
            boolean isIP_OK = Main.CheckIP.containsKey(httpExchange.getRemoteAddress().getAddress().toString());
            if (isIP_OK) {
                if (!Main.CheckIP.get(httpExchange.getRemoteAddress().getAddress().toString()).equals(checkcode)) {
                    throw new Exception("Check Code Error.");
                }
            }
            else {
                throw new Exception("No Check Code.");
            }
            Main.CheckIP.remove(httpExchange.getRemoteAddress().getAddress().toString());

            String url = Main.DBURL+"/accounts";

            // 建立连接
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "INSERT INTO accounts (username,password,about) VALUES ('"+username+"','"+password+"','no data')";
            ResultSet rs = stmt.executeQuery(sqlQuery);

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