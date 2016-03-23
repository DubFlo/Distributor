package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;

public class ChangeTest {

  private Hashtable<Coin,Integer> coinsStock;
  private Hashtable<Coin,Boolean> acceptedCoins;
  private ChangeMachine changeMachine;
  private Change change;

  @Before
  public void setUp() {

    int[] coinsStockTab = { 1, 1, 0, 3, 0, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, false, true, false, true, true, true };
    coinsStock = new Hashtable<Coin,Integer>();
    acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    change = new Change(coinsStock);
    changeMachine = new ChangeMachine(change, acceptedCoins);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorErrorCoinMissing() {
    change.getCoinsStock().remove(Coin.COIN2);
    new Change(coinsStock);
  }

  @Test
  public void testGiveChange() {
    assertTrue(changeMachine.isChangePossible(109));
    Hashtable<Coin, Integer> changeExpected = new Hashtable<Coin, Integer>();
    int[] changeExpectedTab = { 0, 1, 0, 0, 0, 0, 4, 1 };
    for (int i = 0; i < 8; i++) {
      changeExpected.put(Coin.COINS.get(i), changeExpectedTab[i]);
    }
    assertEquals("Incorrect return value of giveChange", change.giveChange(109), changeExpected);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImpossibleGivingOfChange() {
    changeMachine.giveChange(171, new EmptyContext());
  }

  @Test
  public void testModifyCoinsStock() {
    assertEquals(change.getCoinsStock(Coin.COIN10), 0);
    changeMachine.setCoinStock(Coin.COIN10, 5);
    assertEquals(change.getCoinsStock(Coin.COIN10), 5);
    changeMachine.insertCoin(Coin.COIN10);
    assertEquals(change.getCoinsStock(Coin.COIN10), 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNegativeCoinsStockError() {
    changeMachine.setCoinStock(Coin.COIN1, -1);
  }

  @Test
  public void testIsChangePossible() {
    assertFalse("Giving back change should not be possible", changeMachine.isChangePossible(422));

    assertTrue("Giving back change should be possible", changeMachine.isChangePossible(342));

    assertFalse("Giving back change should not be possible", changeMachine.isChangePossible(500));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsChangePossibleError(){
    changeMachine.isChangePossible(-1);
  }

}