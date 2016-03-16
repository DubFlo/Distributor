package vendingmachine;

import javax.swing.ImageIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This enum gives all the coins that could be accepted by a vending machine.
 * This gives all the Euro coins, but it can be easily changed.
 */
public enum Coin { // Coins should be listed in descending order of their values
  COIN200 (200, "2 €", PictureLoader.EURO2_ICON),
  COIN100 (100, "1 €", PictureLoader.EURO1_ICON),
  COIN50  (50, "0.50 €", PictureLoader.CENT50_ICON),
  COIN20  (20, "0.20 €", PictureLoader.CENT20_ICON), 
  COIN10  (10, "0.10 €", PictureLoader.CENT10_ICON),
  COIN5   (5, "0.05 €", PictureLoader.CENT5_ICON),
  COIN2   (2, "0.02 €", PictureLoader.CENT2_ICON),
  COIN1   (1, "0.01 €", PictureLoader.CENT1_ICON);

  /**
   * Unmodifiable List of all the coins, in descending order of their values.
   */
  public static final List<Coin> COINS = Collections.unmodifiableList(Arrays.asList(Coin.values()));
  
  /**
   * The value of the coin, expressed in cents.
   */
  public final int VALUE;
  
  /**
   * The String representation of the coin ("0.50 €" for instance).
   */
  public final String TEXT;
  
  /**
   * An icon that looks like the coin. It can be null, but the
   * vending machine will be uglier.
   */
  public final ImageIcon ICON;

  /**
   * Creates a Coin object with three attributes.
   * If the value is negative or the text representation is empty,
   * an IllegalArgumentException is thrown.
   * 
   * @param value the value of the coin (in cents, positive)
   * @param text the String representation of the coin (can't be empty)
   * @param icon the ImageIcon of the Coin
   */
  private Coin(int value, String text, ImageIcon icon) {
    if (value <= 0) {
      throw new IllegalArgumentException("Value of a Coin should be strictly positive");
    }
    if (text.equals("")) {
      throw new IllegalArgumentException("Text of a Coin can't be empty");
    }
    this.VALUE = value;
    this.TEXT = text;
    this.ICON = icon;
  }
  
  public static int totalValue(Map<Coin, Integer> m) { // à déplacer ??
    int amount = 0;
    for (Coin coin: COINS) {
      amount += coin.VALUE + m.get(coin);
    }
    return amount;
  }
}