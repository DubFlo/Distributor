package vendingmachine.components;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.*;

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
    @Ignore
    //copy is private
    public void testCopy() {
      assertEquals(coinsStock,ChangeMachine.copy(coinsStock));
    }
    @Test
    public void testIsChangePossible(){
      if (cm.isChangePossible(422))
        fail("Shortage of stock");
      
      if (!cm.isChangePossible(342))
        fail("All is ok");
      
      if (cm.isChangePossible(500))
        fail("Problem of accepted coins");
    }
    @Test
    public void testGiveChange() {
      
    }
   
    
 }      

