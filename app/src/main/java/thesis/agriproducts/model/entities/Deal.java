package thesis.agriproducts.model.entities;
import com.google.gson.annotations.SerializedName;

public class Deal {

    @SerializedName("dealId")
    private int dealId;
    @SerializedName("productId")
    private int productId;
    @SerializedName("buyerId")
    private int buyerId;
    @SerializedName("time")
    private String time;
    @SerializedName("productUrl")
    private String productUrl;
    @SerializedName("productName")
    private String productName;
    @SerializedName("name")
    private String name;

    public Deal(int dealId, int productId, int buyerId, String time, String productUrl, String productName, String name) {
        this.dealId = dealId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.time = time;
        this.productUrl = productUrl;
        this.productName = productName;
        this.name = name;
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

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
