package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;


public class Order {

    @Expose
    private int orderId;
    @Expose
    private int order_quantity;
    @Expose
    private String order_status;
    @Expose
    private boolean active;
    @Expose
    private double total;
    @Expose
    private double cash;
    @Expose
    private int productId;
    @Expose
    private int buyerId;
    @Expose
    private int creditId;
    @Expose
    private String created_at;
    @Expose
    private Product product;
    @Expose
    private User user;
    @Expose
    private Credit credit;

    public Order(int quantity, String status, boolean active, double total, double cash, int productId, int userId) {
        this.order_quantity = quantity;
        this.order_status = status;
        this.active = active;
        this.total = total;
        this.cash = cash;
        this.productId = productId;
        this.buyerId = userId;
    }

    public int getId() {
        return orderId;
    }

    public int getQuantity() {
        return order_quantity;
    }

    public String getStatus() {
        return order_status;
    }

    public boolean isActive() {
        return active;
    }

    public double getTotal() {
        return total;
    }

    public int getProduct_id() {
        return productId;
    }

    public int getUser_id() {
        return buyerId;
    }

    public int getCredit_id() {
        return creditId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }

    public Credit getCredit() {
        return credit;
    }

    public double getCash() {
        return cash;
    }
}
