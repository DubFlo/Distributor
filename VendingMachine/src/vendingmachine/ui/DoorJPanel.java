package vendingmachine.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import vendingmachine.PictureLoader;

/**
 * This class defines a JPanel that paints a black rectangle (the door).
 * The size of the rectangle is based on the CUP_ICON of PictureLoader.
 * The {@code step} can be changed to change the size of the rectangle.
 */
public class DoorJPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  /**
   * The width of the black rectangle.
   */
  public static final int WIDTH = PictureLoader.CUP_ICON.getIconWidth();
  
  /**
   * The maximal height of the black rectangle
   * (from which the step will be subtracted).
   */
  public static final int HEIGHT = PictureLoader.CUP_ICON.getIconHeight();

  /**
   * A value that is subtracted from the height to draw the rectangle.
   */
  private int step;

  public DoorJPanel() {
    super();
    this.step = 0;
    this.setDoubleBuffered(true);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Toolkit.getDefaultToolkit().sync();
    g.setColor(Color.BLACK);
    g.fillRect((getWidth() - WIDTH) / 2, getHeight() - HEIGHT,
        WIDTH, HEIGHT - step);
  }

  /**
   * @return the current step
   */
  public int getStep() {
    return step;
  }

  /**
   * Changes the step with the new value specified and repaints the frame.
   * 
   * @param step the value of the new step
   */
  public void setStep(int step) {
    this.step = step;
    repaint();
  }

}
