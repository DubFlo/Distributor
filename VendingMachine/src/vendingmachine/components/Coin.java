package vendingmachine.components;

public enum Coin {
	COIN200	(200),
	COIN100	(100),
	COIN50	(50),
	COIN20	(20),
	COIN10	(10),
	COIN5	(5),
	COIN2	(2),
	COIN1	(1);
	
	private Coin(int value) {
		this.VALUE = value;
	}
	
	public final int VALUE;
}