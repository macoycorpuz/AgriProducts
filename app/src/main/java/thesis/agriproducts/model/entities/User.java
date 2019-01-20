package thesis.agriproducts.model.entities;
import android.net.Uri;

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

    public User(int userId, String name, String email, String password, String number, String address, boolean activate, String url) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.address = address;
        this.url = url;
    }

    public User(String name, String email, String password, String number, String address, boolean activate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.address = address;
    }

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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
