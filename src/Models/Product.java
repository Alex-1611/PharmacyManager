package Models;

public class Product {
    protected int id;
    protected double price;
    protected String name;
    protected int requiredAge;

    public Product(int id, String name, double price, int requiredAge) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.requiredAge = requiredAge;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getRequiredAge() {
        return requiredAge;
    }
}
