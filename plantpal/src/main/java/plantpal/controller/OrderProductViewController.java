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
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLLoginQueries;

public class OrderProductViewController {

  @FXML
  private Button backToOrderPage;

  @FXML
  private Label dateLabel;

  @FXML
  private HBox imagesHbox;

  @FXML
  private Label locationLabel;

  @FXML
  private Label nicknameLabel;

  @FXML
  private Label priceLabel;

  @FXML
  private ImageView productImg;

  @FXML
  private VBox productVboxPage;

  @FXML
  private Label textContent;

  @FXML
  private ImageView userImg;

  private Product product;
  private int userId;

  
  public OrderProductViewController(int userid, Product product){
    this.userId = userid;
    this.product = product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Product getProduct() {
    return this.product;
  }

  @FXML
  private void initialize() {

    // set texts
    String nickname = SQLLoginQueries.getUsernameByUserId(product.getAuthorId());
    nicknameLabel.setText(nickname);
    textContent.setText(product.getDescription());
    int locationId = product.getLocationId();
    String locationName = SQLGeneralQueries.getLocationName(locationId);
    locationLabel.setText(locationName);
    priceLabel.setText(product.getPrice());
    dateLabel.setText(product.getPublishTime());
    userImg.setImage(SQLGeneralQueries.getProfileImg(product.getAuthorId()));

    // set image
    Image image = SQLGeneralQueries.getImageByImageid(product.getImageId());
    productImg.setImage(image);
  }
  

  @FXML
  void backToOrderPageClick(ActionEvent event) {
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
