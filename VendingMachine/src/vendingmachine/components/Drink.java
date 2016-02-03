package vendingmachine.components;

public class Drink {

	private String name;
	private int price;
	private boolean sugar;

	/**
	 * 
	 * @param name
	 * @param sugar
	 * @param quantity
	 */
	public Drink(String name, boolean sugar, int price) {
		this.name = name;
		this.sugar = sugar;
		this.price = price;
		
	}

	public String getName() {
		return this.name;
	}

	public int getPrice() {
		return this.price;
	}

	public boolean isSugar() {
		return this.sugar;
	}

}