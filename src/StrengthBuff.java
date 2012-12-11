import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class StrengthBuff extends Buff {
	private final int respawn = 2;
	private final Color color = Colors.yellow;
	private int reward;
        private BufferedImage icon = null;
	
	public StrengthBuff(String n, int h, int str, int def, int reward) {
		super(n, h, str, def);
               // icon = null;
		setReward(reward);
                setIcon("sword.png");
	}

	public void onDeathBy(Unit u) {
		u.setStrength(u.getStrength()+reward);

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
