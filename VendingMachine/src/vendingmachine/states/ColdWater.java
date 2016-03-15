package vendingmachine.states;

import vendingmachine.components.Context;

/**
 * State reached when the water is too cold to order a hot drink.
 * The client must wait for the water to become hotter.
 */
public final class ColdWater extends Problem {

  private static final ColdWater INSTANCE = new ColdWater();

  public static ColdWater getInstance() {
    return INSTANCE;
  }

  // Singleton design pattern
  private ColdWater() {}

  @Override
  public String getDefaultText(Context c) {
    return "Water is too cold. Please wait a moment...";
  }

}