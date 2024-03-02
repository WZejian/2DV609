package plantpal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantpal.model.ImageLib;
import plantpal.model.Post;
import plantpal.model.SQLqueries.SQLFriendsQueries;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLPostQueries;

/**
 * NOT FINISHED! --Meichen
 */
public class PostController {

    @FXML
    private VBox wholeBox;

    @FXML
    private HBox commentContainer;

    private boolean commentContainerPressed = false;

    @FXML
    private VBox commentVbox;

    @FXML
    private Button followBtn;

    @FXML
    private Label date;

    @FXML
    private Label imgNbLabel;

    @FXML
    private Button leftBtn;

    @FXML
    private HBox likeContainer;

    @FXML
    private ImageView likeImg;

    @FXML
    private Label nbLike;

    @FXML
    private HBox imagesHbox;

    @FXML
    private ImageView postImg;

    @FXML
    private Button rightBtn;

    @FXML
    private HBox saveContainer;

    @FXML
    private ImageView saveImg;

    @FXML
    private HBox shareContainer;

    @FXML
    private HBox tagHbox;

    @FXML
    private Label textContent;

    @FXML
    private Label titleContent;

    @FXML
    private Button viewallBtn;

    @FXML
    private ImageView userImg;

    @FXML
    private Label username;

    @FXML
    private Button deleteBtn;

    // the post object
    private Post post;

    // the user that is using the app
    private int userid;

    // user is on which image
    private int currIdx = 0;

    private boolean viewallBtnClicked = false;

    public PostController(Post post, int userid) {
        this.post = post;
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        leftBtn.setVisible(false);
        rightBtn.setVisible(false);
        imgNbLabel.setVisible(false);
        viewallBtn.setVisible(false);
        deleteBtn.setVisible(false);
        followBtn.setVisible(false);

        // author, date, title
        userImg.setImage(post.getAuthorAvatar());
        username.setText(post.getAuthorName());
        date.setText(post.getDate());
        titleContent.setText(post.getTitle());
        
        // follow button
        if (userid == post.getAuthorid() || SQLFriendsQueries.isFollowing(userid, post.getAuthorid())) {
            followBtn.setVisible(false);
        } else {
            followBtn.setVisible(true);
        }

        // content
        if (post.getTextContent().length() > 300) {
            textContent.setText(post.getTextContent().substring(0, 300));
            viewallBtn.setVisible(true);
        } else {
            textContent.setText(post.getTextContent());
        }

        // tags
        for (String tagStr : post.getTagLst()) {
            Label tagLabel = new Label("#" + tagStr);
            tagLabel.getStyleClass().add("tag");
            tagLabel.getStylesheets().add(
                    PostController.class.getResource("/view/Post_css.css").toExternalForm());
            tagHbox.getChildren().add(tagLabel);
        }

        // images
        if (post.getImageNb() != 0) {
            imagesHbox.setVisible(true);
            imgNbLabel.setVisible(true);
            postImg.setImage(post.getImageAt(currIdx));
            imgNbLabel.setText(constructImgNbLabel());
        } else {
            imagesHbox.getChildren().clear();
        }

        if (post.getImageNb() != 1) {
            leftBtn.setVisible(true);
            rightBtn.setVisible(true);
        }

        leftBtn.setOnAction(event -> {
            if (currIdx == 0) {
                return;
            }
            currIdx -= 1;
            postImg.setImage(post.getImageAt(currIdx));
            imgNbLabel.setText(constructImgNbLabel());

        });

        rightBtn.setOnAction(event -> {
            if (currIdx == post.getImageNb() - 1) {
                return;
            }
            currIdx += 1;
            postImg.setImage(post.getImageAt(currIdx));
            imgNbLabel.setText(constructImgNbLabel());
        });

        // number of likes
        updateNbOfLikeLabel();

        // like post image
        if (SQLPostQueries.postIsLikedByUser(post.getPostid(), userid)) {
            likeImg.setImage(ImageLib.ic_like_clicked);
        } else {
            likeImg.setImage(ImageLib.ic_like_unclicked);
        }

        // save post image
        if (SQLPostQueries.postIsSavedByUser(post.getPostid(), userid)) {
            saveImg.setImage(ImageLib.ic_save_clicked);
        } else {
            saveImg.setImage(ImageLib.ic_save_unclicked);
        }

        // delete button
        if (SQLPostQueries.getAuthoridByPostid(post.getPostid()) == userid) {
            deleteBtn.setVisible(true);
        }
    }

    /**
     * When user click comment.
     */
    @FXML
    void onCommentContainerPressed(MouseEvent event) {
        // already pressed, click to collapse commentvbox
        if (commentContainerPressed) {
            commentVbox.getChildren().clear();
            commentContainerPressed = false;
        }
        // not pressed, click to expend commentvbox
        else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/PostCommentSet.fxml"));
                PostCommentSetController commentSetController = new PostCommentSetController(post.getPostid(), userid);
                loader.setController(commentSetController);
                VBox commentPart = (VBox) loader.load();
                commentContainerPressed = true;
                commentVbox.getChildren().add(commentPart); // add comment part
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * When user click like.
     */
    @FXML
    void onLikeContainerPressed(MouseEvent event) {
        if (SQLPostQueries.postIsLikedByUser(post.getPostid(), userid)) {
            SQLPostQueries.unlikeOnePost(post.getPostid(), userid);
            likeImg.setImage(ImageLib.ic_like_unclicked);
        } else {
            SQLPostQueries.likeOnePost(post.getPostid(), userid);
            SQLGeneralQueries.addPointsbyId(userid, 3); // 3 points for like a post
            likeImg.setImage(ImageLib.ic_like_clicked);

        }
        updateNbOfLikeLabel();
    }

    /**
     * When user click save.
     */
    @FXML
    void onSaveContainerPressed(MouseEvent event) {
        if (SQLPostQueries.postIsSavedByUser(post.getPostid(), userid)) {
            SQLPostQueries.unsaveOnePost(post.getPostid(), userid);
            saveImg.setImage(ImageLib.ic_save_unclicked);
        } else {
            SQLPostQueries.saveOnePost(post.getPostid(), userid);
            saveImg.setImage(ImageLib.ic_save_clicked);
        }
    }

    /**
     * When user click share.
     */
    @FXML
    void onShareContainerPressed(MouseEvent event) {
        PromptDialog.showInfo("Please share the below content to your friend:\n" +
                "Hi! I found a good post. Please search " + "\"" + post.getTitle()
                + "\" by the searchbox in Home to see it!");
        SQLGeneralQueries.addPointsbyId(userid, 2); // 2 points for share a post
    }

    /**
     * When the content is too long and user clicks view all/view less.
     */
    @FXML
    void viewallClick(ActionEvent event) {
        viewallBtnClicked = !viewallBtnClicked;
        if (viewallBtnClicked) {
            textContent.setText(post.getTextContent());
            viewallBtn.setText("view less");
        } else {
            textContent.setText(post.getTextContent().substring(0, 300));
            viewallBtn.setText("view all");
        }

    }

    @FXML
    void followBtnClick(ActionEvent event) {
        SQLFriendsQueries.follow(userid, post.getAuthorid());
        if (userid == post.getAuthorid() || SQLFriendsQueries.isFollowing(userid, post.getAuthorid())) {
            followBtn.setVisible(false);
        } else {        // should not run this part
            followBtn.setVisible(true);
        }
    }

    /**
     * When user is the author and wants to delete the post.
     */
    @FXML
    void delBtnClick(ActionEvent event) {
        SQLPostQueries.deletePost(post.getPostid());
        wholeBox.getChildren().clear(); // maybe better solution?
    }

    /**
     * Help to generate a current number of image text.
     */
    private String constructImgNbLabel() {
        return String.valueOf(currIdx + 1) + "/" + String.valueOf(post.getImageNb());
    }

    /**
     * Update the number of likes of the post.
     */
    private void updateNbOfLikeLabel() {
        nbLike.setText(String.valueOf(SQLPostQueries.getPostLikeCount(post.getPostid())));
    }

}
