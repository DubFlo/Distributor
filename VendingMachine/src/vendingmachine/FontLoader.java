package vendingmachine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 * Supply a digital font which looks like a vending machine display.
 */
public final class FontLoader {

  private static final String PATH = "assets" + File.separator + "fonts" + File.separator;
  
  public static final Font DIGITAL_FONT;

  static {
    Font font;
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, new File(PATH + "digitalFont.ttf"));
      font = font.deriveFont(24f);
    } catch (IOException | FontFormatException e) {
      font = null;
      e.printStackTrace();
    }
    DIGITAL_FONT = font;
  }

  private FontLoader() {}
  
}
