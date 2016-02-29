package vendingmachine;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Supply ImageIcon or BufferedImage images that are useful to display a vending machine.
 */
public final class PictureLoader {

  public static final ImageIcon CUP_ICON = new ImageIcon(getImage("cup.jpg"));
  public static final ImageIcon CHANGE_ICON = new ImageIcon(getImage("change.png"));
  public static final BufferedImage DISPLAY_PANEL = getImage("displayPanel.jpg");
  public static final BufferedImage COFFEE_IMAGE = getImage("coffee.png");
  public static final BufferedImage SLOT_IMAGE = getImage("slot.jpg");
  public static final ImageIcon SUGAR_DISPLAY = new ImageIcon(getImage("sugarDisplay.jpg"));
  public static final ImageIcon DRINK_BUTTON = new ImageIcon(getImage("drinkButton.png"));
  public static final ImageIcon EURO2_ICON = new ImageIcon(getImage("2euro.png"));
  public static final ImageIcon EURO1_ICON = new ImageIcon(getImage("1euro.png"));
  public static final ImageIcon CENT50_ICON = new ImageIcon(getImage("50cent.png"));
  public static final ImageIcon CENT20_ICON = new ImageIcon(getImage("20cent.png"));
  public static final ImageIcon CENT10_ICON = new ImageIcon(getImage("10cent.png"));
  public static final ImageIcon CENT5_ICON = new ImageIcon(getImage("5cent.png"));
  public static final ImageIcon CENT2_ICON = new ImageIcon(getImage("2cent.png"));
  public static final ImageIcon CENT1_ICON = new ImageIcon(getImage("1cent.png"));
  
  private static BufferedImage getImage(String file) {
    BufferedImage image;
    try {
      image = ImageIO.read(PictureLoader.class.getResource("/resources/" + file));
    } catch (IOException e) {
      image = null;
      e.printStackTrace();
    }
    return image;
  }
  
  private PictureLoader() {}
  
}
