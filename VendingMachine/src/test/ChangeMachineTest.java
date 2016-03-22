package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Context;

public class ChangeMachineTest {

  private static Hashtable<Coin,Integer> coinsStock;
  private static Hashtable<Coin,Boolean> acceptedCoins;
  private ChangeMachine cm;
  private static EmptyContext c;
  private Change change;
  
  @Before
  public void setUp() {

    int[] coinsStockTab = {1,1,0,3,0,0,4,1};
    coinsStock = new Hashtable<Coin,Integer>();
    boolean[] acceptedCoinsTab = {false, true, false, true, false, true, true, true};
    acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    change = new Change(coinsStock);
    cm = new ChangeMachine (change, acceptedCoins);
    c = new EmptyContext();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorErrorStock() {
    change.setCoinStock(Coin.COIN2, -4);
    new ChangeMachine(change, acceptedCoins);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorErrorStockContain() {
    acceptedCoins.remove(Coin.COIN5);
    new ChangeMachine(change, acceptedCoins);
  }
  
}



