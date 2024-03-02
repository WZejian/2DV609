package plantpal.controller;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import plantpal.model.Tree;
import plantpal.model.TreeHistory;
import plantpal.model.SQLqueries.SQLPointsQueries;

public class PointsCenterController {

    @FXML
    private VBox wholePage;

    @FXML
    Label pointsLabel;

    @FXML
    private GridPane treeGrid;

    private VBox treesVbox;

    private int userId;

    public PointsCenterController(int userId) {
        this.userId = userId;
    }

    public void setTotalPoint(String credit) {
        this.pointsLabel.setText(credit);

    }

    @FXML
    private void initialize() throws IOException {

        int points = SQLPointsQueries.getPointsbyId(userId);
        String pointtxt = String.valueOf(points);
        pointsLabel.setText(pointtxt);
        // add trees to treesVbox

        // // add treecard into GridPane
        ArrayList<Integer> alltreeId = SQLPointsQueries.getAllTreeIds();
        int column = 0;
        int row = 0;

        for (int treeId : alltreeId) {

            Tree oneTree = SQLPointsQueries.getTree(treeId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TreeCard.fxml"));
            TreeCardController treeCardController = new TreeCardController(oneTree, this, userId);
            loader.setController(treeCardController);
            VBox treeCardbox = loader.load();

            if (treesVbox == null) {
                treesVbox = new VBox();
            }

            if (treesVbox != null) {
                treesVbox = new VBox();
            }

            treesVbox.getChildren().add(treeCardbox);
            treeGrid.add(treesVbox, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    @FXML
    void historyBtnClick(ActionEvent event) {
        this.wholePage.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/TreeHistoryPage.fxml"));
            loader.setController(new TreeHistoryPageController(this.userId));
            this.wholePage.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class TreeHistoryPageController {

        @FXML
        private VBox wholePageHis;

        @FXML
        private VBox treeHistoryVbox;

        private int userid;

        public TreeHistoryPageController(int userid) {
            this.userid = userid;
        }

        @FXML
        private void initialize() {
            for (TreeHistory his : SQLPointsQueries.getTreeHistory(userId)) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/TreeHistoryCard.fxml"));
                    loader.setController(new TreeHistoryCardController(his));
                    VBox oneHisVbox = loader.load();
                    treeHistoryVbox.getChildren().add(oneHisVbox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        @FXML
        void backCenterClick(ActionEvent event) {
            this.wholePageHis.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/PointsCenterPage.fxml"));
                loader.setController(new PointsCenterController(userid));
                VBox centerPage = loader.load();
                this.wholePageHis.getChildren().add(centerPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class TreeHistoryCardController {

        @FXML
        private Label plantDate;

        @FXML
        private ImageView treeImg;

        @FXML
        private Label treeName;

        private TreeHistory history;

        public TreeHistoryCardController(TreeHistory history) {
            this.history = history;
        }

        @FXML
        private void initialize() {
            treeName.setText(history.getTreeName());
            plantDate.setText(history.getPlantDate());
            treeImg.setImage(history.getTreeImg());
        }

    }
}
