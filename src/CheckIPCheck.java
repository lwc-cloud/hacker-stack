import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

class CheckIPCheck implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        httpExchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin",
                "*");
        httpExchange.getResponseHeaders().add(
                "Access-Control-Allow-Methods",
                "POST");
        httpExchange.getResponseHeaders().add(
                "Access-Control-Allow-Headers",
                "*");
        int static_code = 200;
        boolean isApiKey = false;

        try {
            String[] split = java.net.URLDecoder.decode(httpExchange.getRequestURI().toString()).split("/");
            String checkcode = split[2];
            System.out.println(Main.CheckIP);
            boolean isIPOK = Main.CheckIP.containsKey(Main.GetRealIP(httpExchange));
            if (isIPOK) {
                if (!Main.CheckIP.get(Main.GetRealIP(httpExchange)).equals(checkcode)) {
                    throw new Exception("Check Code Error.");
                }
                else {
                    Main.CheckIP.remove(Main.GetRealIP(httpExchange));
                    throw new Exception("ok");
                }
            }
        }catch (Exception e) {
            response = "{\"message\" : \"" + e.getMessage() + "\"}";
            httpExchange.sendResponseHeaders(static_code , 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}