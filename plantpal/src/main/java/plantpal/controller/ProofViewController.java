package plantpal.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantpal.model.Order;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLOrderQueries;

public class ProofViewController {

  @FXML
  private Button backToMarketBtn;

  @FXML
  private Label dateLabel;

  @FXML
  private HBox imagesHbox;

  @FXML
  private Label locationLabel;

  @FXML
  private Label nicknameLabel;

  @FXML
  private ImageView productImg;

  @FXML
  private VBox productVboxPage;

  private int userId;
  private Order order;

  public void setOrder(Order order) {
    this.order = order;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public ProofViewController(int userId, Order order) {
    this.userId = userId;
    this.order = order;
  }

  public void initialize() {
    // Hardcode for now. Will be changed later.
    int productId = order.getProductId();
    int proofId = SQLOrderQueries.getProofIdByProductId(productId);
    Image image = SQLGeneralQueries.getImageByImageid(proofId);
    productImg.setImage(image);
  }


  @FXML
  void backToMarketBtnClick(ActionEvent event) {
    VBox vb = (VBox) productVboxPage.getParent();
    vb.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/OrderPage.fxml"));
      OrderPageController orderPageController = new OrderPageController(userId);
      loader.setController(orderPageController);
      VBox orderPage = loader.load();
      vb.getChildren().add(orderPage);
      orderPageController.initialize();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
