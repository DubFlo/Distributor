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
import javax.swing.ImageIcon;
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
import vendingmachine.components.EventListener;
import vendingmachine.components.Coin;
import static vendingmachine.components.ChangeMachine.COINS;

public class VendingMachineGUI extends JFrame implements ContextListener, TemperatureListener {

  private static final long serialVersionUID = 1L;

  private final DoorJPanel leftPanel;
  
  private final EventListener observer;
  private final JLabel northLabel;
  private final JLabel sugarLabel;
  private final JLabel temperatureLabel;

  private final JTextArea infoArea;
  private final List<DrinkJButton> drinkButtonsList;
  private final List<CoinJButton> coinButtonsList;
  private final JButton cupButton;
  private final JButton changeButton;
  private final JButton lessSugar;
  private final JButton moreSugar;
  private final JButton okButton;
  private final JButton cancelButton;
  
  private final JMenuBar menuBar;

  private final Timer textTimer;
  private final Timer doorTimer;

  public VendingMachineGUI(EventListener observer) {
    super();
    this.observer = observer;
    observer.setObserver(this);
    
    setTitle("Vending Machine");
    northLabel = new JLabel();
    northLabel.setForeground(Color.RED);
    northLabel.setFont(FontLoader.DIGITAL_FONT);
    drinkButtonsList = new ArrayList<DrinkJButton>();
    for (int i = 0; i < observer.getNbrDrinks(); i++) {
      drinkButtonsList.add(new DrinkJButton(observer.getDrinks().get(i)));
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
    sugarLabel.setFont(FontLoader.DIGITAL_FONT.deriveFont(16f));
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
    changeButton.setToolTipText(observer.getChangeOutInfo());
    
    coinButtonsList = new ArrayList<CoinJButton>();
    final ImageIcon[] coinsImages = { PictureLoader.EURO2_ICON, PictureLoader.EURO1_ICON, PictureLoader.CENT50_ICON,
        PictureLoader.CENT20_ICON, PictureLoader.CENT10_ICON, PictureLoader.CENT5_ICON, PictureLoader.CENT2_ICON,
        PictureLoader.CENT1_ICON };
    for (int i = 0; i < coinsImages.length; i++) {
      coinButtonsList.add(new CoinJButton(COINS[i], coinsImages[i]));
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
        observer.takeCup();
        }
      });
    changeButton.addActionListener(e -> observer.takeChange());
    lessSugar.addActionListener(e -> observer.less());
    moreSugar.addActionListener(e -> observer.more());
    okButton.addActionListener(e -> observer.confirm());
    cancelButton.addActionListener(e -> observer.cancel());

    for (int i = 0; i < drinkButtonsList.size(); i++) {
      final DrinkJButton d = drinkButtonsList.get(i);
      d.addActionListener(e -> observer.drinkButton(d.getDrink()));
    }
    for (int i = 0; i < coinButtonsList.size(); i++) {
      final CoinJButton b = coinButtonsList.get(i);
      b.addActionListener(e -> observer.coinInserted(b.getCoin()));
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
    
    newVM.addActionListener(e -> { dispose(); VendingMachineMain.run(); } );
    quit.addActionListener(e -> System.exit(0));
    waterSupplyBox.addActionListener(
        e -> observer.setWaterSupply(waterSupplyBox.isSelected()));
    instantWarming.addActionListener(e -> observer.setTemperature(93));
    
    for (Coin coin: COINS) {
      final JMenuItem item = new JMenuItem(coin.TEXT);
      item.addActionListener(e -> coinStockDialog(coin));
      coinsMenu.add(item);
    }
    
    for (Drink drink: observer.getDrinks()) {
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
    centerPanel.setLayout(new GridLayout((observer.getNbrDrinks() + 1) / 2, 2, 30, 0));
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
    GridBagConstraints c = new GridBagConstraints();
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
    final JPanel coinsPanel = new JPanel(new GridLayout((COINS.length + 1) / 2, 2, 5, 5));
    coinsPanel.setBackground(Color.WHITE);

    for (CoinJButton myButton: coinButtonsList) {
      coinsPanel.add(myButton);
    }
    coinsPanel.setMinimumSize(new Dimension(100, 100));

    // JMenuBar
    createMenuBar();

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
    setJMenuBar(menuBar);

    addListeners();
    updateUI();

    setMinimumSize(new Dimension(400, 400));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  @Override
  public void updateUI() {
    updateInfo();
    updateNorthText();
    updateSugarText();
  }
  
  @Override
  public void updateSugarText() {
    sugarLabel.setText(observer.getSugarText());
  }

  private void updateNorthText() {
    northLabel.setText(observer.getNorthText().toUpperCase());
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
    infoArea.setText(observer.getInfo());
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
    changeButton.setToolTipText(observer.getChangeOutInfo());
  }

  private int stockDialog(String element) {
    int value = -1;
    if (observer.isAvailableForMaintenance()) {
      String inputValue = JOptionPane.showInputDialog(
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
      observer.setCoinStock(coin, value);
    }
  }
  
  private void drinkStockDialog(Drink drink) {
    int value = stockDialog(drink.getName());
    if (value >= 0) {
      observer.setDrinkStock(drink, value);
    }
  }
}