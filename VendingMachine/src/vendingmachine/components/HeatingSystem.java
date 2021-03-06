package vendingmachine.components;

import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;

import vendingmachine.states.ColdWater;
import vendingmachine.states.NoWater;
import vendingmachine.ui.TemperatureListener;

/**
 * This class creates a water heating system linked to a vending machine.
 * Its water temperature is updated every second. May notify an observer of the
 * changes of temperature and notifies an IContext of the changes of states.
 * 
 * <p>Here is an explanation of the formula used to simulate water heating/cooling
 * (source: <a href="http://www.engineersedge.com/heat_transfer/convection.htm">
 * http://www.engineersedge.com/heat_transfer/convection.htm</a>).<br>
 * - When water is not heating, it is considered in an air at 20 degrees;<br>
 * - When water is heating, it is considered to be in a 150 degrees box;<br>
 * - Water is supposed to have a convective heat transfer coefficient of h = 60;<br>
 * - Water has a mass m = 2 kg;<br>
 * - Area is A = 1 m^2 (2 l of water, so in a container of 0,1 m * 0,1 m * 0,2 m);<br>
 * - Liquid water specific heat capacity is C = 4180 J / (kg * K).<br>
 * So the heat transferred is computed by h * A * dT (Joules); dividing by
 * C * m gives thus the difference in temperature at each second.
 * This may not be exactly realistic but is still satisfactory.
 */
public class HeatingSystem {

  /*
   * Some default temperatures of the system.
   */
  private static final double MIN_TEMPERATURE = 90.0;
  private static final double DEFAULT_TEMPERATURE = 93.0;
  private static final double MAX_TEMPERATURE = 96.0;
  private static final double COLD_LIMIT = 80.0;
  private static final double RUNNING_WATER_TEMPERATURE = 60.0;

  /**
   * True if water supply is enabled, false otherwise.
   */
  private boolean waterSupply;

  /**
   * The current temperature.
   */
  private double temperature;

  /**
   * True if the heater is heating, false if it is not.
   */
  private boolean heating;

  /**
   * An observer to notify each time temperature is changed.
   */
  private Set<TemperatureListener> observers;

  /**
   * The machine to update when state changes.
   */
  private final IContext context;

  /**
   * The timer that updates the temperature every second.
   */
  private final Timer timer;

  /**
   * Creates a HeatingSystem linked to the specified context.
   * Temperature is on 93 degrees, water supply is enabled, system is heating.
   * The temperature is updated each second.
   * 
   * @param context the IContext to associate with the HeatingSystem
   */
  public HeatingSystem(IContext context) {
    this.context = context;
    this.waterSupply = true;
    this.temperature = DEFAULT_TEMPERATURE;
    this.heating = true;
    
    observers = new HashSet<TemperatureListener>();

    timer = new Timer(1000, e -> this.updateTemperature());
    timer.start();
  }

  /**
   * Simulates the change of temperature happening in one second.
   * The explanation of the formula is given in the class comment.
   */
  private void updateTemperature() {
    if (waterSupply) {
      if (heating) {
        setTemperature(temperature + 60 * 1 * (150 - temperature) / (4180 * 2));
      } else {
        setTemperature(temperature + 60 * 1 * (20 - temperature) / (4180 * 2));
      }
    }
  }

  /**
   * @return the current temperature
   */
  public double getTemperature() {
    return this.temperature;
  }

  /**
   * @param observer the TemperatureListener to notify of a change of temperature
   */
  public void addObserver(TemperatureListener observer) {
    observers.add(observer);
  }

  /**
   * @return true if the water supply is enabled, false otherwise
   */
  public boolean isWaterSupplyEnabled() {
    return waterSupply;
  }

  /**
   * @param temperature the new temperature to set
   */
  public void setTemperature(double temperature) {
    if (waterSupply) {
      this.temperature = temperature;
      updateState();
      for (TemperatureListener observer: observers) {
        observer.setTemperature(this.temperature);
      }
    }
  }

  /**
   * Reset the temperature to its default value.
   */
  public void resetTemperature() {
    this.setTemperature(DEFAULT_TEMPERATURE);
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
      setTemperature(-1);
      this.waterSupply = bool;
      context.addProblem(NoWater.getInstance());
      timer.stop();
    }
  }

  /**
   * Simulates the cooling of the water caused by the ordering of a drink.
   * A drink is supposed to be around 40 cl, and the water container could hold 2 l.
   * So 1/5 of the water container is replaced by hot running water.
   */
  public void drinkOrdered() {
    if (waterSupply) {
      setTemperature((4 * temperature + RUNNING_WATER_TEMPERATURE) / 5);
      updateState();
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

}