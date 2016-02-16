package vendingmachine.components;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.SoundLoader;
import vendingmachine.states.State;
import vendingmachine.ui.ContextListener;
import vendingmachine.ui.TemperatureListener;

public class Context implements EventListener {

  private static final Logger log = LogManager.getLogger("Context");
  
  private ChangeMachine changeMachine;
  private Stock stock;
  public final int NBR_DRINKS;
  private List<Drink> drinkList; // Plutôt Collection pour pas d'ordre ????
  private HeatingSystem heatingSystem;
  
  private State state;

  private int amountInside;
  private boolean cupInside;
  private Drink chosenDrink;
  private byte chosenSugar;
  private Map<Coin, Integer> changeOut;

  private ContextListener observer;

  public Context(int nbrDrinks, List<Drink> drinkList, Map<Coin, Integer> coinsStock,
      Map<Coin, Boolean> coinsAccepted, Stock stock) {
    this.NBR_DRINKS = nbrDrinks;
    this.drinkList = drinkList;
    this.changeMachine = new ChangeMachine(coinsStock, coinsAccepted, this);
    this.stock = stock;
    
    this.heatingSystem = new HeatingSystem(this);
    this.amountInside = 0;
    this.chosenSugar = 0;
    this.cupInside = false;
    this.changeOut = new Hashtable<Coin, Integer>();
    for (Coin coin: ChangeMachine.COINS) {
      this.changeOut.put(coin, 0);
    }

    log.info("New Vending Machine Created");
  }

  @Override
  public void cancel() {
    SoundLoader.play(SoundLoader.click);
    state.cancel(this);
  }

  @Override
  public void changeState(State newState) {
    state = newState;
    state.entry(this);
    observer.updateInfo();
    observer.setNorthText(state.getDefaultText(this));
    observer.setSugarText(state.getSugarText(this));
    log.info("State " + state + " entered.");
  }

  @Override
  public void coinInserted(Coin coin) {
    state.coinInserted(coin, this);
  }

  @Override
  public void confirm() {
    SoundLoader.play(SoundLoader.click);
    state.confirm(this);
  }

  public void decrementChosenSugar() {
    chosenSugar -= 1;
    observer.setSugarText(state.getSugarText(this));
  }

  @Override
  public void drinkButton(Drink drink) {
    SoundLoader.play(SoundLoader.click);
    state.drinkButton(drink, this);
  }

  public int getAmountInside() {
    return amountInside;
  }

  public ChangeMachine getChangeMachine() {
    return changeMachine;
  }

  public Drink getChosenDrink() {
    return chosenDrink;
  }

  public byte getChosenSugar() {
    return chosenSugar;
  }

  @Override
  public List<Drink> getDrinks() {
    return drinkList;
  }

  public HeatingSystem getHeatingSystem() {
    return heatingSystem;
  }

  @Override
  public String getInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("State: ").append(getState()).append("\n");
    sb.append("\n").append(amountInside / 100.0).append(" € inserted.\n");
    sb.append("\nDrink(s): \n");
    Map<Drink, Integer> s = stock.getDrinkQty();
    for (int i = 0; i < NBR_DRINKS; i++) {
      sb.append(drinkList.get(i).getName()).append(": ").append(s.get(drinkList.get(i)))
      .append(" available.\n");
    }

    sb.append("\nCoins:\n");
    Map<Coin, Integer> cs = changeMachine.getCoinsStock();
    for (int i = 0; i < ChangeMachine.COINS.length; i++) {
      sb.append(ChangeMachine.COINS_TEXT[i]).append(": ").append(cs.get(ChangeMachine.COINS[i]))
      .append(" available.\n");
    }

    sb.append("\n" + stock.getCupsNbr()).append(" cup(s) available.\n")
    .append(stock.getSugarCubesNbr()).append(" sugar cube(s) available.\n")
    .append(stock.getSpoonsNbr()).append(" spoon(s) available.\n");

    return sb.toString();
  }

  @Override
  public String getNorthText() {
    return state.getDefaultText(this);
  }

  @Override
  public State getState() {
    return state;
  }

  public Stock getStock() {
    return stock;
  }

  public double getTemperature() {
    return heatingSystem.getTemperature();
  }

  public void giveChange() {
    changeMachine.giveChange();
    if (amountInside > chosenDrink.getPrice()) {
      setChangeBool(true);
      SoundLoader.play(SoundLoader.cling);
    }
    amountInside = 0;
    observer.updateInfo();
  }
  
  public void giveChangeOnCancel() {
    changeMachine.giveChange();
    setChangeBool(true);
    SoundLoader.play(SoundLoader.cling);
    amountInside = 0;
    observer.updateInfo();
  }

  public void incrementChosenSugar() {
    chosenSugar += 1;
    observer.setSugarText(state.getSugarText(this));
  }

  public void insertCoin(Coin coin) {
    amountInside += coin.VALUE;
    changeMachine.insertCoin(coin);
    observer.setTemporaryNorthText(Double.toString(coin.VALUE / 100.0) + " € inserted");
    observer.updateInfo();
    SoundLoader.play(SoundLoader.fop);
    log.info(coin.VALUE / 100.0 + " € inserted.");
  }

  public boolean isCupInside() {
    return cupInside;
  }

  @Override
  public void less() {
    SoundLoader.play(SoundLoader.click);
    state.less(this);
  }

  @Override
  public void more() {
    SoundLoader.play(SoundLoader.click);
    state.more(this);
  }

  public void resetChosenSugar() {
    chosenSugar = 0;
  }

  public void setAmountInside(int amountInside) {
    this.amountInside = amountInside;
  }

  @Override
  public void setChangeBool(boolean b) {
    observer.setChangeBool(b);
    if (b) {
      SoundLoader.play(SoundLoader.cling);
    }
  }

  public void setChosenDrink(Drink chosenDrink) {
    this.chosenDrink = chosenDrink;
  }

  @Override
  public void setCupBool(boolean b) {
    observer.setCupBool(b);
    cupInside = b;
  }

  @Override
  public <T extends ContextListener & TemperatureListener> void setObserver(T o) {
    this.observer = o;
    heatingSystem.setObserver(o);
  }

  public void setTemporaryNorthText(String msg) {
    observer.setTemporaryNorthText(msg);
  }

  @Override
  public void takeChange() {
    setChangeBool(false);
    for (Coin coin: ChangeMachine.COINS) {
      changeOut.put(coin, 0);
    }
    observer.updateChangeOutInfo();
    SoundLoader.cling.stop(); // stop the sound effect is the change is taken.
    log.info("Change taken.");
  }

  @Override
  public void takeCup() {
    setCupBool(false);
    SoundLoader.beep.stop(); // stop the sound effect is the cup is taken.
    log.info("Cup of " + chosenDrink.getName() + " taken.");
  }

  @Override
  public int getNbrDrinks() {
    return NBR_DRINKS;
  }

  @Override
  public void setWaterSupply(boolean b) {
    heatingSystem.setWaterSupply(b);
  }

  @Override
  public String getChangeOutInfo() {
    StringBuilder sb = new StringBuilder("<html>");
    int nbrCoins;
    int total = 0;
    for (int i = 0; i < ChangeMachine.COINS.length; i++) {//Moyen; hashtable pour les textes aussi ?
      nbrCoins = changeOut.get(ChangeMachine.COINS[i]);
      sb.append(ChangeMachine.COINS_TEXT[i]).append(": ");
      sb.append(nbrCoins).append(" coin(s).<br>");
      total += nbrCoins * ChangeMachine.COINS[i].VALUE;
    }
    sb.append("Total: ").append(total/100.0).append(" €.</html>");
    return sb.toString();
  }

  public void addChangeOutCoin(Coin coin) {
    changeOut.put(coin, changeOut.get(coin) + 1);
    observer.updateChangeOutInfo();
    log.info(coin.VALUE / 100.0 + " € inserted but not allowed.");
  }

  public void addChangeOut(Map<Coin, Integer> moneyToGive) {
    for (Coin coin: ChangeMachine.COINS) {
      changeOut.put(coin, changeOut.get(coin) + moneyToGive.get(coin));
    }
    observer.updateChangeOutInfo();
  }

}