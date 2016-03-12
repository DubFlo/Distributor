package vendingmachine.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import vendingmachine.FontLoader;
import vendingmachine.PictureLoader;
import vendingmachine.components.Drink;
import vendingmachine.components.Machine;
import vendingmachine.components.Coin;

/**
 * This class creates a graphical interface of a vending machine using a Machine object.
 */
public class VendingMachineGUI extends JFrame implements ContextListener, TemperatureListener {

  private static final long serialVersionUID = 1L;

  private final DoorJPanel leftPanel;
  
  private final Machine machine;
  
  /*
   * The labels and text areas of the GUI.
   */
  private final JLabel northLabel;
  private final JLabel sugarLabel;
  private final JLabel temperatureLabel;
  private final JTextArea infoArea;
  
  /*
   * The buttons of the GUI.
   */
  private final List<DrinkJButton> drinkButtonsList;
  private final List<CoinJButton> coinButtonsList;
  private final JButton cupButton;
  private final JButton changeButton;
  private final JButton lessSugar;
  private final JButton moreSugar;
  private final JButton okButton;
  private final JButton cancelButton;
  
  private final JMenuBar menuBar;

  /**
   * Timer that restarts each time a text must be displayed temporarily.
   */
  private final Timer textTimer;
  
  /**
   * Timer that makes the door animation possible.
   */
  private final Timer doorTimer;

  /**
   * Initializes the fields according to the Machine specified.
   * Associates the Machine and the VendingMachineGUI together.
   * 
   * @param machine the Machine to link with the GUI
   */
  public VendingMachineGUI(Machine machine) {
    super();
    this.machine = machine;
    this.machine.setObserver(this);
    this.setTitle("Vending Machine");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    northLabel = new JLabel();
    northLabel.setForeground(Color.RED);
    northLabel.setFont(FontLoader.DIGITAL_FONT);
    drinkButtonsList = new ArrayList<DrinkJButton>();
    for (int i = 0; i < machine.getNbrDrinks(); i++) {
      drinkButtonsList.add(new DrinkJButton(machine.getDrinks().get(i)));
    }
    
    leftPanel = new DoorJPanel();
    leftPanel.setLayout(new BorderLayout());
    cupButton = new JButton();
    cupButton.setBorder(BorderFactory.createEmptyBorder());
    cupButton.setContentAreaFilled(false);
    cupButton.setBorderPainted(false);
    leftPanel.setPreferredSize(new Dimension(120, 550));  
    temperatureLabel = new JLabel();
    temperatureLabel.setFont(FontLoader.DIGITAL_FONT);
    temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
    temperatureLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    temperatureLabel.setBackground(Color.WHITE);
    temperatureLabel.setOpaque(true);
    
    sugarLabel = new JLabel();
    if (FontLoader.DIGITAL_FONT != null) {
      sugarLabel.setFont(FontLoader.DIGITAL_FONT.deriveFont(16f));
    }
    sugarLabel.setForeground(Color.RED);
    sugarLabel.setIcon(PictureLoader.SUGAR_DISPLAY);
    sugarLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    lessSugar = new JButton("-");
    moreSugar = new JButton("+");
    okButton = new JButton("Confirm");
    cancelButton = new JButton("Cancel");   

    changeButton = new JButton();
    changeButton.setBorder(BorderFactory.createEmptyBorder());
    changeButton.setContentAreaFilled(false);
    changeButton.setToolTipText(machine.getChangeOutInfo());
    
    coinButtonsList = new ArrayList<CoinJButton>();

    for (Coin coin: Coin.COINS) {
      coinButtonsList.add(new CoinJButton(coin));
    }
    
    infoArea = new JTextArea();
    infoArea.setEditable(false);
    
    menuBar = new JMenuBar();
    
    textTimer = new Timer(2500, e -> updateNorthText());
    textTimer.setRepeats(false);

    doorTimer = new Timer(8, e -> {
      leftPanel.setStep(leftPanel.getStep() + 1);
      if (leftPanel.getStep() >= DoorJPanel.HEIGHT) {
        ((Timer)e.getSource()).stop();
      }
    });
  }

  private void addListeners() {
    cupButton.addActionListener(e -> {
      if (!doorTimer.isRunning()) {
        machine.takeCup();
        }
      });
    changeButton.addActionListener(e -> machine.takeChange());
    lessSugar.addActionListener(e -> machine.less());
    moreSugar.addActionListener(e -> machine.more());
    okButton.addActionListener(e -> machine.confirm());
    cancelButton.addActionListener(e -> machine.cancel());

    for (int i = 0; i < drinkButtonsList.size(); i++) {
      final DrinkJButton drinkButton = drinkButtonsList.get(i);
      drinkButton.addActionListener(e -> machine.drinkButton(drinkButton.getDrink()));
    }
    for (int i = 0; i < coinButtonsList.size(); i++) {
      final CoinJButton coinButton = coinButtonsList.get(i);
      coinButton.addActionListener(e -> machine.coinInserted(coinButton.getCoin()));
    }
  }
  
  private void createMenuBar() {
    final JMenu waterSupplyMenu = new JMenu("Water Supply");
    final JMenu coinsMenu = new JMenu("Coin Stock");
    final JMenu drinksMenu = new JMenu("Drink Stock");
    final JMenu settings = new JMenu("Settings");
    final JCheckBoxMenuItem waterSupplyBox = new JCheckBoxMenuItem("Water supply enabled", true);
    final JMenuItem instantWarming = new JMenuItem("Instant Warming");
    final JMenuItem newVM = new JMenuItem("New Vending Machine");
    final JMenuItem quit = new JMenuItem("Quit");

    menuBar.add(waterSupplyMenu);
    waterSupplyMenu.add(waterSupplyBox);
    waterSupplyMenu.add(instantWarming);
    menuBar.add(coinsMenu);
    menuBar.add(drinksMenu);
    menuBar.add(settings);
    settings.add(newVM);
    settings.add(quit);
    
    newVM.addActionListener(e -> { dispose(); Main.run(); } );
    quit.addActionListener(e -> System.exit(0));
    waterSupplyBox.addActionListener(
        e -> machine.setWaterSupply(waterSupplyBox.isSelected()));
    instantWarming.addActionListener(e -> machine.setTemperature(93));
    
    for (Coin coin: Coin.COINS) {
      final JMenuItem item = new JMenuItem(coin.TEXT);
      item.addActionListener(e -> coinStockDialog(coin));
      coinsMenu.add(item);
    }
    
    for (Drink drink: machine.getDrinks()) {
      final JMenuItem item = new JMenuItem(drink.getName());
      item.addActionListener(e -> drinkStockDialog(drink));
      drinksMenu.add(item);
    }
  }

  public void init() {
    // Initializes Frame
    final Container myContainer = getContentPane();

    // Main Panel
    final JPanel myPanel = new JPanel(new BorderLayout());

    // North Panel
    final JPanel northPanel = new BackgroundJPanel(PictureLoader.DISPLAY_PANEL);
    northPanel.add(northLabel);
    northPanel.setPreferredSize(new Dimension(100, 50));
    myPanel.add(northPanel, BorderLayout.PAGE_START);

    // Center Panel
    final JPanel centerPanel = new BackgroundJPanel(PictureLoader.COFFEE_IMAGE);
    centerPanel.setLayout(new GridLayout((machine.getNbrDrinks() + 1) / 2, 2, 30, 0));
    for (DrinkJButton myButton: drinkButtonsList) {
      centerPanel.add(myButton);
    }
    myPanel.add(centerPanel, BorderLayout.CENTER);

    // Left Panel
    leftPanel.add(cupButton, BorderLayout.PAGE_END);
    leftPanel.add(temperatureLabel, BorderLayout.PAGE_START);
    myPanel.add(leftPanel, BorderLayout.LINE_START);

    // Right Panel
    final JPanel rightPanel = new BackgroundJPanel(PictureLoader.SLOT_IMAGE);
    rightPanel.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH; // A expliquer
    c.weightx = 1;

    c.gridx = 0;  c.gridy = 0;  c.gridwidth = 2;
    rightPanel.add(sugarLabel, c);
    
    c.gridx = 0;  c.gridy = 1;  c.gridwidth = 1;
    rightPanel.add(lessSugar, c);
    
    c.gridx = 1;  c.gridy = 1;  c.gridwidth = 1;
    rightPanel.add(moreSugar, c);
    
    c.gridx = 0;  c.gridy = 2;  c.gridwidth = 2;
    rightPanel.add(okButton, c);
    
    c.gridx = 0;  c.gridy = 3;  c.gridwidth = 2;
    rightPanel.add(cancelButton, c);
    
    rightPanel.setPreferredSize(new Dimension(100, 550));
    myPanel.add(rightPanel, BorderLayout.LINE_END);

    // South Panel
    final JPanel southPanel = new JPanel();
    southPanel.setBackground(Color.DARK_GRAY);
    southPanel.setLayout(new BorderLayout());
    southPanel.add(changeButton, BorderLayout.LINE_END);
    myPanel.add(southPanel, BorderLayout.PAGE_END);
    southPanel.setPreferredSize(new Dimension(600, 100));

    // coins buttons
    final JPanel coinsPanel = new JPanel(new GridLayout((Coin.COINS.size() + 1) / 2, 2, 5, 5));
    coinsPanel.setBackground(Color.WHITE);

    for (CoinJButton myButton: coinButtonsList) {
      coinsPanel.add(myButton);
    }
    coinsPanel.setMinimumSize(new Dimension(100, 100));

    // JMenuBar
    this.createMenuBar();

    // Information area
    final JPanel infoPanel = new JPanel();
    final JScrollPane scrInfoPanel = new JScrollPane(infoPanel);
    infoPanel.add(infoArea);
    infoPanel.setBackground(Color.WHITE);

    // Ending operations
    final JSplitPane leftPane = new JSplitPane();
    leftPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    leftPane.setLeftComponent(myPanel);
    leftPane.setRightComponent(coinsPanel);
    final JSplitPane rightPane = new JSplitPane();
    rightPane.setLeftComponent(leftPane);
    rightPane.setRightComponent(scrInfoPanel);
    myContainer.add(rightPane);
    this.setJMenuBar(menuBar);

    this.addListeners();
    this.updateUI();

    this.setMinimumSize(new Dimension(400, 400));
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  @Override
  public void updateUI() {
    updateInfo();
    updateNorthText();
    updateSugarText();
  }
  
  @Override
  public void updateSugarText() {
    sugarLabel.setText(machine.getSugarText());
  }

  private void updateNorthText() {
    northLabel.setText(machine.getNorthText().toUpperCase());
  }

  @Override
  public void setChangeBool(boolean bool) {
    if (bool) {
      changeButton.setIcon(PictureLoader.CHANGE_ICON);
    } else {
      changeButton.setIcon(null);
    }
  }

  @Override
  public void setCupBool(boolean bool) {
    if (bool) {
      cupButton.setIcon(PictureLoader.CUP_ICON);
      doorTimer.restart();
    } else {
      cupButton.setIcon(null); // replace with a black picture ??
      leftPanel.setStep(0);
    }
  }

  @Override
  public void updateInfo() {
    infoArea.setText(machine.getInfo());
  }

  @Override
  public void setTemperature(double temperature) {
    if (temperature < 0) {
      temperatureLabel.setText("NO WATER");
    } else {
      final DecimalFormat f = new DecimalFormat("#.#");
      temperatureLabel.setText(f.format(temperature) + " °C");
    }
  }

  @Override
  public void setTemporaryNorthText(String msg) {
    northLabel.setText(msg.toUpperCase(Locale.ENGLISH));
    textTimer.restart();
  }

  @Override
  public void updateChangeOutInfo() {
    changeButton.setToolTipText(machine.getChangeOutInfo());
  }

  private int stockDialog(String element) {
    int value = -1;
    if (machine.isAvailableForMaintenance()) {
      final String inputValue = JOptionPane.showInputDialog(
          this, "Enter the new stock value for the " + element + ": ");
      try {
        value = Integer.parseInt(inputValue);
        if (value < 0) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException exc) {
        JOptionPane.showMessageDialog(this, "The value is not valid. Nothing has been changed.");
      }
    } else {
      JOptionPane.showMessageDialog(
          this, "Now is not the time to use that !\nPlease end or wait for the end of the current operation.");
    }
    return value;
  }
  
  private void coinStockDialog(Coin coin) {
    int value = stockDialog(coin.TEXT + " coin");
    if (value >= 0) {
      machine.setCoinStock(coin, value);
    }
  }
  
  private void drinkStockDialog(Drink drink) {
    int value = stockDialog(drink.getName());
    if (value >= 0) {
      machine.setDrinkStock(drink, value);
    }
  }
}