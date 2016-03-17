package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supply a digital font which looks like a vending machine display.
 */
public final class FontLoader {

  private static FontLoader INSTANCE;
  
  private FontLoader() {}
  
  public static FontLoader getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FontLoader();
    }
    return INSTANCE;
  }
  
  private static final Logger log = LogManager.getLogger("FontLoader");
  
  /**
   * Font that looks like a vending machine display.
   */
  public final Font DIGITAL_FONT = getFont("digitalFont.ttf");

  /**
   * Returns a Font loaded from the file name specified.
   * If the file does not exists or is not an valid font file, returns null.
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
  
}
