package vendingmachine.ui;

public interface ContextListener {

	void setNorthText(String msg);

	void setTemporaryNorthText(String msg);

	void setSugarText(String msg);

	void setCupBool(boolean b);

	void setChangeBool(boolean b);

	void setInfo();

}