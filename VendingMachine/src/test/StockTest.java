package test;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.SoundLoader;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Context;
import vendingmachine.components.Stock;
import vendingmachine.states.Idle;
import vendingmachine.states.NoCup;
import vendingmachine.states.NoSpoon;

public class StockTest {

  private Hashtable<Coin, Integer> coinsStock;
  private Hashtable<Coin, Boolean> acceptedCoins;
  private ChangeMachine cm;
  private Context c;
  private int coinStuck;
  private Stock stock;
 
  @BeforeClass
  public static void load() {
    SoundLoader.getInstance();
  } 
  
  @Before
  public void setUp() {
  //initialize ChangeMachine
    int[] coinsStockTab = {1,1,0,5,5,0,4,1};
    coinsStock = new Hashtable<Coin,Integer>();
    boolean[] acceptedCoinsTab = {false, true, true, true, true, true, false, true};
    acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    cm = new ChangeMachine(coinsStock,acceptedCoins);
    //initialize Stock
    String[] drinkNameTab = {"a","b","c","d","e"};
    boolean[] drinkSugarTab = {true,true,true,true,false};
    int[] drinkPriceTab = {100,40,70,0,20};
    int[] drinkStockTab = {1,5,2,3,1};
    Drink[] drinkTab = new Drink[5];
    Map<Drink,Integer> drinkQty = new LinkedHashMap<Drink,Integer>();
   
    for (int i = 0; i < drinkNameTab.length; i++) {
      drinkTab[i] = new Drink(drinkNameTab[i],drinkSugarTab[i],drinkPriceTab[i]);
      drinkQty.put(drinkTab[i], drinkStockTab[i]);  
    }
     
  
    stock = new Stock(5,5,5,drinkQty); //(sugarCubesNbr, cupsNbr,spoonsNbr, Map<Drink, Integer> drinkQty)
    
    //coinStuckProb
    coinStuck = 0;
    //initialize new context
    c = new Context(cm,stock,coinStuck);
    c.setUI(new EmptyUI());  
  }
  
  @Test
  public void testSetSpoonsStock() {
    stock.setSpoonsStock(2);
    assertEquals(2,stock.getSpoonsNbr());
    
    stock.setSpoonsStock(0);
    c.coinInserted(Coin.COIN100);
    c.drinkButton(c.getDrinks().get(0));
    assertEquals (NoSpoon.getInstance(),c.getState());
  }
  
  @Test
  public void testSetDrinkQty() {
    stock.setDrinkQty(c.getDrinks().get(2),3);
    assertEquals(3, stock.getDrinkQty(c.getDrinks().get(2)));
    
    stock.setDrinkQty(c.getDrinks().get(2),0);
    c.drinkButton(c.getDrinks().get(2));
    assertEquals(Idle.getInstance(), c.getState());
  }
  @Test(expected = IllegalArgumentException.class)
  public void testErrorSetCupStock() {
    stock.setCupStock(-1, c);
  }
  @Test
  public void testSetCupsStock() {
    stock.setCupStock(4,c);
    assertEquals(4,stock.getCupsNbr());
    
    stock.setCupStock(0, c);
    assertEquals(NoCup.getInstance(),c.getState());
  }
  @Test
  public void testSugarStock() {
    stock.setSugarStock(2);
    assertEquals (2, stock.getSugarCubesNbr());
    
    stock.setSugarStock(0);
    c.coinInserted(Coin.COIN100);
    c.drinkButton(c.getDrinks().get(0));
    c.more();
    assertEquals(0,c.getChosenSugar());
  }   
  @Test
  public void testRemoveDrink() {
    
  }
    
    

}
