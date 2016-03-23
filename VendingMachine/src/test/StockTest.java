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
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Context;
import vendingmachine.components.Stock;
import vendingmachine.states.Idle;
import vendingmachine.states.NoCup;
import vendingmachine.states.NoSpoon;

public class StockTest {

  private Context c;
  private Stock stock;
 
  @BeforeClass
  public static void load() {
    SoundLoader.getInstance();
  } 
  
  @Before
  public void setUp() {
    //initialize ChangeMachine
    int[] coinsStockTab = { 1, 1, 0, 5, 5, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, true, true, true, true, false, true };
    Hashtable<Coin, Integer> coinsStock = new Hashtable<Coin,Integer>();
    Hashtable<Coin, Boolean> acceptedCoins = new Hashtable<Coin, Boolean>();

    for (int i = 0; i < 8; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i), acceptedCoinsTab[i]);
    }
    
    Change change = new Change(coinsStock);
    ChangeMachine cm = new ChangeMachine(change,acceptedCoins);
    
    // Initialize Stock
    String[] drinkNameTab = { "a", "b", "c", "d", "e" };
    boolean[] drinkSugarTab = { true, true, true, true, false };
    int[] drinkPriceTab = { 100, 40, 70, 0, 20 };
    int[] drinkStockTab = { 1, 5, 2, 0, 1 };
    Drink[] drinkTab = new Drink[5];
    Map<Drink,Integer> drinkQty = new LinkedHashMap<Drink,Integer>();
   
    for (int i = 0; i < drinkNameTab.length; i++) {
      drinkTab[i] = new Drink(drinkNameTab[i],drinkSugarTab[i],drinkPriceTab[i]);
      drinkQty.put(drinkTab[i], drinkStockTab[i]);  
    }
     
    stock = new Stock(5, 5, 5, drinkQty); //(sugarCubesNbr, cupsNbr, spoonsNbr, drinkQty)
    
    // Initialize new context
    c = new Context(cm, stock, 0); // A coin should never get stuck
    c.setUI(new EmptyUI());  
  }
  
  @Test
  public void testNoSpoonState() {
    stock.setSpoonsStock(2);
    assertEquals(2, stock.getSpoonsNbr());
    
    stock.setSpoonsStock(1);
    stock.removeSpoon();
    assertEquals(0, stock.getSpoonsNbr());
    c.coinInserted(Coin.COIN100);
    c.drinkButton(c.getDrinks().get(0));
    assertEquals(NoSpoon.getInstance(), c.getState());
  }
  
  @Test
  public void testSetDrinkQty() {
    stock.setDrinkQty(c.getDrinks().get(2), 3);
    assertEquals(3, stock.getDrinkQty(c.getDrinks().get(2)));
    
    stock.setDrinkQty(c.getDrinks().get(3), 0);
    c.drinkButton(c.getDrinks().get(3)); // This Drink is free
    assertEquals(Idle.getInstance(), c.getState());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNegativeCupStock() {
    stock.setCupStock(-1, c);
  }
  
  @Test
  public void testSetCupsStock() {
    stock.setCupStock(4, c);
    assertEquals(4, stock.getCupsNbr());
    
    stock.setCupStock(0, c);
    assertEquals(NoCup.getInstance(), c.getState());
    
    stock.setCupStock(1, c);
    assertEquals(Idle.getInstance(), c.getState());
  }
  
  @Test
  public void testSugarStock() { // A retirer ???????????????????????????????????????????????
    stock.setSugarStock(2);
    assertEquals(2, stock.getSugarCubesNbr());
    
    stock.setSugarStock(1);
    c.coinInserted(Coin.COIN100);
    c.drinkButton(c.getDrinks().get(0));
    c.more();
    assertEquals(1, c.getChosenSugar());
    c.more();
    assertEquals(1, c.getChosenSugar());
  }   
  
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveDrinkError() {
    stock.removeDrink(c.getDrinks().get(3));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveCupError() {
    stock.setCupStock(0, c);
    stock.removeCup(c);
  }
  
  @Test
  public void testRemoveCupToZero() {
    stock.setCupStock(1, c);
    stock.removeCup(c);
    assertEquals(NoCup.getInstance(),c.getState());
  }
    
    

}
