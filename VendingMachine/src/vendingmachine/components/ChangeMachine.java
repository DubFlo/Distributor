package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

public class ChangeMachine {

  public static final Coin[] COINS = { Coin.COIN200, Coin.COIN100, Coin.COIN50, Coin.COIN20,
    Coin.COIN10, Coin.COIN5, Coin.COIN2, Coin.COIN1 }; //unmodifiableList ?????

  public static final String[] COINS_TEXT = { "2 €", "1 €", "0,5 €", "0,2 €", "0,1 €", "0,05 €",
    "0,02 €", "0,01 €" }; //unmodifiableList ?????

  private Map<Coin, Integer> coinsStock;
  private Map<Coin, Boolean> acceptedCoins;
  private Map<Coin, Integer> coinsStockTemp;
  private Context context;

  public ChangeMachine(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
    this.coinsStock = coinsStock;
    this.acceptedCoins = acceptedCoins;
  }

  public Map<Coin, Integer> getCoinsStock() {
    return coinsStock;
  }

  public void giveChange() { // à n'utiliser que si isPossibleChange(amount) == true
    Map<Coin, Integer> moneyToGive = new Hashtable<Coin, Integer>();
    for (Coin coin: COINS) {
      moneyToGive.put(coin, coinsStock.get(coin) - coinsStockTemp.get(coin));
    }
    coinsStock = copy(coinsStockTemp);
    context.addChangeOut(moneyToGive);
  }

  public void insertCoin(Coin coin) {
    coinsStock.put(coin, coinsStock.get(coin) + 1);
  }

  public boolean isChangePossible(int amount) {
    coinsStockTemp = copy(coinsStock);
    
    for (int i = 0; i < COINS.length; i++) {
      while (amount >= COINS[i].VALUE && coinsStockTemp.get(COINS[i]) > 0) {
        coinsStockTemp.put(COINS[i], coinsStockTemp.get(COINS[i]) - 1);
        amount -= COINS[i].VALUE;
      } // possibilité de break plus tôt du for (pas indispensable, à
      // discuter)
    }

    return (amount == 0) ? true : false;
  }

  public boolean isCoinAccepted(Coin coin) {
    return acceptedCoins.get(coin);
  }

  private static Map<Coin, Integer> copy(Map<Coin, Integer> t) { // Pas très propre ????
    Map<Coin, Integer> res = new Hashtable<Coin, Integer>();
    for (Coin c : COINS) {
      res.put(c, t.get(c)); // copy of the Integer
    }
    return res;
  }

  public void setContext(Context context) {
    this.context = context;
  }
  
}