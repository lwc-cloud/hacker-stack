import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetUserConfig implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.sendCORS(httpExchange);
        String response = "";
        int static_code = 200;
        try {
            String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
            String username = split[2].trim();
            String password = split[3].trim();
            if (!Main.AllowString(username) || !Main.AllowString(password)) {
                throw new Exception("Not Allowed.");
            }
            if (Main.UserLogin(username,password)) {
                String url = Main.DBURL+"/accounts";
                // 建立连接
                Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
                Statement stmt = conn.createStatement();
                String sqlQuery = "SELECT about,apikey FROM accounts WHERE username='"+username+"';";
                ResultSet rs = stmt.executeQuery(sqlQuery);
                JsonObject jsonObject = new JsonObject("{}");
                while (rs.next()) {
                    String about = rs.getString("about");
                    String apikey = rs.getString("apikey");
                    jsonObject.objectObjectTreeMap.put("about" , about);
                    jsonObject.objectObjectTreeMap.put("apikey" , apikey);
                }
                response = jsonObject.TreeMapToJson(jsonObject.objectObjectTreeMap);
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