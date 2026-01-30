import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import persistence.db.Database;
import persistence.repo.UserRepo;
import presentation.core.View;
import presentation.core.ViewManager;
import service.UserService;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Database db = new Database(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=login_test",
                "postgres",
                "password"
        );
        UserRepo repo = new UserRepo(db);
        repo.init();

        ViewManager.init(primaryStage, new UserService(repo));
        ViewManager.showView(View.LOGIN);
        primaryStage.show();
    }
}
