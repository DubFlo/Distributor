package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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

public class StockWithContextTest {

  private Context context;
  private Stock stock;

  @BeforeClass
  public static void setUpClass() {
    SoundLoader.getInstance(); // Increase in performance as it is loaded only once
  }

  @Before
  public void setUp() {
    //initialize ChangeMachine (not concerned by this test but necessary to create Context)
    int[] coinsStockTab = { 1, 1, 0, 5, 5, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, true, true, true, true, false, true };
    Map<Coin, Integer> coinsStock = new Hashtable<Coin,Integer>();
    Map<Coin, Boolean> acceptedCoins = new Hashtable<Coin, Boolean>();
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
    context = new Context(cm, stock, 0); // A coin should never get stuck
    context.setUI(new EmptyUI());
  }

  @Test
  public void testNoSpoonState() {
    context.setSpoonsStock(1);
    assertEquals(1, stock.getSpoonsNbr());

    stock.removeSpoon();
    assertEquals(0, stock.getSpoonsNbr());
    context.coinInserted(Coin.COIN100);
    context.drinkButton(context.getDrinks().get(0)); // costs 1 euro
    assertEquals(NoSpoon.getInstance(), context.getState());
  }

  @Test
  public void testSetDrinkQty() {
    context.setDrinkStock(context.getDrinks().get(2), 3);
    assertEquals(3, stock.getDrinkQty(context.getDrinks().get(2)));

    context.setDrinkStock(context.getDrinks().get(3), 0);
    context.drinkButton(context.getDrinks().get(3)); // This Drink is free
    assertEquals(Idle.getInstance(), context.getState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNegativeCupStock() {
    context.setCupStock(-1);
  }

  @Test
  public void testSetCupsStock() {
    context.setCupStock(4);
    assertEquals(4, stock.getCupsNbr());

    context.setCupStock(0);
    assertEquals(NoCup.getInstance(), context.getState());

    context.setCupStock(1);
    assertEquals(Idle.getInstance(), context.getState());
  }

  @Test
  public void testSugarStock() {
    context.setSugarStock(2);
    assertEquals(2, stock.getSugarCubesNbr());
    stock.removeSugarCubes(1);
    assertEquals(1, stock.getSugarCubesNbr());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveDrinkError() {
    stock.removeDrink(context.getDrinks().get(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveCupError() {
    context.setCupStock(0);
    stock.removeCup(context);
  }

  @Test
  public void testRemoveCupToZero() {
    context.setCupStock(1);
    stock.removeCup(context);
    assertSame(NoCup.getInstance(), context.getState());
    context.setCupStock(1);
    assertSame(Idle.getInstance(), context.getState());
  }

}