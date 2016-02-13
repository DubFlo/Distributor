package vendingmachine.components;

import javax.swing.Timer;

import vendingmachine.states.ColdWater;
import vendingmachine.states.Idle;
import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

  private static final double MIN_TEMPERATURE = 90.0;
  private static final double MAX_TEMPERATURE = 96.0;
  private static final double COLD_LIMIT = 80.0;
  private static final double RUNNING_WATER_TEMPERATURE = 60.0;
  
  private boolean waterSupply;

  private double temperature;
  private int timeCooling;
  private int timeWarming;

  private boolean heating;
  private TemperatureListener observer;
  private Context context;
  
  private Timer t;

  public HeatingSystem(Context context) {
    this.waterSupply = true;
    this.temperature = 90.1;
    this.heating = false;
    this.context = context;

    this.timeCooling = 0;
    this.timeWarming = 0;
    int delay = 1000; // milliseconds
    t = new Timer(delay, e -> this.updateTemperature());
    t.start();
  }

  public double getTemperature() {
    return this.temperature;
  }

  public boolean isHeating() {
    return heating;
  }

  public void setObserver(TemperatureListener observer) {
    this.observer = observer;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
    if (observer != null) {
      observer.setTemperature(this.temperature);
    }
  }

  public void setWaterSupply(boolean b) {
    if (!waterSupply && b) {
      temperature = RUNNING_WATER_TEMPERATURE; //Hot running water is reintroduced in the system
      t.restart();
    } else if (waterSupply && !b) {
      setTemperature(-1);
      t.stop();
    }
    this.waterSupply = b;
  }

  private void updateState() {
    if (heating && temperature > MAX_TEMPERATURE) {
      heating = false;
      timeWarming = 0;
    } else if (!heating && temperature < MIN_TEMPERATURE) {
      heating = true;
      timeCooling = 0;
    }
    
    if (context.getState() == ColdWater.instance() && temperature >= COLD_LIMIT) {
      context.changeState(Idle.instance());
    } else if (context.getState() != ColdWater.instance() && temperature < COLD_LIMIT) {
      context.changeState(ColdWater.instance());
    }
  }

  public void updateTemperature() { //formules incorrectes !!!!
    if (waterSupply) {
      updateState();
      if (heating) {
        timeWarming += 1;
        setTemperature(temperature + (-10)
            * (Math.exp(-0.0405 * (timeWarming + 1)) - Math.exp(-0.0405 * timeWarming)));
        // http://luciole.ca/gilles/mat265/chap3/var-temp.html
      } else {
        timeCooling += 1;
        setTemperature(temperature - 75 * Math.pow((61 / 75.0), (timeCooling / 400.0))
            * (1 - Math.pow(61 / 75.0, 1 / 400.0)));
      }
    }
  }

  public boolean isWaterSupplyEnabled() {
    return waterSupply;
  }
  
  public void drinkOrdered() {
    temperature = (4*temperature + RUNNING_WATER_TEMPERATURE)/5;
  }
}