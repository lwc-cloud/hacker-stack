import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MailLogin implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.sendCORS(httpExchange);
        String response = "";
        int static_code = 200;
        try {
            Main.IPBaned(httpExchange.getRemoteAddress().getAddress().toString(),httpExchange);
            String json = Main.getHttpBody(httpExchange);
            JsonObject jsonObject = new JsonObject(json);
            String mail = jsonObject.get("mail").toString();
            String checkcode = jsonObject.get("check").toString();
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
            // 返回用户名和密码给前端.

            String url = Main.DBURL+"/accounts";
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT username,password,mail FROM accounts WHERE mail='"+mail+"';";
            ResultSet s1 = stmt.executeQuery(sqlQuery);
            if (!s1.next()) {
                static_code = 400;
                response = "{\"message\" : \"cannot found a account bind this email.\"}";
            }else {
                String username = s1.getString("username").trim();
                String password = s1.getString("password").trim();
                response = "{\"message\" : \"login successful\",\"user\":\""+username+"\",\"pwd\":\""+password+"\"}";
            }

            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            static_code = 500;
            response = "{\"message\" : \"login error\"}";
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
