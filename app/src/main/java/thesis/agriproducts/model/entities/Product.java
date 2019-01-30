package thesis.agriproducts.model.entities;

import com.google.gson.annotations.SerializedName;

public class Product {


    @SerializedName("productId")
    private int productId;
    @SerializedName("sellerId")
    private int sellerId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("description")
    private String description;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private double price;
    @SerializedName("location")
    private String location;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("status")
    private String status;
    @SerializedName("productUrl")
    private String productUrl;

    public Product() {
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getProductId() {
        return productId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getStatus() {
        return status;
    }
}
