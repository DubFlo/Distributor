package vendingmachine.states;

import vendingmachine.components.Context;

public final class NoWater extends Problem {

  private static final NoWater INSTANCE = new NoWater();

  public static NoWater getInstance() {
    return INSTANCE;
  }

  // Singleton design pattern
  private NoWater() {}

  @Override
  public String getDefaultText(Context c) {
    return "Water supply off. No drink can be ordered";
  }

}