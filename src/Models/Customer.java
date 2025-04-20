package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private InsuranceCompany insurance;
    private List<Bill> bills;
    private List<Prescription> prescriptions;

    public Customer(int id, String name, LocalDate dateOfBirth) {
        super(id, name, dateOfBirth);
        this.bills = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void setInsurance(InsuranceCompany insurance) {
        this.insurance = insurance;
    }

    public InsuranceCompany getInsurance() {
        return insurance;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
