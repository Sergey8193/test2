package praktikum.stellarburgers.order;

import praktikum.stellarburgers.ingredient.IngredientData;
import praktikum.stellarburgers.user.User;

import java.util.List;

public class CreateOrderOrderData {
    private List<IngredientData> ingredients;
    private String _id;
    private User owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Integer price;

    public List<IngredientData> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<IngredientData> ingredients) {
        this.ingredients = ingredients;
    }

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    @Override
    public String toString() {
        return "( ingredients: '" + ingredients +
                "', _id: '" + _id +
                "', owner: '" + owner +
                "', status: '" + status +
                "', name: '" + name +
                "', createdAt: '" + createdAt +
                "', updatedAt: '" + updatedAt +
                "', number: '" + number +
                "', price: '" + price + "' )";
    }
}
