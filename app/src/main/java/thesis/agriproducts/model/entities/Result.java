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
    private List<User> users;
    @Expose
    private List<Product> products;
    @Expose
    private List<Deal> deals;
    @Expose
    private List<Message> messages;

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
}
