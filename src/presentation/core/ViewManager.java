package presentation.core;

import domain.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.UserService;

import java.io.IOException;

public class ViewManager
{

    private static UserService service;
    private static Stage primaryStage;

    public static void init(Stage stage, UserService userService)
    {
        service = userService;
        primaryStage = stage;

        primaryStage.initStyle(StageStyle.UNDECORATED);
    }

    public static void showView(View view) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(view.getView()));
        Parent root = loader.load();

        InitializableController controller = loader.getController();
        if (controller == null)
        {
            throw new IllegalStateException("Controller is null. Check fx:controller in " + view.getView());
        }
        controller.init(service);

        // reuse scene if already set
        if (primaryStage.getScene() == null)
        {
            primaryStage.setScene(new Scene(root));
        } else
        {
            primaryStage.getScene().setRoot(root);
        }
    }

    public static void showView(View view, User loggedInUser) throws IOException {

        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(view.getView()));
        Parent root = loader.load();

        Object controllerObj = loader.getController();

        if (controllerObj instanceof InitializableController controller) {
            controller.init(service);
        }

        if (controllerObj instanceof UserAwareController userController) {
            userController.setUser(loggedInUser);
        }

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root));
        } else {
            primaryStage.getScene().setRoot(root);
        }
    }
}

