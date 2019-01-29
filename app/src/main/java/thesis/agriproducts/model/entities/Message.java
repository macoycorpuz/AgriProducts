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

    public Message(int messageId, int dealId, int userId, String content, String time) {
        this.messageId = messageId;
        this.dealId = dealId;
        this.userId = userId;
        this.content = content;
        this.time = time;
    }

    public Message(int dealId, int userId, String content, String time) {
        this.dealId = dealId;
        this.userId = userId;
        this.content = content;
        this.time = time;
    }

    public Message(int dealId, int userId, String content) {
        this.dealId = dealId;
        this.userId = userId;
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
