package vendingmachine.ui;

import vendingmachine.components.*;

public class VendingMachineGUI implements ContextListener {

	private EventListener observer;
	private String info;

	/**
	 * 
	 * @param observer
	 */
	public VendingMachineGUI(EventListener observer) {
		this.observer = observer;
		observer.setContextListener(this);
	}

	private void init() {
		// TODO - implement VendingMachineGUI.init
		
	}

	public void updateGUI() {
		// TODO - implement VendingMachineGUI.updateGUI
		
	}

	public void updateInfo() {
		// TODO - implement VendingMachineGUI.updateInfo
		
	}

	@Override
	public void setNorthText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTempNorthText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfo(String txt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSugarText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCupIcon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChangeIcon() {
		// TODO Auto-generated method stub
		
	}

}