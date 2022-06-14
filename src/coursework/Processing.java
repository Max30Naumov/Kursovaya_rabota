package coursework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

public class Processing {
    /**
     * Считать список товаров в виде JSON из базы данных
     * @return
     * @throws IOException
     */
    public static String start() throws IOException {
        try {
            return new BufferedReader(new FileReader(new File("database/database.json"))).readLine();
        } catch (IOException e) {
            System.out.println("Database file is not accessible");
        }
        return "{\"showed_successfully\": false}";
    }

    /**
     * Добавить новый товар в базу данных
     * @param productJSON
     * @return
     * @throws IOException
     */
    public static String add(String productJSON) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product product = objectMapper.readValue(productJSON, Product.class);
            if (Product.isProductValid(product)) {
                Main.getProducts().add(product);
                Database.SaveInDB(Main.getProducts().getProductsList());
                return "{\"added_successfully\": true}";
            }
        } catch (Exception e) {
            System.out.println("Попытка создать неправильный объект товара");
            return "{\"added_successfully\": false}";
        }
        return "{\"added_successfully\": false}";
    }

    public static String edit(String productJSON) throws IOException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode productJsonNode = objectMapper.readTree(productJSON);
            int id = Integer.parseInt(productJsonNode.get("id").asText());
            Product product = objectMapper.treeToValue(productJsonNode.get("product"), Product.class);

            if (Product.isProductValid(product)) {
                Main.getProducts().set(id, product);
                Database.SaveInDB(Main.getProducts().getProductsList());
                return "{\"edited_successfully\": true}";
            }
        } catch (Exception e) {
            System.out.println("Попытка создать неправильный объект товара");
        }
        return "{\"added_successfully\": false}";
    }

    public static String delete(String ListJSON) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Main.getProducts().setProductsList(objectMapper.readValue(ListJSON, ArrayList.class));
            Database.SaveInDB(Main.getProducts().getProductsList());
            return "{\"deleted_successfully\": true}";
        } catch (IOException e) {
            System.out.println("Файл базы данных недоступен");
            return "{\"deleted_successfully\": false}";
        }
    }
}
