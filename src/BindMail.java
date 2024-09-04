import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BindMail implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.IPBaned(httpExchange.getRemoteAddress().getAddress().toString(),httpExchange);
        try {
            String json = Main.getHttpBody(httpExchange);
            System.out.println(json);
            JsonObject jsonObject = new JsonObject(json);
            String username = jsonObject.get("user").toString();
            String password = jsonObject.get("pwd").toString();
            String mail = jsonObject.get("mail").toString();
            String check= jsonObject.get("check").toString();

            if (!Main.AllowString(username) || !Main.AllowString(password) || !Main.AllowString(mail)) {
                throw new Exception("Not Allowed.");
            }
            if (!Main.UserLogin(username,password)) {
                throw new Exception("username or password error.");
            }

            boolean isIP_OK = Main.CheckMail.containsKey(httpExchange.getRemoteAddress().getAddress().toString());
            if (isIP_OK) {
                if (!Main.CheckMail.get(httpExchange.getRemoteAddress().getAddress().toString()).equals(check)) {
                    throw new Exception("Check Code Error.");
                }
            }
            else {
                throw new Exception("No Check Code.");
            }
            Main.CheckMail.remove(httpExchange.getRemoteAddress().getAddress().toString());

            String url = Main.DBURL+"/accounts";
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE accounts SET mail = '"+mail+"' WHERE username='"+username+"'");

            httpExchange.sendResponseHeaders(200,0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write("{\"message\":\"bind email successful.\"}".getBytes());
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            OutputStream out = httpExchange.getResponseBody();
            out.write("{\"message\":\"send error.\"}".getBytes());
            out.flush();
            out.close();
        }
    }
}
