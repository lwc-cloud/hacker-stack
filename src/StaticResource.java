import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class StaticResource implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Main.sendCORS(httpExchange);
        httpExchange.sendResponseHeaders(200,0);
        OutputStream outputStream = httpExchange.getResponseBody();

        String URL = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString());
        if (URL.contains("../")) {
            httpExchange.close();
            return;
        }
        InputStream inputStream = new FileInputStream( "."+URL);
        byte[] bytes = new byte[1024];
        int length = -1;
        while ((length = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        httpExchange.close();
    }
}
