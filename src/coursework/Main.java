package coursework;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    private static Products products = new Products();
    public static Products getProducts() {
        return products;
    }

    public static void main(String[] args) throws IOException, IllegalArgumentException {
        try {
            products.setProductsList(Database.LoadFromDB());
        }
        catch (Exception e){
            products = new Products();
        }
        simplestServerExample();
    }

    public static void simplestServerExample() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/back", new SimplestServerHttpHandler());
        server.start();
        System.out.println(System.lineSeparator() + "Server started at:\tlocalhost:8001");
    }
}
