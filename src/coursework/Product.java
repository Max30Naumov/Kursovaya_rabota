package coursework;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    private String storeName;
    private String storeAddress;
    private int code;
    private String name;
    private int quantity;
    private double price;

    public Product(String storeName, String storeAddress, int code, String name,
                   int quantity, double price) throws IllegalArgumentException {
        try {
            this.setStoreName(storeName);
            this.setStoreAddress(storeAddress);
            this.setCode(code);
            this.setName(name);
            this.setQuantity(quantity);
            this.setPrice(price);
        } catch (Exception e) {
            throw new IllegalArgumentException("Попытка создать неправильный объект товара");
        }
    }
    public Product(){}

    public String getStoreName() {
        return storeName;
    }
    public String getStoreAddress() {
        return storeAddress;
    }
    public int getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPrice() {
        return price;
    }

    public void setStoreName(String storeName) throws IllegalArgumentException {
        if (Utilities.isInCorrectWordCase(storeName)){
            this.storeName = Utilities.removeDoubleWhitespaces(storeName);
        } else {
            throw new IllegalArgumentException("Имя магазина должно начинаться с большой буквы");
        }
    }

    public void setStoreAddress(String storeAddress) {
        if (Utilities.isInCorrectWordCase(storeAddress)){
            this.storeAddress = Utilities.removeDoubleWhitespaces(storeAddress);
        } else {
            throw new IllegalArgumentException("Адрес магазина должно начинаться с большой буквы");
        }
    }

    public void setCode(int code) throws IllegalArgumentException {
        if (code >= 0) {
            this.code = code;
        } else{
            throw new IllegalArgumentException("Код товара должен быть больше или равен 0");
        }
    }

    public void setName(String name) throws IllegalArgumentException {
        if (Utilities.isInCorrectWordCase(name)){
            this.name = Utilities.removeDoubleWhitespaces(name);
        } else {
            throw new IllegalArgumentException("Название товара должно начинаться с большой буквы");
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else{
            throw new IllegalArgumentException("Количество товара должно быть больше или равно 0");
        }
    }

    public void setPrice(double price) {
        if (price > 0 && price < 1000000) {
            this.price = price;
        } else{
            throw new IllegalArgumentException("Цена товара должна быть больше 0 и меньше 1000000");
        }
        this.price = price;
    }

    /**
     * Проверка объекта товара на правильность заполнения полей
     * @param product объект товара
     * @return правильно ли создан объект товара
     * @throws IllegalArgumentException
     */

    public static boolean isProductValid(Product product) throws IllegalArgumentException {
        try {
            Product result = new Product(product.storeName, product.storeAddress, product.code,
                    product.name, product.quantity, product.price);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
