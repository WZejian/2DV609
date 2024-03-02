package plantpal.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import plantpal.model.Post;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLPostQueries;

/**
 * NOT FINISHED. --meichen
 */
public class CreatePostController {
    @FXML
    private VBox wholePage;

    // images
    @FXML
    private Button chooseImgBtn;

    @FXML
    private CheckBox textonlyCheckbox;

    @FXML
    private HBox imagePreviewHbox;

    @FXML
    private ScrollPane imgscrollpane;

    // title
    @FXML
    private Label titleCharLimit;

    @FXML
    private TextField titleField;

    // content
    @FXML
    private Label contentCharLimit;

    @FXML
    private TextArea contentField;

    // tags
    @FXML
    private ComboBox<String> tagCombox;

    @FXML
    private HBox tagPreviewHbox;

    @FXML
    private ScrollPane tagPreviewPane;

    // hardcorded values
    private final int imgMax = 20;
    private final int tagMax = 3;
    private final int titleMax = 100;
    private final int contentMax = 2000;
    private final List<KeyCode> arrowkeys = Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN);

    // other java attributes
    private ArrayList<File> chosenImages = new ArrayList<>();
    private ArrayList<String> chosenTags = new ArrayList<>();
    private int userid;

    public CreatePostController(int userid) {
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        // char limit
        titleCharLimit.setText("0/" + titleMax);
        contentCharLimit.setText("0/" + contentMax);

        // tag combox
        tagCombox.getItems().addAll(SQLGeneralQueries.getAllTagsInDB());
    }

    // images
    /**
     * When user wants to select an image from her device.
     */
    @FXML
    void chooseImgBtnClick(ActionEvent event) {
        FileChooser fc = new FileChooser();

        // only pictures
        fc.getExtensionFilters()
                .add(new ExtensionFilter("Image Files(*.jpg, *.jpeg, *png)", "*.jpg", "*.jpeg", "*.png"));
        fc.setTitle("Choose your post images");
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
            loader.setController(new CreatePostImagePreviewController(idx));
            oneImgVbox = (VBox) loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return oneImgVbox;
    }

    /**
     * If text-only checkbox is clicked.
     */
    @FXML
    void textOnlyClick(ActionEvent event) {
        if (textonlyCheckbox.isSelected()) {
            chooseImgBtn.setVisible(false);
            chosenImages.clear();
            updatePreviewImages();
        } else {
            chooseImgBtn.setVisible(true);
        }
    }

    // title
    /**
     * To make sure user does not type too much.
     */
    @FXML
    void titleTyped(KeyEvent event) {
        String content = titleField.getText();
        if ((!arrowkeys.contains(event.getCode())) && (content.length() >= titleMax)) {
            titleField.setText(content.substring(0, titleMax));
            titleField.positionCaret(content.length());
        }
        titleCharLimit.setText(titleField.getText().length() + "/" + titleMax);

    }

    // content
    /**
     * To make sure user does not type too much.
     */
    @FXML
    void contentTyped(KeyEvent event) {
        String content = contentField.getText();
        if ((!arrowkeys.contains(event.getCode())) && (content.length() >= contentMax)) {
            contentField.setText(content.substring(0, contentMax));
            contentField.positionCaret(content.length());
        }
        contentCharLimit.setText(contentField.getText().length() + "/" + contentMax);
    }

    // tags
    /**
     * When user chooses a tag and wants to add it to tag list.
     */
    @FXML
    void addTagBtnClick(ActionEvent event) {
        String tag = tagCombox.getValue();
        if (tag != null && !chosenTags.contains(tag) && chosenTags.size() < tagMax) {
            tagPreviewPane.setPrefViewportHeight(30);
            chosenTags.add(tag);
            Label tagLabel = new Label("#" + tag);
            tagLabel.getStyleClass().add("tag");
            tagLabel.getStylesheets().add(getClass().getResource("/view/CreatePost_css.css").toExternalForm());
            tagPreviewHbox.getChildren().add(tagLabel);
        }
    }

    /**
     * Delete all current tags.
     */
    @FXML
    void clearTagBtnClick(ActionEvent event) {
        chosenTags.clear();
        tagPreviewHbox.getChildren().clear();
        tagPreviewPane.setPrefViewportHeight(0);
    }

    // Other methods
    /**
     * User finished writing and decided to publish the post.
     */
    @FXML
    void PostClick(ActionEvent event) {
        boolean titleFilled = (!titleField.getText().equals(""));
        boolean contentFilled = (!contentField.getText().equals(""));

        if (titleFilled && contentFilled) {
            Post newPost = new Post();
            // author
            newPost.setAuthorid(userid);

            // title
            newPost.setTitle(titleField.getText());

            // content
            newPost.setTextContent(contentField.getText());

            // images
            for (File file : chosenImages) {
                Image image = new Image(file.toURI().toString());
                newPost.addToImageLst(image);
            }

            // tags
            for (String tagStr : chosenTags) {
                newPost.addToTagLst(tagStr);
            }

            SQLPostQueries.insertPost(newPost);
            SQLGeneralQueries.addPointsbyId(userid, 5);     // 5 points for create a new post
            PromptDialog.showInfo("The post is successfully posted!");
        } else {
            PromptDialog.showError("Please fill title and content first.");
        }
    }


    @FXML
    void backHomeClick(ActionEvent event) {
        wholePage.getChildren().clear();
        try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/HomeOverview.fxml"));
			loader.setController(new HomeOverviewController(userid));
			VBox homePage = loader.load();
			wholePage.getChildren().add(homePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * The class for image previre card.
     */
    public class CreatePostImagePreviewController {
        @FXML
        private ImageView imgPreview;

        @FXML
        private Button leftBtn;

        @FXML
        private Button rightBtn;

        @FXML
        private Label seqNumLabel;

        private int idx = -1;

        public CreatePostImagePreviewController(int idx) {
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
