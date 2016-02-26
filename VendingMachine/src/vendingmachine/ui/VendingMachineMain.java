package vendingmachine.ui;

import javax.swing.SwingUtilities;

/**
 * Runs the configuration menu that is the starting point
 * of the vending machine application.
 */
public final class VendingMachineMain {

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
  
  private VendingMachineMain() {}
  
}