package vendingmachine.components;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Hashtable;

import vendingmachine.Coin;

public class ChangeMachineTest {
  
  private Hashtable<Coin,Integer> coinsStock;
  private Hashtable<Coin,Boolean> acceptedCoins;
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
    @Test
    public void isChangePossibleTest(){
      assertFalse("Shortage of stock",cm.isChangePossible(422));
      
      assertTrue("Giving back change should be possible",cm.isChangePossible(342));
      
      assertFalse("Problem of accepted coins",cm.isChangePossible(500));
    }
    @Test(expected = IllegalArgumentException.class )
    public void setCoinsStockTest() {
     cm.setCoinStock(Coin.COIN1, -1); 
     cm.setCoinStock(Coin.COIN1, 4);
     assertEquals(cm.coinsStock.Coin.COIN1,4);
    }
   
    
 }      

