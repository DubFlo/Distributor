package vendingmachine.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;
import vendingmachine.components.Stock;

public class Configuration extends JFrame {

  private static final long serialVersionUID = 1L;

  private static final String[] COLUMNS_TITLES = { "  Enter here the drinks names:  ",
    "  Contains sugar?  ", "  Price (in cents):  ", "  Initial stock:  " };
  private static final String[] COINS_TITLES = { "Coins and their values:  ", "  Initial stock:  ",
    "  Accepted by the machine?  " };
  private static final String[] DEFAULT_DRINKS = { "Black Coffee", "Cappuccino", "Hot Chocolate",
    "Hot Milk", "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup", "Hot Water", "Oolong Tea" };
  private static final Integer[] NBR_DRINKS_LIST = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

  private JPanel drinkPanel;

  private final MyDocumentFilter myDocumentFilter;

  private final JComboBox<Integer> drinkNbrComboBox;

  private final JTextField[] drinksNames;
  private final JCheckBox[] drinksSugar;
  private final JTextField[] drinksPrices;
  private final JTextField[] drinksStocks;
  private final JTextField[] coinsStockValues;
  private final JCheckBox[] acceptedCoinsBoxes;

  private final JTextField sugarCubesNbrValue;
  private final JTextField cupsNbrValue;
  private final JTextField spoonsNbrValue;

  private final JButton createButton;
  private final JLabel problemLabel;

  public Configuration() {
    super();
    setTitle("Vending Machine Initialization");

    drinkNbrComboBox = new JComboBox<Integer>(NBR_DRINKS_LIST);
    drinkNbrComboBox.setSelectedIndex(7); //to have 8 drinks as a default value
    drinkNbrComboBox.addActionListener(e -> updateDrinkPanel());

    myDocumentFilter = new MyDocumentFilter();
    
    sugarCubesNbrValue = new JTextField("20", 4);
    ((AbstractDocument)sugarCubesNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    cupsNbrValue = new JTextField("10", 4);
    ((AbstractDocument)cupsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    spoonsNbrValue = new JTextField("8", 4);
    ((AbstractDocument)spoonsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    
    problemLabel = new JLabel();
    problemLabel.setForeground(Color.RED);
    
    createButton = new JButton("Click here to begin the simulation!");
    createButton.addActionListener(e -> check());
    
    coinsStockValues = new JTextField[ChangeMachine.COINS.length];
    acceptedCoinsBoxes = new JCheckBox[ChangeMachine.COINS.length];
    
    drinksNames = new JTextField[10];
    drinksSugar = new JCheckBox[10];
    drinksPrices = new JTextField[10];
    drinksStocks = new JTextField[10];
    
    for (int i = 0; i < 10; i++) {
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
    }
    
    for (int i = 0; i < ChangeMachine.COINS.length; i++) {
      coinsStockValues[i] = new JTextField("5", 4);
      ((AbstractDocument)coinsStockValues[i].getDocument()).setDocumentFilter(myDocumentFilter);
      acceptedCoinsBoxes[i] = new JCheckBox();
      if (i < ChangeMachine.COINS.length - 3) { // to refuse coins too small by default
        acceptedCoinsBoxes[i].setSelected(true);
      }
    }
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void init() {
    final JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridBagLayout());
    final GridBagConstraints c1 = new GridBagConstraints();
    final Border grayLine = BorderFactory.createLineBorder(Color.GRAY);
    c1.insets = new Insets(5, 5, 5, 5); //

    final JLabel drinkNbrLabel = new JLabel("Number of drinks (between 1 and 10): ");
    
    c1.gridy = 0;
    myPanel.add(drinkNbrLabel, c1);
    c1.gridy = 1;
    myPanel.add(drinkNbrComboBox, c1);
    
    drinkPanel = new JPanel(new GridBagLayout());
    drinkPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Drink Information"));
    updateDrinkPanel();
    c1.gridy = 2;
    myPanel.add(drinkPanel, c1);
    
    final JPanel coinPanel = new JPanel(new GridBagLayout());
    coinPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Coin Information"));
    final GridBagConstraints c2 = new GridBagConstraints();
    c2.gridy += 1; c2.gridx = 0;
    for (String s: COINS_TITLES) {
      coinPanel.add(new JLabel(s), c2);
      c2.gridx += 1;
    }

    for (int i = 0; i < ChangeMachine.COINS.length; i++) {
      c2.gridy += 1; c2.gridx = 0;
      coinPanel.add(new JLabel(ChangeMachine.COINS[i].TEXT), c2);
      c2.gridx = 1;
      coinPanel.add(coinsStockValues[i], c2);
      c2.gridx = 2;
      coinPanel.add(acceptedCoinsBoxes[i], c2);
    }

    c1.gridy = 3;
    myPanel.add(coinPanel, c1);
    
    final JPanel stockPanel = new JPanel(new GridBagLayout());
    stockPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Stock Information"));
    final GridBagConstraints c3 = new GridBagConstraints();
    final JLabel sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
    final JLabel cupsNbrLabel = new JLabel("Number of cups available: ");
    final JLabel spoonsNbrLabel = new JLabel("Number of spoons availables: ");
    
    c3.gridy = 0; c3.gridx = 0;
    stockPanel.add(sugarCubesNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(sugarCubesNbrValue, c3);

    c3.gridy += 1;  c3.gridx = 0;
    stockPanel.add(cupsNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(cupsNbrValue, c3);

    c3.gridy += 1;  c3.gridx = 0;
    stockPanel.add(spoonsNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(spoonsNbrValue, c3);

    c1.gridy = 4;
    myPanel.add(stockPanel, c1);
    
    c1.gridy = 5; c1.gridx = 0;
    c1.gridwidth = GridBagConstraints.REMAINDER;
    myPanel.add(createButton, c1);
    c1.gridy = 6;
    myPanel.add(problemLabel, c1);

    add(myPanel);
    final JScrollPane scrPane = new JScrollPane(myPanel); // makes the frame scrollable in case it is shrunk
    add(scrPane);

    myPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void check() {
    // Fetches the values for the drinks
    final List<Drink> drinkList = new ArrayList<Drink>();
    final Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
    try {
      for (int i = 0; i < (Integer)drinkNbrComboBox.getSelectedItem(); i++) {
        if (drinksNames[i].getText().equals("") || drinksNames[i].getText().length() > 18) {
          throw new IllegalArgumentException();
        }
        final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
            Integer.parseInt(drinksPrices[i].getText()));
        drinkList.add(d);
        drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
      }
    } catch (IllegalArgumentException e) {
      problemLabel.setText(getProblemText("drink"));
      pack();
      return;
    }

    // Fetches the values for the change machine
    final Map<Coin, Integer> coinsStock = new Hashtable<Coin, Integer>();
    final Map<Coin, Boolean> coinsAccepted = new Hashtable<Coin, Boolean>();
    try {
      for (int i = 0; i < ChangeMachine.COINS.length; i++) {
        coinsStock.put(ChangeMachine.COINS[i], Integer.parseInt(coinsStockValues[i].getText()));
        coinsAccepted.put(ChangeMachine.COINS[i], acceptedCoinsBoxes[i].isSelected());
      }
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("change machine"));
      pack();
      return;
    }

    // Fetches the values for the stock
    int sugarCubeNbr;
    int cupsNbr;
    int spoonsNbr;
    try {
      sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
      cupsNbr = Integer.parseInt(cupsNbrValue.getText());
      spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("stock"));
      pack();
      return;
    }
    final Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);

    final Context context = new Context(
        (Integer)drinkNbrComboBox.getSelectedItem(), drinkList, coinsStock, coinsAccepted, stock);
    final VendingMachineGUI gui = new VendingMachineGUI(context);
    dispose();
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        gui.init();
      }
    });
  }
  
  private void updateDrinkPanel() {
    final Integer NBR_DRINKS = (Integer)drinkNbrComboBox.getSelectedItem();
    drinkPanel.removeAll();
    
    final GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0; c.gridx = 0;
    for (int i = 0; i < COLUMNS_TITLES.length; i++) {
      drinkPanel.add(new JLabel(COLUMNS_TITLES[i]), c);
      c.gridx += 1;
    }

    for (int i = 0; i < NBR_DRINKS; i++) {
      c.gridy += 1; c.gridx = 0;
      drinkPanel.add(drinksNames[i], c);
      c.gridx = 1;
      drinkPanel.add(drinksSugar[i], c);
      c.gridx = 2;
      drinkPanel.add(drinksPrices[i], c);
      c.gridx = 3;
      drinkPanel.add(drinksStocks[i], c);
    }
    
    drinkPanel.revalidate();
    repaint();
    pack();
  }

  private String getProblemText(String part) {
    return "<html>Error while parsing " + part + " info. Fields can't be empty.<br>"
        + "Names can't be longer than 18 characters. Integers can't be larger than 2^31.</html>";
  }

}
