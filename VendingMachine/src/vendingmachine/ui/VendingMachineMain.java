package vendingmachine.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;
import vendingmachine.components.Stock;

public class VendingMachineMain {

  private static boolean configDone = false;

  private static Context config() { //!!!! Number too large or empty field ??????
    // -----------------------------------
    // This first part creates the menu UI
    // -----------------------------------
    JFrame myFrame = new JFrame();
    JPanel myPanel = new JPanel();
    myFrame.setTitle("Vending Machine Initialization");
    myPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    MyDocumentFilter myDocumentFilter = new MyDocumentFilter();

    final int NBR_DRINKS = VendingMachineGUI.NBR_DRINKS;
    final int NBR_COINS = ChangeMachine.NBR_COINS;

    // create everything for the drinks values
    final String[] COLUMNS_TITLES = { "Enter here the drinks names: ", "Contains sugar ?",
        "Price (in cents)  ", "Initial stock" };
    c.gridy = 0;
    for (int i = 0; i < COLUMNS_TITLES.length; i++) {
      c.gridx = i;
      myPanel.add(new JLabel(COLUMNS_TITLES[i]), c);
    }

    final String[] DEFAULT_DRINKS = { "Black Coffee", "Cappuccino", "Hot Chocolate", "Hot Milk",
        "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup" };
    JTextField[] drinksNames = new JTextField[NBR_DRINKS]; // Limiter la
    // longueur
    JCheckBox[] drinksSugar = new JCheckBox[NBR_DRINKS];
    JTextField[] drinksPrices = new JTextField[NBR_DRINKS];
    JTextField[] drinksStocks = new JTextField[NBR_DRINKS];

    for (int i = 0; i < NBR_DRINKS; i++) {
      c.gridy = i + 1;
      drinksNames[i] = new JTextField(18);
      if (i < DEFAULT_DRINKS.length) {
        drinksNames[i].setText(DEFAULT_DRINKS[i]);
      }
      drinksSugar[i] = new JCheckBox();
      if (i < 6) {
        drinksSugar[i].setSelected(true);
      }
      drinksPrices[i] = new JTextField("100", 5);
      ((AbstractDocument)drinksPrices[i].getDocument()).setDocumentFilter(myDocumentFilter);   
      drinksStocks[i] = new JTextField("5", 5);
      ((AbstractDocument)drinksStocks[i].getDocument()).setDocumentFilter(myDocumentFilter); 
      c.gridx = 0;
      myPanel.add(drinksNames[i], c);
      c.gridx = 1;
      myPanel.add(drinksSugar[i], c);
      c.gridx = 2;
      myPanel.add(drinksPrices[i], c);
      c.gridx = 3;
      myPanel.add(drinksStocks[i], c);
    }

    // create everything for the change machine
    final String[] COINS_TITLES = { "Coins and their values: ", "Initial stock",
    "Accepted by the machine?" };
    c.gridy = 9;
    for (int i = 0; i < COINS_TITLES.length; i++) {
      c.gridx = i;
      myPanel.add(new JLabel(COINS_TITLES[i]), c);
    }

    JTextField[] coinsStockValues = new JTextField[ChangeMachine.COINS.length];
    JCheckBox[] acceptedCoinsBoxes = new JCheckBox[ChangeMachine.COINS.length];
    for (int i = 0; i < NBR_COINS; i++) {
      c.gridy = 10 + i;
      coinsStockValues[i] = new JTextField("5", 4);
      ((AbstractDocument)coinsStockValues[i].getDocument()).setDocumentFilter(myDocumentFilter); 
      acceptedCoinsBoxes[i] = new JCheckBox();
      if (i < NBR_COINS - 3) { // to refuse coins too small by default
        acceptedCoinsBoxes[i].setSelected(true);
      }
      c.gridx = 0;
      myPanel.add(new JLabel(ChangeMachine.COINS_TEXT[i]), c);
      c.gridx = 1;
      myPanel.add(coinsStockValues[i], c);
      c.gridx = 2;
      myPanel.add(acceptedCoinsBoxes[i], c);
    }

    // creates everything for the stock values
    JLabel sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
    JLabel cupsNbrLabel = new JLabel("Number of cups available: ");
    JLabel spoonsNbrLabel = new JLabel("Number of spoons availables: ");
    JTextField sugarCubesNbrValue = new JTextField("20", 4);
    ((AbstractDocument)sugarCubesNbrValue.getDocument()).setDocumentFilter(myDocumentFilter); 
    JTextField cupsNbrValue = new JTextField("10", 4);
    ((AbstractDocument)cupsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter); 
    JTextField spoonsNbrValue = new JTextField("8", 4);
    ((AbstractDocument)spoonsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter); 

    c.gridy = NBR_DRINKS + NBR_COINS + 3;
    c.gridx = 0;
    myPanel.add(sugarCubesNbrLabel, c);
    c.gridx = 1;
    myPanel.add(sugarCubesNbrValue, c);

    c.gridy = NBR_DRINKS + NBR_COINS + 4;
    c.gridx = 0;
    myPanel.add(cupsNbrLabel, c);
    c.gridx = 1;
    myPanel.add(cupsNbrValue, c);

    c.gridy = NBR_DRINKS + NBR_COINS + 5;
    c.gridx = 0;
    myPanel.add(spoonsNbrLabel, c);
    c.gridx = 1;
    myPanel.add(spoonsNbrValue, c);

    // creates the button that allows to continue the code after the while
    // loop
    JButton create = new JButton("Click here to begin the simulation !");
    c.gridy = NBR_DRINKS + NBR_COINS + 6;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    create.addActionListener(e -> configDone = true);
    myPanel.add(create, c);

    myFrame.add(myPanel);
    // makes the frame scrollable in case it is shrunk
    JScrollPane scrPane = new JScrollPane(myPanel);
    myFrame.add(scrPane);

    myPanel.setBorder(new EmptyBorder(10, 10, 18, 10));
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.pack();
    myFrame.setVisible(true);

    // the program waits here until the user pushes the JButton "create"
    while (!configDone) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // ----------------------------------------------------------
    // The second part gets all the data from what the user typed
    // ----------------------------------------------------------

    // Fetches the values for the drinks
    List<Drink> drinkList = new ArrayList<Drink>();
    Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
    for (int i = 0; i < NBR_DRINKS; i++) {
      final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
          Integer.parseInt(drinksPrices[i].getText())); // final ici est suffisant ?
      drinkList.add(d);
      drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
    }

    // Fetches the values for the change machine
    Map<Coin, Integer> coinsStock = new Hashtable<Coin, Integer>();
    Map<Coin, Boolean> coinsAccepted = new Hashtable<Coin, Boolean>();
    for (int i = 0; i < NBR_COINS; i++) {
      coinsStock.put(ChangeMachine.COINS[i], Integer.parseInt(coinsStockValues[i].getText()));
      coinsAccepted.put(ChangeMachine.COINS[i], acceptedCoinsBoxes[i].isSelected());
    }
    ChangeMachine cm = new ChangeMachine(coinsStock, coinsAccepted);

    // Fetches the values for the stock (the drink stock has been done with
    // the drinks)
    int sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
    int cupsNbr = Integer.parseInt(cupsNbrValue.getText());
    int spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
    Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);

    myFrame.dispose(); // closes the configuration frame
    return new Context(drinkList, cm, stock);
  }

  public static void main(String[] args) {
    // THREADING ????
    Context c = config();
    VendingMachineGUI gui = new VendingMachineGUI(c);
    SwingUtilities.invokeLater(new Runnable() { // juste sur this.init() in GUI ?
      @Override
      public void run() {
        gui.init();
      }
    });
  }

}