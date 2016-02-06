package vendingmachine.states;

import vendingmachine.components.*;

public abstract class State {

	protected byte chosenSugar = 0; //Pourquoi le redéfinir dans Asking ? //AUSSI DANS CONTEXT ???
	protected int amountInside; //LE METTRE DANS CONTEXT ???!!!

	protected Drink drinkChosen;

	public void entry(Context c) {
		c.getObserver().setNorthText(getDefaultText());
		c.getObserver().setSugarText(getSugarText());
		c.getObserver().setInfo();
	}

	public void exit() { //Utile ?
		
	}
	
	public abstract String getDefaultText();
	public void coinInserted(Coin coin, Context c) {
		c.getObserver().setChangeBool(true);
	} //toujours fait le bruit de pièces + afficher le montant rendu; à faire
	
	//These buttons do nothing by default
	public void drinkButton(Drink drink, Context c) {}
	public void less(Context c) {}
	public void more(Context c) {}
	
	public void cancel(Context c) {
		if (amountInside > 0) {
			c.getChangeMachine().giveChange(amountInside); //Toujours faisable ???
			chosenSugar = 0;
			amountInside = 0;
			c.getChangeMachine().assessChangeDone = false;
			c.changeState(Idle.Instance());
		}	
	}
	public void confirm(Context c) {}
	public String getSugarText() {return "";}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName(); //instead of getName() to avoid the package name
	}

	public int getAmountInside() {
		return amountInside;
	}

	public void setAmountInside(int amountInside) {
		this.amountInside = amountInside;
	}
}