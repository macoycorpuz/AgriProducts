package thesis.agriproducts.model.entities;

import com.google.gson.annotations.Expose;

public class Message {

    @Expose
    private int messageId;
    @Expose
    private int dealId;
    @Expose
    private int userId;
    @Expose
    private String content;
    @Expose
    private String time;

    public Message(int dealId, int userId, String content, String time) {
        this.dealId = dealId;
        this.userId = userId;
        this.content = content;
        this.time = time;
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
