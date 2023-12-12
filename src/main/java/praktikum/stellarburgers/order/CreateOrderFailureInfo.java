package praktikum.stellarburgers.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderFailureInfo {
    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "( success: '" + success +
                "', message: '" + message + "' )";
    }
}
