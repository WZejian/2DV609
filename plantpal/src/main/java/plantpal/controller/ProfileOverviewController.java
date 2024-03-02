package plantpal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantpal.model.SQLqueries.SQLEditProfileQueries;
import plantpal.model.SQLqueries.SQLPostQueries;
import plantpal.model.SQLqueries.SQLProfileQueries;
import plantpal.model.Post;
import plantpal.model.User;

public class ProfileOverviewController {
    @FXML
    private Label idLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label telLabel;

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label descLabel;

    @FXML
    private ImageView avatarImage;

    @FXML
    private Label followerNbLabel;

    @FXML
    private Label followingNbLabel;

    @FXML
    private HBox tagPreview;

    @FXML
    private VBox postVbox;

    private int userId;

    public ProfileOverviewController(int userid) {
        this.userId = userid;
    }

    @FXML
    private void initialize() {
        User theUser = SQLProfileQueries.getProfileInfo(userId);
        idLabel.setText("ID: " + theUser.getUserId());
        nicknameLabel.setText(theUser.getNickName());
        cityLabel.setText(theUser.getLocationName());
        telLabel.setText(theUser.getTel());
        descLabel.setText(theUser.getDesc());
        avatarImage.setImage(theUser.getAvatar());

        followerNbLabel.setText(String.valueOf(SQLProfileQueries.getFollowerNb(userId)));
        followingNbLabel.setText(String.valueOf(SQLProfileQueries.getFollowingNb(userId)));

        for (String tag : SQLEditProfileQueries.getTagsByUserid(userId)) {
            Label tagLabel = new Label("#" + tag);
            tagLabel.getStyleClass().add("tag");
            tagLabel.getStylesheets().add(getClass().getResource("/view/EditProfilePage_css.css").toExternalForm());
            tagPreview.getChildren().add(tagLabel);
        }
        
        viewMyPosts();
    }

    @FXML
    void myPostClick(ActionEvent event) {
        viewMyPosts();
    }

    @FXML
    void savedPostClick(ActionEvent event) {
        postVbox.getChildren().clear();
        for (int postid : SQLProfileQueries.getSavedPostids(userId)) {
            Post post = SQLPostQueries.getPostByPostid(postid);
            postVbox.getChildren().add(getPostVbox(post, userId));
        }
    }

    private void viewMyPosts() {
        postVbox.getChildren().clear();
        for (int postid : SQLProfileQueries.getMyPostids(userId)) {
            Post post = SQLPostQueries.getPostByPostid(postid);
            postVbox.getChildren().add(getPostVbox(post, userId));
        }
    }

    private VBox getPostVbox(Post post, int userId) {
        VBox onePostVbox = new VBox();
        try {
            FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(getClass().getResource("/view/Post.fxml"));
		    loader.setController(new PostController(post, userId));
		    onePostVbox = loader.load();   
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onePostVbox;
    }

}