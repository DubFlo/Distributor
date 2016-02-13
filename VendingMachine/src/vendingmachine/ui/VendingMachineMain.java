package vendingmachine.ui;

import javax.swing.SwingUtilities;

public class VendingMachineMain {

  public static void main(String[] args) {
    run();
  }

  public static void run() {
    Configuration config = new Configuration();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        config.init();
      }
    });
  }
  
}