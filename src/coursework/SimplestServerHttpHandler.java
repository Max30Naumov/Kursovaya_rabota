package coursework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

public class SimplestServerHttpHandler implements HttpHandler {
    static int requestCounter = 0;

    /**
     * Handling responses from the server
     * @param httpExchange HttpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] requestParams = null;
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET,POST");

        if ("GET".equals(httpExchange.getRequestMethod()) ||
                "POST".equals(httpExchange.getRequestMethod())) {
            requestParams = getRequestParams(httpExchange);
        }

        returnResponse(httpExchange, requestParams);
    }

    /**
     * Gets params from browser address line
     * @param httpExchange HttpExchange
     * @return address line params
     */
    private String[] getRequestParams(HttpExchange httpExchange) {
        String parameters = httpExchange.getRequestURI().toString().split("\\?")[1];
        String[] params = parameters.split("&");

        return params;
    }

    /**
     * Returns response to browser
     * @param httpExchange HttpExchange
     * @param requestParamValues
     * @throws IOException
     */
    private void returnResponse(HttpExchange httpExchange, String[] requestParamValues) throws IOException {
        requestCounter++;
        System.out.println("Request received: " + requestCounter);

        OutputStream outputStream = httpExchange.getResponseBody();

        // build test response of all parameters
        StringBuilder response = new StringBuilder("");

        // Endpoints
        if(requestParamValues[0].equals("start")) {
            response.append(Processing.start());
        }
        if(requestParamValues[0].equals("add")) {
            String result = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
            response.append(Processing.add(result));
        }
        if(requestParamValues[0].equals("delete")) {
            String result = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
            response.append(Processing.delete(result));
        }

        if(requestParamValues[0].equals("edit")){
            String result = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
            response.append(Processing.edit(result));
        }

        httpExchange.sendResponseHeaders(0, response.length());
        outputStream.write(response.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}