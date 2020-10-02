import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FxApplication extends Application {

  private Stage stage;

  /**
   * The window is getting filled with content and shown to the user.
   *
   * @param stage window to be shown.
   */
  @Override
  public void start(Stage stage) {
    try {
      this.stage = stage;
      FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml\\gameView.fxml"));
      Parent root = loader.load();
      ((GamePresenter) loader.getController()).setApplication(this);

      Scene scene = new Scene(root);
      stage.setTitle("Minesweeper");
      stage.setScene(scene);
      stage.setResizable(false);
      this.resizeWindow(((GamePresenter) loader.getController()).getDifficulty());
      stage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Resize the window based on the difficulty. This makes sure the window's size is fitting for the
   * amount of mines in the game.
   *
   * @param difficulty indicates how big the window has to be
   */
  public void resizeWindow(int difficulty) {
    if (difficulty == 0) {
      stage.setHeight(310.0);
      stage.setWidth(225.0);
    } else if (difficulty == 1) {
      stage.setHeight(435.0);
      stage.setWidth(345.0);
    } else {
      stage.setHeight(435.0);
      stage.setWidth(625.0);
    }
  }

  /**
   * Show a text in an alert box.
   *
   * @param text String to be show.
   */
  public void showTextWindow(String text) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Your Records");
    alert.setHeaderText(null);
    alert.setContentText(text);

    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
