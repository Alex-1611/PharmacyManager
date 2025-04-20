package Services;

import java.util.*;
import Models.*;
import java.time.LocalDate;

public class PharmacyService {
    private Map<Integer, Pharmacist> pharmacists;
    private Map<Integer, Customer> customers;
    private Map<Integer, Product> products;
    private Map<Integer, Bill> bills;
    private Map<Integer, InsuranceCompany> insuranceCompanies;
    private Inventory inventory;
    private int nextId = 1;

    public PharmacyService() {
        pharmacists = new HashMap<>();
        customers = new HashMap<>();
        products = new HashMap<>();
        bills = new HashMap<>();
        insuranceCompanies = new HashMap<>();
        inventory = new Inventory();
    }

    // Pharmacist operations
    public Pharmacist createPharmacist(String name, LocalDate dateOfBirth, double salary) {
        Pharmacist pharmacist = new Pharmacist(nextId++, name, dateOfBirth, salary);
        pharmacists.put(pharmacist.getId(), pharmacist);
        return pharmacist;
    }

    public void increaseSalary(int pharmacistId, double amount) {
        if (pharmacists.containsKey(pharmacistId)) {
            pharmacists.get(pharmacistId).increaseSalary(amount);
        }
    }

    // Customer operations
    public Customer createCustomer(String name, LocalDate dateOfBirth) {
        Customer customer = new Customer(nextId++, name, dateOfBirth);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public void setCustomerInsurance(int customerId, int insuranceId) {
        if (customers.containsKey(customerId) && insuranceCompanies.containsKey(insuranceId)) {
            customers.get(customerId).setInsurance(insuranceCompanies.get(insuranceId));
        }
    }

    // Product operations
    public Drug createDrug(String name, double price, int requiredAge, boolean requiresPrescription, String condition) {
        Drug drug = new Drug(nextId++, name, price, requiredAge, requiresPrescription, condition);
        products.put(drug.getId(), drug);
        return drug;
    }

    public Supplement createSupplement(String name, double price, int requiredAge, List<String> ingredients) {
        Supplement supplement = new Supplement(nextId++, name, price, requiredAge, ingredients);
        products.put(supplement.getId(), supplement);
        return supplement;
    }

    public void updateProductPrice(int productId, double newPrice) {
        if (products.containsKey(productId)) {
            products.get(productId).setPrice(newPrice);
        }
    }

    // Insurance operations
    public InsuranceCompany createInsuranceCompany(String name) {
        InsuranceCompany company = new InsuranceCompany(nextId++, name);
        insuranceCompanies.put(company.getId(), company);
        return company;
    }

    public void addDrugToInsurance(int insuranceId, int drugId) {
        if (insuranceCompanies.containsKey(insuranceId) && products.containsKey(drugId) &&
                products.get(drugId) instanceof Drug) {
            insuranceCompanies.get(insuranceId).addInsuredDrug((Drug) products.get(drugId));
        }
    }

    // Prescription operations
    public Prescription createPrescription(int customerId, String medic) {
        if (customers.containsKey(customerId)) {
            Customer customer = customers.get(customerId);
            Prescription prescription = new Prescription(LocalDate.now(), customer, medic);
            customer.addPrescription(prescription);
            return prescription;
        }
        return null;
    }

    public void addDrugToPrescription(Prescription prescription, int drugId) {
        if (prescription != null && products.containsKey(drugId) &&
                products.get(drugId) instanceof Drug) {
            prescription.addDrug((Drug) products.get(drugId));
        }
    }

    // Bill operations
    public Bill createBill(int pharmacistId, int customerId) {
        if (pharmacists.containsKey(pharmacistId) && customers.containsKey(customerId)) {
            Bill bill = new Bill(nextId++, LocalDate.now(),
                    pharmacists.get(pharmacistId),
                    customers.get(customerId));
            bills.put(bill.getId(), bill);
            pharmacists.get(pharmacistId).addBill(bill);
            customers.get(customerId).addBill(bill);
            return bill;
        }
        return null;
    }

    public void addProductToBill(int billId, int productId) {
        if (bills.containsKey(billId) && products.containsKey(productId)) {
            bills.get(billId).addProduct(products.get(productId));
        }
    }

    public void addPrescriptionToBill(int billId, Prescription prescription) {
        if (bills.containsKey(billId) && prescription != null) {
            bills.get(billId).addPrescription(prescription);
        }
    }

    public void payBill(int billId) {
        if (bills.containsKey(billId)) {
            bills.get(billId).pay();
        }
    }

    // Inventory operations
    public void addToInventory(int productId, int quantity, LocalDate expirationDate) {
        if (products.containsKey(productId)) {
            inventory.addProducts(products.get(productId), quantity, expirationDate);
        }
    }

    public void removeExpiredProducts() {
        inventory.removeExpired();
    }

    public LocalDate getEarliestExpiration(int drugId) {
        if (products.containsKey(drugId) && products.get(drugId) instanceof Drug) {
            return inventory.getEarliestExpiration((Drug) products.get(drugId));
        }
        return null;
    }

    // Display methods for validation
    public void displayAllPharmacists() {
        System.out.println("\n=== Pharmacists ===");
        for (Pharmacist pharmacist : pharmacists.values()) {
            System.out.println("ID: " + pharmacist.getId() + ", Name: " + pharmacist.getName() +
                    ", Salary: " + pharmacist.getSalary());
        }
    }

    public void displayAllProducts() {
        System.out.println("\n=== Products ===");
        for (Product product : products.values()) {
            System.out.print("ID: " + product.getId() + ", Name: " + product.getName() +
                    ", Price: " + product.getPrice());
            if (product instanceof Drug) {
                Drug drug = (Drug) product;
                System.out.println(", Type: Drug, Prescription Required: " +
                        drug.getRequiresPrescription() + ", Condition: " + drug.getCondition());
            } else if (product instanceof Supplement) {
                Supplement supplement = (Supplement) product;
                System.out.println(", Type: Supplement, Ingredients: " + supplement.getIngredients());
            }
        }
    }

    public void displayAllCustomers() {
        System.out.println("\n=== Customers ===");
        for (Customer customer : customers.values()) {
            System.out.println("ID: " + customer.getId() + ", Name: " + customer.getName() +
                    ", Insurance: " + (customer.getInsurance() != null ?
                    customer.getInsurance().getName() : "None"));
        }
    }

    public void displayInventory() {
        System.out.println("\n=== Inventory (Sorted by Expiration Date) ===");
        for (InventoryItem item : inventory.getItems()) {
            System.out.println("Product: " + item.getProduct().getName() +
                    ", Quantity: " + item.getQuantity() +
                    ", Expires: " + item.getExpirationDate());
        }
    }

    public void displayBills() {
        System.out.println("\n=== Bills ===");
        for (Bill bill : bills.values()) {
            System.out.println("Bill ID: " + bill.getId() +
                    ", Customer: " + bill.getCustomer().getName() +
                    ", Total: " + bill.calculateTotal() +
                    ", Paid: " + bill.isPaid());
            System.out.println("  Products:");
            for (Product product : bill.getProducts()) {
                System.out.println("    - " + product.getName() + ": $" + product.getPrice());
            }
        }
    }
    // Add these methods to the PharmacyService class

    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    public Bill getBillById(int id) {
        return bills.get(id);
    }

    public void displayAllDrugs() {
        System.out.println("\n=== Drugs ===");
        for (Product product : products.values()) {
            if (product instanceof Drug) {
                Drug drug = (Drug) product;
                System.out.println("ID: " + drug.getId() + ", Name: " + drug.getName() +
                        ", Price: " + drug.getPrice() + ", Requires Prescription: " +
                        drug.getRequiresPrescription() + ", Condition: " + drug.getCondition());
            }
        }
    }

    public void displayAllInsuranceCompanies() {
        System.out.println("\n=== Insurance Companies ===");
        for (InsuranceCompany company : insuranceCompanies.values()) {
            System.out.println("ID: " + company.getId() + ", Name: " + company.getName());
        }
    }

    public void displayUnpaidBills() {
        System.out.println("\n=== Unpaid Bills ===");
        for (Bill bill : bills.values()) {
            if (!bill.isPaid()) {
                System.out.println("Bill ID: " + bill.getId() +
                        ", Customer: " + bill.getCustomer().getName() +
                        ", Total: " + bill.calculateTotal());
            }
        }
    }
}