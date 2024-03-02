package plantpal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import plantpal.model.User;
import plantpal.model.SQLqueries.SQLFriendsQueries;
import plantpal.model.SQLqueries.SQLProfileQueries;

public class FriendsCardController {

    @FXML
    private Button choiceBtn;

    @FXML
    private Label descLabel;

    @FXML
    private Label nicknameLabel;

    @FXML
    private ImageView userImg;


    private int userID;
    private int anotherUserID;

    public FriendsCardController(int userID, int anotherUserID) {
        this.userID = userID;
        this.anotherUserID = anotherUserID;
    }

    @FXML
    private void initialize() {  
        User anotherUser = SQLProfileQueries.getProfileInfo(anotherUserID);
        
        if(anotherUser != null) {
            descLabel.setText(anotherUser.getDesc());
            nicknameLabel.setText(anotherUser.getNickName());
            userImg.setImage(anotherUser.getAvatar());
            boolean isFriend = SQLFriendsQueries.isFriend(userID, anotherUserID);
            boolean isFollowing = SQLFriendsQueries.isFollowing(userID, anotherUserID);
            if(isFriend) {
                choiceBtn.setText("Friend");
            } else if (!isFriend && isFollowing) {
                choiceBtn.setText("Following");
            } else {                                 
                System.out.println();
            }
        }
    }

    @FXML
    void choicebtnClick(ActionEvent event) {

        String status = choiceBtn.getText();
        if(status.equals("Friend")) {
            SQLFriendsQueries.unFollow(userID, anotherUserID);
            choiceBtn.setText("Follow");
        } else if(status.equals("Following")) {
            SQLFriendsQueries.unFollow(userID, anotherUserID);
            choiceBtn.setText("Follow");
        } else {  //status.equals("Follow")
            SQLFriendsQueries.follow(userID, anotherUserID);
            boolean isFriend = SQLFriendsQueries.isFriend(userID, anotherUserID);
            if(isFriend) {
                choiceBtn.setText("Friend");
            } else {  
                choiceBtn.setText("Following");
            }
        }
    }
    

}
