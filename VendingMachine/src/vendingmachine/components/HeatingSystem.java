package vendingmachine.components;

import javax.swing.Timer;
import vendingmachine.ui.TemperatureListener;

public class HeatingSystem {

	private boolean waterSupply;
	private double temperature;
	private boolean heating;
	
	private TemperatureListener observer;
	
	private static final double MIN_TEMPERATURE = 85.0;
	private static final double MAX_TEMPERATURE = 99.0;

	public HeatingSystem() {
		this.waterSupply = true;
		this.temperature = 90;
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

	public void updateTemperature() {
		updateState();
		if (heating) {
			temperature += 3;
		}
		
		else {
			temperature -= 1;
		}
		observer.setTemperature(temperature);
	}
	
	private void updateState() {
		if (heating && temperature > MAX_TEMPERATURE) {
			heating = false;
		}
		
		else if (!heating && temperature < MIN_TEMPERATURE) {
			heating = true;
		}
	}

	public void setObserver(TemperatureListener observer) {
		this.observer = observer;
	}
}