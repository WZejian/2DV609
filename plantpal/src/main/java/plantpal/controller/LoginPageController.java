package plantpal.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import plantpal.model.ImageLib;
import plantpal.model.UserLogin;
import plantpal.model.SQLqueries.SQLLoginQueries;

public class LoginPageController {
	@FXML
	private Label errorMsgLabel;

	@FXML
	private PasswordField passwordField;
	private Boolean showPass = false;

	@FXML
	private TextField passwordTextField;

	@FXML
	private ImageView showPassImg;

	@FXML
	private TextField usernameField;

	@FXML
	private Button signUp;

	@FXML
	private Button signIn;

	@FXML
	private VBox rightside;

	private Stage primaryStage;

	/**
	 * Go to another page to register a new user.
	 * @throws IOException
	 */
	@FXML
	void registerBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/view/RegisterPage.fxml"));
		RegisterPageController rpc = new RegisterPageController(primaryStage);
    loader.setController(rpc);
    HBox rootLayout = (HBox) loader.load();
    primaryStage.setScene(new Scene(rootLayout));
		primaryStage.getIcons().add(ImageLib.ic_plantpal);
    primaryStage.show();
		
	}



	/**
	 * Go to another page to reset password.
	 * @throws IOException
	 */
	@FXML
	void resetPassBtn(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/view/PasswordReset.fxml"));
		ResetPassController rpc = new ResetPassController(primaryStage);
    loader.setController(rpc);
    HBox rootLayout = (HBox) loader.load();
    primaryStage.setScene(new Scene(rootLayout));
    primaryStage.show();
	}


	/**
	 * Sign in.
	 * @throws NoSuchAlgorithmException
	 */
	@FXML
	void signinBtn(ActionEvent event) throws NoSuchAlgorithmException {

		String username = usernameField.getText();
		// Check if the username is empty.
		if (username.isEmpty()) {
			errorMsgLabel.setText("Please enter a username.");
			clearTextFields();
			return;
		} 

		String password = passwordField.getText();
		// Check if the password is empty.
		if (password.isEmpty()) {
			errorMsgLabel.setText("Please enter a password.");
			clearTextFields();
			return;
		} 
		UserLogin userLogin = new UserLogin();
		String hashedPassword = userLogin.passwordToHashString(password);

		// Check if the username exists.
		if (SQLLoginQueries.usernameExists(username)) {
			int userid = SQLLoginQueries.getUserIdWithLogin(username, hashedPassword);
			if (userid == 0) { // wrong password
				errorMsgLabel.setText("The password is incorrect. Please try again.");
				clearTextFields();
				return;
			} else { // correct password
				//PromptDialog.showInfo("Successfully login, welcome to your PlantPal.");
				startMainPage(userid, primaryStage);
			}
		} else {
			errorMsgLabel.setText("Username does not exist. Please try again.");
			clearTextFields();
			return;
		}
	}

	// Show password when the eye icon is clicked on and hide it when clicked again.
	@FXML
	void showPassBtn(ActionEvent event) {
		showPass = !showPass;
		if (showPass) {
			showPassImg.setImage(ImageLib.ic_pass_show);
			passwordField.setVisible(false);
			passwordTextField.setVisible(true);
			passwordTextField.setText(passwordField.getText());
			
		} else {
			showPassImg.setImage(ImageLib.ic_pass_hide);
			passwordField.setVisible(true);
			passwordTextField.setVisible(false);
			passwordField.setText(passwordField.getText());
		}
	}

	public LoginPageController(Stage stage) {
		this.primaryStage = stage;
	}

	/**
	 * Start the main application.
	 * @param userid got from login
	 */
	private void startMainPage(int userid, Stage stage) {
		try {
			// change the scene to main application page
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/MainPage.fxml"));
			MainPageController mainPageController = new MainPageController(userid, stage);
			loader.setController(mainPageController);
			BorderPane rootLayout = (BorderPane) loader.load();
			primaryStage.setScene(new Scene(rootLayout));
			primaryStage.getIcons().add(ImageLib.ic_plantpal);
			primaryStage.centerOnScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear username and password fields.
	 */
	public void clearTextFields() {
		usernameField.clear();
		passwordField.clear();
	}

}
