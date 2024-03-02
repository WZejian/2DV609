package plantpal.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import plantpal.model.ImageLib;
import plantpal.model.UserLogin;
import plantpal.model.SQLqueries.SQLLoginQueries;
import plantpal.model.SQLqueries.SQLProfileQueries;

public class RegisterPageController {
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
  private TextField emailField;

	@FXML
	private VBox rightside;

	private Stage primaryStage;
	
	private Image ic_pass_hide = ImageLib.ic_pass_hide;

	private Image ic_pass_show = ImageLib.ic_pass_show;
	

	public RegisterPageController(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

  /**
	 * Register a new user.
	 * @throws IOException
	 */
	@FXML
	void registerBtn(ActionEvent event) {
    String username = usernameField.getText();
		// Create a new user for UserLogin.
		UserLogin userLogin = new UserLogin();

		// Check if the username is empty.
		if (username.isEmpty()) {
			errorMsgLabel.setText("Please enter a username.");
			return;
		} else if (SQLLoginQueries.usernameExists(username)) {
			errorMsgLabel.setText("Username already exists. Try a new one.");
			return;
		}
		
		String email = emailField.getText();
		// Check if the email is empty.
		if (email.isEmpty()) {
			errorMsgLabel.setText("Please enter an email.");
			return;
		} else if (SQLLoginQueries.emailExists(email)) {
			errorMsgLabel.setText("Email already exists. Try a new one.");
			return;
		} 
		// Check if the email is valid.
		if (!userLogin.isValidEmail(email)) {
			errorMsgLabel.setText("Please enter a valid email.");
			return;
		}

		String password = passwordField.getText();
		// Check if the password is empty.
		if (password.isEmpty()) {
			errorMsgLabel.setText("Please enter a password.");
			return;
		} else if (password.length() < 8) {
			errorMsgLabel.setText("Password must be at least 8 characters.");
			return;
		}

		try {
			String hashedPassword = userLogin.passwordToHashString(password);
			userLogin.setHashedPassword(hashedPassword);
			// Insert the user into the UserLogin table.
			SQLLoginQueries.insertUserLogin(username, hashedPassword, email);
			int userId = SQLLoginQueries.getUserIdWithLogin(username, hashedPassword);
			// Insert the user into the UserProfile table.
			SQLLoginQueries.insertNewUserProfile(userId);
			SQLProfileQueries.updateNickname(username, userId);

			// Check if the user was inserted into the database.
			if (SQLLoginQueries.usernameExists(username)) {
				PromptDialog.showInfo("User registered successfully. Please login.");
				clearTextFields();
				return;
			} else {
				errorMsgLabel.setText("User registration failed. Please try again.");
				clearTextFields();
				return;
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
  }


  // TODO: back to Login page.
	@FXML
	void backToLoginBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/view/LoginPage.fxml"));
		LoginPageController rpc = new LoginPageController(primaryStage);
    loader.setController(rpc);
    HBox rootLayout = (HBox) loader.load();
    primaryStage.setScene(new Scene(rootLayout));
    primaryStage.show();
	}

	// Show password when the eye icon is clicked on and hide it when clicked again.
	@FXML
	void showPassBtn(ActionEvent event) {
		showPass = !showPass;
		if (showPass) {
			showPassImg.setImage(ic_pass_show);
			passwordField.setVisible(false);
			passwordTextField.setVisible(true);
			passwordTextField.setText(passwordField.getText());
			
		} else {
			showPassImg.setImage(ic_pass_hide);
			passwordField.setVisible(true);
			passwordTextField.setVisible(false);
			passwordField.setText(passwordField.getText());
		}
	}

  /**
	 * clear username and password fields.
	 */
	public void clearTextFields() {
		usernameField.clear();
		passwordField.clear();
		emailField.clear();
	}
  
}
