package plantpal.model;

public class Message {
    private int msgId;
    private int receiverId;
    private int senderId;
    private String msgDate;
    private String content;
    private boolean isImage = false;
    private int imageId = 1;
    private boolean isread = false;

    public Message() {
    }


    public int getMsgId() {
        return this.msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMsgDate() {
        return this.msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsImage() {
        return this.isImage;
    }

    public boolean getIsImage() {
        return this.isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public int getImageId() {
        return this.imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isIsread() {
        return this.isread;
    }

    public boolean getIsread() {
        return this.isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

  
}
