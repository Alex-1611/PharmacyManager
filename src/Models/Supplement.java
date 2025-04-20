package Models;

import java.util.List;

public class Supplement extends Product {
    private List<String> ingredients;

    public Supplement(int id, String name, double price, int requiredAge, List<String> ingredients) {
        super(id, name, price, requiredAge);
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
