package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.SoundLoader;
import vendingmachine.states.Asking;
import vendingmachine.states.Idle;
import vendingmachine.states.Preparing;

/**
 * This class runs tests on the Context that wait some seconds for the preparation.
 * It inherits the "Before" and "BeforeClass" from ContextTest.
 */
public class ContextTimerTest extends ContextTest {

  /**
   * Makes a full order of a Drink. Takes some seconds to wait for the end of the preparation.
   * Checks that the states are logical and the stock is correctly updated.
   * 
   * @throws InterruptedException call to Thread.sleep(long)
   */
  @Test
  public void testFullDrinkOrder() throws InterruptedException {
    final int oldSugarStock = context.getStock().getSugarCubesNbr();
    final int oldCupsStock = context.getStock().getCupsNbr();
    final int oldSpoonsStock = context.getStock().getSpoonsNbr();
    final int oldDrinkStock = context.getStock().getDrinkQty(context.getDrinks().get(1));

    assertSame("Should be default state", context.getState(), Idle.getInstance());
    context.coinInserted(Coin.COIN20);
    assertEquals(20, context.getAmountInside());
    context.drinkButton(context.getDrinks().get(1)); // Costs 0.40 euro
    assertSame("Not enough money inserted", context.getState(), Idle.getInstance());

    context.coinInserted(Coin.COIN100);
    context.drinkButton(context.getDrinks().get(1));
    assertSame(Asking.getInstance(), context.getState());
    assertSame(context.getDrinks().get(1), context.getChosenDrink());
    context.more();
    context.more();
    assertEquals(2, context.getChosenSugar());

    context.confirm();
    assertEquals(context.getState(), Preparing.getInstance());

    Thread.sleep(SoundLoader.getInstance().FILLING.getMicrosecondLength() / 1000 + 100);
    assertSame(Idle.getInstance(), context.getState());
    assertEquals(0, context.getAmountInside());
    assertEquals(oldSugarStock - 2, context.getStock().getSugarCubesNbr());
    assertEquals(oldCupsStock - 1, context.getStock().getCupsNbr());
    assertEquals(oldSpoonsStock - 1, context.getStock().getSpoonsNbr());
    assertEquals(oldDrinkStock - 1, context.getStock().getDrinkQty(context.getDrinks().get(1)));
  }

  /**
   * Makes an order of a drink. Then checks that another Drink can not be ordered
   * because a cup is inside the machine. Takes the cup (and the change) and orders again.
   * Takes some second to wait for the end of the preparation.
   * 
   * @throws InterruptedException call to Thread.sleep(long)
   */
  @Test
  public void cantOrderWhenCupInMachine() throws InterruptedException {
    context.coinInserted(Coin.COIN100);
    context.drinkButton(context.getDrinks().get(1)); // Costs 0.40 euro
    context.confirm();
    Thread.sleep(SoundLoader.getInstance().FILLING.getMicrosecondLength() / 1000 + 100);

    context.coinInserted(Coin.COIN100);
    context.drinkButton(context.getDrinks().get(1));
    assertSame("Order impossible because a cup is inside", Idle.getInstance(), context.getState());
    context.takeChange();
    context.takeCup(); // Removes the cup
    context.drinkButton(context.getDrinks().get(1));
    assertSame("Order possible because cup has been removed",
        Asking.getInstance(), context.getState());
  }
}
