package vendingmachine.components;

public class HeatingSystem {

	private boolean waterSupply;
	private double temperature;
	private boolean heating;

	public HeatingSystem() {
		this.waterSupply = true;
		this.temperature = 90;
	}
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