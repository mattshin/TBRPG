import java.net.*;
import java.io.*;
import static java.lang.System.*;

public class Server {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;


    public Server(int port){
        try {
            out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            out.println("Server started: " + server);
            out.println("Waiting for a client ...");
            socket = server.accept();
            out.println("Client accepted: " + socket);
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
           // out.println("begin write");
            streamOut.writeUTF(w);
           // out.println("begin flush");
            streamOut.flush();
           // out.println("end send");
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
