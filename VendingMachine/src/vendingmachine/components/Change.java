package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Coin;
import vendingmachine.Utils;

/**
 *
 */
public class Change {

  private static final Logger log = LogManager.getLogger("Change");
  
  /**
   * A Map of the coins stock, mapping each Coin to its stock.
   */
  private final Map<Coin, Integer> coinsStock;
  
  /**
   * Used by {@code isChangePossible(int} to use the computation done in
   * {@code giveChange(int)}. Can not be accessed from the outside.
   */
  private Map<Coin, Integer> coinsStockTemp;
  
  public Change(Map<Coin, Integer> coinsStock) {
    for (Integer i: coinsStock.values()) {
      Utils.checkPositiveIntIllegal(i, "stock of coins");
    }
    if (!coinsStock.keySet().containsAll(Coin.COINS)) {
      throw new IllegalArgumentException("coinsStock has to list all the coins defined in Coin");
    }
    this.coinsStock = coinsStock;
    
    this.coinsStockTemp = new Hashtable<Coin, Integer>();
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
    Utils.checkPositiveIntIllegal(amount, "the amount to give change on");
    coinsStockTemp = Utils.copy(coinsStock);
    int remainder = amount;
    for (Coin coin: Coin.COINS) {
      while (remainder >= coin.VALUE && coinsStockTemp.get(coin) > 0) {
        coinsStockTemp.put(coin, coinsStockTemp.get(coin) - 1);
        remainder -= coin.VALUE;
      }
    }
    if (remainder != 0) {
      log.warn("Can not give " + amount / 100.0 + " " + Utils.EURO + " of change.");
    }
    return remainder == 0;
  }
  
  /**
   * Gives change on the amount specified and updates the coins stock accordingly.
   * If it is not possible, throws an IllegalArgumentException.
   * Returns a Map of the Coin's given back (mapping each Coin to the number of times
   * it is given). Updates the specified IContext of the coins that are given back.
   * 
   * @param amount the amount to give change on
   * @param context the IContext to notify of the coins given
   * @return a Map of the money that is given back.
   */
  public Map<Coin, Integer> giveChange(int amount) {
    if (!isChangePossible(amount)) {
      throw new IllegalArgumentException();
    }
    final Map<Coin, Integer> moneyToGive = new Hashtable<Coin, Integer>();
    for (Coin coin: Coin.COINS) {
      moneyToGive.put(coin, coinsStock.get(coin) - coinsStockTemp.get(coin));
      coinsStock.put(coin, coinsStockTemp.get(coin));
    }
    return moneyToGive;
  }

  /**
   * @return a Map of the Coin's stock
   */
  public Map<Coin, Integer> getCoinsStock() {
    return coinsStock;
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
   * @param coin the Coin whose value must be known
   * @return the stock value of the specified Coin
   */
  public int getCoinsStock(Coin coin) {
    return coinsStock.get(coin);
  }

  /**
   * Updates the stock of {@code coin} to the {@code value} specified.
   * Throws an IllegalArgumentException if the value is negative.
   * 
   * @param coin the Coin whose stock must be changed
   * @param value the new value for the {@code coin} (must be positive)
   */
  public void setCoinStock(Coin coin, int value) {
    Utils.checkPositiveIntIllegal(value, "stock of " + coin.TEXT + " coins");
    Utils.logChange(value - coinsStock.get(coin), value, "\"" + coin.TEXT + "\" coin(s)");
    coinsStock.put(coin, value);
  }

}
