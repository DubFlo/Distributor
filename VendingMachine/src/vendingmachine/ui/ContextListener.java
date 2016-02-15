package vendingmachine.ui;

public interface ContextListener {
  
  void setChangeBool(boolean b);

  void setCupBool(boolean b);

  void setNorthText(String msg);

  void setSugarText(String msg);

  void setTemporaryNorthText(String msg);

  void updateChangeOutInfo();

  void updateInfo();

}