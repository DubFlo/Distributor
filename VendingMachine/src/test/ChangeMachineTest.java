package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.components.ChangeMachine;

public class ChangeMachineTest {

  private static Hashtable<Coin,Integer> coinsStock;
  private static Hashtable<Coin,Boolean> acceptedCoins;
  private ChangeMachine cm;
  @Before
  public void setUp(){

    int[]coinsStockTab = {1,1,0,3,0,0,4,1};
    coinsStock = new Hashtable<Coin,Integer>();
    boolean[] acceptedCoinsTab = {false, true, false, true, false, true, true, true};
    acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    cm = new ChangeMachine (coinsStock, acceptedCoins);
  }

  @Test(expected = IllegalArgumentException.class)
  public void isChangePossibleErrorTest(){
    cm.isChangePossible(-1);
  }
  @Test
  public void isChangePossibleTest(){
    assertFalse("Shortage of stock",cm.isChangePossible(422));

    assertTrue("Giving back change should be possible",cm.isChangePossible(342));

    assertFalse("Problem of accepted coins",cm.isChangePossible(500));
  }

  @Test(expected = IllegalArgumentException.class )
  public void setCoinsStockErrorTest() {
    cm.setCoinStock(Coin.COIN1, -1);
  }

  @Test
  public void setCoinsStockTest() {
    cm.setCoinStock(Coin.COIN10, 5);
    assertEquals(coinsStock.get(Coin.COIN10),new Integer(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void giveChangeErrorTest() {
    cm.giveChange(171);
  }

  @Test
  // Problème avec CoinsStockTemp(utilisation de isChangePossible pr effet de bord)
  // Pas tester si coinsStock avait bien été remplacé par coinsStockTemp(trivial)
  public void giveChangeTest() {
    cm.isChangePossible(109);
    Hashtable<Coin,Integer>otherMTG = new Hashtable<Coin,Integer>();
    int[] otherMTGTab = {0,1,0,0,0,0,4,1};
    for (int i = 0; i<8; i++) {
      otherMTG.put(Coin.COINS.get(i),otherMTGTab[i]);
    }
    assertEquals("Incorrect return value of giveChange",cm.giveChange(109),otherMTG);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorErrorStockTest() {
    coinsStock.put(Coin.COIN2, -4);
    ChangeMachine newCm = new ChangeMachine(coinsStock, acceptedCoins);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorErrorCoinContainTest() {
    coinsStock.remove(Coin.COIN2);
    ChangeMachine newCm = new ChangeMachine(coinsStock, acceptedCoins);
  }
  @Test(expected = IllegalArgumentException.class)
  public void constructorErrorStockContainTest() {
    acceptedCoins.remove(Coin.COIN5);
    ChangeMachine newCm = new ChangeMachine(coinsStock, acceptedCoins);
  }
}



