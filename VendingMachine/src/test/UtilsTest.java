package test;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.Utils;


import static org.junit.Assert.*;

public class UtilsTest {
  
  private Hashtable<Coin,Integer> hashtable;
  
  @Before
  public void init() {
    int[] hashTab = { 1, 3, 0, 0, 2, 1, 2, 0 };  
    hashtable = new Hashtable<Coin,Integer>();
    for (int i = 0; i < 8; i++) {
      hashtable.put(Coin.COINS.get(i), hashTab[i]);
    }
  }
  
  @Test
  public void testTotalValue() {
    assertEquals(529, Utils.totalValue(hashtable));
    Utils.resetCoinsMap(hashtable);
    assertEquals(0, Utils.totalValue(hashtable));
  }
  
  @Test
  public void testCopy() {
    Map<Coin, Integer> tableCopy = Utils.copy(hashtable);
    assertNotSame(hashtable, tableCopy);
    assertEquals (hashtable, tableCopy);
    
    tableCopy.put(Coin.COIN50, 1);
    assertNotEquals("hashtable should not have been changed",
        hashtable.get(Coin.COIN50), tableCopy.get(Coin.COIN50));
  }
  
  @Test(expected=NumberFormatException.class)
  public void testPositiveIntException() {
    Utils.checkPositiveIntFormat(-1);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testCheckName() {
    Utils.checkName("aaaaaaaaaaaaaaaaaaa"); // 19 characters
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testCheckPercentage() {
    Utils.checkPercentage(101);
  }
  

}
