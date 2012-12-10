import java.awt.Color;
import java.awt.image.BufferedImage;


public abstract class Buff extends RangedUnit {

	int respawn;
	Color color;
	
	public Buff(String n, int h, int str, int def) {
		super(n, h, str, def, 2 , 3);
		
	}
	
	public abstract void onDeathBy(Unit u);

	public Color getColor() {
		return color;
	}

        public BufferedImage getIcon(){
            return null;
        }


}
