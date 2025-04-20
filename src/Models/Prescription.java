package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private List<Drug> drugs;
    private LocalDate date;
    private Customer customer;
    private String medic;

    public Prescription(LocalDate date, Customer customer, String medic) {
        this.date = date;
        this.customer = customer;
        this.medic = medic;
        this.drugs = new ArrayList<>();
    }

    public void addDrug(Drug drug) {
        drugs.add(drug);
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public LocalDate getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getMedic() {
        return medic;
    }
}
