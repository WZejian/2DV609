package plantpal.controller;

import java.io.File;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import plantpal.model.ImageHandler;
import plantpal.model.SQLqueries.SQLEditProfileQueries;
import plantpal.model.SQLqueries.SQLGeneralQueries;

public class EditProfileController {
    @FXML
    private Label idlabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView userimg;

    @FXML
    private TextField nicknameField;

    @FXML
    private TextField TELField;

    @FXML
    private ComboBox<String> locationCombox;

    @FXML
    private TextArea descField;

    @FXML
    private ComboBox<String> tagCombox;

    @FXML
    private HBox tagPreview;

    private int userid;

    private boolean avatarChanged = false;
    private Image newAvatar;

    private ArrayList<String> tagLst = new ArrayList<>();

    private final int tagMax = 5;

    public EditProfileController(int userid) {
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        idlabel.setText(String.valueOf(userid));
        usernameLabel.setText(SQLEditProfileQueries.getUsernameById(userid));
        userimg.setImage(SQLGeneralQueries.getProfileImg(userid));

        locationCombox.getItems().addAll(SQLGeneralQueries.getAllLocationsInDB());
        tagCombox.getItems().addAll(SQLGeneralQueries.getAllTagsInDB());

        for (String tag : SQLEditProfileQueries.getTagsByUserid(userid)) {
            tagLst.add(tag);
        }
        updateTagPreview();
    }

    @FXML
    void uploadBtnClick(ActionEvent event) {
        FileChooser fc = new FileChooser();

        // only pictures
        fc.getExtensionFilters()
                .add(new ExtensionFilter("Image Files(*.jpg, *.jpeg, *png)", "*.jpg", "*.jpeg", "*.png"));
        fc.setTitle("Choose your profile image");
        File newImg = fc.showOpenDialog(new Stage());

        // if file is selected
        if (newImg != null) { 
            Image newImg_fx = new Image(newImg.toURI().toString());
            userimg.setImage(ImageHandler.cropToSquare(newImg_fx));
            this.newAvatar = newImg_fx;
            this.avatarChanged = true;
        }

    }

    @FXML
    void addTagBtnClick(ActionEvent event) {
        String tag = tagCombox.getValue();
        if (tag != null && !tagLst.contains(tag) && tagLst.size() < tagMax) {
            tagLst.add(tag);
        }
        updateTagPreview();
    }

    @FXML
    void clearTagBtnClick(ActionEvent event) {
        tagLst.clear();
        updateTagPreview();
    }

    @FXML
    void saveBtnClick(ActionEvent event) {
        if (!nicknameField.getText().equals("")) {
            SQLEditProfileQueries.updateNickname(userid, nicknameField.getText());
        }

        if (!TELField.getText().equals("")) {
            SQLEditProfileQueries.updateTEL(userid, TELField.getText());
        }

        if (!descField.getText().equals("")) {
            SQLEditProfileQueries.updateDesc(userid, descField.getText());
        }

        if (locationCombox.getValue() != null) {
            SQLEditProfileQueries.updateLocation(userid, locationCombox.getValue());
        }

        if (avatarChanged) {
            SQLEditProfileQueries.updateAvatar(userid, userimg.getImage());
        }

        SQLEditProfileQueries.updateUserTags(userid, this.tagLst);
        PromptDialog.showInfo("The changes have been saved successfully.");
    }


    /**
     * Update tag preview hbox.
     */
    private void updateTagPreview() {
        tagPreview.getChildren().clear();
        for (String tag: tagLst) {
            Label tagLabel = new Label("#" + tag);
            tagLabel.getStyleClass().add("tag");
            tagLabel.getStylesheets().add(getClass().getResource("/view/EditProfilePage_css.css").toExternalForm());
            tagPreview.getChildren().add(tagLabel);
        }
    }
}
