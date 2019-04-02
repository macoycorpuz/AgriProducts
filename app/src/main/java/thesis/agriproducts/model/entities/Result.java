package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;
import java.util.List;

public class Result {

    @Expose
    private Boolean error = false;
    @Expose
    private String message;
    
    @Expose
    private User user;
    @Expose
    private Product product;
    @Expose
    private Order order;
    @Expose
    private List<User> users;
    @Expose
    private List<Product> products;
    @Expose
    private List<Deal> deals;
    @Expose
    private List<Message> messages;
    @Expose
    private List<Order> orders;

    public Result() {
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public Order getOrder() {
        return order;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
