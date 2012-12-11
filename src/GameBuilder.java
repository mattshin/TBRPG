import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBuilder extends JPanel{
	private JFrame board = new JFrame("TBRPG");
	
	private int length;
	private int width;
	private Unit[][] tiles;
	private boolean selected = false;
	private boolean moved = false;
	private boolean attack = false;
	private Unit currUnit = new Unit();
	private int scale;
	
	private Server server;
	private Client client;
	
	private int[][] canMove;
	private boolean[][] canAttack;
	
	private ArrayList<Unit> Blue = new ArrayList<Unit>();
	private ArrayList<Unit> Purple = new ArrayList<Unit>();
	
	private boolean turn = true;

        public GameBuilder(boolean isServ, int port, int w, int l){
		if(isServ){
			server = new Server(port);
			server.open();
		}else{
			client = new Client(port);
		}
		this.length = l;
		this.width = w;

		scale = 60 - 2* w;

		tiles = new Unit[length][width];
		canMove = new int[length+2][width+2];
		canAttack = new boolean[length][width];

		setPreferredSize(new Dimension(scale * (width +2),scale * (length +2)));
	}
	
	public GameBuilder(boolean isServ, String ip, int port, int w, int l){
		if(isServ){
			server = new Server(port);
			server.open();
		}else{
			client = new Client(ip, port);	
		}
		this.length = l;
		this.width = w;
		
		scale = 60 - 2* w;
		
		tiles = new Unit[length][width];
		canMove = new int[length+2][width+2];
		canAttack = new boolean[length][width];
		
		setPreferredSize(new Dimension(scale * (width +2),scale * (length +2)));
	}
	
private static void initializeUnits(GameBuilder game) {
		
		Unit[] bros = new Unit[10];
		for(int i = 0; i < bros.length; i++){
		    bros[i] = new Unit("BRO", 20, 6, 2, 3);
		}	
		    
		Unit defbuff = new DefenseBuff("Woah", 25, 3, 3, 3);
		Unit defbuff2 = new DefenseBuff("Woah", 25, 3, 3, 3);
		
		Unit healerBro = new Healer("heals", 15, 3, 4, 2);
		Unit healerBro2 = new Healer("heals", 15, 3, 4, 2);
		            
		Unit strengthBro = new StrengthBuff("PWOER", 9, 9, 0, 3);
		Unit strengthBro2 = new StrengthBuff("PWOER", 9, 9, 0, 3);
		
		game.add(healerBro, 0, 0,true);
		game.add(defbuff, 0, 1, true);
		game.add(strengthBro, 1, 0, true);
		game.add(bros[0], 1, 1, true);
		game.add(bros[1], 2, 0, true);
		game.add(bros[2], 0, 2, true);
		game.add(bros[6], 1, 2, true);
		game.add(bros[7], 2, 1, true);
		
		game.add(strengthBro2, 7,8, false);
		game.add(healerBro2, 8,8, false);
		game.add(defbuff2, 8, 7, false);
		game.add(bros[3], 7, 7, false);
		game.add(bros[4], 6, 8, false);
		game.add(bros[5], 8, 6, false);
		game.add(bros[8], 7, 6, false);
		game.add(bros[9], 6, 7, false);
		
	}

	public void run() {
		board.setLocation(0,0);
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.add(this);
		board.pack();
		board.setVisible(true);
		initializeUnits(this);
		board.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
	            int x = e.getX()/scale-1;
	            int y = width - (e.getY()-scale/2)/scale;
	            if(turn && client == null){
		            server.send(x+" "+y);	
		            act(x, y);
	            }
	            else if(!turn && server == null){
	            	client.send(x+" "+y);
	            	act(x, y);
	            }
	            
            }
            public void mousePressed(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {  
            }
            public void mouseEntered(MouseEvent e) {   
            }
            public void mouseClicked(MouseEvent e) {
            }
        });	
		
		while(true){
			
			if(server == null && turn){
				//System.out.println("your turn");
				String[] read = client.read().split(" ");
				int x = Integer.parseInt(read[0]);
				int y = Integer.parseInt(read[1]);
				//System.out.println(read);
				act(x, y);
			}
			
			else if(client == null && !turn){
				//System.out.println("your turn");
				String[] read = server.read().split(" ");
				int x = Integer.parseInt(read[0]);
				int y = Integer.parseInt(read[1]);
				//System.out.println(read);
				act(x, y);
			}
		}
	}
	
	public void act(int x, int y){
		 if(x >=0 && y>=0 && x<width && y<length){
	            if(!selected){
			            if(tiles[x][y] != null && tiles[x][y].getTeam() == turn&& !tiles[x][y].isExhausted()){
			            	//System.out.println(tiles[x][y]);
			            	selected = true;
			            	currUnit = tiles[x][y];
			            	
			            	canMove = showMove(currUnit, this.getGraphics());
			            }
		        }
	            else if(selected && !moved && canMove[x+1][y+1]>0){
	            	move(currUnit, x, y);
	            	moved = true;
	            	
	            	attack = enemyInRange(currUnit, this.getGraphics());
	            	if(!attack){
	            		currUnit.exhaust();
	            		reset(this.getGraphics());
	            		
		            	moved = false;
		            	selected = false;
		            	if(turnOver())
		            	turn = !turn;
	            	}
	            }
	            else if(selected && moved && attack && canAttack[x][y] && tiles[x][y]!=null && ((tiles[x][y].getTeam()!= currUnit.getTeam()&&!(currUnit instanceof Healer)) || (currUnit instanceof Healer && tiles[x][y].getTeam() == currUnit.getTeam()))){
	            	currUnit.attack(tiles[x][y]);
	            	check();
	            	currUnit.exhaust();
	            	reset(this.getGraphics());
	            	
	            	moved = false;
	            	selected = false;
	            	if(turnOver())
	            		turn = !turn;
	            }
	            else if(currUnit.getX() == x && currUnit.getY() == y){
	            	currUnit.exhaust();
	            	reset(this.getGraphics());
	            	
	            	moved = false;
	            	selected = false;
	            	if(turnOver())
	            		turn = !turn;
	            }
         } 
	}
	
	public void add(Unit u, int x, int y, boolean team){
		tiles[x][y] = u;
		u.setX(x);
		u.setY(y);
		u.setTeam(team);
		(team?Blue:Purple).add(u);
	//	System.out.println(u instanceof Buff);
	}
	
	private void move(Unit u, int x, int y){
		
		tiles[u.getX()][u.getY()] = null;
		tiles[x][y] = u;
		u.setX(x);
		u.setY(y);
		reset(this.getGraphics());
		
	}
	private int[][] showMove(Unit u, Graphics g){
		for(int[] b : canMove)
			Arrays.fill(b, 0);
		g.setColor(Colors.lightgreen);
		canMove[u.getX()+1][u.getY()+1] = u.getMovement()+1;
		for(int i = 0; i <= u.getMovement(); i++){
			for(int x = 1; x <= width; x++){
				for(int y = 1; y <= length; y++){
					if(tiles[x-1][y-1] == null&& canMove[x][y] ==0){
						canMove[x][y] = max(canMove[x-1][y]-1, 
										 	canMove[x+1][y]-1,
											canMove[x][y+1]-1,
											canMove[x][y-1]-1, 0);
						if(canMove[x][y]>0 && ((turn && client==null) || (!turn && server == null)))
						g.fillRect(scale*(x)+1, scale*(length-y+1)+1, scale-1, scale-1);
					}
				}
			}
		}
		
		return canMove;
	}
	
	private boolean enemyInRange(Unit u, Graphics g){
		for(boolean[] b : canAttack)
			Arrays.fill(b, false);
				
		boolean inRange = false;
		g.setColor(Colors.red);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < length; y++){
				if(Math.abs(x - u.getX()) + Math.abs(y - u.getY()) <= u.getRange()&& (u.getY() !=y || u.getX()!=x)){
					if(tiles[x][y]!=null && ((tiles[x][y].getTeam()!= currUnit.getTeam()&&!(u instanceof Healer)) || (u instanceof Healer && tiles[x][y].getTeam() == u.getTeam() && tiles[x][y].getHealth()<tiles[x][y].getMaxHealth()))){
						canAttack[x][y] = true;
					if(((turn && client==null) || (!turn && server == null))){
						
						g.drawRect(scale*(x+1)+1, scale*(length-y)+1, scale-1, scale-1);
						g.drawRect(scale*(x+1)+2, scale*(length-y)+2, scale-3, scale-3);
					
					}
					inRange = true;
					}
				}
			}
		}
		return inRange;
	}
	
	private int max(int... nums){
		int ret = nums[0];
		for(int i: nums){
			ret = Math.max(i, ret);
		}
		return ret;
	}
	
	private void check(){
		for(int x = 0; x<width; x++){
			for(int y = 0; y<length; y++)
				if(tiles[x][y]!=null && tiles[x][y].getHealth() <= 0){
					(tiles[x][y].getTeam()?Blue:Purple).remove(tiles[x][y]);
					tiles[x][y]= null;
				}
				
		}
	}
	
	private boolean turnOver(){
		ArrayList<Unit> toCheck = turn?Blue:Purple;
		boolean over = true;
		for(Unit u : toCheck)
			over = over && u.isExhausted();
		
		ArrayList<Unit> toRefresh = turn? Purple:Blue;
		if(over)
			for(Unit u : toRefresh)
				u.refresh();
					
		reset(this.getGraphics());
		return over;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Colors.lightgrey);
		g.fillRect(scale,scale, length*scale , width*scale);
                BufferedImage field = null;
                BufferedImage sword = null;
                BufferedImage shield = null;
                try {
                    field = ImageIO.read(new File("board.png"));
                    sword = ImageIO.read(new File("sword.png"));
                    //shield = ImageIO.read(new File("shield.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                g.drawImage(field, scale,scale, length*scale , width*scale, board);
		
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < length; y++){
				if(tiles[x][y] != null){
					g.setColor(tiles[x][y].isExhausted()?
							(tiles[x][y].getTeam()?Colors.blue:Colors.purple):
							(tiles[x][y].getTeam()?Colors.lightblue:Colors.lightpurple));
					g.fillRect(scale*(x+1), scale*(length-y), scale, scale);
					
					
					if(tiles[x][y] instanceof Buff){
						//g.setColor(((Buff)tiles[x][y]).getColor());
						//g.fillRoundRect(scale*(x+1)+scale/2, scale*(length-y)+2*scale/5, scale/5, scale/5, scale/10, scale/10);
                                                g.drawImage(((Buff)tiles[x][y]).getIcon(), scale*(x+1), scale*(length-y), scale, scale, board);
                                        }
					
					if(tiles[x][y] instanceof Healer){
//						g.setColor(Colors.red);
//						g.fillRect(scale*(x+1)+(int)(3.8*scale/7), scale*(length-y)+11*scale/30, scale/10, 4*scale/13);
//						g.fillRect(scale*(x+1)+scale/7 + 2*scale/7, scale*(length-y)+6*scale/13, 2* scale/7, scale/10);
                                                g.drawImage(((Healer)tiles[x][y]).getIcon(), scale*(x+1), scale*(length-y), scale, scale, board);
                                        }
					
					g.setColor(Colors.green);
					g.fillRect(scale*(x+1), scale*(length-y),  scale/7, scale);
					
					g.setColor(Colors.black);
					
					double percent = 1-(double)tiles[x][y].getHealth()/tiles[x][y].getMaxHealth();
					g.fillRect(scale*(x+1), scale*(length-y), scale/7, (int)(scale * percent));
				}
			}
		}
		
		g.setColor(Colors.black);
		for(int i = 1; i <=width+1; i++){
			g.drawLine(scale,scale *i, scale*(width+1), scale*i);
			g.drawLine(scale* i,scale, scale *i, scale*(length+1));
		}
	}
	
	public void reset(Graphics g){
		check();
		g.setColor(Colors.lightgrey);
		g.fillRect(scale,scale, length*scale , width*scale);
		BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("board.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                g.drawImage(img, scale,scale, length*scale , width*scale, board);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < length; y++){
				if(tiles[x][y] != null){
					g.setColor(tiles[x][y].isExhausted()?
							(tiles[x][y].getTeam()?Colors.blue:Colors.purple):
							(tiles[x][y].getTeam()?Colors.lightblue:Colors.lightpurple));
					g.fillRect(scale*(x+1), scale*(length-y), scale, scale);
					
					
					if(tiles[x][y] instanceof Buff){
						g.drawImage(tiles[x][y].getIcon(), scale*(x+1), scale*(length-y), scale, scale, board);
                                        }
					
					if(tiles[x][y] instanceof Healer){
//						g.setColor(Colors.red);
//						g.fillRect(scale*(x+1)+(int)(3.8*scale/7), scale*(length-y)+11*scale/30, scale/10, 4*scale/13);
//						g.fillRect(scale*(x+1)+scale/7 + 2*scale/7, scale*(length-y)+6*scale/13, 2* scale/7, scale/10);
                                                g.drawImage(tiles[x][y].getIcon(), scale*(x+1), scale*(length-y), scale, scale, board);
                                        }
					
					g.setColor(Colors.green);
					g.fillRect(scale*(x+1), scale*(length-y),  scale/7, scale);
					
					g.setColor(Colors.black);
					
					double percent = 1-(double)tiles[x][y].getHealth()/tiles[x][y].getMaxHealth();
					g.fillRect(scale*(x+1), scale*(length-y), scale/7, (int)(scale * percent));
				}
			}
		}
		g.setColor(Colors.black);
		for(int i = 1; i <=width+1; i++){
			g.drawLine(scale,scale *i, scale*(width+1), scale*i);
			g.drawLine(scale* i,scale, scale *i, scale*(length+1));
		}
	}

}
