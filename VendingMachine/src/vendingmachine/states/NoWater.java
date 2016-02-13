package vendingmachine.states;

import vendingmachine.components.Context;

public class NoWater extends Problem {

  private static NoWater instance;

  public static NoWater instance() {
    if (instance == null) {
      instance = new NoWater();
    }
    return instance;
  }

  // Singleton design pattern
  private NoWater() {
  }

  @Override
  public String getDefaultText(Context c) {
    return "Water supply off. No drink can be ordered";
  }

}