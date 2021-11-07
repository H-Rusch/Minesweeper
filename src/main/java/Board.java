import java.util.Random;

public class Board {

  private Field[][] board;
  private int height;
  private int width;
  private int fieldsLeft;
  private int mineCount;
  private int markCount;

  private boolean running = true;

  private GamePresenter presenter;

  /**
   * Constructor creating a Board. Mines will be placed at random and the numbered spaces are
   * generated after.
   *
   * @param height how many field the board should be high
   * @param width how many fields the board should be wide
   * @param mineCount amount of mines which should be placed
   * @param presenter presenter presenting this board
   */
  public Board(int height, int width, int mineCount, GamePresenter presenter) {
    board = new Field[height][width];
    this.mineCount = mineCount;
    markCount = mineCount;
    this.height = height;
    this.width = width;
    fieldsLeft = (height * width) - mineCount;
    this.presenter = presenter;

    generateMines(mineCount);
    generateSpaces();
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getMineCount() {
    return mineCount;
  }

  public int getMarkCount() {
    return markCount;
  }

  /**
   * Mark a Field on the board at a specified location. Fails to mark if the game has ended or if
   * the player can not mark any more fields. The functions to update the UI in the presenter are
   * called.
   *
   * @param y y-position of the field to be marked
   * @param x x-position of the field to be marked
   */
  public void mark(int y, int x) {
    if (!running || markCount == 0 && !board[y][x].isMarked()) {
      return;
    }
    board[y][x].mark();

    if (board[y][x].isMarked()) {
      markCount--;
    } else {
      markCount++;
    }
    presenter.updateMineCount();
    presenter.updateImage(y, x, board[y][x]);
  }

  /**
   * Click field at a specified location. If this field was a mine, the game is lost. If it was the
   * last safe space the gme is won. If a safe space with zero surrounding mines is clicked, all
   * surrounding fields will be clicked.
   */
  public void click(int y, int x) {
    if (!running) {
      return;
    }
    Field field = board[y][x];
    if (field.click()) {
      if (field instanceof Mine) {
        running = false;
        presenter.lose();
      } else {
        fieldsLeft--;
        if (((SafeSpace) field).getSurroundingMines() == 0) {
          clickSurrounding(y, x);
        }
      }
    }
    presenter.updateImage(y, x, field);
    if (fieldsLeft == 0) {
      running = false;
      presenter.win();
    }
  }

  private void clickSurrounding(int y, int x) {
    if (y - 1 >= 0) {
      // above left
      if (x - 1 >= 0) {
        click(y - 1, x - 1);
      }
      // straight above
      click(y - 1, x);
      // above right
      if (x + 1 < width) {
        click(y - 1, x + 1);
      }
    }
    // left
    if (x - 1 >= 0) {
      click(y, x - 1);
    }
    // right
    if (x + 1 < width) {
      click(y, x + 1);
    }
    if (y + 1 < height) {
      // below left
      if (x - 1 >= 0) {
        click(y + 1, x - 1);
      }
      // straight below
      click(y + 1, x);
      // below right
      if (x + 1 < width) {
        click(y + 1, x + 1);
      }
    }
  }

  /**
   * Generate specified amount of mines at random positions.
   *
   * @param mineCount amout of mines to be generated
   */
  private void generateMines(int mineCount) {
    while (mineCount > 0) {
      int x = new Random().nextInt(this.width);
      int y = new Random().nextInt(this.height);

      if (board[y][x] == null && !(board[y][x] instanceof Mine)) {
        board[y][x] = new Mine();
        mineCount--;
      }
    }
  }

  /** SafeSpaces are generated with the amount of Mines surrounding them. */
  private void generateSpaces() {
    // generate other safe-spaces and count surrounding mines
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (!(board[y][x] instanceof Mine)) {
          int surroundingMines = 0;

          // row above
          if (y - 1 >= 0) {
            // above left
            if (x - 1 >= 0) {
              if (board[y - 1][x - 1] instanceof Mine) {
                surroundingMines++;
              }
            }
            // straight above
            if (board[y - 1][x] instanceof Mine) {
              surroundingMines++;
            }
            // above right
            if (x + 1 < width) {
              if (board[y - 1][x + 1] instanceof Mine) {
                surroundingMines++;
              }
            }
          }

          // same row
          // left
          if (x - 1 >= 0) {
            if (board[y][x - 1] instanceof Mine) {
              surroundingMines++;
            }
          }
          // right
          if (x + 1 < width) {
            if (board[y][x + 1] instanceof Mine) {
              surroundingMines++;
            }
          }

          // row below
          if (y + 1 < height) {
            // below left
            if (x - 1 >= 0) {
              if (board[y + 1][x - 1] instanceof Mine) {
                surroundingMines++;
              }
            }
            // straight below
            if (board[y + 1][x] instanceof Mine) {
              surroundingMines++;
            }
            // below right
            if (x + 1 < width) {
              if (board[y + 1][x + 1] instanceof Mine) {
                surroundingMines++;
              }
            }
          }

          board[y][x] = new SafeSpace(surroundingMines);
        }
      }
    }
  }
}
