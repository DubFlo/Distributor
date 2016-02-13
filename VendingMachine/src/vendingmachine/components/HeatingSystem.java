package vendingmachine.components;

import javax.swing.Timer;

import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

  private int timeCooling;
  private int timeWarming;
  private static final double MIN_TEMPERATURE = 90.0;
  private static final double MAX_TEMPERATURE = 96.0;
  private boolean waterSupply;

  private double temperature;

  private boolean heating;
  private TemperatureListener observer;

  public HeatingSystem() {
    this.waterSupply = true;
    this.temperature = 90.1;
    this.heating = false;

    this.timeCooling = 0;
    this.timeWarming = 0;
    int delay = 1000; // milliseconds
    new Timer(delay, e -> this.updateTemperature()).start();
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
  }

  public void setWaterSupply(boolean b) {
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
  }

  public void updateTemperature() {
    if (waterSupply) {
      updateState();
      if (heating) {
        timeWarming += 1;
        temperature += (-10)
            * (Math.exp(-0.0405 * (timeWarming + 1)) - Math.exp(-0.0405 * timeWarming));
        // http://luciole.ca/gilles/mat265/chap3/var-temp.html
      } else {
        timeCooling += 1;
        temperature -= 75 * Math.pow((61 / 75.0), (timeCooling / 400.0))
            * (1 - Math.pow(61 / 75.0, 1 / 400.0));
      }

      if (observer != null) {
        observer.setTemperature(temperature);
      }
    }
  }
}