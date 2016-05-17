package dong.dms.dong;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int wins;
    private int losses;
    private int red;
    private int green;
    private int blue;

    public Player(String name) {
        this.name = name;
        wins = 0;
        losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setPaddleColours(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }
}
