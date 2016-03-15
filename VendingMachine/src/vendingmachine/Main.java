package vendingmachine;

import javax.swing.SwingUtilities;

import vendingmachine.ui.Configuration;

/**
 * Runs the configuration menu that is the starting point
 * of the vending machine application.
 */
public final class Main {

  /**
   * Calls static method {@code run()}.
   */
  public static void main(String[] args) {
    run();
  }

  /**
   * Runs the configuration menu of the vending machine
   * on the Event Dispatch Thread.
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