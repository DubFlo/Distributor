package vendingmachine.ui;

/**
 * This interface defines only the method {@code setTemperature()}.
 * An observer may implement this interface to be notified
 * when a temperature has been changed.
 */
public interface TemperatureListener {

  /**
   * Called when a temperature has been updated.
   * A negative temperature is used to indicate that no water is supplied.
   * 
   * @param temperature the (double) temperature to set
   */
  void setTemperature(double temperature);

}
