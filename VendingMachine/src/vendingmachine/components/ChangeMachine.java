package vendingmachine.components;

import java.util.Map;

public class ChangeMachine {

	private Map<Coin, Integer> coinsStock;
	private Map<Coin, Boolean> acceptedCoins;
	
	private Map<Coin,Integer> coinsStockTemp;
	
	public final static Coin[] COINS = {Coin.COIN200, Coin.COIN100, 
		Coin.COIN50, Coin.COIN20, Coin.COIN10, Coin.COIN5, Coin.COIN2, Coin.COIN1};
	public static final String[] COINS_TEXT = {"2 �", "1 �", 
		"0,5 �", "0,2 �", "0,1 �", "0,05 �", "0,02 �", "0,01 �"};

	public ChangeMachine(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
		this.coinsStock = coinsStock;
		this.acceptedCoins = acceptedCoins;
	}

	public void giveChange() { // � n'utiliser que si isPossibleChange(amount) == true	
		coinsStock = coinsStockTemp;		
	}
	
	public boolean isChangePossible(int amount) {
		coinsStockTemp = coinsStock;
		//long[] moneyToGive = {0,0,0,0,0,0,0,0}; //utilit� ???? on l'utilisera apres pour donner la monnaie rendue						
		for (int i = 0; i < COINS.length; i++) {
			while (amount >= COINS[i].VALUE && coinsStockTemp.get(COINS[i]) > 0) {	
				coinsStockTemp.put(COINS[i], coinsStockTemp.get(COINS[i]) - 1);
				amount -= COINS[i].VALUE;
				//moneyToGive[i] += 1;
			}	//possibilit� de break plus t�t du for (pas indispensable, � discuter)
		}	
	
		return (amount == 0) ? true : false;
	}

	public Map<Coin, Integer> getCoinsStock() {
		return coinsStock;
	}

	public boolean isCoinAccepted(Coin coin) {
		return acceptedCoins.get(coin);
	}

	public void insertCoin(Coin coin) {
		coinsStock.put(coin, coinsStock.get(coin) + 1);
	}
}