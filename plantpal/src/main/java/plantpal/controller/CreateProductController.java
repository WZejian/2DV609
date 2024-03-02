package plantpal.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import plantpal.model.ImageHandler;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLProductQueries;

public class CreateProductController {

    @FXML
    private Button chooseImgBtn;

    @FXML
    private Label contentCharLimit;

    @FXML
    private TextArea contentField;

    @FXML
    private ComboBox<String> locationCombox;

    @FXML
    private HBox locationPreviewHbox;

    @FXML
    private ScrollPane locationPreviewPane;

    @FXML
    private TextField priceField;

    @FXML
    private ImageView productImg;

    @FXML
    private Label titleCharLimit;

    @FXML
    private VBox wholePage;

    private int userid;
    private ArrayList<String> chosenLocation = new ArrayList<>();
    private boolean imageSelected = false;
    private Image chosenImage;

    // hardcorded values
    private final int locationMax = 1;
    private final int priceInfoMax = 100;
    private final int contentMax = 200;

    public CreateProductController(int userid) {
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        // tag combox
        locationCombox.getItems().addAll(SQLGeneralQueries.getAllLocationsInDB());
    }

    @FXML
    void PublishClick(ActionEvent event) {
        String priceInfo = priceField.getText();
        // Check if the priceInfo is empty
        if (priceInfo.isEmpty()) {
            PromptDialog.showError("Price cannot be empty, please enter a price, such as SEK 35.");
            return;
        }
        // Check if the priceInfo length is greater than 100
        if (priceInfo.length() > priceInfoMax) {
            PromptDialog.showError("Price cannot be longer than 100 characters.");
            return;
        }
        // Check if the priceInfo is a valid price
        if (!isPriceValid(priceInfo)) {
            PromptDialog.showError("Price must be in the format of SEK + positive integer, such as SEK 35.");
            return;
        }

        String textdesc = contentField.getText();
        // Check if the textdesc is empty
        if (textdesc.isEmpty()) {
            PromptDialog.showError("Description cannot be empty, please enter some contents.");
            return;
        }
        // Check if the textdesc length is greater than 200
        if (textdesc.length() > contentMax) {
            PromptDialog.showError("Description cannot be longer than 200 characters.");
            return;
        }

        // Check if the user has chosen one location
        if (chosenLocation.size() != locationMax) {
            PromptDialog.showError("Please choose one location.");
            return;
        }

        String location = chosenLocation.get(0);
        int locationId = SQLGeneralQueries.getLocidByLocName(location);

        if (!imageSelected) {
            PromptDialog.showError("Please select an image.");
            return; 
        }

        int imageId = SQLGeneralQueries.insertImage(this.chosenImage, this.userid);
        

        // Check if the product exists in database.
        if (SQLProductQueries.productExists(userid, textdesc, priceInfo)) {
            PromptDialog.showError(
                    "The product seems already exists. If you want to publish it again, please try to give a unique description for the product.");
            return;
        }

        // Insert the product into database.
        SQLProductQueries.insertProduct(userid, priceInfo, textdesc, locationId, imageId);

        // Check if the product has been successfully inserted into database.
        if (SQLProductQueries.productExists(userid, textdesc, priceInfo)) {
            PromptDialog.showInfo("Your product has been successfully published.");
            SQLGeneralQueries.addPointsbyId(userid, 5);
            backToMarket();
            return;
        } else {
            PromptDialog.showError("Something went wrong, please try again.");
            return;
        }

    }

    @FXML
    void addLocationBtnClick(ActionEvent event) {
        String location = locationCombox.getValue();
        if (location != null && !chosenLocation.contains(location) && chosenLocation.size() < locationMax) {
            locationPreviewPane.setPrefViewportHeight(30);
            chosenLocation.add(location);
            Label locationLabel = new Label("#" + location);
            locationLabel.getStyleClass().add("tag");
            locationLabel.getStylesheets().add(getClass().getResource("/view/CreatePost_css.css").toExternalForm());
            locationPreviewHbox.getChildren().add(locationLabel);
        }
    }

    @FXML
    void clearLocationBtnClick(ActionEvent event) {
        chosenLocation.clear();
        locationPreviewHbox.getChildren().clear();
        locationPreviewPane.setPrefViewportHeight(0);
    }

    @FXML
    void backMarketPlaceClick(ActionEvent event) {
        backToMarket();
    }

    private void backToMarket() {
        // Go back to market place when the back button is clicked.
        VBox rightSide = (VBox) wholePage.getParent();
        rightSide.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MarketPlace.fxml"));
            loader.setController(new MarketPlaceController(userid));
            VBox marketPlacePage = loader.load();
            rightSide.getChildren().add(marketPlacePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if a string has a format of "SEK " + positive double.
    private boolean isPriceValid(String price) {
        if (price.length() < 4) {
            return false;
        }
        if (!price.substring(0, 4).equals("SEK ")) {
            return false;
        }
        try {
            Double priceInt = Double.parseDouble(price.substring(4));
            if (priceInt <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @FXML
    void chooseImgBtnClick(ActionEvent event) {
        FileChooser fc = new FileChooser();

        // only pictures
        fc.getExtensionFilters()
                .add(new ExtensionFilter("Image Files(*.jpg, *.jpeg, *png)", "*.jpg", "*.jpeg", "*.png"));
        fc.setTitle("Choose your product image");
        File newImg = fc.showOpenDialog(new Stage());

        // if file is selected
        if (newImg != null) {
            Image newImg_fx = new Image(newImg.toURI().toString());
            productImg.setImage(ImageHandler.cropTo32(newImg_fx));
            this.imageSelected = true;
            this.chosenImage = newImg_fx;
        }
    }

}
