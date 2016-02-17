package vendingmachine.ui;

import javax.swing.SwingUtilities;

public final class VendingMachineMain {

  public static void main(String[] args) {
    run();
  }

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