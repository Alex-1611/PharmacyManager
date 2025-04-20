import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import Services.PharmacyService;
import Models.*;

public class Main {
    private static PharmacyService service;
    private static Scanner scanner;

    public static void main(String[] args) {
        service = new PharmacyService();
        scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    pharmacistMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    productMenu();
                    break;
                case 4:
                    prescriptionMenu();
                    break;
                case 5:
                    billMenu();
                    break;
                case 6:
                    inventoryMenu();
                    break;
                case 7:
                    insuranceMenu();
                    break;
                case 8:
                    displayMenu();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting the Pharmacy Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n===== PHARMACY MANAGEMENT SYSTEM =====");
        System.out.println("1. Pharmacist Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Product Management");
        System.out.println("4. Prescription Management");
        System.out.println("5. Bill Management");
        System.out.println("6. Inventory Management");
        System.out.println("7. Insurance Management");
        System.out.println("8. Display Data");
        System.out.println("0. Exit");
        System.out.println("=====================================");
    }

    // PHARMACIST MENU
    private static void pharmacistMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== PHARMACIST MANAGEMENT ====");
            System.out.println("1. Add New Pharmacist");
            System.out.println("2. Increase Pharmacist Salary");
            System.out.println("3. List All Pharmacists");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addPharmacist();
                    break;
                case 2:
                    increaseSalary();
                    break;
                case 3:
                    service.displayAllPharmacists();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPharmacist() {
        System.out.println("\nADD NEW PHARMACIST");
        String name = getStringInput("Enter name: ");
        LocalDate dob = getDateInput("Enter date of birth (yyyy-MM-dd): ");
        double salary = getDoubleInput("Enter salary: ");

        Pharmacist pharmacist = service.createPharmacist(name, dob, salary);
        System.out.println("Pharmacist added successfully with ID: " + pharmacist.getId());
    }

    private static void increaseSalary() {
        System.out.println("\nINCREASE PHARMACIST SALARY");
        service.displayAllPharmacists();
        int id = getIntInput("Enter pharmacist ID: ");
        double amount = getDoubleInput("Enter salary increase amount: ");

        service.increaseSalary(id, amount);
        System.out.println("Salary increased successfully.");
    }

    // CUSTOMER MENU
    private static void customerMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== CUSTOMER MANAGEMENT ====");
            System.out.println("1. Add New Customer");
            System.out.println("2. Set Customer Insurance");
            System.out.println("3. List All Customers");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    setCustomerInsurance();
                    break;
                case 3:
                    service.displayAllCustomers();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCustomer() {
        System.out.println("\nADD NEW CUSTOMER");
        String name = getStringInput("Enter name: ");
        LocalDate dob = getDateInput("Enter date of birth (yyyy-MM-dd): ");

        Customer customer = service.createCustomer(name, dob);
        System.out.println("Customer added successfully with ID: " + customer.getId());
    }

    private static void setCustomerInsurance() {
        System.out.println("\nSET CUSTOMER INSURANCE");
        service.displayAllCustomers();
        int customerId = getIntInput("Enter customer ID: ");

        System.out.println("\nAvailable Insurance Companies:");

        service.displayAllInsuranceCompanies();
        int insuranceId = getIntInput("Enter insurance company ID: ");

        service.setCustomerInsurance(customerId, insuranceId);
        System.out.println("Insurance set successfully.");
    }

    // PRODUCT MENU
    private static void productMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== PRODUCT MANAGEMENT ====");
            System.out.println("1. Add New Drug");
            System.out.println("2. Add New Supplement");
            System.out.println("3. Update Product Price");
            System.out.println("4. List All Products");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addDrug();
                    break;
                case 2:
                    addSupplement();
                    break;
                case 3:
                    updateProductPrice();
                    break;
                case 4:
                    service.displayAllProducts();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addDrug() {
        System.out.println("\nADD NEW DRUG");
        String name = getStringInput("Enter name: ");
        double price = getDoubleInput("Enter price: ");
        int requiredAge = getIntInput("Enter required age (0 for no restriction): ");
        boolean requiresPrescription = getBooleanInput("Requires prescription? (true/false): ");
        String condition = getStringInput("Enter condition: ");

        Drug drug = service.createDrug(name, price, requiredAge, requiresPrescription, condition);
        System.out.println("Drug added successfully with ID: " + drug.getId());
    }

    private static void addSupplement() {
        System.out.println("\nADD NEW SUPPLEMENT");
        String name = getStringInput("Enter name: ");
        double price = getDoubleInput("Enter price: ");
        int requiredAge = getIntInput("Enter required age (0 for no restriction): ");

        List<String> ingredients = new ArrayList<>();
        boolean addMore = true;
        while (addMore) {
            String ingredient = getStringInput("Enter ingredient: ");
            ingredients.add(ingredient);
            addMore = getBooleanInput("Add another ingredient? (true/false): ");
        }

        Supplement supplement = service.createSupplement(name, price, requiredAge, ingredients);
        System.out.println("Supplement added successfully with ID: " + supplement.getId());
    }

    private static void updateProductPrice() {
        System.out.println("\nUPDATE PRODUCT PRICE");
        service.displayAllProducts();
        int productId = getIntInput("Enter product ID: ");
        double newPrice = getDoubleInput("Enter new price: ");

        service.updateProductPrice(productId, newPrice);
        System.out.println("Price updated successfully.");
    }

    // PRESCRIPTION MENU
    private static void prescriptionMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== PRESCRIPTION MANAGEMENT ====");
            System.out.println("1. Create New Prescription");
            System.out.println("2. Add Drug to Prescription");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createPrescription();
                    break;
                case 2:
                    addDrugToPrescription();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createPrescription() {
        System.out.println("\nCREATE NEW PRESCRIPTION");
        service.displayAllCustomers();
        int customerId = getIntInput("Enter customer ID: ");
        String medic = getStringInput("Enter medic name: ");

        Prescription prescription = service.createPrescription(customerId, medic);
        if (prescription != null) {
            System.out.println("Prescription created successfully.");
        } else {
            System.out.println("Failed to create prescription. Check customer ID.");
        }
    }

    private static void addDrugToPrescription() {
        System.out.println("\nADD DRUG TO PRESCRIPTION");
        service.displayAllCustomers();
        int customerId = getIntInput("Enter customer ID: ");

        Customer customer = service.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        List<Prescription> prescriptions = customer.getPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found for this customer.");
            return;
        }

        System.out.println("\nPrescriptions for " + customer.getName() + ":");
        for (int i = 0; i < prescriptions.size(); i++) {
            Prescription p = prescriptions.get(i);
            System.out.println((i + 1) + ". Date: " + p.getDate() + ", Medic: " + p.getMedic());
        }

        int prescriptionIndex = getIntInput("Select prescription (1-" + prescriptions.size() + "): ") - 1;
        if (prescriptionIndex < 0 || prescriptionIndex >= prescriptions.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.println("\nAvailable drugs:");
        service.displayAllDrugs();
        int drugId = getIntInput("Enter drug ID: ");

        service.addDrugToPrescription(prescriptions.get(prescriptionIndex), drugId);
        System.out.println("Drug added to prescription successfully.");
    }

    // BILL MENU
    private static void billMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== BILL MANAGEMENT ====");
            System.out.println("1. Create New Bill");
            System.out.println("2. Add Product to Bill");
            System.out.println("3. Add Prescription to Bill");
            System.out.println("4. Pay Bill");
            System.out.println("5. List All Bills");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createBill();
                    break;
                case 2:
                    addProductToBill();
                    break;
                case 3:
                    addPrescriptionToBill();
                    break;
                case 4:
                    payBill();
                    break;
                case 5:
                    service.displayBills();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createBill() {
        System.out.println("\nCREATE NEW BILL");
        service.displayAllPharmacists();
        int pharmacistId = getIntInput("Enter pharmacist ID: ");

        service.displayAllCustomers();
        int customerId = getIntInput("Enter customer ID: ");

        Bill bill = service.createBill(pharmacistId, customerId);
        if (bill != null) {
            System.out.println("Bill created successfully with ID: " + bill.getId());
        } else {
            System.out.println("Failed to create bill. Check pharmacist and customer IDs.");
        }
    }

    private static void addProductToBill() {
        System.out.println("\nADD PRODUCT TO BILL");
        service.displayBills();
        int billId = getIntInput("Enter bill ID: ");

        service.displayAllProducts();
        int productId = getIntInput("Enter product ID: ");

        service.addProductToBill(billId, productId);
        System.out.println("Product added to bill successfully.");
    }

    private static void addPrescriptionToBill() {
        System.out.println("\nADD PRESCRIPTION TO BILL");
        service.displayBills();
        int billId = getIntInput("Enter bill ID: ");

        Bill bill = service.getBillById(billId);
        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }

        Customer customer = bill.getCustomer();
        List<Prescription> prescriptions = customer.getPrescriptions();
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found for this customer.");
            return;
        }

        System.out.println("\nPrescriptions for " + customer.getName() + ":");
        for (int i = 0; i < prescriptions.size(); i++) {
            Prescription p = prescriptions.get(i);
            System.out.println((i + 1) + ". Date: " + p.getDate() + ", Medic: " + p.getMedic());
        }

        int prescriptionIndex = getIntInput("Select prescription (1-" + prescriptions.size() + "): ") - 1;
        if (prescriptionIndex < 0 || prescriptionIndex >= prescriptions.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        service.addPrescriptionToBill(billId, prescriptions.get(prescriptionIndex));
        System.out.println("Prescription added to bill successfully.");
    }

    private static void payBill() {
        System.out.println("\nPAY BILL");
        System.out.println("Unpaid Bills:");
        service.displayUnpaidBills();
        int billId = getIntInput("Enter bill ID: ");

        service.payBill(billId);
        System.out.println("Bill marked as paid successfully.");
    }

    // INVENTORY MENU
    private static void inventoryMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== INVENTORY MANAGEMENT ====");
            System.out.println("1. Add Products to Inventory");
            System.out.println("2. Remove Expired Products");
            System.out.println("3. Get Earliest Expiration Date");
            System.out.println("4. Display Inventory");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addToInventory();
                    break;
                case 2:
                    service.removeExpiredProducts();
                    System.out.println("Expired products removed successfully.");
                    break;
                case 3:
                    getEarliestExpiration();
                    break;
                case 4:
                    service.displayInventory();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addToInventory() {
        System.out.println("\nADD PRODUCTS TO INVENTORY");
        service.displayAllProducts();
        int productId = getIntInput("Enter product ID: ");
        int quantity = getIntInput("Enter quantity: ");
        LocalDate expirationDate = getDateInput("Enter expiration date (yyyy-MM-dd): ");

        service.addToInventory(productId, quantity, expirationDate);
        System.out.println("Products added to inventory successfully.");
    }

    private static void getEarliestExpiration() {
        System.out.println("\nGET EARLIEST EXPIRATION DATE");
        service.displayAllDrugs();
        int drugId = getIntInput("Enter drug ID: ");

        LocalDate earliestExp = service.getEarliestExpiration(drugId);
        if (earliestExp != null) {
            System.out.println("Earliest expiration date: " + earliestExp);
        } else {
            System.out.println("No expiration date found for this drug or drug not found.");
        }
    }

    // INSURANCE MENU
    private static void insuranceMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== INSURANCE MANAGEMENT ====");
            System.out.println("1. Add New Insurance Company");
            System.out.println("2. Add Drug to Insurance Coverage");
            System.out.println("3. List Insurance Companies");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addInsuranceCompany();
                    break;
                case 2:
                    addDrugToInsurance();
                    break;
                case 3:
                    service.displayAllInsuranceCompanies();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addInsuranceCompany() {
        System.out.println("\nADD NEW INSURANCE COMPANY");
        String name = getStringInput("Enter company name: ");

        InsuranceCompany company = service.createInsuranceCompany(name);
        System.out.println("Insurance company added successfully with ID: " + company.getId());
    }

    private static void addDrugToInsurance() {
        System.out.println("\nADD DRUG TO INSURANCE COVERAGE");
        service.displayAllInsuranceCompanies();
        int insuranceId = getIntInput("Enter insurance company ID: ");

        service.displayAllDrugs();
        int drugId = getIntInput("Enter drug ID: ");

        service.addDrugToInsurance(insuranceId, drugId);
        System.out.println("Drug added to insurance coverage successfully.");
    }

    // DISPLAY MENU
    private static void displayMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n==== DISPLAY DATA ====");
            System.out.println("1. Display All Pharmacists");
            System.out.println("2. Display All Customers");
            System.out.println("3. Display All Products");
            System.out.println("4. Display All Bills");
            System.out.println("5. Display Inventory");
            System.out.println("6. Display Insurance Companies");
            System.out.println("0. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    service.displayAllPharmacists();
                    break;
                case 2:
                    service.displayAllCustomers();
                    break;
                case 3:
                    service.displayAllProducts();
                    break;
                case 4:
                    service.displayBills();
                    break;
                case 5:
                    service.displayInventory();
                    break;
                case 6:
                    service.displayAllInsuranceCompanies();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // UTILITY METHODS FOR INPUT HANDLING
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next(); // Consume invalid input
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            scanner.next(); // Consume invalid input
            System.out.print(prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static boolean getBooleanInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextBoolean()) {
            System.out.println("Please enter true or false.");
            scanner.next(); // Consume invalid input
            System.out.print(prompt);
        }
        boolean value = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static LocalDate getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }
}