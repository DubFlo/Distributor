package test;

import vendingmachine.ui.IMachineGUI;
import vendingmachine.ui.TemperatureListener;

public class EmptyUI implements IMachineGUI, TemperatureListener {

  @Override
  public void setChangeBool(boolean bool) {}

  @Override
  public void setCupBool(boolean cup, boolean spoon) {}

  @Override
  public void setTemporaryNorthText(String msg) {}

  @Override
  public void updateChangeOutInfo() {}

  @Override
  public void updateInfo() {}

  @Override
  public void updateNorthText() {}
  
  @Override
  public void updateSugarText() {}

  @Override
  public void updateUI() {}

  @Override
  public void enableRepair(boolean bool) {}

  @Override
  public void setCupText(String msg) {}

  @Override
  public void setTemperature(double temperature) {}

}
