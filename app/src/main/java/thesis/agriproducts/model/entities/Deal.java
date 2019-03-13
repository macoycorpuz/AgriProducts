package thesis.agriproducts.model.entities;
import com.google.gson.annotations.Expose;

public class Deal {

    @Expose
    private int dealId;
    @Expose
    private int productId;
    @Expose
    private int buyerId;
    @Expose
    private String time;
    @Expose
    private Product product;
    @Expose
    private User user;

    public Deal(int dealId, int productId, int buyerId, String time, Product product, User user) {
        this.dealId = dealId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.time = time;
        this.product = product;
        this.user = user;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
