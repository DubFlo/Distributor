package vendingmachine.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import test.EmptyContext;
import vendingmachine.components.HeatingSystem;
import vendingmachine.components.IContext;
import vendingmachine.states.NoWater;

public class HeatingSystemTest {
  
  private static HeatingSystem heatSys;
  private static IContext c;
  @BeforeClass
  public static void setup() {
    c = new EmptyContext();
    heatSys = new HeatingSystem(c);
  }
  
  @Test
  public void setTemperatureTest() {
      heatSys.setTemperature(70);
      assertEquals(70,heatSys.getTemperature(),1);
      heatSys.setTemperature(100);
      assertEquals(100,heatSys.getTemperature(),0.01); 
  }
  @Test
  public void setWaterSupplyTest() {
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
  @Test
  public void drinkOrderedTest() {
    sleep(1);
  }

}
