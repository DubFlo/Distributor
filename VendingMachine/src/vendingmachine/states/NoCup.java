package vendingmachine.states;

import vendingmachine.components.Context;

/**
 * State that must be reached when no cups are left in the Stock of the Context.
 * No drink can obviously be ordered.
 */
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