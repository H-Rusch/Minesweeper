import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import lombok.Getter;

public class GamePresenter {

  private Board board;
  private ImageView[][] images;
  @Getter private int difficulty;

  private volatile Boolean timerRunning = false;
  private int time;
  private Timer timer;

  @FXML private GridPane gridPane;
  @FXML private TextField minesLeft;
  @FXML private TextField timeUsed;
  @FXML private ImageView newGame;

  private FxApplication application;

  /**
   * Initialize the presenter. The cached difficulty is loaded or if the game has never been played
   * before, the "easy" difficulty is taken. A new game in this difficulty is created.
   *
   * @throws IOException opening or creating the file fails
   */
  @FXML
  public void initialize() throws IOException {
    Path file = Paths.get("src/main/resources/data/settings.txt");
    if (!Files.exists(file)) {
      Files.createFile(file);
      Files.write(file, Collections.singleton("0"));
    }
    difficulty = Integer.parseInt(Files.readAllLines(file).get(0));

    newGame();
    newGame.requestFocus();
  }

  /** Setter for FxApplication. */
  public void setApplication(FxApplication application) {
    this.application = application;
  }

  /**
   * A new game is created. The fitting minefield will be shown on screen. The UI-elements (timer
   * and mines) are reset and the timer is being reset.
   */
  public void newGame() {
    time = 0;
    timeUsed.setText(String.valueOf(time));

    if (timerRunning) {
      timer.stopRunning();
    }
    timerRunning = false;
    newGame.setImage(new Image("img/ui_elements/smile.png"));
    board = BoardFactory.createBoard(difficulty, this);
    minesLeft.setText(String.valueOf(this.board.getMineCount()));
    initBoardView();
  }

  /**
   * The GridPane is being filled with images which represent a specific field on the board.
   * Clicking the images is connected to the onPress function
   */
  public void initBoardView() {
    images = new ImageView[board.getHeight()][board.getWidth()];
    gridPane.getChildren().clear();

    for (int col = 0; col < board.getWidth(); col++) {
      for (int row = 0; row < board.getHeight(); row++) {
        ImageView image = new ImageView(new Image("img/fields/unrevealed.png"));
        gridPane.add(image, col, row);
        images[row][col] = image;
        image.setOnMouseClicked(this::onPress);
      }
    }
  }

  /**
   * When clicking an image the coordinates are determined and the click function in the game is
   * called.
   *
   * @param event click on an image
   */
  private void onPress(javafx.scene.input.MouseEvent event) {
    int x = GridPane.getColumnIndex((Node) event.getSource());
    int y = GridPane.getRowIndex((Node) event.getSource());

    if (!timerRunning) {
      timerRunning = true;
      timer = new Timer(this);
      new Thread(this.timer).start();
    }

    if (event.getButton() == MouseButton.PRIMARY) {
      board.click(y, x);
    } else if (event.getButton() == MouseButton.SECONDARY) {
      board.mark(y, x);
    }
  }

  /**
   * An image of a specific field is being updated.
   *
   * @param y y-position of this field
   * @param x x-position of this field
   * @param field the field which has been updated
   */
  public void updateImage(int y, int x, Field field) {
    if (field.isMarked()) {
      images[y][x].setImage(new Image("img/fields/marked.png"));
    } else if (field.isRevealed()) {
      images[y][x].setImage(new Image(field.getImgPath()));
    } else {
      images[y][x].setImage(new Image("img/fields/unrevealed.png"));
    }
  }

  /** Set difficulty to easy and create a new game. Fit screen-size to game. */
  public void easy() {
    difficulty = 0;
    cacheDifficulty();
    newGame();
    application.resizeWindow(difficulty);
  }

  /** Set difficulty to medium and create a new game. Fit screen-size to game. */
  public void medium() {
    difficulty = 1;
    cacheDifficulty();
    newGame();
    application.resizeWindow(difficulty);
  }

  /** Set difficulty to hard and create a new game. Fit screen-size to game. */
  public void hard() {
    difficulty = 2;
    cacheDifficulty();
    newGame();
    application.resizeWindow(difficulty);
  }

  /** Write last used difficulty into a file so it can be reloaded on startup. */
  private void cacheDifficulty() {
    try {
      Path file = Paths.get("src/main/resources/data/settings.txt");
      List<String> lines = Files.readAllLines(file);
      lines.set(0, String.valueOf(difficulty));
      Files.write(file, lines);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Stop timer and save time used to file if the former record was broken. */
  public void win() {
    newGame.setImage(new Image("img/ui_elements/win.png"));
    timerRunning = false;
    timer.stopRunning();

    try {
      Path file = Paths.get("src/main/resources/data/records.txt");
      if (!Files.exists(file)) {
        Files.createFile(file);
        Files.write(file, Collections.singleton("-\n-\n-"));
      }

      List<String> records = Files.readAllLines(file);
      if (records.get(difficulty).equals("-") || Integer.parseInt(records.get(difficulty)) > time) {
        records.set(difficulty, String.valueOf(time));
        Files.write(file, records);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Stop timer after losing the game. */
  public void lose() {
    newGame.setImage(new Image("img/ui_elements/lose.png"));
    timerRunning = false;
    timer.stopRunning();
  }

  public void updateMineCount() {
    minesLeft.setText(String.valueOf(board.getMarkCount()));
  }

  public void incrementTimer() {
    time++;
    timeUsed.setText(String.valueOf(time));
  }

  /** Open a youtube tutorial on how to play the game in the browser. */
  public void showGuide() {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Action.BROWSE)) {
      try {
        desktop.browse(new URI("https://www.youtube.com/watch?v=7B85WbEiYf4"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /** Show best times the difficulties were beaten in previously. */
  public void showRecords() {
    List<String> records = null;
    try {
      Path file = Paths.get("src/main/resources/data/records.txt");
      if (!Files.exists(file)) {
        Files.createFile(file);
        Files.write(file, Collections.singleton("-\n-\n-"));
      }

      records = Files.readAllLines(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.application.showTextWindow(
        String.format(
            "Easy: %s s\nMedium: %s s\nHard: %s s",
            records.get(0), records.get(1), records.get(2)));
  }
}
