import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangePassword implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException{
        Main.sendCORS(httpExchange);
        int static_code = 200;
        String response = null;
        try {
            Main.IPBaned(httpExchange.getRemoteAddress().getAddress().toString() , httpExchange);
            JsonObject jsonObject = new JsonObject(Main.getHttpBody(httpExchange));
            String username = jsonObject.get("user").toString();
            String old_pwd = jsonObject.get("old_pwd").toString();
            String new_pwd = jsonObject.get("new_pwd").toString();

            if (!Main.AllowString(username) || !Main.AllowString(old_pwd) || Main.AllowString(new_pwd)) {
                throw new Exception("Not Allowed.");
            }
            if (Main.UserLogin(username,old_pwd)) {
                // 建立连接
                String url = Main.DBURL + "/accounts";
                Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
                Statement stmt = conn.createStatement();
                String sqlQuery = "UPDATE accounts SET password='"+new_pwd+"' WHERE username='"+username+"';";
                stmt.execute(sqlQuery);
                response = "{\"message\" : \"Change password ok!\"}";
            }
            else {
                throw new Exception("username or password error.");
            }
        }catch (Exception e) {
            static_code = 400;
            response = "{\"message\" : \"" + e.getMessage() + "\"}";
        }
        httpExchange.sendResponseHeaders(static_code , 0);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
