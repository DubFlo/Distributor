package vendingmachine.states;

/**
 * The subclasses of this class are states that indicate a problem.
 * The Context can have multiple Problem's at once.
 */
public abstract class Problem extends State {

  /**
   * When a Problem state is reached, the machine is always available
   * to change its stock. Can not be overridden.
   * 
   * @return true
   */
  @Override
  public final boolean isAvailableForMaintenance() {
    return true;
  }

  /**
   * Returns true because these states are problems. Can not be overridden.
   * 
   * @return true
   */
  @Override
  public final boolean isProblem() {
    return true;
  }

}