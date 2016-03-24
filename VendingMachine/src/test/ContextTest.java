package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import vendingmachine.states.Asking;
import vendingmachine.states.Idle;
import vendingmachine.states.NoCup;
import vendingmachine.states.NoSpoon;
import vendingmachine.states.NoWater;
import vendingmachine.states.Preparing;
import vendingmachine.states.StuckCoin;

public class ContextTest {

  private Hashtable<Coin, Integer> coinsStock;
  private Hashtable<Coin, Boolean> acceptedCoins;
  private ChangeMachine changeMachine;
  private Context context;
  private Stock stock;

  @BeforeClass
  public static void load() {
    SoundLoader.getInstance(); // Increase in performance as it is loaded only once
  }

  @Before
  public void setUp() {
    // Initialize ChangeMachine
    int[] coinsStockTab = { 1, 1, 0, 5, 5, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, true, true, true, true, false, true };
    coinsStock = new Hashtable<Coin,Integer>();
    acceptedCoins = new Hashtable<Coin, Boolean>();
    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i), acceptedCoinsTab[i]);
    }
    changeMachine = new ChangeMachine(new Change(coinsStock), acceptedCoins);

    // Initialize Stock
    String[] drinkNameTab = { "a", "b", "c", "d", "e" };
    boolean[] drinkSugarTab = { true, true, true, false, true };
    int[] drinkPriceTab = {30,40,70,10,0};
    int[] drinkStockTab = {0,5,2,3,1};
    Drink[] drinkTab = new Drink[5];
    Map<Drink,Integer> drinkQty = new LinkedHashMap<Drink,Integer>();
    for (int i = 0; i < drinkNameTab.length; i++) {
      drinkTab[i] = new Drink(drinkNameTab[i],drinkSugarTab[i],drinkPriceTab[i]);
      drinkQty.put(drinkTab[i], drinkStockTab[i]);
    }
    stock = new Stock(5,5,5,drinkQty); //(sugarCubesNbr, cupsNbr,spoonsNbr, Map<Drink, Integer> drinkQty)

    //Initialize new context
    context = new Context(changeMachine, stock, 0); // No coin should get stuck
    context.setUI(new EmptyUI());
  }

  @Test
  public void testCoinInserted() {
    //Idle - Coin accepted
    assertEquals(context.getAmountInside(), 0);
    context.coinInserted(Coin.COIN100);
    assertEquals(context.getAmountInside(), 100);
    assertEquals(changeMachine.getCoinsStock(Coin.COIN100), 2);
    
    //Idle - Coin not accepted
    int oldAmountInside = context.getAmountInside();
    context.coinInserted(Coin.COIN200);
    assertEquals(context.getAmountInside(), oldAmountInside);
    
    //Asking
    context.drinkButton(context.getDrinks().get(1));
    assertSame(Asking.getInstance(), context.getState());
    context.coinInserted(Coin.COIN200);
    assertEquals("The coin should not be inserted", 1, changeMachine.getCoinsStock(Coin.COIN200));
    
    //Preparing
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
    context.coinInserted(Coin.COIN10);
    int oldAmount = context.getAmountInside();
    assertEquals(oldAmount, context.getAmountInside());
    
    //StuckCoin
    context.addProblem(StuckCoin.getInstance());
    context.coinInserted(Coin.COIN5);
    assertEquals("Nothing should have changed", 0, changeMachine.getCoinsStock(Coin.COIN5));
  }

  @Test
  public void testAddProblem() {
    context.addProblem(StuckCoin.getInstance());
    assertSame(StuckCoin.getInstance(), context.getState()); //Singleton design pattern
    context.addProblem(NoCup.getInstance());
    assertSame(NoCup.getInstance(), context.getState()); //Singleton design pattern
  }

  @Test
  public void testProblemSolved() {
    context.addProblem(StuckCoin.getInstance());
    context.addProblem(NoCup.getInstance());
    context.addProblem(NoWater.getInstance());

    assertSame(context.getState(), NoWater.getInstance());
    context.problemSolved(NoCup.getInstance());
    assertSame(context.getState(), NoWater.getInstance());

    context.problemSolved(NoWater.getInstance());
    assertSame(context.getState(), StuckCoin.getInstance());

    context.problemSolved(StuckCoin.getInstance());
    assertSame(context.getState(), Idle.getInstance());
  }

  @Test
  public void testGiveChange() {
    context.insertCoin(Coin.COIN50);
    assertEquals(50, context.getAmountInside());
    context.giveChange(25);
    assertEquals(0, context.getAmountInside());
  }
  
  @Test
  public void testAreDrinksFree() {
    assertFalse(context.areDrinksFree());
  }

  /**
   * Makes a full order of a Drink.
   * Checks that the states are logical and the stock is correctly updated.
   */
  @Test
  public void testOrderOfADrink() throws InterruptedException {
    final int oldSugarStock = context.getStock().getSugarCubesNbr();
    final int oldCupsStock = context.getStock().getCupsNbr();
    final int oldSpoonsStock = context.getStock().getSpoonsNbr();
    final int oldDrinkStock = context.getStock().getDrinkQty(context.getDrinks().get(1));
    
    assertSame(context.getState(), Idle.getInstance());
    context.coinInserted(Coin.COIN100);
    assertEquals(context.getAmountInside(), 100);
    
    context.drinkButton(context.getDrinks().get(1));
    assertSame(Asking.getInstance(), context.getState());
    assertSame(context.getDrinks().get(1), context.getChosenDrink());
    context.more();
    context.more();
    assertEquals(2, context.getChosenSugar());
    
    context.confirm();
    assertEquals(context.getState(), Preparing.getInstance());
    
    Thread.sleep(SoundLoader.getInstance().FILLING.getMicrosecondLength() / 1000 + 100);
    assertSame(context.getState(), Idle.getInstance());
    assertEquals(oldSugarStock - 2, context.getStock().getSugarCubesNbr());
    assertEquals(oldCupsStock - 1, context.getStock().getCupsNbr());
    assertEquals(oldSpoonsStock - 1, context.getStock().getSpoonsNbr());
    assertEquals(oldDrinkStock - 1, context.getStock().getDrinkQty(context.getDrinks().get(1)));
  }

  @Test
  public void testConfirm() {
    //Test in Idle
    context.drinkButton(context.getDrinks().get(4));
    context.confirm();
    assertSame(Idle.getInstance(), context.getState());

    //Test in Asking
    context.insertCoin(Coin.COIN20);
    context.drinkButton(context.getDrinks().get(4));
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testNoSpoonConfirm() {
    context.setSugarStock(0);
    context.drinkButton(context.getDrinks().get(4));
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
  }
  
  @Test
  public void testDrinkButton() {
    //Idle
    context.coinInserted(Coin.COIN20);
    context.coinInserted(Coin.COIN50);
    context.addProblem(StuckCoin.getInstance());
    context.drinkButton(context.getDrinks().get(2));
    assertSame(StuckCoin.getInstance(), context.getState());
  }

  @Test
  public void testCancel() {
    //Cancel authorized in Asking
    context.coinInserted(Coin.COIN20);
    context.coinInserted(Coin.COIN20);
    context.drinkButton(context.getDrinks().get(1));
    assertSame(Asking.getInstance(),context.getState());
    context.cancel();
    assertSame(Idle.getInstance(), context.getState());
    assertEquals(0, context.getAmountInside());

    //Cancel authorized in Problem
    context.coinInserted(Coin.COIN200);
    context.addProblem(NoWater.getInstance());
    context.cancel();
    assertEquals(0, context.getAmountInside());

    //Cancel not authorized
    context.problemSolved(NoWater.getInstance());
    context.drinkButton(context.getDrinks().get(4));
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
    context.cancel();
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testNoSpoonCancel() {
    context.setSpoonsStock(0);
    context.drinkButton(context.getDrinks().get(4));
    assertSame(NoSpoon.getInstance(), context.getState());
    context.cancel();
    assertSame(Idle.getInstance(), context.getState());
  }
  @Test
  public void testMore() {
    //Test in Idle
    context.setSugarStock(1);
    context.more();
    assertEquals(0, context.getChosenSugar());

    //Test in Asking
    context.drinkButton(context.getDrinks().get(4));
    context.more();
    assertEquals(1, context.getChosenSugar());
    context.more();
    assertEquals("Problem of stock",1, context.getChosenSugar());
    context.setSugarStock(6);
    context.setChosenSugar(5);
    context.more();
    assertEquals("Maximum 5 sugar chosen",5, context.getChosenSugar());
  }
  
  @Test
  public void testLess() {
    //Test in Idle(?)
    context.setChosenSugar(1);
    assertEquals(1, context.getChosenSugar());
    context.less();
    assertEquals(1, context.getChosenSugar());
    //Test in Asking
    context.drinkButton(context.getDrinks().get(4));
    context.less();
    assertEquals(0, context.getChosenSugar());
    context.more();//chosenSugar += 1
    context.less();
    assertEquals(0, context.getChosenSugar());
  }
  
}
