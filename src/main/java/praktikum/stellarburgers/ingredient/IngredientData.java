package praktikum.stellarburgers.ingredient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientData {
    private String _id;
    private String name;
    private String type;
    private Integer proteins;
    private Integer fat;
    private Integer carbohydrates;
    private Integer calories;
    private Integer price;
    private String image;
    private String image_mobile;
    private String image_large;
    private Integer __v;

    public IngredientData(String _id, String name, String type, Integer proteins,
                          Integer fat, Integer carbohydrates, Integer calories, Integer price,
                          String image, String image_mobile, String image_large, Integer __v) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        this.image_mobile = image_mobile;
        this.image_large = image_large;
        this.__v = __v;
    }
    
    public IngredientData() {}

    @Override
    public String toString() {
        return "( _id: '" + _id +
                "', name: '" + name +
                "', type: '" + type +
                "', proteins: '" + proteins +
                "', fat: '" + fat +
                "', carbohydrates: '" + carbohydrates +
                "', calories: '" + calories +
                "', price '" + price +
                "', image: '" + image +
                "', image_mobile: '" + image_mobile +
                "', image_large: '" + image_large +
                "', __v: '" + __v + "' )";
    }
}
