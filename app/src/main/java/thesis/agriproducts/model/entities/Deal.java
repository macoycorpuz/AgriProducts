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
    @SerializedName("content")
    private String content;

    public Deal(int dealId, int productId, int buyerId, String time, String content) {
        this.dealId = dealId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.time = time;
        this.content = content;
    }

    public Deal(int productId, int buyerId, String time, String content) {
        this.productId = productId;
        this.buyerId = buyerId;
        this.time = time;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
