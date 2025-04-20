package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int id;
    private List<Product> products;
    private List<Prescription> prescriptions;
    private LocalDate date;
    private Pharmacist pharmacist;
    private Customer customer;
    private boolean isPaid;

    public Bill(int id, LocalDate date, Pharmacist pharmacist, Customer customer) {
        this.id = id;
        this.date = date;
        this.pharmacist = pharmacist;
        this.customer = customer;
        this.products = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.isPaid = false;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void pay() {
        isPaid = true;
    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getProducts() {
        return products;
    }
}
