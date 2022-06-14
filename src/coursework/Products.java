package coursework;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Products implements Serializable {
    private ArrayList<Product> productsList;

    public Products() {
        productsList = new ArrayList<>();
    }
    public ArrayList<Product> getProductsList() {
        return productsList;
    }
    public void setProductsList(ArrayList<Product> productsList){
        this.productsList = productsList;
    }

    /**
     * Добавить товар в список
     * @param product объект товара
     */
    public void add(Product product){
        productsList.add(product);
    }

    /**
     * Изменить товар в списке
     * @param id порядковый номер товара в списке
     * @param product объект товара
     */
    public void set(int id, Product product){
        productsList.set(id, product);
    }

    /**
     * Удалить товар из списка
     * @param product объект товара
     */
    public void remove(Product product){
        productsList.remove(product);
    }

    /**
     * Удаление товара из списка по номеру
     * @param number порядковый номер товара
     */
    public void remove(int number) throws IllegalArgumentException {
        if (number < productsList.size() && number >= 0){
            productsList.remove(number);
        } else {
            throw new IllegalArgumentException("Введен неправильный индекс товара для удаления");
        }
    }

    @Override
    public String toString() {
        return "Товары: " + productsList;
    }
}
