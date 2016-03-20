package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supplies a digital font which looks like a vending machine display.
 * Uses the singleton design pattern to be created only once when needed (thread-safe). This
 * solution has been found on http://stackoverflow.com/a/11165975 . As explained on this link,
 * "The class [FontLoader].Loader is first accessed inside the getInstance() method, so the Loader
 * class loads when getInstance() is called for the first time. Further, the class loader
 * guarantees that all static initialization is complete before you get access to the class -
 * that's what gives you thread-safety."
 */
public final class FontLoader {
  
  private static final Logger log = LogManager.getLogger("FontLoader");

  /**
   * Font that looks like a vending machine display.
   * Source: http://goo.gl/W4xrrd
   */
  public final Font DIGITAL_FONT = getFont("digitalFont.ttf");

  /**
   * Returns a Font loaded from the file name specified.
   * If the file does not exist or is not an valid font file, returns null.
   * 
   * @param file the name of the font file, placed in the resources/ folder.
   * @return the Font loaded from the file if it exists, null otherwise.
   */
  private static Font getFont(String file) {
    Font font;
    InputStream inputStream;
    try {
      inputStream = FontLoader.class.getResourceAsStream("/resources/" + file);
      font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
      font = font.deriveFont(24f);
    } catch (IOException | FontFormatException e) {
      font = null;
      log.error(file + " not properly loaded. Graphical glitches are expected.");
    }
    return font;
  }

  private FontLoader() {}

  /**
   * @return the only FontLoader instance (creates it if it doesn't exist)
   */
  public static FontLoader getInstance() {
    return Loader.INSTANCE;
  }
  
  private static class Loader {
    public static final FontLoader INSTANCE = new FontLoader();
  }

}
