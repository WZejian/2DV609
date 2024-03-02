package plantpal.controller;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import plantpal.model.Order;
import plantpal.model.SQLqueries.SQLOrderQueries;

public class OrderPageController {

  @FXML
  private ToggleButton finishedBtn;

  @FXML
  private ToggleGroup friendsTypeSwitch;

  @FXML
  private VBox resultPane;

  @FXML
  private ToggleButton unfinishedBtn;

  @FXML
  private VBox wholePage;

  private int userid;

  public OrderPageController(int userid) {
    this.userid = userid;
  }


  @FXML
  void finishedBtnClick(ActionEvent event) {
    resultPane.setVisible(true);
    resultPane.getChildren().clear();
    ArrayList<Integer> finishedProductIds = SQLOrderQueries.getAllFinishedProductIdsByUserId(userid);
    for (int productId : finishedProductIds) {
      Order oneOrder = SQLOrderQueries.getOrder(productId);
      VBox oneOrderVBox = getOrderVbox(oneOrder);
      resultPane.getChildren().add(oneOrderVBox);
    }

  }

  @FXML
  void unfinishedBtnClick(ActionEvent event) {
    handleUnfinishedBtnClick();
  }

  // Get the VBox of an order in simple version.
  private VBox getOrderVbox(Order order) {
    VBox oneOrderVbox = new VBox();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/SimpleOrder.fxml"));
      loader.setController(new SimpleOrderController(userid,order));
      oneOrderVbox = loader.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return oneOrderVbox;
  }

  void initialize() {
    unfinishedBtn.setSelected(true);
    handleUnfinishedBtnClick();
    
  }

  // Handle the event when unfinished-order button is clicked.
  private void handleUnfinishedBtnClick() {
    resultPane.setVisible(true);
    resultPane.getChildren().clear();
    ArrayList<Integer> unfinishedProductIds = SQLOrderQueries.getAllUnfinishedProductIdsByUserId(userid);
    for (int productId : unfinishedProductIds) {
        Order oneOrder = SQLOrderQueries.getOrder(productId);
        VBox oneOrderVBox = getOrderVbox(oneOrder);
        resultPane.getChildren().add(oneOrderVBox);
    }
  }
}
