package vendingmachine.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DoorJPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private int step;
  
  public DoorJPanel() {
    super();
    step = 0;
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.black);
    g.fillRect(getHeight()/3, getWidth(), 10*step, 100);
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

}
