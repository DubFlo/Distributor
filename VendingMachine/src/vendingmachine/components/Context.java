package vendingmachine.components;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.SoundLoader;
import vendingmachine.Utils;
import vendingmachine.states.Idle;
import vendingmachine.states.NoCup;
import vendingmachine.states.Problem;
import vendingmachine.states.State;
import vendingmachine.states.StuckCoin;
import vendingmachine.ui.IMachineGUI;
import vendingmachine.ui.TemperatureListener;

/**
 * This class defines a vending machine selling hot drinks.
 * It consists of a Stock, a ChangeMachine and a HeatingSystem object.
 */
public class Context implements IMachine, IContext {

  private static final Logger log = LogManager.getLogger("Context");

  /**
   * The probability for a coin to get stuck (between 0 and 1).
   */
  public final double COIN_STUCK_PROB;
  
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
   * The amount of sugar chosen by the client.
   */
  private int chosenSugar;

  /**
   * If there is (true) or not (false) a cup inside.
   */
  private boolean cupInside;

  /**
   * The drink that is currently ordered.
   */
  private Drink chosenDrink;

  /**
   * The Coin's currently in the container to be given back.
   */
  private final Map<Coin, Integer> changeOut;

  /**
   * The UI associated with the machine.
   */
  private IMachineGUI machineGUI;

  /**
   * The timer that triggers the end of the preparation after some seconds.
   */
  private final Timer preparingTimer;

  /**
   * A Set of the Problem's the machine is currently facing.
   */
  private final Set<Problem> currentProblems;
  
  /**
   * A Map of the Coin's that are stuck inside the machine.
   */
  private final Map<Coin, Integer> stuckCoins;

  /**
   * Creates a vending machine with the specified attributes.
   * Also creates a HeatingSystem linked to the machine.
   * Initializes all the fields and logs that a new machine has been built.
   * 
   * @param changeMachine the ChangeMachine associated with the Context
   * @param stock the Stock associated with the Context
   * @param coinStuckProb the probability (between 0 and 1) of a coin getting stuck
   */
  public Context(ChangeMachine changeMachine, Stock stock, double coinStuckProb) {
    this.state = Idle.getInstance();

    this.changeMachine = changeMachine;
    this.stock = stock;
    this.COIN_STUCK_PROB = coinStuckProb;

    this.heatingSystem = new HeatingSystem(this);
    this.amountInside = 0;
    this.chosenSugar = 0;
    this.cupInside = false;
    this.changeOut = new Hashtable<Coin, Integer>();
    Utils.resetCoinsMap(changeOut);

    currentProblems = new HashSet<Problem>();
    stuckCoins = new Hashtable<Coin, Integer>();
    Utils.resetCoinsMap(stuckCoins);
    if (!stock.isCupInStock()) {
      this.state = NoCup.getInstance();
      currentProblems.add(NoCup.getInstance());
    }

    preparingTimer = new Timer(
        (int) (SoundLoader.getInstance().FILLING.getMicrosecondLength() / 1000),
        e -> this.preparingOver());
    preparingTimer.setRepeats(false); // makes its action only once

    log.info("New Vending Machine Built");
  }

  /**
   * Called at the end of the preparation of a drink.
   * Updates all the stock values and logs all the information about the order.
   * If no problems were created during the preparation, changes state to Idle.
   */
  private void preparingOver() {
    giveChange(amountInside - chosenDrink.getPrice());
    final StringBuilder logMsg = new StringBuilder(100);
    logMsg.append("New order:\n\t").append(chosenDrink.getName());
    stock.removeDrink(chosenDrink);
    logMsg.append(" (").append(stock.getDrinkQty(chosenDrink)).append(" remaining);\n");

    if (chosenDrink.isSugar()) {
      stock.removeSugarCubes(chosenSugar);
      logMsg.append("\tWith ").append(chosenSugar).append(" sugar cube(s) (")
      .append(stock.getSugarCubesNbr()).append(" remaining);\n");
    }

    boolean spoon = false;
    if (chosenDrink.isSugar() && stock.isSpoonInStock()) {
      stock.removeSpoon();
      spoon = true;
      logMsg.append("\tWith a spoon (").append(stock.getSpoonsNbr()).append(" remaining);\n");
    }

    stock.removeCup(this);
    logMsg.append('\t').append(stock.getCupsNbr()).append(" cup(s) remaining.");
    setCupBool(true, spoon);

    log.info(logMsg.toString());
    machineGUI.setCupText(chosenDrink.getName() + " (" + chosenSugar + " sugar cube(s))");
    machineGUI.setTemporaryNorthText("Your " + chosenDrink.getName() + " is ready!");

    heatingSystem.drinkOrdered();
    chosenSugar = 0;
    if (currentProblems.isEmpty()) {
      changeState(Idle.getInstance());
    }
    machineGUI.updateUI();
  }

  /**
   * Restarts the Timer that triggers the end of the preparation.
   */
  public void restartPreparingTimer() {
    preparingTimer.restart();
  }

  /**
   * Changes the state of the machine with the specified State.
   * Performs the {@code entry()} method of the {@code newState}.
   * 
   * @param newState the State the machine should be in
   */
  public void changeState(State newState) {
    this.state.exit(this);
    this.state = newState;
    this.state.entry(this);

    machineGUI.updateUI();
  }
  
  @Override
  public State getState() {
    return state;
  }

  @Override
  public void addProblem(Problem problem) {
    if (currentProblems.add(problem)) {
      log.warn(problem + " problem encountered!");
      if (this.state.isProblem()) {
        this.state = problem;
        this.state.entry(this);
        machineGUI.updateUI();
      } else {
        changeState(problem);
      }
    }
  }

  @Override
  public void problemSolved(Problem problem) {
    if (currentProblems.remove(problem)) {
      log.info(problem + " problem solved!");
      if (currentProblems.isEmpty()) {
        changeState(Idle.getInstance());
      } else if (this.state == problem) {
        changeState(currentProblems.iterator().next());
      } else {
        problem.exit(this);
      }
    }
  }

  @Override
  public void coinInserted(Coin coin) {
    state.coinInserted(coin, this);
  }

  @Override
  public void confirm() {
    state.confirm(this);
  }

  @Override
  public void cancel() {
    state.cancel(this);
  }

  @Override
  public void less() {
    state.less(this);
    machineGUI.updateSugarText();
  }

  @Override
  public void more() {
    state.more(this);
    machineGUI.updateSugarText();
  }

  @Override
  public void drinkButton(Drink drink) {
    state.drinkButton(drink, this);
  }

  @Override
  public List<Drink> getDrinks() {
    return stock.getDrinks();
  }

  /**
   * @return true if all the Drinks are free, false otherwise.
   */
  public boolean areDrinksFree() {
    for (Drink drink: stock.getDrinks()) {
      if (drink.getPrice() != 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getInfo() {
    final StringBuilder sb = new StringBuilder(300);
    sb.append("State: ").append(state)
    .append("\n\nStuck coin probability: ").append((int) (COIN_STUCK_PROB * 100)).append(" %\n\n")
    .append(amountInside / 100.0).append(" " + Utils.EURO + " inserted.\n\n")

    .append(changeMachine.getInfo())

    .append('\n').append(stock.getInfo());

    return sb.toString();
  }

  @Override
  public String getNorthText() {
    return state.getDefaultText(this);
  }

  @Override
  public String getSugarText() {
    return state.getSugarText(this);
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
   * Updates the IMachineGUI associated with the Context.
   */
  public void updateUI() {
    machineGUI.updateUI();
  }

  /**
   * @return the Stock associated with the machine
   */
  public Stock getStock() {
    return stock;
  }

  /**
   * @return the amount entered by the client (in cents)
   */
  public int getAmountInside() {
    return amountInside;
  }

  /**
   * Simulates the giving of the change on the amount specified.
   * The amount must be positive.
   * 
   * @param amount the value (in cents) to give change on
   */
  public void giveChange(int amount) {
    if (amount != 0) {
      changeMachine.giveChange(amount, this);
      log.info(amount / 100.0 + " " + Utils.EURO + " of change given back.");
      machineGUI.setChangeBool(true);
    }
    amountInside = 0;
    machineGUI.updateInfo();
  }

  /**
   * @param amount the amount to give change on
   * @return true if it is possible to give change on the amount, false otherwise
   */
  public boolean isChangePossible(int amount) {
    return changeMachine.isChangePossible(amount);
  }

  @Override
  public boolean isCoinAccepted(Coin coin) {
    return changeMachine.isCoinAccepted(coin);
  }

  /**
   * Simulates the insertion of the specified Coin.
   * 
   * @param coin the Coin to insert
   */
  public void insertCoin(Coin coin) {
    amountInside += coin.VALUE;
    changeMachine.insertCoin(coin);
    log.info(coin.TEXT + " inserted (" + amountInside / 100.0 + " " + Utils.EURO + " in total).");
    machineGUI.setTemporaryNorthText(coin.TEXT + " inserted");
    machineGUI.updateInfo();
  }

  /**
   * @return true if a cup is waiting to be taken, false otherwise
   */
  public boolean isCupInside() {
    return cupInside;
  }

  /**
   * @param chosenDrink the new Drink that may be ordered by the client
   */
  public void setChosenDrink(Drink chosenDrink) {
    this.chosenDrink = chosenDrink;
  }

  /**
   * @return the Drink that is currently ordered by the client
   */
   public Drink getChosenDrink() {
    return chosenDrink;
  }

  /**
   * @return the quantity of sugar chosen by the client
   */
  public int getChosenSugar() {
    return chosenSugar;
  }

  /**
   * @param chosenSugar the new quantity of sugar chosen by the client
   */
  public void setChosenSugar(int chosenSugar) {
    this.chosenSugar = chosenSugar;
  }

  /**
   * Tells the UI to display or not the cup, with or without a spoon.
   * The {@code spoon} parameter is useless if {@code cup} is false.
   * 
   * @param cup true if the cup must be displayed, false otherwise
   * @param spoon true if a spoon is given with the cup
   */
  public void setCupBool(boolean cup, boolean spoon) {
    machineGUI.setCupBool(cup, spoon);
    cupInside = cup;
  }

  @Override
  public void takeChange() {
    machineGUI.setChangeBool(false);
    final int value = Utils.totalValue(changeOut);
    if (value > 0) {
      Utils.resetCoinsMap(changeOut);
      machineGUI.updateChangeOutInfo();
    }
  }

  @Override
  public void takeCup() {
    if (cupInside) {
      this.setCupBool(false, false);
      machineGUI.setTemporaryNorthText("Have a nice day!");
      log.info("Cup of " + chosenDrink.getName() + " taken.");
    }
  }

  @Override
  public void setWaterSupply(boolean bool) {
    heatingSystem.setWaterSupply(bool);
  }

  @Override
  public <T extends IMachineGUI & TemperatureListener> void setUI(T machineGUI) {
    this.machineGUI = machineGUI;
    heatingSystem.setObserver(machineGUI);
  }

  @Override
  public String getChangeOutInfo() {
    final StringBuilder sb = new StringBuilder(40);
    sb.append("<html>");
    for (Coin coin: Coin.COINS) {
      sb.append(coin.TEXT).append(": ");
      sb.append(changeOut.get(coin)).append(" coin(s).<br>");
    }
    sb.append("Total: ").append(Utils.totalValue(changeOut) / 100.0).append(" " + Utils.EURO + ".</html>");
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
    machineGUI.setChangeBool(true);
    log.info(coin.TEXT + " inserted but not allowed.");
  }
  
  @Override
  public void addChangeOut(Map<Coin, Integer> moneyToGive) {
    for (Coin coin: Coin.COINS) {
      changeOut.put(coin, changeOut.get(coin) + moneyToGive.get(coin));
    }
    machineGUI.updateChangeOutInfo();
  }

  @Override
  public void setCoinStock(Coin coin, int value) {
    changeMachine.setCoinStock(coin, value);
    machineGUI.updateInfo();
  }

  @Override
  public void setDrinkStock(Drink drink, int value) {
    stock.setDrinkStock(drink, value);
    machineGUI.updateInfo();
  }

  @Override
  public void setCupStock(int value) {
    stock.setCupStock(value, this);
    machineGUI.updateInfo();
  }

  @Override
  public void setSugarStock(int value) {
    stock.setSugarStock(value);
    machineGUI.updateInfo();
  }

  @Override
  public void setSpoonsStock(int value) {
    stock.setSpoonsStock(value);
    machineGUI.updateInfo();
  }

  @Override
  public boolean isAvailableForMaintenance() {
    return state.isAvailableForMaintenance();
  }

  @Override
  public void resetTemperature() {
    heatingSystem.resetTemperature();
  }

  @Override
  public void repairStuckCoins() {
    problemSolved(StuckCoin.getInstance());
  }

  /**
   * Disable/enable the button of the UI to repair the stuck coins.
   * 
   * @param b true to enable the repairing, false to disable
   */
  public void enableRepair(boolean b) {
    machineGUI.enableRepair(b);
  }

  /**
   * @return true if at least a coin is stuck, false otherwise
   */
  public boolean isACoinStuck() {
    return currentProblems.contains(StuckCoin.getInstance());
  }

  /**
   * Adds the specified coin to the list of stuck coins.
   * 
   * @param coin the Coin that is stuck
   */
  public void addStuckCoin(Coin coin) {
    stuckCoins.put(coin, stuckCoins.get(coin) + 1);
  }

  /**
   * "Unsticks" the stuck coins and gives them back in the container to the user.
   */
  public void unstickCoins() {
    if (Utils.totalValue(stuckCoins) > 0) {
      this.addChangeOut(stuckCoins);
      Utils.resetCoinsMap(stuckCoins);
      machineGUI.setChangeBool(true);
    }
  }

}