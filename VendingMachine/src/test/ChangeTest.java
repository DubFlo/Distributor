package test;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;

public class ChangeTest {

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
  public void testConstructorErrorCoinContain() {
    change.getCoinsStock().remove(Coin.COIN2);
    new Change(coinsStock);
  }
  
  @Test
  public void testGiveChange() {
    cm.isChangePossible(109);
    Hashtable<Coin,Integer>otherMTG = new Hashtable<Coin,Integer>();
    int[] otherMTGTab = {0,1,0,0,0,0,4,1};
    for (int i = 0; i<8; i++) {
      otherMTG.put(Coin.COINS.get(i),otherMTGTab[i]);
    }
    assertEquals("Incorrect return value of giveChange",change.giveChange(109),otherMTG);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void tesGiveChangeError() {
    cm.giveChange(171,c);
  }
  
  @Test
  public void testSetCoinsStock() {
    cm.setCoinStock(Coin.COIN10, 5);
    assertEquals(coinsStock.get(Coin.COIN10), Integer.valueOf(5));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetCoinsStockError() {
    cm.setCoinStock(Coin.COIN1, -1);
  }
  @Test
  public void testIsChangePossible() {
    assertFalse("Shortage of stock",cm.isChangePossible(422));

    assertTrue("Giving back change should be possible",cm.isChangePossible(342));

    assertFalse("Problem of accepted coins",cm.isChangePossible(500));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testIsChangePossibleError(){
    cm.isChangePossible(-1);
  }
}

