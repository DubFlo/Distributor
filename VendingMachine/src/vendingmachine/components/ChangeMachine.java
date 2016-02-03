package vendingmachine.components;

import java.util.Hashtable;
import java.util.Map;

public class ChangeMachine {

	private Map<Coin, Integer> coinsStock;
	private Map<Coin, Boolean> acceptedCoins;
	public final static Coin[] COINS = {Coin.COIN200, Coin.COIN100, 
		Coin.COIN50, Coin.COIN20, Coin.COIN10, Coin.COIN5, Coin.COIN2, Coin.COIN1};
	public static final String[] COINS_TEXT = {"2 €", "1 €", 
		"0,5 €", "0,2 €", "0,1 €", "0,05 €", "0,02 €", "0,01 €"};

	public ChangeMachine(Hashtable<Coin, Integer> coinsStock, Hashtable<Coin, Boolean> acceptedCoins) {
		this.coinsStock = coinsStock;
		this.acceptedCoins = acceptedCoins;
	}

	public void giveChange() {
		// TODO - implement ChangeMachine.giveChange
		
	}

	public boolean isCoinAccepted(Coin coin) {
		return acceptedCoins.get(coin);
	}

}