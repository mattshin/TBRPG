
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class Healer extends Unit {
        private  BufferedImage icon;
	public Healer() {
	}

	public Healer(String n, int h, int str, int def, int mov) {
		super(n, h, -str, def, mov);
                setIcon("cross.png");
	}
}
