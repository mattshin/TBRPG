import java.net.*;
import java.io.*;
import static java.lang.System.*;
import java.util.*;

public class Server {
    private final boolean DEBUG = true;

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;

    public static void main(String args[]) {
        Server server = new Server(19999);
        server.open();
        out.println(server.read());
        server.close();
    }

    public Server(int port){
        try {
            if (DEBUG)out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            if (DEBUG)out.println("Server started: " + server);
            if (DEBUG)out.println("Waiting for a client ...");
            socket = server.accept();
            if (DEBUG)out.println("Client accepted: " + socket);
        } catch (IOException ioe) {
            out.println(ioe);
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
    public void send(String w){
        try{
            streamOut.writeUTF(w);
            streamOut.flush();
        }catch (IOException e){
            err.println("Error Sending: "+e);
        }
    }
    public void readType(){
        boolean done = false;
        while (!done) {
            try {
                String line = streamIn.readUTF();
                out.println(line);
                done = line.equals(".bye");
            } catch (IOException ioe) {
                done = true;
            }
        }
    }
    public void open(){
        try{
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            err.println("Error opening: "+e);
        }
    }

    public void close(){
        try{
            if (socket != null) {
                socket.close();
            }
            if (streamIn != null) {
                streamIn.close();
            }
        }catch (IOException e){
            err.println("Error closing: "+e);
        }
    }
}