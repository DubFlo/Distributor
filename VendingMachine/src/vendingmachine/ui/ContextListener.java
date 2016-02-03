package vendingmachine.ui;

public interface ContextListener {

	void setNorthText(String msg);

	void setTempNorthText(String msg);

	void setInfo(String txt);

	void setSugarText(String msg);

	void setCupIcon();

	void setChangeIcon();

}