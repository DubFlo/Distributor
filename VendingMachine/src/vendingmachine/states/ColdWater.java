package vendingmachine.states;

import vendingmachine.components.Context;

public class ColdWater extends Problem {

  private static ColdWater instance = new ColdWater();

  public static ColdWater instance() {
    return instance;
  }

  // Singleton design pattern
  private ColdWater() {}

  @Override
  public String getDefaultText(Context c) {
    return "Water is too cold. Please wait a moment...";
  }

}