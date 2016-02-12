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

  // Singleton design pattern A RETIRER OU DEPLACER DU CACA
  private NoCup() {
  }

  @Override
  public String getDefaultText(Context c) {
    return "No cup is available. No drink can be ordered"; // Too large
  }

}