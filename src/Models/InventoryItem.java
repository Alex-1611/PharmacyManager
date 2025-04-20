package Models;

import java.time.LocalDate;

public class InventoryItem implements Comparable<InventoryItem> {
    private Product product;
    private int quantity;
    private LocalDate expirationDate;

    public InventoryItem(Product product, int quantity, LocalDate expirationDate) {
        this.product = product;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    @Override
    public int compareTo(InventoryItem other) {
        return this.expirationDate.compareTo(other.expirationDate);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}