package plantpal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import plantpal.model.Tree;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import plantpal.model.SQLqueries.SQLPointsQueries;

public class TreeCardController {

    @FXML
    private Label descLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label treeNameLabel;

    @FXML
    private ImageView treeimg;

    private Tree tree;

    private int userId;

    public TreeCardController(Tree tree) {
        this.tree = tree;
    }

    private PointsCenterController pointsCenterController;

    public TreeCardController(Tree tree, PointsCenterController pointsCenterController, int userId) {
        this.tree = tree;
        this.pointsCenterController = pointsCenterController;
        this.userId = userId;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return this.tree;
    }

    @FXML
    private void initialize() {
        // get trees data from database

        treeNameLabel.setText(tree.getTreeName());
        descLabel.setText(tree.getDescription());
        String price = String.valueOf(tree.getCredits());
        priceLabel.setText(price);
        locationLabel.setText(tree.getLocation());
        Image image = SQLGeneralQueries.getImageByImageid(tree.getImageId());
        treeimg.setImage(image);
    }

    @FXML
    void plantBtnClick(ActionEvent event) {

        // get the total
        int totalcredits = SQLPointsQueries.getPointsbyId(userId);
        int treeCredits = this.tree.getCredits();

        // check if user has enough
        if (totalcredits >= treeCredits) {
            String newCredit = String.valueOf(totalcredits - treeCredits);

            // then use adddPointsbyId(), num is negative
            SQLGeneralQueries.addPointsbyId(this.userId, -treeCredits);
            pointsCenterController.setTotalPoint(newCredit);
            SQLPointsQueries.insertTree(userId, this.tree.getTreeId());
            PromptDialog.showInfo("The tree is successfully planted!");
        } else {
            PromptDialog.showError("You do not have sufficient points.");
        }
    }
}
