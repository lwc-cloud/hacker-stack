import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Reg implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
        String response = "";
        Main.sendCORS(httpExchange);
        int static_code = 200;
        try {
            Main.IPBaned(httpExchange.getRemoteAddress().getAddress().toString() , httpExchange);
            String[] split = Main.getHttpBody(httpExchange).split("\n");
            String username = split[0].trim();
            String password = split[1].trim();
            String checkcode= split[2].trim();
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

            // 查看这个IP在以前是否建立过账户，如果有的话那么不允许创建
            ResultSet resultSet = stmt.executeQuery("SELECT username FROM accounts;");
            if (resultSet.next()) {
                throw new Exception("user was existed.");
            }
            String ip = httpExchange.getRemoteAddress().getAddress().toString();
            ResultSet result = stmt.executeQuery("SELECT registered_ip FROM accounts WHERE registered_ip='"+ip+"';");
            if (result.next()) {
                throw new Exception("ip was existed.");
            }

            String sqlQuery = "INSERT INTO accounts (username,password,about,registered_ip) VALUES ('"+username+"','"+password+"','no data','"+ip+"')";
            stmt.execute(sqlQuery);

            response = "{\"message\" : \"create successful\"}";
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            static_code = 500;
            response = "{\"message\" : \"account was existed or create error\"}";
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}