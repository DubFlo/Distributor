package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import vendingmachine.components.HeatingSystem;
import vendingmachine.components.IContext;

public class HeatingSystemTest {
  
  private static HeatingSystem heatSys;
  private static IContext c;
  @BeforeClass
  public static void setup() {
    c = new EmptyContext();
    heatSys = new HeatingSystem(c);
    heatSys.setObserver(new EmptyUI());
  }
  
  @Test
  public void testSetTemperature() {
      heatSys.setTemperature(70);
      assertEquals(70,heatSys.getTemperature(),1);
      heatSys.setTemperature(100);
      assertEquals(100,heatSys.getTemperature(),0.01); 
  }
  @Test
  public void testSetWaterSupply() {
    //heatSys.waterSupply = true
    heatSys.setWaterSupply(true);
    assertTrue("Nothing should have changed",heatSys.isWaterSupplyEnabled());
    
    heatSys.setWaterSupply(false);
    assertFalse(heatSys.isWaterSupplyEnabled());
    assertEquals(heatSys.getTemperature(),-1.0,0.01);
    
    //heatSys.waterSupply = false
    
    heatSys.setWaterSupply(false);
    assertTrue("Nothing should have changed",!heatSys.isWaterSupplyEnabled());
    
    heatSys.setWaterSupply(true);
    assertTrue(heatSys.isWaterSupplyEnabled());
  }

  @Ignore
  public void testUpdate () throws InterruptedException {
    heatSys.setTemperature(88.1);
    Thread.sleep(2500);
    assertTrue (88.1 < heatSys.getTemperature());
    heatSys.setTemperature(110);
    Thread.sleep(250);
    assertTrue(110 > heatSys.getTemperature());
    
  }
  @Test
  public void testDrinkOrdered() {
    assertEquals(93,heatSys.getTemperature(),0.2);
    double heatSysBefore = heatSys.getTemperature();
    heatSys.drinkOrdered();
    assertTrue(heatSys.getTemperature() < heatSysBefore);
  }

}
