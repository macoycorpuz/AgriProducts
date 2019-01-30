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

    public Deal() {
    }

    public int getDealId() {
        return dealId;
    }

    public int getProductId() {
        return productId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public String getTime() {
        return time;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getName() {
        return name;
    }
}
