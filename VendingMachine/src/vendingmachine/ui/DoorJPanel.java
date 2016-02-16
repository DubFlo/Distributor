package vendingmachine.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import vendingmachine.PictureLoader;

public class DoorJPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  public static final int imageWidth = PictureLoader.cupImage.getWidth();
  public static final int imageHeight = PictureLoader.cupImage.getHeight();
  
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
    g.fillRect((getWidth() - imageWidth)/2, getHeight() - imageHeight,
        imageWidth, imageHeight - step);
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

}
