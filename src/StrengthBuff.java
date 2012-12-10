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

                try{
                    icon = ImageIO.read(new File("sword.png"));
                }catch (IOException e){
                    //icon = null;
                    System.err.println("Error: "+e);
            }
	}

	public void onDeathBy(Unit u) {
		u.setStrength(u.getStrength()+reward);

	}

	public int getRespawn() {
		return respawn;
	}

        public BufferedImage getIcon(){

                return icon;

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
