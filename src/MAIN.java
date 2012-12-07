import java.net.InetAddress;
import java.net.UnknownHostException;


public class MAIN {
	// Hello pato
	
	public static void main(String[] args) throws UnknownHostException {
		System.out.println("Your IP is " + InetAddress.getLocalHost().getHostAddress());
		GameBuilder game = new GameBuilder(true, InetAddress.getLocalHost().getHostAddress(), 19999, 9, 9);
		game.run();

	}


}
