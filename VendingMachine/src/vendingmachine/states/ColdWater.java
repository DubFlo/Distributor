package vendingmachine.states;

import vendingmachine.components.Context;

public final class ColdWater extends Problem {

  private static ColdWater INSTANCE = new ColdWater();

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