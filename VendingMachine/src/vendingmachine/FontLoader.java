package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Supply a digital font which looks like a vending machine display.
 */
public final class FontLoader {

  private static final String PATH = File.separator + "resources" + File.separator;
  
  public static final Font DIGITAL_FONT;

  static {
    Font font;
    File file;
    try {
      file = new File(FontLoader.class.getClassLoader().getResource(PATH + "digitalFont.ttf").toURI());
      font = Font.createFont(Font.TRUETYPE_FONT, file);
      font = font.deriveFont(24f);
    } catch (IOException | FontFormatException | URISyntaxException e) {
      font = null;
      e.printStackTrace();
    }
    DIGITAL_FONT = font;
  }

  private FontLoader() {}
  
}
