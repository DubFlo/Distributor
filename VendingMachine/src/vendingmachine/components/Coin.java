package vendingmachine.components;

import javax.swing.ImageIcon;

import vendingmachine.PictureLoader;

public enum Coin {
  COIN200 (200, "2 €", PictureLoader.EURO2_ICON),
  COIN100 (100, "1 €", PictureLoader.EURO1_ICON),
  COIN50  (50, "0.50 €", PictureLoader.CENT50_ICON),
  COIN20  (20, "0.20 €", PictureLoader.CENT20_ICON), 
  COIN10  (10, "0.10 €", PictureLoader.CENT10_ICON),
  COIN5   (5, "0.05 €", PictureLoader.CENT5_ICON),
  COIN2   (2, "0.02 €", PictureLoader.CENT2_ICON),
  COIN1   (1, "0.01 €", PictureLoader.CENT1_ICON);

  public final int VALUE;
  public final String TEXT;
  public final ImageIcon ICON;

  private Coin(int value, String text, ImageIcon icon) {
    this.VALUE = value;
    this.TEXT = text;
    this.ICON = icon;
  }
  
}