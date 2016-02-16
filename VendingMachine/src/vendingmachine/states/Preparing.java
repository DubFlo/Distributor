package vendingmachine.states;

import javax.swing.Timer;

import vendingmachine.SoundLoader;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;

public class Preparing extends State {

  private static final int DELAY = (int) (SoundLoader.filling.getMicrosecondLength()/1000);
  private static Preparing instance = new Preparing();
  
  private Timer timer;

  public static Preparing instance() {
    return instance;
  }
  
  // Singleton design pattern
  private Preparing() {}

  public void entry(Context c) {
    super.entry(c);
    if (timer == null) {
      timer = new Timer(DELAY, e -> preparingOver(c)); //Si nouvelle machine, c n'est pas actualisé!!!!!
      timer.setRepeats(false);
      timer.start();  
    } else {
      instance.timer.restart();
    }
    SoundLoader.play(SoundLoader.filling);
  }
  

  @Override
  public void coinInserted(Coin coin, Context c) {
    super.coinInserted(coin, c);
    c.setTemporaryNorthText("Wait for the end of the preparation...");
  }

  private void preparingOver(Context c) { //Mettre davantage dans le context ?
    c.setCupBool(true);
    c.getStock().removeCup();
    if (c.getChosenDrink().isSugar()) {
      if ((c.getStock().getSpoonsNbr() > 0)) {
        c.getStock().removeSpoon(); // deal with 0 spoon here !!!!!!
      }
    }
    c.getStock().removeDrink(c.getChosenDrink());
    SoundLoader.play(SoundLoader.beep);
    c.setTemporaryNorthText("Your drink is ready !");
    log.info(c.getChosenDrink().getName() + " prepared.");
    c.getHeatingSystem().drinkOrdered();
    if (c.getHeatingSystem().isWaterSupplyEnabled()) {
      c.changeState(Idle.instance());
    }
  }
  
  @Override
  public String getDefaultText(Context c) {
    return "Your drink is in preparation...";
  }
}