package vendingmachine.ui;

/**
 * This interface provides methods needed for the GUI of a drinks vending machine.
 */
public interface IMachineGUI {

  /**
   * Tells the UI to display change on the machine (if {@code bool} is true)
   * or to remove the change icon (if {@code bool} is false).
   * 
   * @param bool true if change must be displayed, false otherwise
   */
  void setChangeBool(boolean bool);

  /**
   * Tells the UI to display a cup on the machine (if {@code cup} is true)
   * or to remove the cup icon (if {@code cup} is false).
   * A spoon is displayed if {@code spoon} is true.
   * 
   * @param cup true if a cup must be displayed, false otherwise
   * @param spoon true if a spoon must be displayed, false otherwise
   */
  void setCupBool(boolean cup, boolean spoon);

  /**
   * Sets a temporary north text. The north text then rebecomes the default one.
   * 
   * @param msg the String to display temporarily
   */
  void setTemporaryNorthText(String msg);

  /**
   * Updates the tool tip of the change button.
   * It gets all the information about the coins waiting to be taken.
   */
  void updateChangeOutInfo();

  /**
   * Updates the JTextArea with the String provided by the IMachine.
   */
  void updateInfo();

  /**
   * Updates the the north text with the String provided by the IMachine.
   */
  void updateNorthText();

  /**
   * Updates the value of the sugar text with the String provided by the IMachine.
   */
  void updateSugarText();

  /**
   * Updates all the graphical information that depends on the IMachine.
   */
  void updateUI();

  /**
   * Makes the JMenuItem to repair stuck coins clickable or not.
   * 
   * @param bool true to enable the "coins stuck repair", false to disable it
   */
  void enableRepair(boolean bool);

  /**
   * Sets the tooltip text of the cup button with the specified message.
   * 
   * @param msg the String to put as a tooltip text of the cup
   */
  void setCupText(String msg);

}