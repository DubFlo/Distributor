package vendingmachine;

import javax.swing.SwingUtilities;

import vendingmachine.ui.Configuration;

/**
 * Runs the configuration menu of the vending machine application.
 */
public final class Main {

  /**
   * Calls static method {@code run()} to display the configuration.
   * Loads resources while user inputs information to load the machine faster.
   * 
   * @param args /
   */
  public static void main(String[] args) {
    run();
    Utils.loadResources(); 
  }

  /**
   * Runs the configuration menu of the machine on the Event Dispatch Thread.
   */
  public static void run() {
    final Configuration config = new Configuration();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        config.init();
      }
    });
  }

  private Main() {}

}