package vendingmachine.states;

import vendingmachine.components.Context;

public final class NoCup extends Problem {

  private static final NoCup INSTANCE = new NoCup();

  public static NoCup getInstance() {
    return INSTANCE;
  }

  private NoCup() {}

  @Override
  public String getDefaultText(Context c) {
    return "No cup available. No drink can be ordered";
  }

}