package plantpal.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import plantpal.model.ImageHandler;
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;

public class SimpleProductController {

  @FXML
  private Label dateLabel;

  @FXML
  private HBox imagesHbox;

  @FXML
  private Label locationLabel;

  @FXML
  private Label priceLabel;

  @FXML
  private ImageView productImg;

  @FXML
  private Label textContent;

  private Product product;

  public void setProduct(Product product) {
    this.product = product;
  }

  public Product getProduct() {
    return this.product;
  }

  public SimpleProductController(Product product) {
    this.product = product;
  }

  @FXML
  private void initialize() {
    // set the text of the labels
    this.priceLabel.setText(this.product.getPrice());

    int locationId = product.getLocationId();
    String location = SQLGeneralQueries.getLocationName(locationId);
    this.locationLabel.setText(location);
    this.textContent.setText(this.product.getDescription());

    // set the image of the product
    Image image = SQLGeneralQueries.getImageByImageid(product.getImageId());
    productImg.setImage(ImageHandler.cropTo32(image));
  }

}
