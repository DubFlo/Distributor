package vendingmachine.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import vendingmachine.PictureLoader;

public class DoorJPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  public static final int WIDTH = PictureLoader.cupImage.getWidth();
  public static final int HEIGHT = PictureLoader.cupImage.getHeight();
  
  private int step;
  
  public DoorJPanel() {
    super();
    step = 0;
    setDoubleBuffered(true);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Toolkit.getDefaultToolkit().sync();
    g.setColor(Color.black);
    g.fillRect((getWidth() - WIDTH) / 2, getHeight() - HEIGHT,
        WIDTH, HEIGHT - step);
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

}
