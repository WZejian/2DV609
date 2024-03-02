package plantpal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import plantpal.controller.LoginPageController;
import plantpal.model.ImageLib;
import plantpal.model.SQLqueries.SQLConnector;

import java.io.IOException;

// https://www.youtube.com/watch?v=hvsvb-BVNoE
// color palette from: https://mycolor.space
// icon from: https://www.flaticon.com
public class Main extends Application {
    private Stage primaryStage;
    private HBox rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SQLConnector.connect();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PlantPal App");
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginPage.fxml"));
            LoginPageController loginController = new LoginPageController(primaryStage);
            loader.setController(loginController);
            rootLayout = (HBox) loader.load();
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.getIcons().add(ImageLib.ic_plantpal);
            primaryStage.show();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Main.launch(args);
    }
}