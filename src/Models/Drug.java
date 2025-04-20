package Models;

public class Drug extends Product {
    private boolean requiresPrescription;
    private String condition;

    public Drug(int id, String name, double price, int requiredAge, boolean requiresPrescription, String condition) {
        super(id, name, price, requiredAge);
        this.requiresPrescription = requiresPrescription;
        this.condition = condition;
    }

    public boolean getRequiresPrescription() {
        return requiresPrescription;
    }

    public String getCondition() {
        return condition;
    }
}