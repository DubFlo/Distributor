package vendingmachine;

import java.util.Hashtable;
import java.util.Map;

public final class Utils {
  
  private Utils() {}
  
  public static int totalValue(Map<Coin, Integer> m) {
    int amount = 0;
    for (Coin coin: Coin.COINS) {
      amount += coin.VALUE * m.get(coin);
    }
    return amount;
  }
  
  public static Map<Coin, Integer> copy(Map<Coin, Integer> map) {
    final Map<Coin, Integer> res = new Hashtable<Coin, Integer>();
    for (Coin coin: Coin.COINS) {
      res.put(coin, map.get(coin)); // copy of the Integer
    }
    return res;
  }
  
  public static void resetCoinsMap(Map<Coin, Integer> map) {
    for (Coin coin: Coin.COINS) {
      map.put(coin, 0);
    }
  }
}
