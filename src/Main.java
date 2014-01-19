import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by uziak on 19.01.14.
 */
public class Main extends Application {

    GameManager gameManager;

    @Override public void start(Stage stage) throws Exception {
        gameManager = new GameManager();

        Scene scene = gameManager.getGameScene();
        scene.getStylesheets().add(
                getResource(
                        "freedom-skin.css"
                )
        );

        stage.setTitle("Freedom PSZT");
        stage.getIcons().add(SquareSkin.blackImage);
        stage.setScene(scene);
        stage.show();
    }


    private String getResource(String resourceName) {
        return getClass().getResource(resourceName).toExternalForm();
    }

    public static void main(String[] args) {
        Application.launch(Main.class);
    }

}
