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
   * The Drink's that the machine dispenses.
   */
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
  
  /**
   * If there is (true) or not (false) a cup inside.
   */
  private boolean cupInside;
  
  /**
   * The drink that is currently ordered.
   */
  private Drink chosenDrink;
  
  private int chosenSugar; //le mettre dans Asking ?
  
  /**
   * The Coin's currently in the container to be given back.
   */
  private final Map<Coin, Integer> changeOut;

  private IMachineGUI machineGUI;
  
  private final Timer preparingTimer;

  public Context(List<Drink> drinkList, ChangeMachine changeMachine, Stock stock) {
    this.drinkList = drinkList;
    this.changeMachine = changeMachine;
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

  /**
   * Changes the state of the machine with the specified State.
   * Performs the {@code entry()} method of the {@code newState}.
   * 
   * @param newState the State the machine should be in
   */
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

  public int getChosenSugar() {
    return chosenSugar;
  }

  @Override
  public void drinkButton(Drink drink) {
    SoundLoader.play(SoundLoader.CLICK);
    state.drinkButton(drink, this);
  }

  /**
   * @return the amount entered by the client (in cents)
   */
  public int getAmountInside() {
    return amountInside;
  }

  public ChangeMachine getChangeMachine() {
    return changeMachine;
  }

  /**
   * @return a List<Drink> that the machine can dispense
   */
  @Override
  public List<Drink> getDrinks() {
    return drinkList;
  }

  /**
   * @return a String containing all the info about the stock, the machine and the coins
   */
  @Override
  public String getInfo() {
    final StringBuilder sb = new StringBuilder(30);
    sb.append("State: ").append(state).append("\n\n")
    .append(amountInside / 100.0).append(" € inserted.\n");

    sb.append("\n").append(changeMachine.getInfo());

    sb.append("\n").append(stock.getInfo());

    return sb.toString();
  }

  @Override
  public String getNorthText() {
    return state.getDefaultText(this);
  }

  /**
   * @return the State the Context is currently in
   */
  public State getState() {
    return state;
  }

  public Stock getStock() {
    return stock;
  }

  /**
   * Simulates the giving of the change, based on the {@code chosenDrink}.
   * Change to give is {@code chosenDrink.getPrice() - amountInside}.
   */
  public void giveChangeOnDrink() {
    int amountToGive = amountInside - chosenDrink.getPrice();
    addChangeOut(changeMachine.giveChange(amountToGive));
    if (amountToGive > 0) {
      setChangeBool(true);
      SoundLoader.play(SoundLoader.CLING);
    }
    amountInside = 0;
    machineGUI.updateInfo();
  }
  
  /**
   * Simulates the giving of the change on the amount entered by the client.
   */
  public void giveChangeOnCancel() {
    if (amountInside > 0) {
      addChangeOut(changeMachine.giveChange(amountInside));
      setChangeBool(true);
      SoundLoader.play(SoundLoader.CLING);
      amountInside = 0;
      machineGUI.updateInfo();
    }
  }

  public void incrementChosenSugar() {
    chosenSugar += 1;
    machineGUI.updateSugarText();
  }

  /**
   * Simulates the insertion of the specified Coin.
   * 
   * @param coin the Coin to insert
   */
  public void insertCoin(Coin coin) {
    amountInside += coin.VALUE;
    changeMachine.insertCoin(coin);
    machineGUI.setTemporaryNorthText(coin.TEXT + " inserted");
    machineGUI.updateInfo();
    SoundLoader.play(SoundLoader.FOP);
  }

  /**
   * @return true if a cup is waiting to be taken, false otherwise
   */
  public boolean isCupInside() {
    return cupInside;
  }

  /**
   * Called when "-" button is pressed.
   * Calls the {@code less()} method of the current State.
   */
  @Override
  public void less() {
    SoundLoader.play(SoundLoader.CLICK);
    state.less(this);
  }

  /**
   * Called when "+" button is pressed.
   * Calls the {@code more()} method of the current State.
   */
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

  /**
   * @param chosenDrink the new Drink that may be ordered by the client
   */
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

  /**
   * Tells the UI to display {@code msg} temporarily.
   * 
   * @param msg the String to display temporarily
   */
  public void setTemporaryNorthText(String msg) {
    machineGUI.setTemporaryNorthText(msg);
  }

  /**
   * Simulates what happens when the client takes his change.
   */
  @Override
  public void takeChange() {
    setChangeBool(false);
    for (Coin coin: Coin.COINS) {
      changeOut.put(coin, 0);
    }
    machineGUI.updateChangeOutInfo();
    SoundLoader.stop(SoundLoader.CLING); // stops the sound effect is the change is taken.
    log.info("Change taken.");
  }

  /**
   * Simulates what happens when the client takes his cup.
   */
  @Override
  public void takeCup() {
    setCupBool(false);
    SoundLoader.stop(SoundLoader.BEEP); // stops the sound effect is the cup is taken.
    log.info("Cup of " + chosenDrink.getName() + " taken.");
  }

  @Override
  public void setWaterSupply(boolean bool) {
    heatingSystem.setWaterSupply(bool);
  }

  /**
   * @return a String about the coins currently waiting to be taken
   */
  @Override
  public String getChangeOutInfo() {
    final StringBuilder sb = new StringBuilder(40);
    sb.append("<html>");
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

  /**
   * Adds the specified Coin to the container to be taken by the client.
   * 
   * @param coin the Coin that is given back
   */
  public void addChangeOutCoin(Coin coin) {
    changeOut.put(coin, changeOut.get(coin) + 1);
    machineGUI.updateChangeOutInfo();
    log.info(coin.TEXT + " inserted but not allowed.");
  }

  /**
   * Adds a Map<Coin, Integer> of Coin's to the outside container.
   * 
   * @param moneyToGive the Map<Coin, Integer> to add to {@code changeOut}
   */
  public void addChangeOut(Map<Coin, Integer> moneyToGive) {
    for (Coin coin: Coin.COINS) {
      changeOut.put(coin, changeOut.get(coin) + moneyToGive.get(coin));
    }
    machineGUI.updateChangeOutInfo();
  }

  /**
   * @return a String with the current information about sugar
   */
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

  /**
   * @return true if the stocks of the machine can be currently changed, false otherwise
   */
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