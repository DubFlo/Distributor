package vendingmachine.states;

public abstract class Problem extends State {
  
  @Override
  public boolean isAvailableForMaintenance() {
    return true;
  }
  
}