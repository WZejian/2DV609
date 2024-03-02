package plantpal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import plantpal.model.Comment;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLPostQueries;

/**
 * Finished! --Meichen
 */
public class PostCommentSetController {
    @FXML
    private Label charLabel;

    @FXML
    private Label commentCount;

    @FXML
    private VBox commentsHolder;

    @FXML
    private TextArea commenttext;

    private ArrayList<Comment> commentsOb = new ArrayList<>();

    private int postid;

    private int userid;

    private final List<KeyCode> arrowkeys = Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN);

    private final int charMax = 500;

    public PostCommentSetController(int postid, int userid) {
        this.postid = postid;
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        charLabel.setText("0/" + charMax);

        // coment count
        commentCount.setText(String.valueOf(SQLPostQueries.getCommentCount(postid)));

        // load all comments
        loadAllComments(postid);
    }

    @FXML
    void commentTyped(KeyEvent event) {
        String content = commenttext.getText();
        if ((!arrowkeys.contains(event.getCode())) && (content.length() >= charMax)) {
            commenttext.setText(content.substring(0, charMax));
            commenttext.positionCaret(content.length());
        }
        charLabel.setText(commenttext.getText().length() + "/" + charMax);
    }


    @FXML
    void sendComment(ActionEvent event) {
        String content = commenttext.getText();
        if (!content.equals("")) {
            SQLPostQueries.insertComment(postid, userid, content);
            SQLGeneralQueries.addPointsbyId(userid, 3);     // 3 points for send a comment
            loadAllComments(postid);
            commenttext.clear();
        }
    }

    @FXML
    void commentRefresh(MouseEvent event) {
        loadAllComments(postid);
    }

    private void loadAllComments(int postid) {
        commentsHolder.getChildren().clear();
        int postAuthorid = SQLPostQueries.getAuthoridByPostid(postid);

        commentsOb.clear();

        commentCount.setText(String.valueOf(SQLPostQueries.getCommentCount(postid)));
        // load all comments
        for (Comment comment : SQLPostQueries.getAllComments(postid)) {
            commentsHolder.getChildren().add(getOneComment(comment, postAuthorid));
            commentsOb.add(comment);
        }
        
    }

    private VBox getOneComment(Comment comment, int postAuthorid) {
        VBox oneComment = new VBox();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/PostComment.fxml"));
            PostCommentController commentController = new PostCommentController(comment, postAuthorid, userid);
            loader.setController(commentController);
            oneComment = (VBox) loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return oneComment;
    }

    private void notifyListeners() {
        loadAllComments(postid);
    }


    public class PostCommentController {
        @FXML
        private Label authorLabel;
    
        @FXML
        private Button deleteBtn;
    
        @FXML
        private Label date;
    
        @FXML
        private Label textContent;
    
        @FXML
        private ImageView userImg;
    
        @FXML
        private Label username;
    
        private Comment comment;
        private int postAuthorid;
        private int userid;
    
        public PostCommentController(Comment comment, int postAuthorid, int userid) {
            this.comment = comment;
            this.postAuthorid = postAuthorid;
            this.userid = userid;
        }
    
        @FXML
        private void initialize() {
            if (comment.getAuthorid() == postAuthorid) {
                authorLabel.setVisible(true);
            } else {
                authorLabel.setVisible(false);
            }
    
            if (comment.getAuthorid() == userid) {
                deleteBtn.setVisible(true);
            } else {
                deleteBtn.setVisible(false);
            }
            userImg.setImage(comment.getAuthorImage());
            username.setText(comment.getNickname());
            date.setText(comment.getCommentTime());
            textContent.setText(comment.getCommentText());
        }
    
        @FXML
        void deletePressed(ActionEvent event) {
            SQLPostQueries.deleteComment(comment.getCommentid());
            notifyListeners();
        }
    }
}
