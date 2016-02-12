package vendingmachine.ui;

import javax.swing.SwingUtilities;

public class VendingMachineMain {

  public static void main(String[] args) {
    Configuration config = new Configuration();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        config.init();
      }
    });
  }

}