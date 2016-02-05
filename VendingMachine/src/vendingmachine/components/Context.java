package vendingmachine.components;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import vendingmachine.ui.*;
import vendingmachine.states.*;

public class Context implements EventListener {

	private ChangeMachine changeMachine;
	private Stock stock;
	private List<Drink> drinkList;
	private HeatingSystem heatingSystem;
	
	private State state;
	
	private ContextListener observer;
	
	private static Clip clip;
	static {
		//http://www.freesound.org/people/AlaskaRobotics/sounds/221087/
		String file = "src"+File.separator+"resources"+File.separator+"beep.wav";    
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public Context(List<Drink> drinkList, ChangeMachine changeMachine, Stock stock) {
		this.drinkList = drinkList;
		this.changeMachine = changeMachine;
		this.stock = stock;
		this.state = Idle.Instance();
		this.heatingSystem = new HeatingSystem();
	}

	@Override
	public void changeState(State state) {
		this.state = state;
		this.state.entry();
	}

	public void playAlarmSound() {
		if (clip.isRunning())
			clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	@Override
	public void drinkButton(Drink drink) {
		state.drinkButton(drink, this);
	}

	@Override
	public void less() {
		state.less(this);
	}

	@Override
	public void more() {
		state.more(this);
	}

	@Override
	public void cancel() {
		state.cancel(this);
	}

	@Override
	public void confirm() {
		state.confirm(this);
	}

	@Override
	public void coinInserted(Coin coin) {
		state.coinInserted(coin, this);
	}

	@Override
	public List<Drink> getDrinks() {
		return drinkList;
	}

	@Override
	public <T extends ContextListener & TemperatureListener> void setListener(T o) {
		state.setContextListener(o);
		heatingSystem.setObserver(o);
	}
	
	public ContextListener getObserver() {
		return observer;
	}
	
	@Override
	public State getState() {
		return state;
	}

	@Override
	public String getInfo() {
		String info = "State: " + getState() + "\n";
		info += "\nDrinks: \n";
		Map<Drink, Integer> s = stock.getDrinkQty();
		for (int i = 0; i < 8; i++) {
			info += drinkList.get(i).getName() + ": " + s.get(drinkList.get(i)) + " available.\n";
		}
		
		info += "\nCoins:\n";
		for (int i = 0; i < 8; i++) {
			info += ChangeMachine.COINS_TEXT[i] + ": "
					+ changeMachine.getCoinsStock().get(ChangeMachine.COINS[i]) + " available.\n";
		}
		
		info += "\n" + stock.getCupsNbr() + " cup(s) available.\n"
				+ stock.getSugarCubesNbr() + " sugar cube(s) available.\n"
				+ stock.getSpoonsNbr() + " spoon(s) available.\n";
		
		return info;
	}

	public double getTemperature() {
		return heatingSystem.getTemperature();
	}
	
	public ChangeMachine getChangeMachine() {
		return changeMachine;
	}

	public Stock getStock() {
		return stock;
	}
	
	public HeatingSystem getHeatingSystem() {
		return heatingSystem;
	}
	
}