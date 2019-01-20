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

    public Product(int productId, int sellerId, String productName, String description, int quantity, double price, String location, String lat, String lng, String status, String productUrl) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.productUrl = productUrl;
    }

    public Product(String productName, double price, String location, String status) {
        this.productName = productName;
        this.price = price;
        this.location = location;
        this.status = status;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
