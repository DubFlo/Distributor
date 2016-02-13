package vendingmachine.states;

import javax.swing.Timer;

import vendingmachine.SoundLoader;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;

public class Preparing extends State {

  private static Preparing instance;
  private static Timer timer; // Le mettre d'instance ? Ne marche pas

  public static Preparing instance(Context c) {
    if (instance == null) {
      instance = new Preparing(c);
    }
    timer.restart();
    SoundLoader.play(SoundLoader.filling);
    return instance;
  }

  // Singleton design pattern
  private Preparing(Context c) {
    int delay = (int) (SoundLoader.filling.getMicrosecondLength()/1000);
    timer = new Timer(delay, e -> c.changeState(Idle.instance()));
    timer.setRepeats(false);
  }

  @Override
  public void coinInserted(Coin coin, Context c) {
    super.coinInserted(coin, c);
    c.setTemporaryNorthText("Please wait for the end of the preparation...");
  }

  @Override
  public void exit(Context c) {
    c.setCupBool(true);
    c.getStock().removeCup();
    if (c.getChosenDrink().isSugar()) {
      if (!(c.getStock().getSpoonsNbr() == 0)) {
        c.getStock().removeSpoon(); // deal with 0 spoon here !!!!!!
      }
    }
    c.getStock().removeDrink(c.getChosenDrink());
    c.playAlarmSound();
    c.setTemporaryNorthText("Your drink is ready !");
    log.info(c.getChosenDrink().getName() + " prepared.");
  }

  @Override
  public String getDefaultText(Context c) {
    return "Your drink is in preparation...";
  }
}