package plantpal.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import plantpal.model.Product;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLOrderQueries;
import plantpal.model.SQLqueries.SQLProductQueries;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class uploadProofController {

  @FXML
  private Button backBtn;

  @FXML
  private Button chooseImgBtn;

  @FXML
  private HBox imagePreviewHbox;

  @FXML
  private ScrollPane imgscrollpane;

  @FXML
  private Button uploadBtn;

  @FXML
  private VBox wholePage;

  private int userId;
  private int productId;
  private ArrayList<File> chosenImages = new ArrayList<>();
  private final int imgMax = 1;
  private final static String FINISHED = "paid";


  public uploadProofController(int userId, int productId) {
    this.userId = userId;
    this.productId = productId;
  }

  @FXML
  void backBtnClick(ActionEvent event) {
    // Go back to market place when the back button is clicked.
    VBox rightSide = (VBox) wholePage.getParent();
    rightSide.getChildren().clear();

    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/OrderPage.fxml"));
      OrderPageController orderPageController = new OrderPageController(userId);
      loader.setController(orderPageController);
      VBox orderPage = loader.load();
      rightSide.getChildren().add(orderPage);
      orderPageController.initialize();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @FXML
  void chooseImgBtnClick(ActionEvent event) {
    FileChooser fc = new FileChooser();

    // only pictures
    fc.getExtensionFilters()
        .add(new ExtensionFilter("Image Files(*.jpg, *.jpeg, *png)", "*.jpg", "*.jpeg", "*.png"));
    fc.setTitle("Choose your product image");
    List<File> files = fc.showOpenMultipleDialog(new Stage());

    if (files != null) { // if any file is selected
      for (File f : files) {
        if (chosenImages.size() < imgMax) {
          chosenImages.add(f);
        }
      }
      updatePreviewImages();
    }
  }

  /**
   * Update image preview box.
   */
  public void updatePreviewImages() {
    imagePreviewHbox.getChildren().clear();
    if (chosenImages.size() == 0) {
      imgscrollpane.setPrefViewportHeight(0);
    } else {
      imgscrollpane.setPrefViewportHeight(210);
    }

    for (int i = 0; i < chosenImages.size(); i++) {
      imagePreviewHbox.getChildren().add(FileToImgPreviewBox(i));
    }
  }

  /**
   * Convert File type to a vbox with chosen file image.
   */
  private VBox FileToImgPreviewBox(int idx) {
    VBox oneImgVbox = new VBox();
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/CreatePostImagePreview.fxml"));
      loader.setController(new CreateProofImagePreviewController(idx));
      oneImgVbox = (VBox) loader.load();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return oneImgVbox;
  }

  @FXML
  // Upload the proof.
  void uploadBtnClick(ActionEvent event) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    if (chosenImages.size() == 0) {
      PromptDialog.showError("Please choose an image to upload.");
      return;
    } else {
      // images
      File file = chosenImages.get(0);
      Image image = new Image(file.toURI().toString());
      // insert image into database and get the image id.
      int proofId = SQLGeneralQueries.insertImage(image, userId);
      System.out.println("The id in image tabel is: " + proofId);
      // insert proofid into proof table.
      SQLOrderQueries.updateProof(productId, proofId);
      int proof = SQLOrderQueries.getProofIdByProductId(productId);
      System.out.println("The proofid in order table is: " + proof);
      // Check if the upload is successful.
      if (proof == proofId) {
        String status = FINISHED;
        SQLOrderQueries.updateOrder(productId, status, timestamp);
        SQLProductQueries.updateSoldStatus(productId);
        PromptDialog.showInfo("Upload successfully!");
        // Add points to the buyer.
        SQLGeneralQueries.addPointsbyId(userId, 30);
        Product product = SQLProductQueries.getProduct(productId);
        int sellerId = product.getAuthorId();
        // Add points to the seller.
        SQLGeneralQueries.addPointsbyId(sellerId, 30);
        refreshOrderPage();
      } else {
        PromptDialog.showError("Upload failed, please try again.");
      }
    }

    
  }

  // Refresh the order page.
  private void refreshOrderPage() {
    VBox vb = (VBox) wholePage.getParent();
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

  /**
   * The class for image previre card.
   */
  public class CreateProofImagePreviewController {
    @FXML
    private ImageView imgPreview;

    @FXML
    private Label seqNumLabel;

    @FXML
    private Button leftBtn;

    @FXML
    private Button rightBtn;

    private int idx = -1;

    public CreateProofImagePreviewController(int idx) {
      this.idx = idx;
    }

    @FXML
    private void initialize() {
      Image image = new Image(chosenImages.get(idx).toURI().toString());
      imgPreview.setImage(image);
      seqNumLabel.setText((idx + 1) + "/" + chosenImages.size());

      if (idx == 0) {
        leftBtn.setVisible(false);
      }

      if (idx == chosenImages.size() - 1) {
        rightBtn.setVisible(false);
      }
    }

    @FXML
    void delImgBtnClick(ActionEvent event) {
      chosenImages.remove(idx);
      updatePreviewImages();
    }

    @FXML
    void leftBtnClick(ActionEvent event) {
      File file = chosenImages.get(idx);
      File previous_file = chosenImages.get(idx - 1);
      chosenImages.set(idx - 1, file);
      chosenImages.set(idx, previous_file);
      updatePreviewImages();
    }

    @FXML
    void rightBtnClick(ActionEvent event) {
      File file = chosenImages.get(idx);
      File next_file = chosenImages.get(idx + 1);
      chosenImages.set(idx + 1, file);
      chosenImages.set(idx, next_file);
      updatePreviewImages();
    }
  }
}
