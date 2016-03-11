package vendingmachine;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supply ImageIcon or BufferedImage images that are useful to display a vending machine.
 */
public final class PictureLoader {

  private static final Logger log = LogManager.getLogger("PictureLoader");
  
  public static final ImageIcon CUP_ICON = getIcon("cup.jpg");
  public static final ImageIcon CHANGE_ICON = getIcon("change.png");
  public static final BufferedImage DISPLAY_PANEL = getImage("displayPanel.jpg");
  public static final BufferedImage COFFEE_IMAGE = getImage("coffee.png");
  public static final BufferedImage SLOT_IMAGE = getImage("slot.jpg");
  public static final ImageIcon SUGAR_DISPLAY = getIcon("sugarDisplay.jpg");
  public static final ImageIcon DRINK_BUTTON = getIcon("drinkButton.png");
  public static final ImageIcon EURO2_ICON = getIcon("2euro.png");
  public static final ImageIcon EURO1_ICON = getIcon("1euro.png");
  public static final ImageIcon CENT50_ICON = getIcon("50cent.png");
  public static final ImageIcon CENT20_ICON = getIcon("20cent.png");
  public static final ImageIcon CENT10_ICON = getIcon("10cent.png");
  public static final ImageIcon CENT5_ICON = getIcon("5cent.png");
  public static final ImageIcon CENT2_ICON = getIcon("2cent.png");
  public static final ImageIcon CENT1_ICON = getIcon("1cent.png");
  
  /**
   * Returns a BufferedImage loaded from the file name specified.
   * If the file does not exists or is not an image file, returns null.
   * 
   * @param file the name of the image file, placed in the resources/ folder.
   * @return the BufferedImage loaded from the file if it exists, null otherwise.
   * @see BufferedImage
   */
  private static BufferedImage getImage(String file) {
    BufferedImage image;
    try {
      image = ImageIO.read(PictureLoader.class.getResource("/resources/" + file));
    } catch (IOException | IllegalArgumentException e) {
      image = null;
      log.error(file + " not properly loaded. Graphical glitches are expected.");
    }
    return image;
  }
  
  /**
   * Returns an ImageIcon loaded from the file name specified.
   * If the file does not exists or is not an image file, returns null.
   * 
   * @param file the name of the image file, placed in the resources/ folder.
   * @return the ImageIcon loaded from the file if it exists, null otherwise.
   * @see ImageIcon
   */
  private static ImageIcon getIcon(String file) {
    ImageIcon icon;
    try {
      icon = new ImageIcon(PictureLoader.class.getResource("/resources/" + file));
    } catch (NullPointerException e) {
      icon = null;
      log.error(file + " not properly loaded. Graphical glitches are expected.");
    }
    return icon;
  }
  
  private PictureLoader() {}
  
}
