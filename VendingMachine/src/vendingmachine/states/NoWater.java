package vendingmachine.states;

import vendingmachine.components.Context;

/**
 * This State is reached when the water supply is disabled.
 * No drink can be ordered.
 */
public final class NoWater extends Problem {

  private static final NoWater INSTANCE = new NoWater();

  public static NoWater getInstance() {
    return INSTANCE;
  }

  private NoWater() {}

  @Override
  public String getDefaultText(Context c) {
    return "Water supply off. No drink can be ordered";
  }

}