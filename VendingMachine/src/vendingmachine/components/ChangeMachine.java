package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

public class ChangeMachine {

	private Map<Coin, Integer> coinsStock;
	private Map<Coin, Boolean> acceptedCoins;
	public static Coin[] COINS;

	public ChangeMachine(Hashtable<Coin, Integer> coinsStock, Hashtable<Coin, Boolean> acceptedCoins) {
		this.coinsStock = coinsStock;
		this.acceptedCoins = acceptedCoins;
	}

	public int[] giveChange() {
		// TODO - implement ChangeMachine.giveChange
		
	}

	public boolean isCoinAccepted(Coin coin) {
		return acceptedCoins.get(coin);
	}

}