package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FontLoader {

  private static final Logger log = LogManager.getLogger("FontLoader");
  private static final String PATH = "assets" + File.separator + "fonts" + File.separator;
  public static final Font DIGITAL_FONT;

  static {
    Font font;
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, new File(PATH + "digitalFont.ttf"));
      font = font.deriveFont(24f);
    } catch (IOException | FontFormatException e) {
      font = null;
      log.error("DIGITAL_FONT not properly initialized.");
    }
    DIGITAL_FONT = font;
  }

}
