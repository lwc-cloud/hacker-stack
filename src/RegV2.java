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
            String jsonContent = Main.getHttpBody(httpExchange);
            System.out.println(jsonContent);
            JsonObject jsonObject = new JsonObject(jsonContent);
            String username = jsonObject.get("user").toString();
            String password = jsonObject.get("pwd").toString();
            String mail = jsonObject.get("mail").toString();
            String checkcode= jsonObject.get("check").toString();

            if (!Main.AllowString(username) || !Main.AllowString(password) || !Main.AllowString(mail)) {
                throw new Exception("Not Allowed.");
            }

            if (username.length() > 20 || password.length() > 20) {
                throw new Exception("username or password's length mustn't > 20.");
            }
            boolean isIP_OK = Main.CheckMail.containsKey(httpExchange.getRemoteAddress().getAddress().toString());
            if (isIP_OK) {
                if (!Main.CheckMail.get(httpExchange.getRemoteAddress().getAddress().toString()).equals(checkcode)) {
                    throw new Exception("Check Code Error.");
                }
            }
            else {
                throw new Exception("No Check Code.");
            }
            Main.CheckMail.remove(httpExchange.getRemoteAddress().getAddress().toString());

            String url = Main.DBURL+"/accounts";

            // 建立连接
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "INSERT INTO accounts (username,password,about,mail) VALUES ('"+username+"','"+password+"','no data','"+mail+"')";
            stmt.execute(sqlQuery);

            response = "{\"message\" : \"create successful\"}";
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
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