package plantpal.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLLoginQueries;
import plantpal.model.SQLqueries.SQLOrderQueries;
import plantpal.model.SQLqueries.SQLProductQueries;

public class ProductController {
  @FXML
  private Button addOrderBtn;

  @FXML
  private Button backToMarketBtn;

  @FXML
  private Label dateLabel;

  @FXML
  private VBox productVboxPage;

  @FXML
  private Button deleteBtn;

  @FXML
  private HBox imagesHbox;

  @FXML
  private Label locationLabel;

  @FXML
  private Label nicknameLabel;

  @FXML
  private ImageView productImg;

  @FXML
  private Button sendMsgBtn;

  @FXML
  private VBox soldoutvbox;

  @FXML
  private Label textContent;

  @FXML
  private Label priceLabel;

  @FXML
  private ImageView userImg;

  // user id of the person that is using the app
  private int userid;

  private Product product;

  // userid and product obj ok aswell
  public ProductController(int userid, Product product) {
    this.userid = userid;
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
    if (userid == product.getAuthorId()) {
      deleteBtn.setVisible(true);
      sendMsgBtn.setVisible(false);
      addOrderBtn.setVisible(false);
    } else {
      deleteBtn.setVisible(false);
      sendMsgBtn.setVisible(true);
      addOrderBtn.setVisible(true);
    }

    // soldout image
    if (product.isSold()) {
      soldoutvbox.setVisible(true);
      addOrderBtn.setVisible(false);
    } else {
      soldoutvbox.setVisible(false);
    }

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

  /**
   * Add an order to the product.
   * 
   * @param event
   */
  @FXML
  void addOrderBtnClick(ActionEvent event) {
    int productId = product.getProductId();
    Boolean addOrder = PromptDialog.addOrderConfirmation();
    if (addOrder) {
      SQLOrderQueries.addOrder(userid, product);

      if (SQLOrderQueries.isProductInOrder(productId)) {
        PromptDialog.showInfo("Order added successfully!");
      } else {
        PromptDialog.showError("Order addition failed! Please try again.");
        return;
      }
    } else {
      PromptDialog.showInfo("Order addition cancelled!");
    }

    backToMarket();

    return;

  }

  @FXML
  // delete the product published.
  void deleteBtnClick(ActionEvent event) {
    int productId = product.getProductId();
    boolean deleteOrNot = PromptDialog.deleteProductConfirmation();
    if (deleteOrNot) {
      SQLProductQueries.deleteProductById(product.getProductId());
      if (!SQLProductQueries.productExists(productId)) {
        PromptDialog.showInfo("Product deleted successfully!");
      } else {
        PromptDialog.showError("Product deletion failed!");
      }
    } else {
      PromptDialog.showInfo("Product deletion cancelled!");
    }

    backToMarket();
  }


  @FXML
  // Hanlde the send message button click.
  void sendMsgBtnClick(ActionEvent event) {
    int productCreatorId = product.getAuthorId();
    PromptDialog.showInfo("The seller id is " + productCreatorId
        + ". You can chat with the seller by searching the seller id in the chat page.");
    GridPane gridPane = (GridPane) productVboxPage.getParent();
    VBox parent = (VBox) gridPane.getParent().getParent().getParent();
    parent.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/ChatPage.fxml"));
      loader.setController(new ChatPageController(userid));
      VBox chatPage = loader.load();
      parent.getChildren().add(chatPage);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @FXML
  // go back to market place.
  void backToMarketBtnClick(ActionEvent event) {
    backToMarket();
  }

  private void backToMarket() {
    GridPane gridPane = (GridPane) productVboxPage.getParent();
    VBox parent = (VBox) gridPane.getParent().getParent().getParent();
    parent.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/MarketPlace.fxml"));
      loader.setController(new MarketPlaceController(userid));
      VBox marketPlacePage = loader.load();
      parent.getChildren().add(marketPlacePage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
