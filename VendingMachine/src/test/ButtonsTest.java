package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.PictureLoader;
import vendingmachine.ui.CoinJButton;
import vendingmachine.ui.DrinkJButton;

public class ButtonsTest {

  @BeforeClass
  public static void setUpClass() {
    PictureLoader.getInstance();
  }

  @Test
  public void testCoinJButton() {
    CoinJButton coinButton = new CoinJButton(Coin.COIN100, false);
    assertSame(PictureLoader.getInstance().REFUSED_COINS_ICONS.get(Coin.COIN100), coinButton.getIcon());
    assertSame(Coin.COIN100, coinButton.getCoin());
  }

  @Test
  public void testDrinkJButton() {
    DrinkJButton drinkButton = new DrinkJButton(new Drink("Coffee", true, 100));
    assertEquals("Coffee", drinkButton.getDrink().getName());
  }
}
