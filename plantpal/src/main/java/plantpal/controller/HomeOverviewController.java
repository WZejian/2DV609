package plantpal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import plantpal.model.Post;
import plantpal.model.SQLqueries.SQLPostQueries;

public class HomeOverviewController {

  @FXML
  private VBox wholePage;

  @FXML
  private Label resultNbLabel;

  @FXML
  private ComboBox<String> searchCombo;

  @FXML
  private TextField searchField;

  @FXML
  private VBox postVbox;

  private int userid;

  public HomeOverviewController(int userid) {
    this.userid = userid;
  }

  @FXML
  private void initialize() {

    searchCombo.getItems().add("title");
    searchCombo.getItems().add("author");

    searchCombo.setValue("title"); // defalut search title

    setPostVBox(SQLPostQueries.getAllPostid()); // all posts
  }

  @FXML
  void searchBtnClick(ActionEvent event) {
    String searchContent = searchField.getText().toLowerCase();
    if (searchContent.equals("")) { // nothing typed
      setPostVBox(SQLPostQueries.getAllPostid()); // show all posts
    } else {
      if (searchCombo.getValue().equals("title")) {
        setPostVBox(SQLPostQueries.searchByTitle(searchContent));
      } else if (searchCombo.getValue().equals("author")) {
        setPostVBox(SQLPostQueries.searchByAuthor(searchContent));
      }
    }
  }

  @FXML
  void createNewPostBtn(ActionEvent event) {
    wholePage.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/CreatePost.fxml"));
      loader.setController(new CreatePostController(userid));
      VBox createPostPage = loader.load();
      wholePage.getChildren().add(createPostPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setPostVBox(ArrayList<Integer> postIDs) {
    postVbox.getChildren().clear();
    resultNbLabel.setText(postIDs.size() + " results are found");

    for (int postid : postIDs) {
      Post onePost = SQLPostQueries.getPostByPostid(postid);
      postVbox.getChildren().add(getPostVbox(onePost, userid));
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
