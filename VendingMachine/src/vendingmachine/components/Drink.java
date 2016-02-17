package vendingmachine.components;

public class Drink {

  private final String name;
  private final int price;
  private final boolean sugar;

  public Drink(String name, boolean sugar, int price) {
    this.name = name;
    this.sugar = sugar;
    this.price = price;
  }

  public String getName() {
    return this.name;
  }

  public int getPrice() {
    return this.price;
  }

  public boolean isSugar() {
    return this.sugar;
  }

}