package vendingmachine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PictureLoader {

  private static final String PATH = "src" + File.separator + "resources" + File.separator;
  private static final Logger log = LogManager.getLogger("PictureLoader");

  public static final BufferedImage cupImage = getImage("cup.jpg");
  public static final BufferedImage changeImage = getImage("change.png");
  public static final BufferedImage displayPanel = getImage("displayPanel.jpg");
  public static final BufferedImage coffee = getImage("coffee.png");
  public static final BufferedImage slot = getImage("slot.jpg");
  public static final BufferedImage sugarDisplay = getImage("sugarDisplay.jpg");
  public static final BufferedImage drinkButton = getImage("drinkButton.png");
  public static final BufferedImage euro2 = getImage("2euro.png");
  public static final BufferedImage euro1 = getImage("1euro.png");
  public static final BufferedImage cent50 = getImage("50cent.png");
  public static final BufferedImage cent20 = getImage("20cent.png");
  public static final BufferedImage cent10 = getImage("10cent.png");
  public static final BufferedImage cent5 = getImage("5cent.png");
  public static final BufferedImage cent2 = getImage("2cent.png");
  public static final BufferedImage cent1 = getImage("1cent.png");
  
  private static BufferedImage getImage(String file) {
    BufferedImage image;
    try {
      image = ImageIO.read(new File(PATH + file));
    } catch (IOException e) {
      image = null;
      log.error(file + " not properly loaded.");
    }
    return image;
  }
  
  private PictureLoader() {}
  
}
