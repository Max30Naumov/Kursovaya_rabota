package coursework;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    /**
     * Сохранить в базу данных значение списка товаров
     * @param products список товаров
     * @throws IOException запись в файл недоступна
     */
    public static void SaveInDB(List<Product> products) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("database/database.json"), products);
    }

    /**
     * Загрузить в список значение с базы данных
     * @return список товаров
     * @throws IOException чтение с файла недоступно
     */
    public static ArrayList<Product> LoadFromDB() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        return objectMapper.readValue(new File("database/database.json"), ArrayList.class);
    }
}
