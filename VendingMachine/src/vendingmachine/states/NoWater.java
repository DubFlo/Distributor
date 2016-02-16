package vendingmachine.states;

import vendingmachine.components.Context;

public class NoWater extends Problem {

  private static NoWater instance = new NoWater();

  public static NoWater instance() {
    return instance;
  }

  // Singleton design pattern
  private NoWater() {}

  @Override
  public String getDefaultText(Context c) {
    return "Water supply off. No drink can be ordered";
  }

}