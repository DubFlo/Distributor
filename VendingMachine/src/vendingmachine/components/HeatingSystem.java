package vendingmachine.components;

public class HeatingSystem {

	private boolean waterSupply = true;
	private double temperature;
	private boolean heating;

	public void setWaterSupply(boolean b) {
		this.waterSupply = b;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public void updateTemperature() {
		// TODO - implement HeatingSystem.updateTemperature
		
	}

}