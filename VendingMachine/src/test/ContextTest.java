package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

  protected ChangeMachine changeMachine;
  protected Context context;
  protected Stock stock;

  @BeforeClass
  public static void setUpClass() {
    SoundLoader.getInstance(); // Increase in performance as it is loaded only once
  }

  @Before
  public void setUp() {
    // Initialize ChangeMachine
    int[] coinsStockTab = { 1, 1, 0, 5, 5, 0, 4, 1 };
    boolean[] acceptedCoinsTab = { false, true, true, true, true, true, false, true };
    Map<Coin, Integer> coinsStock = new Hashtable<Coin,Integer>();
    Map<Coin, Boolean> acceptedCoins = new Hashtable<Coin, Boolean>();
    for (int i = 0; i < 8 ; i++) {
      coinsStock.put(Coin.COINS.get(i), coinsStockTab[i]);
      acceptedCoins.put(Coin.COINS.get(i), acceptedCoinsTab[i]);
    }
    changeMachine = new ChangeMachine(new Change(coinsStock), acceptedCoins);

    // Initialize Stock
    String[] drinkNameTab = { "a", "b", "c", "d", "e" };
    boolean[] drinkSugarTab = { true, true, true, false, true };
    int[] drinkPriceTab = { 30, 40, 70, 0, 0 };
    int[] drinkStockTab = { 0, 5, 2, 3, 1 };
    Drink[] drinkTab = new Drink[5];
    Map<Drink,Integer> drinkQty = new LinkedHashMap<Drink,Integer>();
    for (int i = 0; i < drinkNameTab.length; i++) {
      drinkTab[i] = new Drink(drinkNameTab[i],drinkSugarTab[i],drinkPriceTab[i]);
      drinkQty.put(drinkTab[i], drinkStockTab[i]);
    }
    stock = new Stock(5, 5, 5, drinkQty); // (sugarCubesNbr, cupsNbr, spoonsNbr, drinkQty)

    //Initialize new context
    context = new Context(changeMachine, stock, 0); // No coin should get stuck
    context.setUI(new EmptyUI());
  }

  @Test
  public void testCoinInsertedInMultipleStates() {
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
    assertSame("Should stay in NoWater", context.getState(), NoWater.getInstance());

    context.problemSolved(NoWater.getInstance());
    assertSame("Should change to StuckCoin (only problem left)",
        context.getState(), StuckCoin.getInstance());

    context.coinInserted(Coin.COIN200);
    context.repairStuckCoins();
    assertSame("No problem left, changes to Idle", context.getState(), Idle.getInstance());
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

  @Test
  public void testConfirmNotSugaredDrink() {
    //Test in Idle
    context.drinkButton(context.getDrinks().get(3)); // Free drink, not sugared
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testConfirmSugaredDrink() {
    //Test in Asking
    context.drinkButton(context.getDrinks().get(4)); // Free drink, sugared
    assertSame(Asking.getInstance(), context.getState());
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testNoSpoonStateAndConfirm() {
    context.setSpoonsStock(0);
    context.drinkButton(context.getDrinks().get(4)); // Free drink, sugared
    assertSame(NoSpoon.getInstance(), context.getState());
    context.confirm();
    assertSame(Asking.getInstance(), context.getState());
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testNoSpoonStateAndCancel() {
    context.setSpoonsStock(0);
    context.drinkButton(context.getDrinks().get(4)); // Free drink, sugared
    assertSame(NoSpoon.getInstance(), context.getState());
    context.cancel();
    assertSame(Idle.getInstance(), context.getState());
  }

  @Test
  public void testDrinkButton() {
    context.coinInserted(Coin.COIN20);
    context.coinInserted(Coin.COIN50);
    context.addProblem(StuckCoin.getInstance());
    context.drinkButton(context.getDrinks().get(2)); // costs 0.7 euro
    assertSame("No drink order during Problem", StuckCoin.getInstance(), context.getState());
  }

  @Test
  public void testCancelInAskingWorks() {
    context.coinInserted(Coin.COIN20);
    context.coinInserted(Coin.COIN20);
    context.drinkButton(context.getDrinks().get(1)); // costs 0.4 euro, sugared
    assertSame(Asking.getInstance(),context.getState());
    context.cancel();
    assertSame(Idle.getInstance(), context.getState());
    assertEquals(0, context.getAmountInside());
  }

  @Test
  public void testCancelInProblemStateWorks() {
    context.coinInserted(Coin.COIN200);
    context.setWaterSupply(false);
    assertSame(NoWater.getInstance(), context.getState());
    context.cancel();
    assertEquals(0, context.getAmountInside());
  }

  @Test
  public void testCancelInPreparingIsRefused() {
    context.drinkButton(context.getDrinks().get(4)); // this drink is free, sugared
    context.confirm();
    assertSame(Preparing.getInstance(), context.getState());
    context.cancel();
    assertSame(Preparing.getInstance(), context.getState());
  }

  @Test
  public void testNoSpoonCancel() {
    context.setSpoonsStock(0);
    context.drinkButton(context.getDrinks().get(4)); // Free, sugared
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
    context.drinkButton(context.getDrinks().get(4)); // Free, sugared
    context.more();
    assertEquals(1, context.getChosenSugar());
    context.more();
    assertEquals("Should not be > 1", 1, context.getChosenSugar());
    context.setSugarStock(6);
    context.setChosenSugar(5);
    context.more();
    assertEquals("Maximum 5 sugar cubes chosen", 5, context.getChosenSugar());
  }

  @Test
  public void testLess() {
    //Test in Idle
    context.setChosenSugar(1);
    assertEquals(1, context.getChosenSugar());
    context.less();
    assertEquals(1, context.getChosenSugar());

    //Test in Asking
    context.drinkButton(context.getDrinks().get(4)); // Free, sugared
    assertEquals("Chosen sugar should be 0 when entrying Asking", 0, context.getChosenSugar());
    context.less();
    assertEquals("Chosen Sugar should not go below 0", 0, context.getChosenSugar());
    context.more();
    context.less();
    assertEquals(0, context.getChosenSugar());
  }

  @Test
  public void shouldBeginInNoCupIfNoCupsInitially() {
    context.setCupStock(0); // Changes the stock object
    Context newContext = new Context(changeMachine, stock, 0);
    newContext.setUI(new EmptyUI());
    assertSame(NoCup.getInstance(), newContext.getState());
  }

  @Test
  public void isMaintenancePossible() {
    assertTrue("Maintenance is possible in Idle", context.isAvailableForMaintenance());
    context.coinInserted(Coin.COIN100);
    context.drinkButton(context.getDrinks().get(1)); // 0.4 euro, sugared
    assertSame(Asking.getInstance(), context.getState());
    assertFalse("Maintenance is impossible in Asking", context.isAvailableForMaintenance());
  }

}
