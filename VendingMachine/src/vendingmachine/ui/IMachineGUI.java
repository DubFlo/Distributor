package vendingmachine.ui;

/**
 * This interface provides methods needed for the GUI of a drinks vending machine.
 */
public interface IMachineGUI {
  
  /**
   * Tells the UI to display change is of the machine (if {@code bool} is true)
   * or to remove the change icon (if {@code bool} is false).
   * 
   * @param bool true if change must be displayed, false otherwise
   */
  void setChangeBool(boolean bool);

  /**
   * Tells the UI to display a cup on the machine (if {@code bool} is true)
   * or to remove the cup icon (if {@code bool} is false).
   * 
   * @param bool true if a cup must be displayed, false otherwise
   */
  void setCupBool(boolean bool);

  /**
   * Sets a temporary north text. The north text becomes then the default one.
   * 
   * @param msg the String to display temporarily
   */
  void setTemporaryNorthText(String msg);

  /**
   * Updates the tool tip of the change button.
   */
  void updateChangeOutInfo();

  /**
   * Updates the JTextArea with the String provided by the IMachine.
   */
  void updateInfo();
  
  /**
   * Updates the value of the north text with the String provided by the IMachine.
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

  void enableRepair(boolean b);

}