package vendingmachine.components;

import javax.swing.Timer;
import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

	private boolean waterSupply;
	private double temperature;
	private boolean heating;
	private static int timeCooling = 0;
	private static int timeWarming = 0;
	
	private TemperatureListener observer;
	
	private static final double MIN_TEMPERATURE = 90.0;
	private static final double MAX_TEMPERATURE = 96.0;

	public HeatingSystem() {
		this.waterSupply = true;
		this.temperature = 97;
		this.heating = false;
		
		int delay = 1000; //milliseconds
		new Timer(delay, e -> this.updateTemperature()).start();
	}
	
	public void setWaterSupply(boolean b) {
		this.waterSupply = b;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public void updateTemperature() {
		if (waterSupply) {
			updateState();
			if (heating) {
				timeWarming += 1;
				temperature += -90 * Math.pow((-61/90.0),(timeCooling/400.0)) * (1- Math.pow(-61/90.0, 1/400.0));
			}
			else {
				timeCooling += 1;
				temperature -= 75 * Math.pow((61/75.0),(timeCooling/400.0)) * (1- Math.pow(61/75.0, 1/400.0)); 
			}
			
			if (observer != null) {
				observer.setTemperature(temperature);
			}
		}
	}

	private void updateState() {
		if (heating && temperature > MAX_TEMPERATURE) {
			heating = false;
			timeWarming = 0;
		}
		
		else if (!heating && temperature < MIN_TEMPERATURE) {
			heating = true;
			timeCooling = 0;
		}
	}

	public void setObserver(TemperatureListener observer) {
		this.observer = observer;
	}

	public boolean isHeating() {
		return heating;
	}
}