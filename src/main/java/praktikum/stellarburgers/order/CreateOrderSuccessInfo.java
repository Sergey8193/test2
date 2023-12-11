package praktikum.stellarburgers.order;

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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CreateOrderOrderData getOrder() { return order; }
    public void setOrder(CreateOrderOrderData createOrderOrderData) { this.order = createOrderOrderData; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    @Override
    public String toString() {
        return "( name: '" + name +
                "', order: '" + order +
                "', success: '" + success + "' )";
    }
}
