package praktikum.stellarburgers.order;

import praktikum.stellarburgers.user.User;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrdersOrderData {
    private List<String> ingredients;
    private String _id;
    private User owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Integer price;

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
