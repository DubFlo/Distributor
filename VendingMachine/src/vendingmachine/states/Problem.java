package vendingmachine.states;

public abstract class Problem extends State {
  
  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }
  
  @Override
  public boolean isProblem() {
    return true;
  }
  
}