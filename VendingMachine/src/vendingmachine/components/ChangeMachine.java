package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Coin;
import vendingmachine.Utils;

/**
 * This class is able to perform operations about change and coins.
 * It uses as an attribute a stock of Coin's. Coin's can be inserted.
 * It can also give change on a specified amount (if it is possible).
 * Some coins can be refused.
 * 
 * @see Coin
 */
public class ChangeMachine {

  private static final Logger log = LogManager.getLogger("ChangeMachine");
  
  /**
   * A Map of the coins stock, mapping each Coin to its stock.
   */
  private final Map<Coin, Integer> coinsStock;
  
  /**
   * A Map mapping each Coin to whether or not it is accepted.
   */
  private final Map<Coin, Boolean> acceptedCoins;
  
  //private final Change change;
  
  /**
   * Used by {@code isChangePossible(int} to use the computation done in
   * {@code giveChange(int)}. Can not be accessed from the outside.
   */
  private Map<Coin, Integer> coinsStockTemp;

  /**
   * Builds a change machine with the specified coins stock.
   * Each coin may be accepted or not by the change machine.
   * Throws an IllegalArgumentException if a stock value is negative, or if
   * {@code coinsStock} or {@code acceptedCoins} do not list all the coins of Coin.
   * 
   * @param coinsStock a Map that maps each Coin to its stock value
   * @param acceptedCoins a Map that tells if each Coin is accepted or not
   */
  public ChangeMachine(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
    for (Integer i: coinsStock.values()) {
      if (i < 0) {
        throw new IllegalArgumentException("coinsStock values can not be negative");
      }
    }
    if (!coinsStock.keySet().containsAll(Coin.COINS)) {
      throw new IllegalArgumentException("coinsStock has to list all the coins defined in Coin");
    }
    if (!acceptedCoins.keySet().containsAll(Coin.COINS)) {
      throw new IllegalArgumentException("acceptedCoins has to list all the coins defined in Coin");
    }
    //change = new Change(coinsStock, acceptedCoins);
    this.coinsStock = coinsStock;
    this.acceptedCoins = acceptedCoins;
    
    this.coinsStockTemp = new Hashtable<Coin, Integer>();
  }

  /**
   * Gives change on the amount specified and updates the coins stock accordingly.
   * If it is not possible, throws an IllegalArgumentException.
   * Returns a Map of the Coin's given back (mapping each Coin to the number of times it is given).
   * Updates the specified IContext of the coins that are given back.
   * 
   * @param amount the amount to give change on
   * @param context the IContext to notify of the coins given
   * @return a Map of the money that is given back.
   */
  public Map<Coin, Integer> giveChange(int amount, IContext context) {
    if (!isChangePossible(amount)) {
      throw new IllegalArgumentException();
    }
    final Map<Coin, Integer> moneyToGive = new Hashtable<Coin, Integer>();
    for (Coin coin: Coin.COINS) {
      moneyToGive.put(coin, coinsStock.get(coin) - coinsStockTemp.get(coin));
      coinsStock.put(coin, coinsStockTemp.get(coin));
    }
    context.addChangeOut(moneyToGive);
    return moneyToGive;
  }

  /**
   * Returns true if it is possible to give change with the current stock
   * for the amount value (in cents), false otherwise. Updates coinsStockTemp 
   * accordingly, which is the new value coinsStock should take after giving the change.
   * Throws an IllegalArgumentException if {@code amount} is negative.
   * 
   * @param amount number of cents to give change for.
   * @return true if change on the amount is possible, false otherwise
   */
  public boolean isChangePossible(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Can't give change on negative amount");
    }
    coinsStockTemp = Utils.copy(coinsStock);
    for (Coin coin: Coin.COINS) {
      while (amount >= coin.VALUE && coinsStockTemp.get(coin) > 0) {
        coinsStockTemp.put(coin, coinsStockTemp.get(coin) - 1);
        amount -= coin.VALUE;
      }
    }
    
    return amount == 0;
  }

  /**
   * Adds the specified coin to the stock.
   * 
   * @param coin the Coin to add to the stock
   */
  public void insertCoin(Coin coin) {
    coinsStock.put(coin, coinsStock.get(coin) + 1);
  }

  /**
   * @param coin the Coin that may be accepted
   * @return true if the coin is accepted, false otherwise
   */
  public boolean isCoinAccepted(Coin coin) {
    return acceptedCoins.get(coin);
  }
  
  /**
   * @param coin the Coin whose value must be known
   * @return the stock value of the specified Coin
   */
  public int getCoinsStock(Coin coin) {
    return coinsStock.get(coin);
  }

  /**
   * @return a String containing all the information about the current coins.
   */
  public Object getInfo() {
    final StringBuilder sb = new StringBuilder(160);
    sb.append("Coins:\n");
    for (Coin coin: Coin.COINS) {
      sb.append(coin.TEXT).append(": ")
      .append(coinsStock.get(coin))
      .append(" available.\n");
    }
    return sb.toString();
  }

  /**
   * Updates the stock of {@code coin} to the {@code value} specified.
   * Throws an IllegalArgumentException if the value is negative.
   * 
   * @param coin the Coin whose stock must be changed
   * @param value the new value for the {@code coin} (must be positive)
   */
  public void setCoinStock(Coin coin, int value) {
    if (value < 0) {
      throw new IllegalArgumentException("The stock value can't be negative");
    }
    final int difference = value - coinsStock.get(coin);
    if (difference > 0) {
      log.info(difference + " \"" + coin.TEXT + "\" coin(s) resupplied.");
    } else if (difference < 0) {
      log.info(-difference + " \"" + coin.TEXT + "\" coin(s) removed from the stock.");
    }
    
    coinsStock.put(coin, value);
  }
  
}