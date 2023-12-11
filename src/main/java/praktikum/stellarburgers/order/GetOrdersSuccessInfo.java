package praktikum.stellarburgers.order;

import java.util.List;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<GetOrdersOrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<GetOrdersOrderData> orders) {
        this.orders = orders;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(Integer totalToday) {
        this.totalToday = totalToday;
    }

    @Override
    public String toString() {
        return "( success: '" + success +
                "', order: '" + orders +
                "', total: '" + total +
                "', totalToday: '" + totalToday + "' )";
    }
}
