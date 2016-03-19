package vendingmachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This enumeration gives all the coins that could be accepted by a vending machine.
 * This gives the eight Euro coins.
 * Coins are and should be listed in descending order of their values.
 */
public enum Coin {
  COIN200 (200, "2 €"),
  COIN100 (100, "1 €"),
  COIN50  (50, "0.50 €"),
  COIN20  (20, "0.20 €"), 
  COIN10  (10, "0.10 €"),
  COIN5   (5, "0.05 €"),
  COIN2   (2, "0.02 €"),
  COIN1   (1, "0.01 €");

  /**
   * Unmodifiable List of all the coins, in descending order of their values.
   */
  public static final List<Coin> COINS = Collections.unmodifiableList(Arrays.asList(Coin.values()));
  
  /**
   * The integer value of the coin, expressed in cents.
   */
  public final int VALUE;
  
  /**
   * The String representation of the coin ("0.50 €" for instance).
   */
  public final String TEXT;

  /**
   * Creates a Coin object with a value (in cents) and a String.
   * If the value is negative or the text representation is empty,
   * an IllegalArgumentException is thrown.
   * 
   * @param value the value of the coin (in cents, strictly positive)
   * @param text the String representation of the coin (can't be empty)
   */
  private Coin(int value, String text) {
    if (value <= 0) {
      throw new IllegalArgumentException("Value of a Coin should be strictly positive");
    }
    if (text == null || text.isEmpty()) {
      throw new IllegalArgumentException("Text of a Coin can't be empty or null");
    }
    this.VALUE = value;
    this.TEXT = text;
  }
  
}