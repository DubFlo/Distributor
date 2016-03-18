package vendingmachine.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


/**
 * This class creates a JPanel with a background that is a BufferedImage.
 * The background is resized when the frame is resized.
 * Inspired by a solution found on the 22-11-2015 on https://goo.gl/uJhelw.
 */
public class BackgroundJPanel extends JPanel {

  private static final long serialVersionUID = 1L; // generated by Eclipse
  
  private final BufferedImage background;

  /**
   * Creates a JPanel with the specified background.
   * 
   * @param background the BufferedImage of the background
   */
  public BackgroundJPanel(BufferedImage background) {
    super();
    this.background = background;
  }

  /**
   * Calls the overridden method of JPanel and draws the background.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
  }

}