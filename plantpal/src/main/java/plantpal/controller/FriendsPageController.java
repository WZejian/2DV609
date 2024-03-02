package plantpal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import plantpal.model.User;
import plantpal.model.SQLqueries.SQLFriendsQueries;

public class FriendsPageController {
    @FXML
    private ToggleGroup friendsTypeSwitch;

    @FXML
    private VBox friendsVbox;

    private int userid;

    public FriendsPageController(int userid) {
        this.userid = userid;
        friendsVbox = new VBox();
    }

    @FXML
    private void initialize() throws IOException {
        viewFollowings();     
    }



    @FXML
    void FollowingBtnClick(ActionEvent event) {
        friendsVbox.getChildren().clear();
        viewFollowings();
    }

    @FXML
    void FolloweBtnClick(ActionEvent event) {
        friendsVbox.getChildren().clear();
        viewFollowers();
    }

    private void viewFollowings() {
        ArrayList<User> followings = SQLFriendsQueries.getFollowingUsers(userid);    

        for(User following: followings) {
            VBox friendCard = getFriendCardVbox(userid, following.getUserId());
            friendsVbox.getChildren().add(friendCard);          
        }
    }

    private void viewFollowers() {
        ArrayList<User> followers = SQLFriendsQueries.getFollowers(userid);

        for(User follower: followers) {
            VBox friendCard = getFriendCardVbox(userid, follower.getUserId());
            friendsVbox.getChildren().add(friendCard);          
        }
    }

    private VBox getFriendCardVbox(int userid, int anotherUserid) {
        VBox card = new VBox();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/FriendsCard.fxml"));
            loader.setController(new FriendsCardController(userid, anotherUserid));
            card = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return card;
    }
    
}
