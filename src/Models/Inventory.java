package Models;

import java.time.LocalDate;
import java.util.TreeSet;

public class Inventory {
    // TreeSet will keep the items sorted by expiration date
    private TreeSet<InventoryItem> items;

    public Inventory() {
        this.items = new TreeSet<>();
    }

    public void addProducts(Product product, int quantity, LocalDate expirationDate) {
        InventoryItem newItem = new InventoryItem(product, quantity, expirationDate);
        // Check if this product with this expiration date already exists
        boolean found = false;
        for (InventoryItem item : items) {
            if (item.getProduct().getId() == product.getId() &&
                    item.getExpirationDate().equals(expirationDate)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(newItem);
        }
    }

    public void removeExpired() {
        LocalDate today = LocalDate.now();
        items.removeIf(item -> item.getExpirationDate().isBefore(today));
    }

    public LocalDate getEarliestExpiration(Drug drug) {
        for (InventoryItem item : items) {
            if (item.getProduct() instanceof Drug &&
                    item.getProduct().getId() == drug.getId()) {
                return item.getExpirationDate();
            }
        }
        return null;
    }

    public TreeSet<InventoryItem> getItems() {
        return items;
    }
}
