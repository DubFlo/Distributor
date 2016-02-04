package vendingmachine.ui;

import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;

import vendingmachine.components.*;

public class VendingMachineGUI implements ContextListener {

	private EventListener observer;

	public VendingMachineGUI(EventListener observer) {
		this.observer = observer;
		observer.setContextListener(this);
		this.init();
	}

	private void init() {
		final String fs = File.separator;
		
		//init Frame
		JFrame myFrame = new JFrame("Vending Machine");
		Container myContainer = myFrame.getContentPane();
		
		
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