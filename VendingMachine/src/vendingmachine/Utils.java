package vendingmachine;

import java.util.Hashtable;
import java.util.Map;

/**
 * This class defines static methods useful to a vending machine application.
 * This class is final and is not instantiable.
 */
public final class Utils {

  /**
   * Returns the total amount stored in a Map<Coin, Integer>.
   * (The sum for each Coin of its value times the Integer it is mapped to.)
   * 
   * @param map the Map<Coin, Integer> whose value must be computed
   * @return the value stored in {@code map}
   */
  public static int totalValue(Map<Coin, Integer> map) {
    int amount = 0;
    for (Coin coin: Coin.COINS) {
      amount += coin.VALUE * map.get(coin);
    }
    return amount;
  }

  /**
   * Creates a copy of the Map<Coin, Integer> passed as a parameter.
   * 
   * @param map the Map<Coin, Integer> to copy
   * @return a copy of the map
   */
  public static Map<Coin, Integer> copy(Map<Coin, Integer> map) {
    final Map<Coin, Integer> res = new Hashtable<Coin, Integer>();
    for (Coin coin: Coin.COINS) {
      res.put(coin, map.get(coin)); // copy of the Integer
    }
    return res;
  }

  /**
   * Maps each Coin defined in the Coin class to the Integer 0.
   * If the key set is empty, add all the Coin's and maps it to 0.
   * 
   * @param map the map to reset/init
   */
  public static void resetCoinsMap(Map<Coin, Integer> map) {
    for (Coin coin: Coin.COINS) {
      map.put(coin, 0);
    }
  }

  /**
   * Checks if an integer is positive (>= 0).
   * If it is not positive, throws a NumberFormatException.
   * 
   * @param i the int to check
   */
  public static void checkPositiveInt(int i) {
    if (i < 0) {
      throw new NumberFormatException();
    }
  }

  /**
   * Checks if a String is a valid name (not empty and less than 18 characters).
   * If it is not a valid name, throws a NumberFormatException.
   * 
   * @param name the String to check
   */
  public static void checkName(String name) {
    if (name == null || name.isEmpty() || name.length() > 18) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Checks if an integer is a valid percentage (between 0 and 100).
   * If it is not a valid percentage, throws a NumberFormatException.
   * 
   * @param i the int to check
   */
  public static void checkPercentage(int i) {
    if (i < 0 || i > 100) {
      throw new NumberFormatException();
    }
  }
  
  public static void loadResources() {
    PictureLoader.getInstance();
    SoundLoader.getInstance();
    FontLoader.getInstance();
  }

  private Utils() {}

}
