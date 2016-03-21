package vendingmachine.components;

import java.util.Map;

import vendingmachine.Coin;

/**
 * This class is able to perform operations about change and coins.
 * It uses as an attribute a stock of Coin's. Coin's can be inserted.
 * It can also give change on a specified amount (if it is possible).
 * Some coins can be refused.
 * 
 * @see Coin
 */
public class ChangeMachine {
  
  /**
   * A Map mapping each Coin to whether or not it is accepted.
   */
  private final Map<Coin, Boolean> acceptedCoins;
  
  private final Change change;

  /**
   * Builds a change machine with the specified coins stock.
   * Each coin may be accepted or not by the change machine.
   * Throws an IllegalArgumentException if {@code acceptedCoins}
   * do not list all the coins of Coin.
   * 
   * @param change a Change that deals with the stock of coins
   * @param acceptedCoins a Map that tells if each Coin is accepted or not
   */
  public ChangeMachine(Change change, Map<Coin, Boolean> acceptedCoins) {
    if (!acceptedCoins.keySet().containsAll(Coin.COINS)) {
      throw new IllegalArgumentException("acceptedCoins has to list all the coins defined in Coin");
    }
    this.change = change;
    this.acceptedCoins = acceptedCoins;
  }

  /**
   * Gives change on the specified amount.
   * 
   * @param amount the amount to give change on
   * @param context the IContext to notify of the coins given
   */
  public void giveChange(int amount, IContext context) {
    context.addChangeOut(change.giveChange(amount));
  }

  /**
   * Returns true if it is possible to give change with the current stock
   * for the amount value (in cents), false otherwise.
   * 
   * @param amount number of cents to give change for.
   * @return true if change on the amount is possible, false otherwise
   */
  public boolean isChangePossible(int amount) {
    return change.isChangePossible(amount);
  }

  /**
   * Adds the specified coin to the stock.
   * 
   * @param coin the Coin to add to the stock
   */
  public void insertCoin(Coin coin) {
    change.insertCoin(coin);
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
    return change.getCoinsStock(coin);
  }

  /**
   * @return a String containing all the information about the current coins.
   */
  public Object getInfo() {
    final StringBuilder sb = new StringBuilder(160);
    sb.append("Coins:\n");
    for (Coin coin: Coin.COINS) {
      sb.append(coin.TEXT).append(": ")
      .append(change.getCoinsStock(coin))
      .append(" available.\n");
    }
    return sb.toString();
  }

  /**
   * Updates the stock of {@code coin} to the {@code value} specified.
   * 
   * @param coin the Coin whose stock must be changed
   * @param value the new value for the {@code coin} (must be positive)
   */
  public void setCoinStock(Coin coin, int value) {
    change.setCoinStock(coin, value);
  }
  
}