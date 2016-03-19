package test;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Context;
import vendingmachine.components.Stock;

public class ContextTest {

  private Hashtable<Coin, Integer> coinsStock;
  private Hashtable<Coin, Boolean> acceptedCoins;
  private ChangeMachine cm;
  private Context c;
  private int coinStuck;
  private Stock stock;
  @Before
  public void setUp() {
    //init ChangeMachine
    int[] coinsStockTab = {1,1,0,3,0,0,4,1};
    coinsStock = new Hashtable<Coin,Integer>();
    boolean[] acceptedCoinsTab = {false, true, false, true, false, true, true, true};
    acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    //init Stock
    String[] drinkNameTab = {"a","b","c","d","e"};
    boolean[] drinkSugarTab = {true,false,true,true,true};
    int[] drinkPriceTab = {100,95,80,0,45};
    int[] drinkStockTab = {0,1,2,3,1};
    Drink[] drinkTab = new Drink[5];
    Map<Drink,Integer> drinkQty = new Hashtable<Drink,Integer>();
   
    for (int i = 0; i < drinkNameTab.length; i++) {
      drinkTab[i] = new Drink(drinkNameTab[i],drinkSugarTab[i],drinkPriceTab[i]);
      drinkQty.put(drinkTab[i], drinkStockTab[i] );  
    }
    

    stock = new Stock(5,4,2,drinkQty); //(sugarCubesNbr, cupsNbr, int spoonsNbr, Map<Drink, Integer> drinkQty)
    
    //coinStuckProb
    coinStuck = 0;
    c = new Context(cm,stock,coinStuck);
  }
  
  @Test
  public void testCancel() {
    
  }

}
