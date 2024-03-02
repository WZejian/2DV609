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

public class ResetPassController {

	@FXML
	private ImageView showPassImg;

	@FXML
	private TextField usernameField;

	// set this to "a validation email is sent to 12345@gmail.com" when user click
	// validate btn
	@FXML
	private Label emailValidateLabel;

	@FXML
	private Label errorMsgLabel;

	@FXML
	private TextField newPasswordTextField;

	@FXML
	private PasswordField newPasswordField;
	private Boolean showPass1 = false;

	@FXML
	private TextField AgainPasswordTextField;

	@FXML
	private PasswordField AgainPasswordField;
	private Boolean showPass2 = false;

	@FXML
	private VBox rightside;

	private Stage primaryStage;

	private Image ic_pass_hide = ImageLib.ic_pass_hide;

	private Image ic_pass_show = ImageLib.ic_pass_show;

	public ResetPassController(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// Validate button
	@FXML
	void validateBtn(ActionEvent event) {
		String username = usernameField.getText();
		String userEmail = SQLLoginQueries.getUserEmail(username);
		PromptDialog.showInfo("A validation email is sent to " + userEmail + ", please check your email.");
	}

	/**
	 * Reset password.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@FXML
	void resetBtn(ActionEvent event) throws NoSuchAlgorithmException {

		String username = usernameField.getText();
		// Check if the username is empty.
		if (username.isEmpty()) {
			errorMsgLabel.setText("Please enter your username.");
			clearTextFields();
			return;
		}

		String newPassword = newPasswordField.getText();
		// Check if the password is empty.
		if (newPassword.isEmpty()) {
			errorMsgLabel.setText("Please enter your new password.");
			clearTextFields();
			return;
		} else if (newPassword.length() < 8) {
			errorMsgLabel.setText("Password must be at least 8 characters.");
			clearTextFields();
			return;
		}

		String againPassword = AgainPasswordField.getText();
		// Check if the password is empty.
		if (newPassword.isEmpty()) {
			errorMsgLabel.setText("Please enter your new password again.");
			clearTextFields();
			return;
		}
		// Check if the re-entered password is the same as the new password entered.
		else if (!againPassword.equals(newPassword)) {
			errorMsgLabel.setText("The passwords do not match. Please try again.");
			clearTextFields();
			return;
		}

		UserLogin userLogin = new UserLogin();
		String newHashedPassword = userLogin.passwordToHashString(newPassword);
		// Check if the username exists in the database.
		// If not, show error message.
		// If yes, update user password with the new password.
		if (SQLLoginQueries.usernameExists(username)) {
			SQLLoginQueries.resetPassword(username, newHashedPassword);
			// Check if the password is updated successfully.
			if (SQLLoginQueries.getUserIdWithLogin(username, newHashedPassword) == 0) {
				errorMsgLabel.setText("Password update failed. Please try again.");
				clearTextFields();
				return;
			}
			PromptDialog.showInfo("New password has been reset successfully, now you can login with your new password.");
			clearTextFields();
			return;
		} else {
			errorMsgLabel.setText("Username does not exist. Please try again.");
			clearTextFields();
			return;
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
	void showPassBtn1(ActionEvent event) {
		showPass1 = !showPass1;
		if (showPass1) {
			showPassImg.setImage(ic_pass_show);
			newPasswordField.setVisible(false);
			newPasswordTextField.setVisible(true);
			newPasswordTextField.setText(newPasswordField.getText());

		} else {
			showPassImg.setImage(ic_pass_hide);
			newPasswordField.setVisible(true);
			newPasswordTextField.setVisible(false);
			newPasswordField.setText(newPasswordField.getText());
		}
	}

	// Show password when the eye icon is clicked on and hide it when clicked again.
	@FXML
	void showPassBtn2(ActionEvent event) {
		showPass2 = !showPass2;
		if (showPass2) {
			showPassImg.setImage(ic_pass_show);
			AgainPasswordField.setVisible(false);
			AgainPasswordTextField.setVisible(true);
			AgainPasswordTextField.setText(AgainPasswordField.getText());

		} else {
			showPassImg.setImage(ic_pass_hide);
			AgainPasswordField.setVisible(true);
			AgainPasswordTextField.setVisible(false);
			AgainPasswordField.setText(AgainPasswordField.getText());
		}
	}

	/**
	 * clear username and password fields.
	 */
	public void clearTextFields() {
		usernameField.clear();
		newPasswordTextField.clear();
		newPasswordField.clear();
		AgainPasswordTextField.clear();
		AgainPasswordField.clear();
	}

}
