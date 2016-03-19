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
    heatSys.setTemperature(96.1);
    Thread.sleep(2500);
    assertEquals (95,heatSys.getTemperature(),0.3);
    heatSys.setTemperature(110);
    Thread.sleep(250);
    assertEquals(110, heatSys.getTemperature(),0.02);
    
  }
  @Test
  public void testDrinkOrdered() {
    assertEquals(90.1,heatSys.getTemperature(),0.1);
    heatSys.drinkOrdered();
    assertEquals(84.45, heatSys.getTemperature(), 1);
  }

}
