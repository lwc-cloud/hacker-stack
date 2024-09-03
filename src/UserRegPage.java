import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class UserRegPage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = new FileInputStream("./static/reg.html");
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
        Main.sendCORS(httpExchange);
        httpExchange.sendResponseHeaders(200 , 0);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(stringBuilder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
        httpExchange.close();
    }
}
