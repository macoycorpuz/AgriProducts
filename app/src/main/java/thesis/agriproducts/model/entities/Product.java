package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;

public class Product {


    @Expose
    private int productId;
    @Expose
    private int sellerId;
    @Expose
    private String productName;
    @Expose
    private String description;
    @Expose
    private double quantity;
    @Expose
    private String unit;
    @Expose
    private double price;
    @Expose
    private String location;
    @Expose
    private double lat;
    @Expose
    private double lng;
    @Expose
    private String status;
    @Expose
    private String productUrl;
    @Expose
    private User user;

    public Product(int sellerId, String productName, String description, double quantity, String unit, double price, String location, double lat, double lng, String status) {
        this.sellerId = sellerId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
    }

    public Product(String productName, String description, double quantity, String unit, double price, String location, double lat, double lng, String status) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
