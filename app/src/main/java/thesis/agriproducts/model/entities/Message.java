package thesis.agriproducts.model.entities;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("messageId")
    private int messageId;
    @SerializedName("dealId")
    private int dealId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("content")
    private String content;
    @SerializedName("time")
    private String time;

    public Message(int dealId, int userId, String content) {
        this.dealId = dealId;
        this.userId = userId;
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getDealId() {
        return dealId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
