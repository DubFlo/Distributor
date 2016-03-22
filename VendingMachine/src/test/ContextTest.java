package test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Timer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.FontLoader;
import vendingmachine.SoundLoader;
import vendingmachine.components.Change;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Context;
import vendingmachine.components.IContext;
import vendingmachine.components.Stock;
import vendingmachine.states.Asking;
import vendingmachine.states.Idle;
import vendingmachine.states.NoCup;
import vendingmachine.states.NoWater;
import vendingmachine.states.Preparing;
import vendingmachine.states.StuckCoin;
import vendingmachine.ui.VendingMachineGUI;


public class ContextTest {

  private Hashtable<Coin, Integer> coinsStock;
  private Hashtable<Coin, Boolean> acceptedCoins;
  private ChangeMachine cm;
  private Context c;
  private int coinStuck;
  private Stock stock;
  private Change change;
  
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
    change = new Change(coinsStock);
    cm = new ChangeMachine(change,acceptedCoins);
    //initialize Stock
    String[] drinkNameTab = {"a","b","c","d","e"};
    boolean[] drinkSugarTab = {true,true,true,false,true};
    int[] drinkPriceTab = {30,40,70,0,20};
    int[] drinkStockTab = {0,5,2,3,1};
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
  public void testchangeState() {
    c.changeState(Preparing.getInstance());
    assertEquals(c.getState(), Preparing.getInstance());
  }
  
  
  @Test
  public void testCoinInserted() {
    //Idle - Coin accepted
    assertEquals(c.getAmountInside(),0);
    c.coinInserted(Coin.COIN100);
    assertEquals(c.getAmountInside(),100);
    assertEquals(cm.getCoinsStock(Coin.COIN100),2);
    //Idle - Coin not accepted 
    int oldAmountInside = c.getAmountInside();
    c.coinInserted(Coin.COIN200);
    assertEquals(c.getAmountInside(),oldAmountInside);
    //Asking
    c.setChosenDrink(c.getStock().getDrinks().get(1));
    c.changeState(Asking.getInstance());
    c.coinInserted(Coin.COIN200);
    assertEquals("Nothing should have changed",1,cm.getCoinsStock(Coin.COIN200));
    //StuckCoin
    c.addProblem(StuckCoin.getInstance());
    c.coinInserted(Coin.COIN5);
    assertEquals("Nothing should have changed",0,cm.getCoinsStock(Coin.COIN5));
  //verif monnaie coincée
    }
  @Test
  public void testAddProblem() {
    c.addProblem(StuckCoin.getInstance());
    assertSame(c.getState(),StuckCoin.getInstance());//Singleton design pattern
    c.addProblem(NoCup.getInstance());
    assertSame(c.getState(),NoCup.getInstance());//Singleton design pattern
  }
  @Test
  public void testProblemSolved() {
    c.addProblem(StuckCoin.getInstance());
    c.addProblem(NoCup.getInstance());
    c.addProblem(NoWater.getInstance());
    
    c.problemSolved(NoCup.getInstance());
    assertSame(c.getState(),NoWater.getInstance());
    
    c.problemSolved(NoWater.getInstance());
    assertSame(c.getState(),StuckCoin.getInstance());
    
    c.problemSolved(StuckCoin.getInstance());
    assertSame(c.getState(),Idle.getInstance());
  }
  @Test
  public void testGiveChange() {
    c.changeState(Idle.getInstance()); 
    c.insertCoin(Coin.COIN50);
    assertEquals(50,c.getAmountInside());
    c.giveChange(25);
    assertEquals(0,c.getAmountInside());
  }
  @Test
  public void testAreDrinksFree() {
    assertFalse(c.areDrinksFree());
  }
  @Test
  public void testPreparingOver() throws InterruptedException {
    int oldStock = c.getStock().getSugarCubesNbr();
    int old = c.getStock().getDrinkQty(c.getDrinks().get(1));
    assertEquals(c.getState(),Idle.getInstance());
    c.coinInserted(Coin.COIN100);
    assertEquals(c.getAmountInside(),100);
    c.drinkButton(c.getDrinks().get(1));
    c.more();
    c.more();
    c.confirm();
    assertEquals(c.getState(),Preparing.getInstance());
    Thread.sleep(3500);
    assertEquals(c.getState(),Idle.getInstance());
    assertEquals(oldStock-2,c.getStock().getSugarCubesNbr());
    assertEquals(old-1,c.getStock().getDrinkQty(c.getDrinks().get(1)));
  }
  @Test
  public void testConfirm() {
    //Idle
    c.setChosenDrink(c.getDrinks().get(4));
    c.confirm();
    assertEquals(Idle.getInstance(),c.getState());
    //Asking
    c.insertCoin(Coin.COIN20);
    c.drinkButton(c.getDrinks().get(4));
    c.confirm();
    assertEquals(Preparing.getInstance(), c.getState());
    
  }
  @Test
  public void testDrinkButton() {
    //Idle
    c.coinInserted(Coin.COIN20);
    c.drinkButton(c.getDrinks().get(2));
    assertEquals(c.getState(), Idle.getInstance());
    c.coinInserted(Coin.COIN50);
    c.drinkButton(c.getDrinks().get(2));
    assertEquals(c.getState(),Asking.getInstance());
    
    //StuckCoin
    c.cancel(); //return to Idle
    c.coinInserted(Coin.COIN20);
    c.coinInserted(Coin.COIN50);
    c.addProblem(StuckCoin.getInstance());
    c.drinkButton(c.getDrinks().get(2));
    

  }
  
}
