package praktikum.stellarburgers.order;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
    List<String> ingredients;

    public OrderData(List<String> ingredients) { this.ingredients = ingredients; }
    public OrderData() { }

    @Override
    public String toString() { return "( ingredients: '" + ingredients + "' )"; }
}
