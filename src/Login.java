import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class Login implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        /**
         *  这个是老旧的接口
         */
        System.out.println(httpExchange.getRemoteAddress().getAddress().toString()+" "+this.getClass().getName());
        String response = "";
        int static_code = 200;
        Main.sendCORS(httpExchange);
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            inputStream.close();
            String[] split = stringBuilder.toString().split("\n");
            String username = split[0].trim();
            String password = split[1].trim();

            if (!Main.AllowString(username) && !Main.AllowString(password)) {
                throw new Exception("Not Allowed.");
            }

            String url = Main.DBURL+"/accounts";

            // 建立连接
            Connection conn = DriverManager.getConnection(url, Main.DBUserName, Main.DBPassword);
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT username,password FROM accounts WHERE username='"+username+"' AND password='"+password+"';";
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