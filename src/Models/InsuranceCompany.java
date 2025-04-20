package Models;

import java.util.ArrayList;
import java.util.List;

public class InsuranceCompany {
    private int id;
    private String name;
    private List<Drug> insuredDrugs;

    public InsuranceCompany(int id, String name) {
        this.id = id;
        this.name = name;
        this.insuredDrugs = new ArrayList<>();
    }

    public void addInsuredDrug(Drug drug) {
        insuredDrugs.add(drug);
    }

    public boolean isInsured(Drug drug) {
        return insuredDrugs.contains(drug);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
