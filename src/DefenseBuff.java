import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class DefenseBuff extends Buff {

	private final int respawn = 2;
	private final Color color = Colors.lightpink;
	private int reward;
        private  BufferedImage icon;
	
	public DefenseBuff(String n, int h, int str, int def, int reward) {
		super(n, h, str, def);
		setReward(reward);
                setIcon("shield.png");
	}
	public void onDeathBy(Unit u) {
		u.setDefense(u.getDefense()+reward);
	}
	public int getRespawn() {
		return respawn;
	}
	public Color getColor() {
		return color;
	}
	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}

}
