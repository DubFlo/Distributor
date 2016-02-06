package vendingmachine.components;

import java.util.Map;

public class ChangeMachine {

	private Map<Coin, Integer> coinsStock;
	private Map<Coin, Boolean> acceptedCoins;
	public boolean assessChangeDone;// ne pas oublier de le remettre faux apres giveChange
	public Map<Coin,Integer> coinsStockTemp;// private et rajouter getter?
	public final static Coin[] COINS = {Coin.COIN200, Coin.COIN100, 
		Coin.COIN50, Coin.COIN20, Coin.COIN10, Coin.COIN5, Coin.COIN2, Coin.COIN1};
	public static final String[] COINS_TEXT = {"2 €", "1 €", 
		"0,5 €", "0,2 €", "0,1 €", "0,05 €", "0,02 €", "0,01 €"};

	public ChangeMachine(Map<Coin, Integer> coinsStock, Map<Coin, Boolean> acceptedCoins) {
		this.coinsStock = coinsStock;
		this.acceptedCoins = acceptedCoins;
	}

	public void giveChange(int amount) { // à n'utiliser que si isPossibleChange = true	
		coinsStockTemp = assessChange(amount);
		coinsStock = coinsStockTemp;
		assessChangeDone = false;
		
	}
	public Map<Coin,Integer> assessChange(int amount) {	
		if (!assessChangeDone){
			Map<Coin,Integer> coinsStockTemp = coinsStock;
		
		long[] moneyToGive = {0,0,0,0,0,0,0,0}; //utilité ???? on l'utilisera apres pour donner la monnaie rendue
												//Alors Hashmap non ?
			for(int i = 0; i < COINS.length; i++) {
				while(amount >= COINS[i].VALUE && coinsStockTemp.get(COINS[i]) > 0) {	
					coinsStockTemp.put(COINS[i], coinsStockTemp.get(COINS[i]) -1);
					amount -= COINS[i].VALUE;
					moneyToGive[i] += 1;
				}	
			}	
		
			if (amount!= 0) {
				coinsStockTemp.put(COINS[0],-1);
			}
			assessChangeDone = true;
		}
		return coinsStockTemp;
	}
	public boolean isPossibleChange (int amount){
		coinsStockTemp = assessChange(amount);
		return coinsStockTemp.get(COINS[0]) != -1 ;
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