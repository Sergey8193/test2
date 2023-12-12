package praktikum.stellarburgers.order;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrdersSuccessInfo {
    private boolean success;
    private List<GetOrdersOrderData> orders;
    private Integer total;
    private Integer totalToday;


    public GetOrdersSuccessInfo(boolean success, List<GetOrdersOrderData> orders, Integer total, Integer totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public GetOrdersSuccessInfo() { }

    @Override
    public String toString() {
        return "( success: '" + success +
                "', order: '" + orders +
                "', total: '" + total +
                "', totalToday: '" + totalToday + "' )";
    }
}
