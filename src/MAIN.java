import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;


public class MAIN {
	
	public static void main(String[] args) throws UnknownHostException {
            //JOptionPane.showMessageDialog(null, "Your IP is " + InetAddress.getLocalHost().getHostAddress());
            GameBuilder game = new GameBuilder(true, InetAddress.getLocalHost().getHostAddress(), 19999, 9, 9);
            game.run();
	}
}
