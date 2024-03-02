package plantpal.model;

import javafx.scene.image.Image;

public class Comment {
    private int commentid;
    private int authorid;
    private String nickname;
    private Image authorImage;
    private String commentText;
    private String commentTime;

    public Comment() {
    }


    public int getCommentid() {
        return this.commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getAuthorid() {
        return this.authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }


    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public Image getAuthorImage() {
        return this.authorImage;
    }

    public void setAuthorImage(Image authorImage) {
        this.authorImage = authorImage;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentTime() {
        return this.commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

}
