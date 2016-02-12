package vendingmachine.components;

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

  private ContextListener observer;

  public Context(int nbrDrinks, List<Drink> drinkList, ChangeMachine changeMachine, Stock stock) {
    this.NBR_DRINKS = nbrDrinks;
    this.drinkList = drinkList;
    this.changeMachine = changeMachine;
    this.stock = stock;
    this.heatingSystem = new HeatingSystem();

    this.amountInside = 0;
    this.chosenSugar = 0;
    this.cupInside = false;

    log.info("New Vending Machine Created");
  }

  @Override
  public void cancel() {
    state.cancel(this);
  }

  @Override
  public void changeState(State newState) {
    if (state != null) {
      state.exit(this);
    }
    state = newState;
    state.entry(this);
  }

  @Override
  public void coinInserted(Coin coin) {
    state.coinInserted(coin, this);
  }

  @Override
  public void confirm() {
    state.confirm(this);
  }

  public void decrementChosenSugar() {
    chosenSugar -= 1;
  }

  @Override
  public void drinkButton(Drink drink) {
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

  public ContextListener getObserver() {
    return observer;
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
    if (amountInside > 0) {
      setChangeBool(true);
    }
    SoundLoader.play(SoundLoader.cling);
    amountInside = 0;
  }

  public void incrementChosenSugar() {
    chosenSugar += 1;
  }

  public void insertCoin(Coin coin) {
    changeMachine.insertCoin(coin);
    SoundLoader.play(SoundLoader.fop);
  }

  public boolean isCupInside() {
    return cupInside;
  }

  @Override
  public void less() {
    state.less(this);
  }

  @Override
  public void more() {
    state.more(this);
  }

  public void playAlarmSound() {
    SoundLoader.play(SoundLoader.beep);
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

  public void setInfo() {
    observer.setInfo(getInfo());
  }

  public void setNorthText(String msg) {
    observer.setNorthText(msg);
  }

  @Override
  public <T extends ContextListener & TemperatureListener> void setObserver(T o) {
    this.observer = o;
    heatingSystem.setObserver(o);
  }

  public void setSugarText(String msg) {
    observer.setSugarText(msg);
  }

  public void setTemporaryNorthText(String msg) {
    observer.setTemporaryNorthText(msg);
  }

  @Override
  public void takeChange() {
    setChangeBool(false);
    SoundLoader.cling.stop();
    log.info("Change taken.");
  }

  @Override
  public void takeCup() {
    setCupBool(false);
    SoundLoader.beep.stop(); // stop the sound effect is the cup is taken.
    log.info("Cup taken.");
  }

  @Override
  public int getNbrDrinks() {
    return NBR_DRINKS;
  }

}