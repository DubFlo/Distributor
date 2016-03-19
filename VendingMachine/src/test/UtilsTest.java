package test;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.Coin;
import vendingmachine.Utils;


import static org.junit.Assert.*;


public class UtilsTest {
  private static Hashtable<Coin,Integer> hash;
  @Before
  public void init() {
    int[] hashTab = {1,3,0,0,2,1,2,0};  
    hash = new Hashtable<Coin,Integer>();
    for (int i = 0; i < 8; i++) {
      hash.put(Coin.COINS.get(i), hashTab[i]);
    }
  }
  @Test
  public void totalValueTest() {
    assertEquals(529,Utils.totalValue(hash));
  }
  @Test
  public void copyTest() {
    assertNotSame(hash, Utils.copy(hash));
    assertEquals (hash, Utils.copy(hash));

  }

}
