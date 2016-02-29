package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Supply a digital font which looks like a vending machine display.
 */
public final class FontLoader {

  public static final Font DIGITAL_FONT;

  static {
    Font font;
    InputStream is;
    try {
      is = FontLoader.class.getResourceAsStream("/resources/" + "digitalFont.ttf");
      font = Font.createFont(Font.TRUETYPE_FONT, is);
      font = font.deriveFont(24f);
    } catch (IOException | FontFormatException e) {
      font = null;
      e.printStackTrace();
    }
    DIGITAL_FONT = font;
  }

  private FontLoader() {}
  
}
