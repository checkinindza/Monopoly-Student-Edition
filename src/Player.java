public class Player {
    private Integer money;
    private Integer points;
    private final Integer whichPlayer;

    public Player(Integer money, Integer points, Integer whichPlayer) {
        this.money = money;
        this.points = points;
        this.whichPlayer = whichPlayer;
    }

    public Integer getMoney() {
        return money;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getWhichPlayer() {
        return whichPlayer;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
