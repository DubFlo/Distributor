package vendingmachine.components;

import java.util.Hashtable;

public class ChangeMachine {

	private Hashtable<Coin, Integer> coinsStock;
	private Hashtable<Coin, Boolean> acceptedCoins;
	public static Coin[] COINS;

	/**
	 * 
	 * @param coinsStock
	 * @param acceptedCoins
	 */
	public ChangeMachine(int[] coinsStock, boolean[] acceptedCoins) {
		// TODO - implement ChangeMachine.ChangeMachine
		
	}

	public int[] giveChange() {
		// TODO - implement ChangeMachine.giveChange
		
	}

	public boolean isCoinAccepted(Coin coin) {
		return acceptedCoins.get(coin);
	}

}