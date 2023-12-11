package praktikum.stellarburgers.ingredient;

import java.util.List;

public class IngredientsSuccessInfo {
    private boolean success;
    List<IngredientData>  data;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<IngredientData> getData() { return data; }
    public void setData(List<IngredientData> data) { this.data = data; }

    @Override
    public String toString() {
        return "( data: '" + data +
                "',success: '" + success + "' )";
    }
}
