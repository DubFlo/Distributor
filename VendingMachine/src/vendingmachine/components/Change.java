package vendingmachine.components;

import java.util.Map;

import vendingmachine.Coin;

/**
 *
 */
public class Change {

  private final Map<Coin, Integer> coinsStock;
  private final Map<Coin, Boolean> acceptedCoins;
  
  public Change(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
    this.coinsStock = coinsStock;
    this.acceptedCoins = acceptedCoins;
  }

  /**
   * @return a Map of the Coin's stock
   */
  public Map<Coin, Integer> getCoinsStock() {
    return coinsStock;
  }

  /**
   * @return a Map that tells for each coin if it is accepted or not
   */
  public Map<Coin, Boolean> getAcceptedCoins() {
    return acceptedCoins;
  }

}
