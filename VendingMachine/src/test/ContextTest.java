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
  private ChangeMachine cm;
  private Context c;
  private Stock stock;
  private Change change;

  @BeforeClass
  public static void load() {
    SoundLoader.getInstance();
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
      acceptedCoins.put(Coin.COINS.get(i),acceptedCoinsTab[i]);
    }
    change = new Change(coinsStock);
    cm = new ChangeMachine(change,acceptedCoins);

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
    c = new Context(cm, stock, 0); // No coin should get stuck
    c.setUI(new EmptyUI());
  }

  @Test
  public void testchangeState() { //Bizarre
    c.changeState(Preparing.getInstance());
    assertEquals(c.getState(), Preparing.getInstance());
  }

  @Test
  public void testCoinInserted() {
    //Idle - Coin accepted
    assertEquals(c.getAmountInside(), 0);
    c.coinInserted(Coin.COIN100);
    assertEquals(c.getAmountInside(), 100);
    assertEquals(cm.getCoinsStock(Coin.COIN100), 2);
    
    //Idle - Coin not accepted
    int oldAmountInside = c.getAmountInside();
    c.coinInserted(Coin.COIN200);
    assertEquals(c.getAmountInside(), oldAmountInside);
    
    //Asking
    c.drinkButton(c.getDrinks().get(1));
    assertSame(Asking.getInstance(), c.getState());
    c.coinInserted(Coin.COIN200);
    assertEquals("The coin should not be inserted", 1, cm.getCoinsStock(Coin.COIN200));
    
    //Preparing
    c.confirm();
    assertSame(Preparing.getInstance(), c.getState());
    c.coinInserted(Coin.COIN10);
    int oldAmount = c.getAmountInside();
    assertEquals(oldAmount, c.getAmountInside());
    
    //StuckCoin
    c.addProblem(StuckCoin.getInstance());
    c.coinInserted(Coin.COIN5);
    assertEquals("Nothing should have changed", 0, cm.getCoinsStock(Coin.COIN5));
    //assertEquals(5, c.get?) vérif montant coincé
  }

  @Test
  public void testAddProblem() {
    c.addProblem(StuckCoin.getInstance());
    assertSame(StuckCoin.getInstance(), c.getState()); //Singleton design pattern
    c.addProblem(NoCup.getInstance());
    assertSame(NoCup.getInstance(), c.getState()); //Singleton design pattern
  }

  @Test
  public void testProblemSolved() {
    c.addProblem(StuckCoin.getInstance());
    c.addProblem(NoCup.getInstance());
    c.addProblem(NoWater.getInstance());

    assertSame(c.getState(), NoWater.getInstance());
    c.problemSolved(NoCup.getInstance());
    assertSame(c.getState(), NoWater.getInstance());

    c.problemSolved(NoWater.getInstance());
    assertSame(c.getState(), StuckCoin.getInstance());

    c.problemSolved(StuckCoin.getInstance());
    assertSame(c.getState(), Idle.getInstance());
  }

  @Test
  public void testGiveChange() {
    c.insertCoin(Coin.COIN50);
    assertEquals(50, c.getAmountInside());
    c.giveChange(25);
    assertEquals(0, c.getAmountInside());
  }
  
  @Test
  public void testAreDrinksFree() {
    assertFalse(c.areDrinksFree());
  }

  @Test
  public void testOrderOfADrink() throws InterruptedException {
    final int oldSugarStock = c.getStock().getSugarCubesNbr();
    final int oldCupsStock = c.getStock().getCupsNbr();
    final int oldSpoonsStock = c.getStock().getSpoonsNbr();
    final int oldDrinkStock = c.getStock().getDrinkQty(c.getDrinks().get(1));
    
    assertSame(c.getState(), Idle.getInstance());
    c.coinInserted(Coin.COIN100);
    assertEquals(c.getAmountInside(), 100);
    
    c.drinkButton(c.getDrinks().get(1));
    assertSame(Asking.getInstance(), c.getState());
    assertSame(c.getDrinks().get(1), c.getChosenDrink());
    c.more();
    c.more();
    assertEquals(2, c.getChosenSugar());
    
    c.confirm();
    assertEquals(c.getState(), Preparing.getInstance());
    
    Thread.sleep(SoundLoader.getInstance().FILLING.getMicrosecondLength() / 1000 + 100);
    assertSame(c.getState(), Idle.getInstance());
    assertEquals(oldSugarStock - 2, c.getStock().getSugarCubesNbr());
    assertEquals(oldCupsStock - 1, c.getStock().getCupsNbr());
    assertEquals(oldSpoonsStock - 1, c.getStock().getSpoonsNbr());
    assertEquals(oldDrinkStock - 1, c.getStock().getDrinkQty(c.getDrinks().get(1)));
  }

  @Test
  public void testConfirm() {
    //Test in Idle
    c.setChosenDrink(c.getDrinks().get(4));
    c.confirm();
    assertSame(Idle.getInstance(), c.getState());

    //Test in Asking
    c.insertCoin(Coin.COIN20);
    c.drinkButton(c.getDrinks().get(4));
    c.confirm();
    assertSame(Preparing.getInstance(), c.getState());
  }

  @Test
  public void testNoSpoonConfirm() {
    c.setSugarStock(0);
    c.drinkButton(c.getDrinks().get(4));
    c.confirm();
    assertSame(Preparing.getInstance(), c.getState());
  }
  
  @Test
  public void testDrinkButton() {
    //Idle
    c.coinInserted(Coin.COIN20);
    c.coinInserted(Coin.COIN50);
    c.addProblem(StuckCoin.getInstance());
    c.drinkButton(c.getDrinks().get(2));
    assertSame(StuckCoin.getInstance(), c.getState());
  }

  @Test
  public void testCancel() {
    //Cancel authorized in Asking
    c.coinInserted(Coin.COIN20);
    c.coinInserted(Coin.COIN20);
    c.drinkButton(c.getDrinks().get(1));
    assertSame(Asking.getInstance(),c.getState());
    c.cancel();
    assertSame(Idle.getInstance(), c.getState());
    assertEquals(0, c.getAmountInside());

    //Cancel authorized in Problem
    c.coinInserted(Coin.COIN200);
    c.addProblem(NoWater.getInstance());
    c.cancel();
    assertEquals(0, c.getAmountInside());

    //Cancel not authorized
    c.problemSolved(NoWater.getInstance());
    c.drinkButton(c.getDrinks().get(4));
    c.confirm();
    assertSame(Preparing.getInstance(), c.getState());
    c.cancel();
    assertSame(Preparing.getInstance(), c.getState());
  }

  @Test
  public void testNoSpoonCancel() {
    c.setSpoonsStock(0);
    c.drinkButton(c.getDrinks().get(4));
    assertSame(NoSpoon.getInstance(), c.getState());
    c.cancel();
    assertSame(Idle.getInstance(), c.getState());
  }
  @Test
  public void testMore() {
    //Test in Idle
    c.setSugarStock(1);
    c.more();
    assertEquals(0, c.getChosenSugar());

    //Test in Asking
    c.drinkButton(c.getDrinks().get(4));
    c.more();
    assertEquals(1, c.getChosenSugar());
    c.more();
    assertEquals("Problem of stock",1, c.getChosenSugar());
    c.setSugarStock(6);
    c.setChosenSugar(5);
    c.more();
    assertEquals("Maximum 5 sugar chosen",5, c.getChosenSugar());
  }
  
  @Test
  public void testLess() {
    //Test in Idle(?)
    c.setChosenSugar(1);
    assertEquals(1, c.getChosenSugar());
    c.less();
    assertEquals(1, c.getChosenSugar());
    //Test in Asking
    c.drinkButton(c.getDrinks().get(4));
    c.less();
    assertEquals(0, c.getChosenSugar());
    c.more();//chosenSugar += 1
    c.less();
    assertEquals(0, c.getChosenSugar());
  }
  
}
