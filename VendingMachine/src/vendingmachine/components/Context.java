package vendingmachine.components;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.SoundLoader;
import vendingmachine.states.Idle;
import vendingmachine.states.State;
import vendingmachine.ui.IMachineGUI;
import vendingmachine.ui.TemperatureListener;

public class Context implements IMachine {

  private static final Logger log = LogManager.getLogger("Context");
  
  /**
   * The number of drinks the machine must deal with.
   */
  private final int NBR_DRINKS;
  private final List<Drink> drinkList;
  
  /*
   * The different parts of the machine.
   */
  private final ChangeMachine changeMachine;
  private final Stock stock;
  private final HeatingSystem heatingSystem;

  /**
   * The state the machine is currently in (State design pattern).
   */
  private State state;

  /**
   * The amount of money inside of the machine (in cents).
   */
  private int amountInside;
  private boolean cupInside;
  private Drink chosenDrink;
  private int chosenSugar;
  private Map<Coin, Integer> changeOut;

  private IMachineGUI machineGUI;
  
  private final Timer preparingTimer;

  public Context(int nbrDrinks, List<Drink> drinkList, Map<Coin, Integer> coinsStock,
      Map<Coin, Boolean> coinsAccepted, Stock stock) {
    this.NBR_DRINKS = nbrDrinks;
    this.drinkList = drinkList;
    this.changeMachine = new ChangeMachine(coinsStock, coinsAccepted, this);
    this.stock = stock;
    
    this.state = Idle.getInstance();
    this.heatingSystem = new HeatingSystem(this);
    this.amountInside = 0;
    this.chosenSugar = 0;
    this.cupInside = false;
    this.changeOut = new Hashtable<Coin, Integer>();
    for (Coin coin: Coin.COINS) {
      this.changeOut.put(coin, 0);
    }

    int delay;
    if (SoundLoader.FILLING != null) {
      delay = (int) SoundLoader.FILLING.getMicrosecondLength() / 1000;
    } else {
      delay = 3000; // milliseconds
    }
    preparingTimer = new Timer(delay, e -> this.preparingOver());
    preparingTimer.setRepeats(false);
    
    log.info("New Vending Machine Created");
  }

  private void preparingOver() {
    setCupBool(true);
    stock.removeCup();
    if (chosenDrink.isSugar() && stock.isSpoonInStock()) {
      stock.removeSpoon();
    }
    stock.removeDrink(chosenDrink);
    SoundLoader.play(SoundLoader.BEEP);
    machineGUI.setTemporaryNorthText("Your " + chosenDrink + " is ready !");
    heatingSystem.drinkOrdered();
    if (heatingSystem.isWaterSupplyEnabled()) {
      changeState(Idle.getInstance());
    }
  }

  @Override
  public void cancel() {
    SoundLoader.play(SoundLoader.CLICK);
    state.cancel(this);
  }

  public void changeState(State newState) {
    state = newState;
    state.entry(this);
    machineGUI.updateUI();
  }

  @Override
  public void coinInserted(Coin coin) {
    state.coinInserted(coin, this);
  }

  @Override
  public void confirm() {
    SoundLoader.play(SoundLoader.CLICK);
    state.confirm(this);
  }

  public void decrementChosenSugar() {
    chosenSugar -= 1;
    machineGUI.updateSugarText();
  }

  @Override
  public void drinkButton(Drink drink) {
    SoundLoader.play(SoundLoader.CLICK);
    state.drinkButton(drink, this);
  }

  public int getAmountInside() {
    return amountInside;
  }

  public ChangeMachine getChangeMachine() {
    return changeMachine;
  }

  public int getChosenSugar() {
    return chosenSugar;
  }

  @Override
  public List<Drink> getDrinks() {
    return drinkList;
  }

  @Override
  public String getInfo() {
    final StringBuilder sb = new StringBuilder();
    sb.append("State: ").append(state).append("\n")
    .append("\n").append(amountInside / 100.0).append(" € inserted.\n");

    sb.append("\n").append(changeMachine.getInfo());

    sb.append("\n").append(stock.getInfo());

    return sb.toString();
  }

  @Override
  public String getNorthText() {
    return state.getDefaultText(this);
  }

  public State getState() {
    return state;
  }

  public Stock getStock() {
    return stock;
  }

  public void giveChange() {
    changeMachine.giveChange();
    if (amountInside > chosenDrink.getPrice()) {
      setChangeBool(true);
      SoundLoader.play(SoundLoader.CLING);
    }
    amountInside = 0;
    machineGUI.updateInfo();
  }
  
  public void giveChangeOnCancel() {
    changeMachine.giveChange();
    setChangeBool(true);
    SoundLoader.play(SoundLoader.CLING);
    amountInside = 0;
    machineGUI.updateInfo();
  }

  public void incrementChosenSugar() {
    chosenSugar += 1;
    machineGUI.updateSugarText();
  }

  public void insertCoin(Coin coin) {
    amountInside += coin.VALUE;
    changeMachine.insertCoin(coin);
    machineGUI.setTemporaryNorthText(coin.TEXT + " inserted");
    machineGUI.updateInfo();
    SoundLoader.play(SoundLoader.FOP);
    log.info(coin.TEXT + " inserted.");
  }

  public boolean isCupInside() {
    return cupInside;
  }

  @Override
  public void less() {
    SoundLoader.play(SoundLoader.CLICK);
    state.less(this);
  }

  @Override
  public void more() {
    SoundLoader.play(SoundLoader.CLICK);
    state.more(this);
  }

  public void resetChosenSugar() {
    chosenSugar = 0;
  }

  @Override
  public void setChangeBool(boolean bool) {
    machineGUI.setChangeBool(bool);
    if (bool) {
      SoundLoader.play(SoundLoader.CLING);
    }
  }

  public void setChosenDrink(Drink chosenDrink) {
    this.chosenDrink = chosenDrink;
  }

  @Override
  public void setCupBool(boolean bool) {
    machineGUI.setCupBool(bool);
    cupInside = bool;
  }

  @Override
  public <T extends IMachineGUI & TemperatureListener> void setObserver(T observer) {
    this.machineGUI = observer;
    heatingSystem.setObserver(observer);
  }

  public void setTemporaryNorthText(String msg) {
    machineGUI.setTemporaryNorthText(msg);
  }

  @Override
  public void takeChange() {
    setChangeBool(false);
    for (Coin coin: Coin.COINS) {
      changeOut.put(coin, 0);
    }
    machineGUI.updateChangeOutInfo();
    if (SoundLoader.CLING != null) {
      SoundLoader.CLING.stop(); // stops the sound effect is the change is taken.
    }
    log.info("Change taken.");
  }

  @Override
  public void takeCup() {
    setCupBool(false);
    if (SoundLoader.BEEP != null) {
      SoundLoader.BEEP.stop(); // stops the sound effect is the cup is taken.
    }
    log.info("Cup of " + chosenDrink.getName() + " taken.");
  }

  @Override
  public int getNbrDrinks() {
    return NBR_DRINKS;
  }

  @Override
  public void setWaterSupply(boolean bool) {
    heatingSystem.setWaterSupply(bool);
  }

  @Override
  public String getChangeOutInfo() {
    final StringBuilder sb = new StringBuilder("<html>");
    int nbrCoins;
    int total = 0;
    for (Coin coin: Coin.COINS) {
      nbrCoins = changeOut.get(coin);
      sb.append(coin.TEXT).append(": ");
      sb.append(nbrCoins).append(" coin(s).<br>");
      total += nbrCoins * coin.VALUE;
    }
    sb.append("Total: ").append(total/100.0).append(" €.</html>");
    return sb.toString();
  }

  public void addChangeOutCoin(Coin coin) {
    changeOut.put(coin, changeOut.get(coin) + 1);
    machineGUI.updateChangeOutInfo();
    log.info(coin.TEXT + " inserted but not allowed.");
  }

  public void addChangeOut(Map<Coin, Integer> moneyToGive) {
    for (Coin coin: Coin.COINS) {
      changeOut.put(coin, changeOut.get(coin) + moneyToGive.get(coin));
    }
    machineGUI.updateChangeOutInfo();
  }

  @Override
  public String getSugarText() {
    return state.getSugarText(this);
  }

  @Override
  public void setCoinStock(Coin coin, int value) {
    changeMachine.setCoinStock(coin, value);
    machineGUI.updateInfo();
  }

  @Override
  public void setDrinkStock(Drink drink, int value) {
    stock.setDrinkQty(drink, value);
    machineGUI.updateInfo();
  }

  @Override
  public boolean isAvailableForMaintenance() {
    return state.isAvailableForMaintenance();
  }

  @Override
  public void setTemperature(int i) {
    heatingSystem.setTemperature(i);
  }

  public void restartPreparingTimer() {
    preparingTimer.restart();
  }

}