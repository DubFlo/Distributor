package vendingmachine.components;

public enum Coin {
  COIN200(200, "2 €"),
  COIN100(100, "1 €"),
  COIN50(50, "0.50 €"),
  COIN20(20, "0.20 €"), 
  COIN10(10, "0.10 €"),
  COIN5(5, "0.05 €"),
  COIN2(2, "0.02 €"),
  COIN1(1, "0.01 €");

  public final int VALUE;
  public final String TEXT;

  private Coin(int value, String text) {
    this.VALUE = value;
    this.TEXT = text;
  }
}