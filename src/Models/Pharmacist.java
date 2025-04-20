package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pharmacist extends Person {
    private double salary;
    private List<Bill> bills;

    public Pharmacist(int id, String name, LocalDate dateOfBirth, double salary) {
        super(id, name, dateOfBirth);
        this.salary = salary;
        this.bills = new ArrayList<>();
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public void increaseSalary(double amount) {
        this.salary += amount;
    }

    public double getSalary() {
        return salary;
    }
}
