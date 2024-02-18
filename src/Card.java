public class Card {

    private final String name;
    private final int price;
    private final int points;
    private final String color;
    private final String type;

    public Card (String name, int price, int points, String color, String type) {
        this.name = name;
        this.price = price;
        this.points = points;
        this.color = color;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getPoints() {
        return points;
    }

    public String getColor() {
        return color;
    }

    public String getType(){
        return type;
    }
}