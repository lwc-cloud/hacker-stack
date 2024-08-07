import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LinwinHackerKiller {
    public static String UserInfo(String ip) throws Exception {
        URL url = new URL("http://ip-api.com/json/"+ip);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
    public static void ToHacker(HttpExchange httpExchange) {
        try {
/*
            OutputStream outputStream = httpExchange.getResponseBody();
            String response = "<h1>LinwinSoft Anti-intrusion System</h1>" +
                    "<p>You mustn't have too much requests.</p>" +
                    "<code>"+UserInfo(httpExchange.getRemoteAddress().getAddress().toString())+"</code>";
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
            httpExchange.close();
 */
            httpExchange.close();
        }catch (Exception e) {
            httpExchange.close();
        }
    }
}
