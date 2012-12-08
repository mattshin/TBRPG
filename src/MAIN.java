import java.util.logging.*;
import javax.swing.*;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.*;

public class MAIN extends JPanel {
    JLabel label;
    JFrame frame;
    private static JFrame myframe;
    private static JTextField serverIP = null;
    private static final int port = 19999;


    public MAIN(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;

        JPanel serverPanel = createServerDialogBox();
        JPanel clientPanel = createClientDialogBox();
        JPanel aboutPanel = createAboutDialogBox();
        label = new JLabel("TBRPG (ver 0.5)",JLabel.CENTER);

        Border padding = BorderFactory.createEmptyBorder(20,20,5,20);
        serverPanel.setBorder(padding);
        clientPanel.setBorder(padding);
        aboutPanel.setBorder(padding);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Server", null, serverPanel,"Server");
        tabbedPane.addTab("Client", null,clientPanel, "Client");
        tabbedPane.addTab("About", null,aboutPanel,"About"); //tooltip text

        add(tabbedPane, BorderLayout.CENTER);
        add(label, BorderLayout.PAGE_END);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    void setLabel(String newText) {
        label.setText(newText);
    }

    private JPanel createServerDialogBox() {
        JButton showItButton = null;

        showItButton = new JButton("Start Server!");
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myframe.setVisible(false);
                myframe.dispose();
                Thread runner = new Thread(){
                    public void run() {
                       GameBuilder game = new GameBuilder(true, "", port, 9, 9);
                        game.run();
                    }
                };
                runner.start();
                
            }
        });
        return createPane("Start a server", showItButton, 0);
    }
    
    private JPanel createClientDialogBox() {
        JButton showItButton = null;
        showItButton = new JButton("Connect!");
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myframe.setVisible(false);
                myframe.dispose();
                Thread runner = new Thread(){
                    public void run() {
                        GameBuilder game = new GameBuilder(false, serverIP.getText(), port, 9, 9);
                        game.run();
                    }
                };
                runner.start();
            }
        });
        String desc = "Join an existing server";
        return createPane(desc,showItButton, 1);
    }

    private JPanel createAboutDialogBox() {
        JButton showItButton = null;
        showItButton = new JButton("Exit");
        showItButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        String desc = "About";
        return createPane(desc,showItButton, 2);
    }

    private JPanel createPane(String description, JButton showButton, int t) {
        //t: 0=server, 1=client, 2=about
        JPanel box = new JPanel();
        JLabel label = new JLabel(description);
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);
        box.add(new JLabel("\n"));

        if (t==0){//server
            String ip = "";
            try{
                ip = InetAddress.getLocalHost().getHostAddress();
            }catch (Exception e){
                System.err.println("Error: "+e.getMessage());
            }
            box.add(new JLabel("Your IP: "+ip));
        }else if (t==1){//client
            box.add(new JLabel("Server IP:"));
            serverIP = new JTextField("");
            box.add(serverIP);
        }else if (t==2){//about
            box.add(new JLabel("Game Engine:"));
            box.add(new JLabel("   Matt Shin"));
            box.add(new JLabel("Networking/GUI:"));
            box.add(new JLabel("   Pato Lankenau"));
        }

        box.add(new JLabel("\n"));

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(showButton, BorderLayout.PAGE_END);
        return pane;
    }



    private static void createAndShowGUI() {
        myframe = new JFrame("TBRPG");
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MAIN newContentPane = new MAIN(myframe);
        newContentPane.setOpaque(true);
        myframe.setContentPane(newContentPane);

        myframe.pack();
        myframe.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
