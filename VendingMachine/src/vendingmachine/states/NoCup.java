package vendingmachine.states;

import vendingmachine.components.Context;

public class NoCup extends Problem {

  private static NoCup instance;

  public static NoCup instance() {
    if (instance == null) {
      instance = new NoCup();
    }
    return instance;
  }

  private NoCup() {}

  @Override
  public String getDefaultText(Context c) {
    return "No cup available. No drink can be ordered"; // Too large
  }

}