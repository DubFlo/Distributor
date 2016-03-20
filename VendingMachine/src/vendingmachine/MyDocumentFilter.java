package vendingmachine;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Creates a DocumentFilter that allows only digits (used in JTextField's).
 * Deals with the case that something is typed or something is pasted.
 * 
 * Found on the 12-02-2016 on
 * <a href="http://stackoverflow.com/a/9478124">http://stackoverflow.com/a/9478124</a>.
 */
public class MyDocumentFilter extends DocumentFilter {
  @Override
  public void insertString(DocumentFilter.FilterBypass fp,
      int offset, String string, AttributeSet aset)
          throws BadLocationException {
    int len = string.length();
    boolean isValidInteger = true;

    for (int i = 0; i < len; i++) {
      if (!Character.isDigit(string.charAt(i))) {
        isValidInteger = false;
        break;
      }
    }
    if (isValidInteger) {
      super.insertString(fp, offset, string, aset);
    } else {
      Toolkit.getDefaultToolkit().beep();
    }
  }

  @Override
  public void replace(DocumentFilter.FilterBypass fp, int offset,
      int length, String string, AttributeSet aset)
          throws BadLocationException {
    int len = string.length();
    boolean isValidInteger = true;

    for (int i = 0; i < len; i++) {
      if (!Character.isDigit(string.charAt(i))) {
        isValidInteger = false;
        break;
      }
    }
    if (isValidInteger) {
      super.replace(fp, offset, length, string, aset);
    } else {
      Toolkit.getDefaultToolkit().beep();
    }
  }
}