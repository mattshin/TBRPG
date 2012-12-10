
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class Healer extends Unit {
        private  BufferedImage icon;
	public Healer() {
	}

	public Healer(String n, int h, int str, int def, int mov) {
		super(n, h, -str, def, mov);
                try{
                    icon = ImageIO.read(new File("cross.png"));
                }catch (Exception e){
                    //icon = null;
                    System.err.println("Error: "+e);
                }
	}
        public BufferedImage getIcon(){

                return icon;

        }

}
