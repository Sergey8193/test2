package praktikum.stellarburgers.ingredient;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientsSuccessInfo {
    private boolean success;
    List<IngredientData>  data;

    @Override
    public String toString() {
        return "( data: '" + data +
                "',success: '" + success + "' )";
    }
}
