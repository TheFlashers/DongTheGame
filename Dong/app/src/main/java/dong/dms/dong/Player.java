package dong.dms.dong;

import java.io.Serializable;
import java.util.Random;

public class Player implements Serializable {

    private byte wins;
    private byte losses;
    private byte red;
    private byte green;
    private byte blue;

    public Player() {
        wins = 0;
        losses = 0;
    }

    public void setWins(byte wins) {
        this.wins = wins;
    }

    public byte getWins() {
        return wins;
    }

    public void setLosses(byte losses) {
        this.losses = losses;
    }

    public byte getLosses() {
        return losses;
    }

    public void setPaddleColours(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }
}
