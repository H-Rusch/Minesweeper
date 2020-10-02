public class BoardFactory {

  /**
   * Create a Board object with different fixed sizes based on the difficulty, the player chooses.
   *
   * @param difficulty chosen by the player
   * @param presenter GamePresenter presenting this Board. Needed for the Board so it can directly
   *     report changes.
   * @return created Board object
   */
  public static Board createBoard(int difficulty, GamePresenter presenter) {
    if (difficulty == 1) {
      return new Board(16, 16, 40, presenter);
    } else if (difficulty == 2) {
      return new Board(16, 30, 99, presenter);
    } else {
      return new Board(10, 10, 10, presenter);
    }
  }
}
