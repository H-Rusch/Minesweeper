import lombok.Getter;

public class SafeSpace extends Field {
  @Getter private int surroundingMines;

  /**
   * Constructor creating a SafeSpace. Sets the image for this space based on the surrounding mines.
   *
   * @param surroundingMines amount of mines surrounding this space
   */
  public SafeSpace(int surroundingMines) {
    super();
    this.surroundingMines = surroundingMines;

    if (surroundingMines == 0) {
      super.setImgPath("img/fields/0.png");
    } else if (surroundingMines == 1) {
      super.setImgPath("img/fields/1.png");
    } else if (surroundingMines == 2) {
      super.setImgPath("img/fields/2.png");
    } else if (surroundingMines == 3) {
      super.setImgPath("img/fields/3.png");
    } else if (surroundingMines == 4) {
      super.setImgPath("img/fields/4.png");
    } else if (surroundingMines == 5) {
      super.setImgPath("img/fields/5.png");
    } else if (surroundingMines == 6) {
      super.setImgPath("img/fields/6.png");
    } else if (surroundingMines == 7) {
      super.setImgPath("img/fields/7.png");
    } else if (surroundingMines == 8) {
      super.setImgPath("img/fields/8.png");
    }
  }
}
