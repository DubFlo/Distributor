package vendingmachine.states;

import vendingmachine.SoundLoader;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;

public final class Preparing extends State {

  private static final Preparing INSTANCE = new Preparing();

  public static Preparing getInstance() {
    return INSTANCE;
  }
  
  // Singleton design pattern
  private Preparing() {}

  public void entry(Context c) {
    super.entry(c);
    SoundLoader.play(SoundLoader.FILLING);
  }
  
  @Override
  public void coinInserted(Coin coin, Context c) {
    super.coinInserted(coin, c);
    c.setTemporaryNorthText("Wait for the end of the preparation...");
  }
  
  @Override
  public String getDefaultText(Context c) {
    return "Your drink is in preparation...";
  }
}