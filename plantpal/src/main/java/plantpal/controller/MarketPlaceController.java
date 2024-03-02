package plantpal.controller;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLOrderQueries;
import plantpal.model.SQLqueries.SQLProductQueries;

public class MarketPlaceController {

  @FXML
  private Label resultNbLabel;

  @FXML
  private VBox wholePage;

  @FXML
  private GridPane resultPane;

  @FXML
  private TextField searchField;

  @FXML
  private ComboBox<String> searchCombo;

  private int userid;

  public MarketPlaceController(int userid) {
    this.userid = userid;
  }

  @FXML
  public void initialize() {

    // location combox
    searchCombo.getItems().addAll(SQLGeneralQueries.getAllLocationsInDB());

    // Get all the product ids that are not in order table
    ArrayList<Integer> al1 = SQLProductQueries.getAllProductIdsNotInOrder();
    // Get all the product ids that are paid in order table.
    ArrayList<Integer> al2 = SQLOrderQueries.getAllPaidProductIds();
    // Get all the product ids that need to be shown in the market place.
    ArrayList<Integer> allProductId = new ArrayList<>();
    allProductId.addAll(al1);
    allProductId.addAll(al2);

    layoutProduct(allProductId);
  }

  @FXML
  // Handle the event when publish-product button is clicked.
  void createNewProductBtn(ActionEvent event) {
    wholePage.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/CreateProduct.fxml"));
      loader.setController(new CreateProductController(userid));
      VBox createProductPage = loader.load();
      wholePage.getChildren().add(createProductPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Get the VBox of a product in simple version.
  private VBox getProductVbox(Product product) {
    VBox oneProductVbox = new VBox();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/SimpleProduct.fxml"));
      loader.setController(new SimpleProductController(product));
      oneProductVbox = loader.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return oneProductVbox;
  }

  // Handle the event when a product is clicked.
  public void onProductClicked(VBox vbox, Product product) {
    resultNbLabel.setText("The details of the product are shown below");
    resultPane.getChildren().clear();
    // System.out.println("Product " + product.getProductId() + " is clicked");
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/Product.fxml"));
      loader.setController(new ProductController(userid, product));
      VBox productPage = loader.load();
      resultPane.getChildren().add(productPage);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @FXML
  // Handle the event when search button is clicked.
  void searchBtnClick(ActionEvent event) {
    String searchContent = searchField.getText().toLowerCase();
    
    if (searchContent.equals("") && searchCombo.getValue() == null) { 
      // Show all products that should be shown in the market place.
      initialize();
    } else if (!searchContent.equals("") && searchCombo.getValue() == null) {
      ArrayList<Integer> al1 = SQLProductQueries.searchByDesc(searchContent);
      ArrayList<Integer> al2 = SQLOrderQueries.getAllPaidProductIdsByDesc(searchContent);
      ArrayList<Integer> allProductIds = new ArrayList<>();
      if (al1 != null) {
        allProductIds.addAll(al1);
      }
      if (al2 != null) {
        allProductIds.addAll(al2);
      }
      layoutProduct(allProductIds);
    } else if (searchContent.equals("") && searchCombo.getValue() != null) {
      String locationChosen = searchCombo.getValue().toString();
      int locationId = SQLGeneralQueries.getLocidByLocName(locationChosen);
      ArrayList<Integer> al1 = SQLProductQueries.searchByLoc(locationId);
      ArrayList<Integer> al2 = SQLOrderQueries.getAllPaidProductIdsByLoc(locationId);
      ArrayList<Integer> allProductIds = new ArrayList<>();
      if (al1 != null) {
        allProductIds.addAll(al1);
      }
      if (al2 != null) {
        allProductIds.addAll(al2);
      }
      layoutProduct(allProductIds);
    } else {
      String locationChosen = searchCombo.getValue().toString();
      int locationId = SQLGeneralQueries.getLocidByLocName(locationChosen);
      ArrayList<Integer> al1 = SQLProductQueries.searchByLocAndDesc(searchContent, locationId);
      ArrayList<Integer> al2 = SQLOrderQueries.getAllPaidProductIdsByLocAndDesc(searchContent, locationId);
      ArrayList<Integer> allProductIds = new ArrayList<>();
      if (al1 != null) {
        allProductIds.addAll(al1);
      }
      if (al2 != null) {
        allProductIds.addAll(al2);
      }
      layoutProduct(allProductIds);
    }
  }

  // Layout the products in the market place.
  private void layoutProduct(ArrayList<Integer> allProductIds) {

    resultPane.getChildren().clear();
    
    int column = 0;
    int row = 0;
    int count = 0;

    for (int productId : allProductIds) {
      count++;
      Product oneProduct = SQLProductQueries.getProduct(productId);
      VBox oneProductVBox = getProductVbox(oneProduct);
      oneProductVBox.setOnMouseClicked(event -> onProductClicked(oneProductVBox, oneProduct));
      resultPane.add(oneProductVBox, column, row);

      column++;

      if (column == 2) {
        column = 0;
        row++;
      }
    }

    resultNbLabel.setText(count + " results are found");
    resultPane.toFront();
  }

  
}
