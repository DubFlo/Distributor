package vendingmachine.ui;

public interface ContextListener {
  
  void setChangeBool(boolean bool);

  void setCupBool(boolean bool);

  void setTemporaryNorthText(String msg);

  void updateChangeOutInfo();

  void updateInfo();

  void updateUI();

  void updateSugarText();

}