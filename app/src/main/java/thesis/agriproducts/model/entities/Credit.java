package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;

public class Credit {

    @Expose
    private int creditId;
    @Expose
    private String credit_number;
    @Expose
    private String expiry;
    @Expose
    private int csv;

    public Credit(String number, String expiry, int csv) {
        this.credit_number = number;
        this.expiry = expiry;
        this.csv = csv;
    }

    public int getId() {
        return creditId;
    }

    public String getNumber() {
        return credit_number;
    }

    public String getExpiry() {
        return expiry;
    }

    public int getCsv() {
        return csv;
    }
}
