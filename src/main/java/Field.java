import lombok.Getter;

public class Field {

  @Getter private boolean marked = false;
  @Getter private boolean revealed = false;
  @Getter private String imgPath;

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  /**
   * Click a field as long as it is not already revealed or marked.
   *
   * @return boolean wether clicking this field was successfull
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
