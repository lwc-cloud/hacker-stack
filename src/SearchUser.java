import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchUser implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.sendCORS(httpExchange);
        String response = "";
        int static_code = 200;
        try {
            String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
            String search = split[2].trim();

            if (!Main.AllowString(search)) {
                throw new Exception("Not Allowed.");
            }

            Connection conn = DriverManager.getConnection(Main.DBURL+"/accounts",Main.DBUserName,Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT username FROM accounts WHERE username LIKE '%"+search+"%'";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            ArrayList<String> userArrays = new ArrayList<>();
            while (rs.next()) {
                userArrays.add(rs.getString("username").trim());
            }
            JsonObject jsonObject = new JsonObject("{}");
            jsonObject.objectObjectTreeMap.put("message" , userArrays);
            System.out.println(jsonObject.TreeMapToJson(jsonObject.objectObjectTreeMap));
            response = jsonObject.TreeMapToJson(jsonObject.objectObjectTreeMap);

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