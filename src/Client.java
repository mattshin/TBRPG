
import java.net.*;
import java.io.*;
import static java.lang.System.*;

public class Client {

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private DataInputStream streamIn = null;


    public Client(String serverName, int serverPort) {
        out.println("Client started.");
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void send(String w){
        try{
            //out.println("begin send");
            streamOut.writeUTF(w);
            //out.println("begin flush");
            streamOut.flush();
            //out.println("end send");
        }catch (IOException e){
            err.println("Sending error: "+e);
        }
    }
    public String read(){
        boolean done = false;
        String line = "";
        while (!done){
            try{
                line = streamIn.readUTF();
                done = true;
            }catch (IOException ioe) {
                done = true;
            }
        }
        return line;
    }
    @SuppressWarnings("deprecation")
	public void readType(){
        String line = "";
        while (!line.equals(".bye")){
            try {
                line = console.readLine();
                streamOut.writeUTF(line);
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void stop() {
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}
