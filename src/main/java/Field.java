public class Field {

  private boolean marked = false;
  private boolean revealed = false;
  private String imgPath;

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  public boolean isMarked() {
    return marked;
  }

  public boolean isRevealed() {
    return revealed;
  }

  public String getImgPath() {
    return imgPath;
  }

  /**
   * Click a field as long as it is not already revealed or marked.
   *
   * @return boolean whether clicking this field was successful
   */
  public boolean click() {
    if (!revealed && !marked) {
      revealed = true;
      return true;
    }
    return false;
  }

  /** Invert the mark status of a field as long as it is not already revealed. */
  public void mark() {
    if (!revealed) {
      marked = !marked;
    }
  }
}
