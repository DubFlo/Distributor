package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;

public class ChangeMachineTest {

  private Map<Coin,Boolean> acceptedCoins;
  private Change change;

  @Before
  public void setUp() {
    int[] coinsStockTab = { 1, 1, 0, 3, 0, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, false, true, false, true, true, true };
    Map<Coin,Integer> coinsStock = new Hashtable<Coin,Integer>();
    acceptedCoins = new Hashtable<Coin, Boolean>();
    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }

    change = new Change(coinsStock);
  }

  @Test
  public void testGettersAndSetters() {
    ChangeMachine changeMachine = new ChangeMachine(change, acceptedCoins);
    assertFalse(changeMachine.isCoinAccepted(Coin.COIN50));
    assertEquals(3, changeMachine.getCoinsStock(Coin.COIN20));
    changeMachine.insertCoin(Coin.COIN2);
    assertEquals(5, changeMachine.getCoinsStock(Coin.COIN2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeStock() {
    change.setCoinStock(Coin.COIN2, -4);
    new ChangeMachine(change, acceptedCoins);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorErrorCoinMissing() {
    acceptedCoins.remove(Coin.COIN5);
    new ChangeMachine(change, acceptedCoins);
  }

}