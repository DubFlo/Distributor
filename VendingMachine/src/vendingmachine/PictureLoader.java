package vendingmachine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supplies ImageIcon's or BufferedImage's useful to display a vending machine.
 * Uses the singleton design pattern.
 */
public final class PictureLoader {

  private static PictureLoader INSTANCE;

  private static final Logger log = LogManager.getLogger("PictureLoader");

  /**
   * The ImageIcon of a red cup.
   */
  public final ImageIcon CUP_ICON = getIcon("cup.jpg");

  /**
   * The ImageIcon of a stack of coins.
   */
  public final ImageIcon CHANGE_ICON = getIcon("change.png");

  /**
   * The BufferedImage of a large vending machine screen.
   */
  public final BufferedImage DISPLAY_PANEL = getImage("displayPanel.jpg");

  /**
   * The BufferedImage of a huge coffee cup.
   */
  public final BufferedImage COFFEE_IMAGE = getImage("coffee.png");

  /**
   * The BufferedImage of a coin slot.
   */
  public final BufferedImage SLOT_IMAGE = getImage("slot.jpg");

  /**
   * The BufferedImage of a small vending machine screen.
   */
  public final ImageIcon SUGAR_DISPLAY = getIcon("sugarDisplay.jpg");

  /**
   * The ImageIcon of a round red button.
   */
  public final ImageIcon DRINK_BUTTON = getIcon("drinkButton.png");

  /**
   * Map linking each Coin to its corresponding ImageIcon.
   */
  public final Map<Coin, ImageIcon> COINS_IMAGES;

  /**
   * Returns a BufferedImage loaded from the file name specified.
   * If the file does not exists or is not an image file, returns null.
   * 
   * @param file the name of the image file, placed in the resources/ folder.
   * @return the BufferedImage loaded from the file if it exists, null otherwise.
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

  /**
   * @return the only PictureLoader instance (creates it if it doesn't exist)
   */
  public static PictureLoader getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PictureLoader();
    }
    return INSTANCE;
  }

  private PictureLoader() {
    COINS_IMAGES = new Hashtable<Coin, ImageIcon>();
    COINS_IMAGES.put(Coin.COIN200, getIcon("2euro.png"));
    COINS_IMAGES.put(Coin.COIN100, getIcon("1euro.png"));
    COINS_IMAGES.put(Coin.COIN50, getIcon("50cent.png"));
    COINS_IMAGES.put(Coin.COIN20, getIcon("20cent.png"));
    COINS_IMAGES.put(Coin.COIN10, getIcon("10cent.png"));
    COINS_IMAGES.put(Coin.COIN5, getIcon("5cent.png"));
    COINS_IMAGES.put(Coin.COIN2, getIcon("2cent.png"));
    COINS_IMAGES.put(Coin.COIN1, getIcon("1cent.png"));
  }

}
