package plantpal.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantpal.model.Order;
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLOrderQueries;
import plantpal.model.SQLqueries.SQLProductQueries;

public class SimpleOrderController {

  @FXML
  private Label dateLabel;

  @FXML
  private Label userLabel;

  @FXML
  private VBox simpleOrderCard;

  @FXML
  private Button deleteBtn;

  @FXML
  private Label descLabel;

  @FXML
  private Button detailViewBtn;

  @FXML
  private Label locationLabel;

  @FXML
  private Label orderTimeLabel;

  @FXML
  private Label priceLabel;

  @FXML
  private Label proofTimeLabel;

  @FXML
  private Button uploadBtn;

  @FXML
  private HBox ButtonLayout;

  @FXML
  private Button viewBtn;

  private int userId;

  private Order order;
  private final String UNFINISHED = "not paid";
  private final String FINISHED = "paid";


  public void setOrder(Order order) {
    this.order = order;
  }

  public Order getOrder() {
    return this.order;
  }

  public SimpleOrderController(int userId,Order order) {
    this.order = order;
    this.userId = userId;
  }
  
  @FXML
  // handle the delete button click event.
  void deleteBtnClick(ActionEvent event) {
    Boolean delete = PromptDialog.deleteOrderConfirmation();
    if (delete) {
      int productId = order.getProductId();
      // delete the product from the database.
      SQLOrderQueries.deleteOrderByProductId(productId);
      // Check if the order is deleted successfully.
      if (!SQLOrderQueries.isProductInOrder(productId)) {
        PromptDialog.showInfo("Order deleted successfully!");
      } else {
        PromptDialog.showInfo("Order deleted failed, please try again!");
      }
      
      simpleOrderCard.setVisible(false);
    }

    refreshOrderPage();
  }


  @FXML
  // handle the detail view button click event.
  void detailViewBtnClick(ActionEvent event) {
    VBox vb = (VBox) simpleOrderCard.getParent();
    VBox parent = (VBox) vb.getParent().getParent().getParent();
    parent.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/OrderProductView.fxml"));
      Product product = SQLProductQueries.getProduct(order.getProductId());
      OrderProductViewController OrderProductViewController = new OrderProductViewController(userId, product);
      loader.setController(OrderProductViewController);
      VBox orderDetailPage = loader.load();
      parent.getChildren().add(orderDetailPage);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @FXML
  // handle the upload button click event.
  void uploadBtnClick(ActionEvent event) {
    VBox vb = (VBox) simpleOrderCard.getParent();
    VBox parent = (VBox) vb.getParent().getParent().getParent();
    parent.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/uploadProof.fxml"));
      uploadProofController uploadProofController = new uploadProofController(userId, order.getProductId());
      loader.setController(uploadProofController);
      VBox uploadProofPage = loader.load();
      parent.getChildren().add(uploadProofPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  // handle the view proof button click event.
  void viewBtnClick(ActionEvent event) {
    VBox vb = (VBox) simpleOrderCard.getParent();
    VBox parent = (VBox) vb.getParent().getParent().getParent();
    parent.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/ProofView.fxml"));
      ProofViewController ProofViewController = new ProofViewController(userId, order);
      loader.setController(ProofViewController);
      VBox uploadViewPage = loader.load();
      parent.getChildren().add(uploadViewPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  @FXML
  // initialize the simple order card.
  private void initialize() {
    if (userId == order.getSellerId()) {
      userLabel.setText("What I sold");
    } else {
      userLabel.setText("What I bought");
    }

    String orderStatus = order.getStatus();
    // Check if the order is finished or not
    if (orderStatus.equals(UNFINISHED)) {
      this.deleteBtn.setVisible(true);
      this.uploadBtn.setVisible(true);
      this.viewBtn.setVisible(false);
      this.proofTimeLabel.setVisible(false);
      this.detailViewBtn.setVisible(true);
      this.orderTimeLabel.setVisible(true);
    } else if (orderStatus.equals(FINISHED)) {
      this.deleteBtn.setVisible(false);
      this.uploadBtn.setVisible(false);
      this.orderTimeLabel.setVisible(true);
      this.detailViewBtn.setVisible(true);
      this.viewBtn.setVisible(true);
      this.proofTimeLabel.setVisible(true);
      // set the proof time of the product.
      this.proofTimeLabel.setText("Proof uploading time: " + order.getProofTime());
    }

    int productId = order.getProductId();
    Product product = SQLProductQueries.getProduct(productId);

    // set the price of the product.
    this.priceLabel.setText(product.getPrice());
    // set the location of the product.
    int locationId = product.getLocationId();
    String location = SQLGeneralQueries.getLocationName(locationId);
    this.locationLabel.setText(location);
    // set the description of the product.
    this.descLabel.setText(product.getDescription());
    // set the ordering time of the product.
    this.orderTimeLabel.setText("Order time: " + order.getOrderTime());
    
  }


  // Refresh the order page.
  private void refreshOrderPage() {
    VBox vb = (VBox) simpleOrderCard.getParent();
    VBox parent = (VBox) vb.getParent().getParent().getParent();
    parent.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/OrderPage.fxml"));
      OrderPageController orderPageController = new OrderPageController(userId);
      loader.setController(orderPageController);
      VBox orderPage = loader.load();
      parent.getChildren().add(orderPage);
      orderPageController.initialize();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
