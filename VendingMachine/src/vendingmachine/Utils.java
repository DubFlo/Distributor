package vendingmachine;

import java.util.Hashtable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class defines static methods useful to a vending machine application.
 * This class is final and is not instantiable.
 */
public final class Utils {

  /**
   * Unicode code for the Euro symbol.
   */
  public static final String EURO = "\u20ac";

  private static final Logger log = LogManager.getLogger("Utils");
  
  /**
   * Returns the total amount stored in a Map.
   * (The sum for each Coin of its value times the Integer it is mapped to.)
   * 
   * @param map the Map whose value must be computed
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
   * Creates a copy of the Map passed as a parameter.
   * 
   * @param map the Map to copy
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
   * If the key set is empty, add all the Coin's and maps them to 0.
   * 
   * @param map the map to reset/init
   */
  public static void resetCoinsMap(Map<Coin, Integer> map) {
    for (Coin coin: Coin.COINS) {
      map.put(coin, 0);
    }
  }

  /**
   * Checks if an integer is positive.
   * If it is not positive, throws a NumberFormatException.
   * 
   * @param i the int to check
   * @throws NumberFormatException if the int {@code i} is strictly negative
   */
  public static void checkPositiveIntFormat(int i) {
    if (i < 0) {
      throw new NumberFormatException("Number can not be stricly negative.");
    }
  }
  
  /**
   * Checks if an integer is positive.
   * If it is not positive, throws a IllegalArgumentException.
   * 
   * @param i the int to check
   * @param element the String representation of the element whose number is changed
   * @throws IllegalArgumentException if the int {@code i} is strictly negative
   */
  public static void checkPositiveIntIllegal(int i, String element) {
    if (i < 0) {
      throw new IllegalArgumentException(
          "The value of " + element + " can not be stricty negative.");
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
   * Checks if an integer is a valid integer percentage (between 0 and 100).
   * If it is not a valid percentage, throws a NumberFormatException.
   * 
   * @param i the int to check
   */
  public static void checkPercentage(int i) {
    if (i < 0 || i > 100) {
      throw new NumberFormatException();
    }
  }
  
  /**
   * Loads all the images, sounds and font of the project.
   * Run at the right time, it may make the application faster.
   */
  public static void loadResources() {
    PictureLoader.getInstance();
    SoundLoader.getInstance();
    FontLoader.getInstance();
  }
  
  /**
   * Logs a message indicating a change in stock of the {@code part} specified.
   * The {@code difference} indicates how much has been resupplied (if positive)
   * or removed (if negative). The {@code value} is the new value of the part.
   * 
   * @param difference the difference of stock from the previous value
   * @param value the new value in stock
   * @param part the String name of the element whose stock has been changed
   */
  public static void logChange(int difference, int value, String part) {
    if (difference > 0) {
      log.info(difference + " " + part + " resupplied (" + value + " in stock).");
    } else if (difference < 0) {
      log.info(-difference + " " + part + " removed from the stock (" + value + " remaining).");
    }
  }

  private Utils() {}

}
