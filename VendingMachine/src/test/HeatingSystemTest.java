package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import vendingmachine.components.HeatingSystem;

public class HeatingSystemTest {

  private static HeatingSystem heatSys;

  @Before
  public void setUp() {
    heatSys = new HeatingSystem(new EmptyContext());
  }

  @Test
  public void testSetTemperature() {
    heatSys.setTemperature(70);
    assertEquals(70, heatSys.getTemperature(), 10e-6);
    heatSys.setTemperature(100);
    assertEquals(100, heatSys.getTemperature(), 10e-6);
  }

  @Test
  public void testSetWaterSupply() {
    heatSys.setWaterSupply(true);
    assertTrue("Nothing should have changed", heatSys.isWaterSupplyEnabled());

    heatSys.setWaterSupply(false);
    assertFalse("Water supply should be enabled", heatSys.isWaterSupplyEnabled());
    assertTrue("Temperature should be negative", heatSys.getTemperature() < 0);
    heatSys.setTemperature(100); // Should do nothing
    assertTrue("Temperature should still be negative", heatSys.getTemperature() < 0);

    heatSys.setWaterSupply(false);
    assertFalse("Nothing should have changed", heatSys.isWaterSupplyEnabled());

    heatSys.setWaterSupply(true);
    assertTrue(heatSys.isWaterSupplyEnabled());
    assertTrue("Temperature should be positive", heatSys.getTemperature() > 0);
  }

  @Test
  public void testUpdate() throws InterruptedException {
    heatSys.setTemperature(88.1);
    Thread.sleep(1500); // More than one second
    assertTrue("Temperature should have increased", 88.1 < heatSys.getTemperature());

    heatSys.setTemperature(110);
    Thread.sleep(1500);
    assertTrue("Temperature should have decreased", 110 > heatSys.getTemperature());
  }

  @Test
  public void testDrinkOrdered() {
    final double initTemperature = heatSys.getTemperature();
    heatSys.drinkOrdered();
    assertTrue(heatSys.getTemperature() < initTemperature);
    heatSys.resetTemperature();
    assertEquals(93, heatSys.getTemperature(), 10e-6);
  }

}
