import java.io.IOException;
import java.util.Scanner;


public class MAIN2 {


	public static void main(String[] args) throws IOException {
		
		//169.254.33.180 169.254.145.138
		Scanner in = new Scanner(System.in);
		String ip = "127.0.0.1";//in.nextLine();
		GameBuilder game = new GameBuilder(false, ip, 19999, 9, 9);
		game.run();

	}

}
