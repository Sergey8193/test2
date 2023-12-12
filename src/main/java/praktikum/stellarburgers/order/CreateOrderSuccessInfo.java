package praktikum.stellarburgers.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderSuccessInfo {
    private String name;
    private CreateOrderOrderData order;
    private boolean success;

    public CreateOrderSuccessInfo(String name, CreateOrderOrderData order, boolean success) {
        this.name = name;
        this.order = order;
        this.success = success;
    }

    public CreateOrderSuccessInfo() { }

    @Override
    public String toString() {
        return "( name: '" + name +
                "', order: '" + order +
                "', success: '" + success + "' )";
    }
}
