package thesis.agriproducts.model.entities;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userId")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("number")
    private String number;
    @SerializedName("address")
    private String address;
    @SerializedName("url")
    private String url;
    @SerializedName("isActivated")
    private Boolean isActivated;

    public User(int userId, String name, String email, String number, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

}
