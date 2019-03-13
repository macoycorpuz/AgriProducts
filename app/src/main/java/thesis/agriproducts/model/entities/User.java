package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    private int userId;
    @Expose
    private int accountId;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String number;
    @Expose
    private String address;
    @Expose
    private String url;
    @Expose
    private Boolean isActivated;

    public User(int userId, int accountId, String name, String email, String number, String address) {
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
    }

    public User(String name, String email, String password, String number, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }
}
