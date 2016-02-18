package vendingmachine.states;

import vendingmachine.components.Context;

public final class NoSpoon extends Problem{

  private static final NoSpoon INSTANCE = new NoSpoon();

  public static NoSpoon getInstance() {
    return INSTANCE;
  }
  
  
  // Singleton design pattern
  private NoSpoon() {}
  
  @Override
  public String getDefaultText (Context c){
    return "No spoon. Confirm to continue or Cancel ";
  }
  @Override
  public void cancel (Context c) {
    super.cancel(c);
    c.changeState(Idle.getInstance());
  }
  public void confirm (Context c){
    c.changeState(Asking.getInstance());
  }
}
