package plantpal.model;

import javafx.scene.image.Image;

public class User {
    private int userId;

    private String nickName;
    private String locationName;

    private String desc;

    private Image avatar;

    private String tel;

    public User(int userid) {
        this.userId = userid;
    }

    public User(int id, String nickname, String locationName,
                String desc, Image avatar) {
        this.userId = id;
        this.nickName = nickname;
        this.locationName = locationName;
        this.desc = desc;
        this.avatar = avatar;
    }


    public String getLocationName() {
        return locationName;
    }

    public int getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
