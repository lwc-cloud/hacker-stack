import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginV2 implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        /**
         *  这个是老新的接口
         */
        System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
        String response = "";
        int static_code = 200;
        Main.sendCORS(httpExchange);
        try {
            String HttpBody = Main.getHttpBody(httpExchange);
            JsonObject jsonObject = new JsonObject(HttpBody);
            String username = (String) jsonObject.get("user");
            String password = (String) jsonObject.get("pwd");

            if (!Main.AllowString(username) && !Main.AllowString(password)) {
                throw new Exception("Not Allowed.");
            }

            String url = Main.DBURL+"/accounts";

            // 建立连接
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT username,password,mail FROM accounts WHERE (username='"+username+"' OR mail='"+username+"') AND password='"+password+"';";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                response = "{\"message\" : \"login successful.\"}";
            } else {
                static_code = 400;
                response = "{\"message\" : \"login failed.\"}";
            }
        }catch (Exception e) {
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