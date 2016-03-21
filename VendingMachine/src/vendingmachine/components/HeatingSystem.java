package vendingmachine.components;

import javax.swing.Timer;

import vendingmachine.states.ColdWater;
import vendingmachine.states.NoWater;
import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

  private static final double MIN_TEMPERATURE = 90.0;
  private static final double MAX_TEMPERATURE = 96.0;
  private static final double COLD_LIMIT = 80.0;
  private static final double RUNNING_WATER_TEMPERATURE = 60.0;
  
  //private static final double diffTemperature = 2000/(4.18 * 2500); // Q/(C*m)
  
  private boolean waterSupply;

  private double temperature;

  private boolean heating;
  private TemperatureListener observer;
  private final IContext context;
  
  private final Timer timer;

  public HeatingSystem(IContext context) {
    this.waterSupply = true;
    this.temperature = 90.1;
    this.heating = false;
    this.context = context;

    timer = new Timer(1000, e -> this.updateTemperature());
    timer.start();
  }

  /**
   * @return the current temperature
   */
  public double getTemperature() {
    return this.temperature;
  }

  public void setObserver(TemperatureListener observer) {
    this.observer = observer;
  }

  /**
   * @param temperature the new temperature to set
   */
  public void setTemperature(double temperature) {
    this.temperature = temperature;
    if (waterSupply) {
      updateState();
    }
    observer.setTemperature(this.temperature);
  }

  /**
   * @param bool true to enable the water supply, false to disable it
   */
  public void setWaterSupply(boolean bool) {
    if (!waterSupply && bool) {
      this.waterSupply = bool;
      setTemperature(RUNNING_WATER_TEMPERATURE); // Running water is reintroduced in the system
      context.problemSolved(NoWater.getInstance());
      timer.restart();
    } else if (waterSupply && !bool) {
      this.waterSupply = bool;
      setTemperature(-1);
      context.addProblem(NoWater.getInstance());
      timer.stop();
    }
  }

  /**
   * Updates the state of the HeatingSystem based on the current temperature.
   * Notifies the IContext associated if the temperature becomes too cold.
   */
  private void updateState() {
    if (heating && temperature > MAX_TEMPERATURE) {
      heating = false;
    } else if (!heating && temperature < MIN_TEMPERATURE) {
      heating = true;
    }
    
    if (temperature >= COLD_LIMIT) {
      context.problemSolved(ColdWater.getInstance());
    } else if (context.getState() != ColdWater.getInstance() && temperature < COLD_LIMIT) {
      context.addProblem(ColdWater.getInstance());
    }
  }

  /**
   * Simulates the change of temperature happening in one second.
   * ++
   */
  private void updateTemperature() {
    if (waterSupply) {
      if (heating) {
        //setTemperature(temperature + diffTemperature);
        setTemperature(temperature - 100 * 0.6 * (temperature - 150) / (4.18 * 2500));
        //http://www.engineeringtoolbox.com/convective-heat-transfer-d_430.html
      } else {
        setTemperature(temperature - 100 * 0.6 * (temperature - 20 )/ (4.18 * 2500));
      }
    }
  }
  
  /**
   * @return true if the water supply is enabled, false otherwise
   */
  public boolean isWaterSupplyEnabled() {
    return waterSupply;
  }

  
  /**
   * Simulates the cooling of the water caused by the ordering of a drink.
   * A drink is supposed to be around 40 cl, and the water container could hold 2 l.
   * So 1/5 of the water container is replaced by running water.
   */
  public void drinkOrdered() {
    if (waterSupply) {
      setTemperature((4 * temperature + RUNNING_WATER_TEMPERATURE) / 5);
      updateState();
    }
  }
  
} 