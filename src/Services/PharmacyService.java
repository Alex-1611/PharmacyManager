package Services;

import java.util.*;
import Models.*;
import java.time.LocalDate;
import Persistance.*;

// Add import for AuditService
import Util.AuditService;
// Add import for DrugRepository
import Persistance.DrugRepository;

public class PharmacyService {
    private PharmacistRepository pharmacistRepository;
    private CustomerRepository customerRepository;
    private InsuranceCompanyRepository insuranceCompanyRepository;
    private DrugRepository drugRepository; // Add DrugRepository
    private Map<Integer, Product> products;
    private Map<Integer, Bill> bills;
    private Inventory inventory;
    private int nextId = 1;

    public PharmacyService() {
        pharmacistRepository = PharmacistRepository.getInstance();
        customerRepository = CustomerRepository.getInstance();
        insuranceCompanyRepository = InsuranceCompanyRepository.getInstance();
        drugRepository = DrugRepository.getInstance(); // Initialize DrugRepository
        products = new HashMap<>();
        bills = new HashMap<>();
        inventory = new Inventory();

        // Load all drugs from DB into products map
        for (Drug drug : drugRepository.getAllDrugs()) {
            products.put(drug.getId(), drug);
            nextId = Math.max(nextId, drug.getId() + 1);
        }
    }

    // Pharmacist operations
    public Pharmacist createPharmacist(String name, LocalDate dateOfBirth, double salary) {
        AuditService.logAction("createPharmacist");
        pharmacistRepository.addPharmacist(name, dateOfBirth.toString(), salary);
        // Optionally fetch the created pharmacist from DB if needed
        return null;
    }

    public void increaseSalary(int pharmacistId, double amount) {
        AuditService.logAction("increaseSalary");
        pharmacistRepository.updatePharmacistSalary(pharmacistId, amount);
    }

    public void deletePharmacist(int pharmacistId) {
        AuditService.logAction("deletePharmacist");
        pharmacistRepository.deletePharmacist(pharmacistId);
    }

    public Pharmacist getPharmacistById(int id) {
        AuditService.logAction("getPharmacistById");
        return pharmacistRepository.getPharmacistById(id);
    }

    // Customer operations
    public Customer createCustomer(String name, LocalDate dateOfBirth) {
        AuditService.logAction("createCustomer");
        customerRepository.addCustomer(name, dateOfBirth.toString());
        return null;
    }

    public void setCustomerInsurance(int customerId, int insuranceId) {
        AuditService.logAction("setCustomerInsurance");
        customerRepository.setCustomerInsurance(customerId, insuranceId);
    }

    public void deleteCustomer(int customerId) {
        AuditService.logAction("deleteCustomer");
        customerRepository.deleteCustomer(customerId);
    }

    // Product operations
    public Drug createDrug(String name, double price, int requiredAge, boolean requiresPrescription, String condition) {
        AuditService.logAction("createDrug");
        drugRepository.addDrug(name, price, requiredAge, requiresPrescription, condition);
        // Fetch the latest drug from DB (assuming auto-increment id)
        List<Drug> allDrugs = drugRepository.getAllDrugs();
        Drug latestDrug = allDrugs.isEmpty() ? null : allDrugs.get(allDrugs.size() - 1);
        if (latestDrug != null) {
            products.put(latestDrug.getId(), latestDrug);
            nextId = Math.max(nextId, latestDrug.getId() + 1);
        }
        return latestDrug;
    }

    public Supplement createSupplement(String name, double price, int requiredAge, List<String> ingredients) {
        AuditService.logAction("createSupplement");
        Supplement supplement = new Supplement(nextId++, name, price, requiredAge, ingredients);
        products.put(supplement.getId(), supplement);
        return supplement;
    }

    public void updateProductPrice(int productId, double newPrice) {
        AuditService.logAction("updateProductPrice");
        Product product = products.get(productId);
        if (product != null) {
            product.setPrice(newPrice);
            if (product instanceof Drug) {
                drugRepository.updateDrugPrice(productId, newPrice);
                // Refresh from DB
                Drug updatedDrug = drugRepository.getDrugById(productId);
                if (updatedDrug != null) {
                    products.put(productId, updatedDrug);
                }
            }
        }
    }

    public void deleteProduct(int productId) {
        AuditService.logAction("deleteProduct");
        Product product = products.get(productId);
        if (product instanceof Drug) {
            drugRepository.deleteDrug(productId);
        }
        products.remove(productId);
    }

    // Insurance operations
    public InsuranceCompany createInsuranceCompany(String name) {
        AuditService.logAction("createInsuranceCompany");
        insuranceCompanyRepository.addInsuranceCompany(name);
        return null;
    }

    public void addDrugToInsurance(int insuranceId, int drugId) {
        AuditService.logAction("addDrugToInsurance");
        insuranceCompanyRepository.addDrugToInsurance(insuranceId, drugId);
    }

    public void deleteInsuranceCompany(int insuranceId) {
        AuditService.logAction("deleteInsuranceCompany");
        insuranceCompanyRepository.deleteInsuranceCompany(insuranceId);
    }

    // Prescription operations
    public Prescription createPrescription(int customerId, String medic) {
        AuditService.logAction("createPrescription");
        if (customerRepository.getCustomerById(customerId) != null) {
            Customer customer = customerRepository.getCustomerById(customerId);
            Prescription prescription = new Prescription(LocalDate.now(), customer, medic);
            customer.addPrescription(prescription);
            return prescription;
        }
        return null;
    }

    public void addDrugToPrescription(Prescription prescription, int drugId) {
        AuditService.logAction("addDrugToPrescription");
        if (prescription != null && products.containsKey(drugId) &&
                products.get(drugId) instanceof Drug) {
            prescription.addDrug((Drug) products.get(drugId));
        }
    }

    // Bill operations
    public Bill createBill(int pharmacistId, int customerId) {
        AuditService.logAction("createBill");
        if (pharmacistRepository.getPharmacistById(pharmacistId) != null && customerRepository.getCustomerById(customerId) != null) {
            Bill bill = new Bill(nextId++, LocalDate.now(),
                    pharmacistRepository.getPharmacistById(pharmacistId),
                    customerRepository.getCustomerById(customerId));
            bills.put(bill.getId(), bill);
            pharmacistRepository.getPharmacistById(pharmacistId).addBill(bill);
            customerRepository.getCustomerById(customerId).addBill(bill);
            return bill;
        }
        return null;
    }

    public void addProductToBill(int billId, int productId) {
        AuditService.logAction("addProductToBill");
        if (bills.containsKey(billId) && products.containsKey(productId)) {
            bills.get(billId).addProduct(products.get(productId));
        }
    }

    public void addPrescriptionToBill(int billId, Prescription prescription) {
        AuditService.logAction("addPrescriptionToBill");
        if (bills.containsKey(billId) && prescription != null) {
            bills.get(billId).addPrescription(prescription);
        }
    }

    public void payBill(int billId) {
        AuditService.logAction("payBill");
        if (bills.containsKey(billId)) {
            bills.get(billId).pay();
        }
    }

    // Inventory operations
    public void addToInventory(int productId, int quantity, LocalDate expirationDate) {
        AuditService.logAction("addToInventory");
        if (products.containsKey(productId)) {
            inventory.addProducts(products.get(productId), quantity, expirationDate);
        }
    }

    public void removeExpiredProducts() {
        AuditService.logAction("removeExpiredProducts");
        inventory.removeExpired();
    }

    public LocalDate getEarliestExpiration(int drugId) {
        AuditService.logAction("getEarliestExpiration");
        if (products.containsKey(drugId) && products.get(drugId) instanceof Drug) {
            return inventory.getEarliestExpiration((Drug) products.get(drugId));
        }
        return null;
    }

    // Display methods for validation
    public void displayAllPharmacists() {
        AuditService.logAction("displayAllPharmacists");
        System.out.println("\n=== Pharmacists ===");
        for (Pharmacist pharmacist : pharmacistRepository.getAllPharmacists()) {
            System.out.println("ID: " + pharmacist.getId() + ", Name: " + pharmacist.getName() +
                    ", Salary: " + pharmacist.getSalary());
        }
    }

    public void displayAllProducts() {
        AuditService.logAction("displayAllProducts");
        System.out.println("\n=== Products ===");
        // Refresh drugs from DB
        for (Drug drug : drugRepository.getAllDrugs()) {
            products.put(drug.getId(), drug);
        }
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
        AuditService.logAction("displayAllCustomers");
        System.out.println("\n=== Customers ===");
        for (Customer customer : customerRepository.getAllCustomers()) {
            System.out.println("ID: " + customer.getId() + ", Name: " + customer.getName() +
                    ", Insurance: " + (customer.getInsurance() != null ?
                    customer.getInsurance().getName() : "None"));
        }
    }

    public void displayInventory() {
        AuditService.logAction("displayInventory");
        System.out.println("\n=== Inventory (Sorted by Expiration Date) ===");
        for (InventoryItem item : inventory.getItems()) {
            System.out.println("Product: " + item.getProduct().getName() +
                    ", Quantity: " + item.getQuantity() +
                    ", Expires: " + item.getExpirationDate());
        }
    }

    public void displayBills() {
        AuditService.logAction("displayBills");
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

    public Customer getCustomerById(int id) {
        AuditService.logAction("getCustomerById");
        return customerRepository.getCustomerById(id);
    }

    public Bill getBillById(int id) {
        AuditService.logAction("getBillById");
        return bills.get(id);
    }

    public void displayAllDrugs() {
        AuditService.logAction("displayAllDrugs");
        System.out.println("\n=== Drugs ===");
        // Refresh drugs from DB
        for (Drug drug : drugRepository.getAllDrugs()) {
            products.put(drug.getId(), drug);
        }
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
        AuditService.logAction("displayAllInsuranceCompanies");
        System.out.println("\n=== Insurance Companies ===");
        for (InsuranceCompany company : insuranceCompanyRepository.getAllInsuranceCompanies()) {
            System.out.println("ID: " + company.getId() + ", Name: " + company.getName());
        }
    }

    public void displayUnpaidBills() {
        AuditService.logAction("displayUnpaidBills");
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
