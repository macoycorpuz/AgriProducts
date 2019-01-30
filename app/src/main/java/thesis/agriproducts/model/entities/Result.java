package thesis.agriproducts.model.entities;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {

    @SerializedName("error")
    private Boolean error = false;
    @SerializedName("message")
    private String message;
    @SerializedName("isAdmin")
    private Boolean isAdmin = false;
    @SerializedName("user")
    private User user;
    @SerializedName("product")
    private Product product;
    @SerializedName("users")
    private List<User> users;
    @SerializedName("products")
    private List<Product> products;
    @SerializedName("deals")
    private List<Deal> deals;
    @SerializedName("messages")
    private List<Message> messages;

    public Result() {
    }

    public Boolean getIsAdmin() {
        return isAdmin;
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
