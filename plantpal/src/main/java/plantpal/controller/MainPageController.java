package plantpal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import plantpal.model.SQLqueries.SQLGeneralQueries;

import java.io.IOException;

public class MainPageController {
	@FXML
	private ImageView avatarImage;

	@FXML
	private Label displayNicknameLabel;

	@FXML
	private ToggleGroup leftMenuToggleGroup;

	@FXML
	private VBox rightside;

	private int userId;

	private Stage primaryStage;

	public MainPageController(int userid, Stage stage) {
		this.userId = userid;
		this.primaryStage = stage;
	}

	@FXML
	private void initialize() {
		displayNicknameLabel.setText(SQLGeneralQueries.getNicknameByid(userId));
		avatarImage.setImage(SQLGeneralQueries.getProfileImg(userId));
	}

	@FXML
	void myPageBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/ProfileOverview.fxml"));
			loader.setController(new ProfileOverviewController(userId));
			VBox profilePage = loader.load();
			setRightside(profilePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void chatBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/ChatPage.fxml"));
			loader.setController(new ChatPageController(userId));
			VBox chatPage = loader.load();
			setRightside(chatPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void editProfileBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/EditProfilePage.fxml"));
			loader.setController(new EditProfileController(userId));
			VBox editProfilePage = loader.load();
			setRightside(editProfilePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void friendsBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/FriendsPage.fxml"));
			loader.setController(new FriendsPageController(userId));
			VBox friendsPage = loader.load();
			setRightside(friendsPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void homeBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/HomeOverview.fxml"));
			loader.setController(new HomeOverviewController(userId));
			VBox homePage = loader.load();
			setRightside(homePage);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void logoutBtnClick(ActionEvent event) {
		startLoginPage();
	}

	@FXML
	void marketBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/MarketPlace.fxml"));
			loader.setController(new MarketPlaceController(userId));
			VBox marketPlacePage = loader.load();
			setRightside(marketPlacePage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void orderBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/OrderPage.fxml"));
			OrderPageController orderPageController = new OrderPageController(userId);
			loader.setController(orderPageController);
			VBox orderPage = loader.load();
			setRightside(orderPage);
			orderPageController.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void pointsCenterBtnClick(ActionEvent event) {
		clearRightside();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/PointsCenterPage.fxml"));
			loader.setController(new PointsCenterController(userId));
			VBox pointsCenterPage = loader.load();
			setRightside(pointsCenterPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setRightside(VBox rightside) {
		this.rightside.getChildren().add(rightside);
	}

	public void clearRightside() {
		this.rightside.getChildren().clear();
	}

	/**
	 * change the scene of stage to login page.
	 */
	private void startLoginPage() {
		try {
			// change the scene to login page
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/LoginPage.fxml"));
			loader.setController(new LoginPageController(null));
			HBox loginLayout = (HBox) loader.load();
			primaryStage.setScene(new Scene(loginLayout));
			primaryStage.centerOnScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
