import java.awt.Color;


public class StrengthBuff extends Buff {

	private final int respawn = 2;
	private final Color color = Colors.yellow;
	private int reward;
	
	public StrengthBuff(String n, int h, int str, int def, int reward) {
		super(n, h, str, def);
		setReward(reward);
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
