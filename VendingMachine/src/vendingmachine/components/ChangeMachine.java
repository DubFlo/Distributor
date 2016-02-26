package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

public class ChangeMachine {

  public static final Coin[] COINS = Coin.values();
  
  private Map<Coin, Integer> coinsStock; //Les rendre final ?
  private Map<Coin, Integer> coinsStockTemp;
  private final Map<Coin, Boolean> acceptedCoins;
  private final Context context;

  public ChangeMachine(Map<Coin, Integer> coinsStock,
      Map<Coin, Boolean> acceptedCoins, Context context) {
    this.coinsStock = coinsStock;
    this.acceptedCoins = acceptedCoins;
    this.context = context;
    
    this.coinsStockTemp = new Hashtable<Coin, Integer>();
  }

  public Map<Coin, Integer> getCoinsStock() {
    return coinsStock;
  }

  /**
   * Updates the coinsStock with the value computed in isChangePossible(int) and stored
   * in coinsStockTemp. Adds the coins given to the "changeOut" Hashtable of the Context.
   */
  public void giveChange() { // à n'utiliser que si isPossibleChange(amount) == true
    Map<Coin, Integer> moneyToGive = new Hashtable<Coin, Integer>();
    for (Coin coin: COINS) {
      moneyToGive.put(coin, coinsStock.get(coin) - coinsStockTemp.get(coin));
    }
    coinsStock = copy(coinsStockTemp);
    context.addChangeOut(moneyToGive);
  }

  /**
   * Add the specified coin to the stock.
   */
  public void insertCoin(Coin coin) {
    coinsStock.put(coin, coinsStock.get(coin) + 1);
  }

  /**
   * Returns true if it is possible to give change with the current stock
   * for the amount value (in cents), false otherwise. Updates coinsStockTemp 
   * accordingly, which is the new value that the coinsStock should take after giving the change.
   * @param amount  number of cents to give change for.
   */
  public boolean isChangePossible(int amount) {
    coinsStockTemp = copy(coinsStock);
    
    for (Coin coin: COINS) {
      while (amount >= coin.VALUE && coinsStockTemp.get(coin) > 0) {
        coinsStockTemp.put(coin, coinsStockTemp.get(coin) - 1);
        amount -= coin.VALUE;
      }
    }

    return (amount == 0) ? true : false;
  }

  public boolean isCoinAccepted(Coin coin) {
    return acceptedCoins.get(coin);
  }

  private static Map<Coin, Integer> copy(Map<Coin, Integer> map) {
    final Map<Coin, Integer> res = new Hashtable<Coin, Integer>();
    for (Coin coin : COINS) {
      res.put(coin, map.get(coin)); // copy of the Integer
    }
    return res;
  }
  
}