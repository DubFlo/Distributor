package vendingmachine.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import vendingmachine.components.*;

public class VendingMachineMain {

	private static boolean configDone = false;

	public static void main(String[] args) throws IOException {
		//THREADING ????
		Context c = config();

    	SwingUtilities.invokeLater(new Runnable() { //juste sur this.init() in GUI ?
    		public void run() {
    			try {
					new VendingMachineGUI(c);
				} catch (IOException e) {
					// TO DO - 
				}
    		}
    	});
	}

	private static Context config() {
		//-----------------------------------
		//This first part creates the menu UI
		//-----------------------------------
		JFrame myFrame = new JFrame();
		myFrame.setTitle("Vending Machine Initialization");
		Container myPane = myFrame.getContentPane();
		myPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//create everything for the drinks values
		final String[] COLUMNS_TITLES = {"Enter here the drinks names: ",
				"Contains sugar ?", "Price (in cents)  ", "Initial stock"};
		c.gridy = 0;
		for (int i = 0; i < COLUMNS_TITLES.length; i++) {
			c.gridx = i;
			myPane.add(new JLabel(COLUMNS_TITLES[i]), c);
		}
		
		final String[] DEFAULT_DRINKS = {"Black Coffee", "Cappuccino", "Hot Chocolate",
				"Hot Milk", "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup"};
		final int NBR_DRINKS = VendingMachineGUI.NBR_DRINKS;
		JTextField[] drinksNames = new JTextField[NBR_DRINKS];
		JCheckBox[] drinksSugar = new JCheckBox[NBR_DRINKS];
		JTextField[] drinksPrices = new JTextField[NBR_DRINKS];
		JTextField[] drinksStocks = new JTextField[NBR_DRINKS];
		
		for (int i = 0; i < NBR_DRINKS; i++) {
			c.gridy = i + 1;
			if (i < DEFAULT_DRINKS.length) {
				drinksNames[i] = new JTextField(DEFAULT_DRINKS[i], 18);
			}
			else {
				drinksNames[i] = new JTextField(18);
			}
			drinksSugar[i] = new JCheckBox();
			drinksSugar[i].setSelected(true);
			drinksPrices[i] = new JTextField("100", 5);
			drinksStocks[i] = new JTextField("5", 5);
			c.gridx = 0;
			myPane.add(drinksNames[i], c);
			c.gridx = 1;
			myPane.add(drinksSugar[i], c);
			c.gridx = 2;
			myPane.add(drinksPrices[i], c);
			c.gridx = 3;
			myPane.add(drinksStocks[i], c);
		}
		
		//create everything for the change machine
		final String[] COINS_TITLES = {"Coins and their values: ",
				"Initial stock", "Accepted by the machine?"};
		c.gridy = 9;
		for (int i = 0; i < COINS_TITLES.length; i++) {
			c.gridx = i;
			myPane.add(new JLabel(COINS_TITLES[i]), c);
		}
		
		JTextField[] coinsStockValues = new JTextField[ChangeMachine.COINS.length];
		JCheckBox[] acceptedCoinsBoxes = new JCheckBox[ChangeMachine.COINS.length];
		for (int i = 0; i < ChangeMachine.COINS.length; i++) {
			c.gridy = 10 + i;
			coinsStockValues[i] = new JTextField("5", 4);
			acceptedCoinsBoxes[i] = new JCheckBox();
			if (i <= 4) {
				acceptedCoinsBoxes[i].setSelected(true);
			}
			c.gridx = 0;
			myPane.add(new JLabel(ChangeMachine.COINS_TEXT[i]), c);
			c.gridx = 1;
			myPane.add(coinsStockValues[i], c);
			c.gridx = 2;
			myPane.add(acceptedCoinsBoxes[i], c);
		}
		
		//creates everything for the stock values
		JLabel sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
		JLabel cupsNbrLabel = new JLabel("Number of cups available: ");
		JLabel spoonsNbrLabel = new JLabel("Number of spoons availables: ");
		JTextField sugarCubesNbrValue = new JTextField("20", 4);
		JTextField cupsNbrValue = new JTextField("10", 4);
		JTextField spoonsNbrValue = new JTextField("9", 4);
		
		c.gridy = 19;	c.gridx = 0;
		myPane.add(sugarCubesNbrLabel, c);
		c.gridy = 19;	c.gridx = 1;
		myPane.add(sugarCubesNbrValue, c);
		
		c.gridy = 20;	c.gridx = 0;
		myPane.add(cupsNbrLabel, c);
		c.gridy = 20;	c.gridx = 1;
		myPane.add(cupsNbrValue, c);
		
		c.gridy = 21;	c.gridx = 0;
		myPane.add(spoonsNbrLabel, c);
		c.gridy = 21;	c.gridx = 1;
		myPane.add(spoonsNbrValue, c);
		
		//creates the button that allows to continue the code after the while loop
		JButton create = new JButton("Click here to begin the simulation !");
		c.gridy = 25; c.gridx = 0; c.gridwidth = GridBagConstraints.REMAINDER;
		create.addActionListener(e -> configDone = true);
		myPane.add(create, c);
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
		//the program waits here until the user pushes the JButton "create"
		while (!configDone) {
			try {
	            Thread.sleep(100);
	        }
	        catch (InterruptedException e) {
	        	e.printStackTrace();
	        }
		}
		
		//----------------------------------------------------------
		//The second part gets all the data from what the user typed
		//----------------------------------------------------------
		
		//Fetches the values for the drinks
		List<Drink> drinkList = new ArrayList<Drink>();
		Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
		for (int i = 0; i < NBR_DRINKS; i++) {
			final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
									Integer.parseInt(drinksPrices[i].getText())); //final ici est suffisant ????
			drinkList.add(d);
			drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
		}
		
		//Fetches the values for the change machine
		Map<Coin, Integer> coinsStock = new Hashtable<Coin, Integer>();
		Map<Coin, Boolean> coinsAccepted = new Hashtable<Coin, Boolean>();
		for (int i = 0; i < ChangeMachine.COINS.length; i++) {
			coinsStock.put(ChangeMachine.COINS[i], Integer.parseInt(coinsStockValues[i].getText()));
			coinsAccepted.put(ChangeMachine.COINS[i], acceptedCoinsBoxes[i].isSelected());
		}
		ChangeMachine cm = new ChangeMachine(coinsStock, coinsAccepted);
		
		//Fetches the values for the stock (the drink stock has been done with the drinks)
		int sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
		int cupsNbr = Integer.parseInt(cupsNbrValue.getText());
		int spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
		
		Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);
		myFrame.dispose(); //closes the configuration frame
		return new Context(drinkList, cm, stock);
	}

}