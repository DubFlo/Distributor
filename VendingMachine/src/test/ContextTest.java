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
import vendingmachine.components.IContext;
import vendingmachine.components.Stock;
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
  private static Context c;
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
    cm = new ChangeMachine(coinsStock,acceptedCoins);
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
    //init new context
    c = new Context(cm,stock,coinStuck);
    c.setUI(new VendingMachineGUI(c));  
  }
  @Test
  public void testchangeState() {
    c.changeState(Preparing.getInstance());
    assertEquals(c.getState(), Preparing.getInstance());
  }
  
  
  @Test
  public void testCoinInserted() {
    //IDLE
    assertEquals(c.getAmountInside(),0);
    c.coinInserted(Coin.COIN100);
    assertEquals(c.getAmountInside(),100);
    assertEquals(c.getChangeMachine().getCoinsStock(Coin.COIN100),2);
    //Preparing
    c.changeState(Preparing.getInstance());
    c.coinInserted(Coin.COIN200);
    assertEquals("Nothing should have changed",1,c.getChangeMachine().getCoinsStock(Coin.COIN200));
    //StuckCoin
    c.changeState(StuckCoin.getInstance());
    c.coinInserted(Coin.COIN5);
    assertEquals("Nothing should have changed",0,c.getChangeMachine().getCoinsStock(Coin.COIN5));
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
    c.insertCoin(Coin.COIN50);
    c.giveChange(-15);
    assertEquals(50,c.getAmountInside());
    c.giveChange(25);
    assertEquals(0,c.getAmountInside());
  }
 
}
