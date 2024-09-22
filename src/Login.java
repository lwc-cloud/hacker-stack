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
        System.out.println(Main.GetRealIP(httpExchange)+" "+this.getClass().getName());
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
            String sqlQuery = "SELECT username,password,edu_vip,mail FROM accounts WHERE (username='"+username+"' OR mail='"+username+"') AND password='"+password+"';";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                String eduVIP = rs.getString("edu_vip");
                if (eduVIP == null) {
                    long apiTime = ToolKits.getNextHourAsInt();
                    stmt.execute("UPDATE accounts SET edu_vip = '"+String.valueOf(apiTime)+"'");
                    response = "{\"message\" : \"login successful.\" , \"vip_time\":\"" +
                            String.valueOf(apiTime)+"\",\"server\": " +
                            String.valueOf(apiTime > ToolKits.getNowAsInt())+"}";
                }
                else {
                    eduVIP = eduVIP.trim();
                    response = "{\"message\" : \"login successful.\" , \"vip_time\":\"" +
                            eduVIP+"\",\"server\": " +
                            String.valueOf(Long.parseLong(eduVIP) > ToolKits.getNowAsInt())+"}";
                }
            } else {
                static_code = 400;
                response = "{\"message\" : \"login failed.\"}";
            }
        }catch (Exception e) {
            e.printStackTrace();
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