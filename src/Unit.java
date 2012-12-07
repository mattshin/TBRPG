
public class Unit {
	private String name;
	private int maxHealth;
	private int health;
	private int strength;
	private int defense;
	private int movement;
	private int x; 
	private int y;
	private int range = 1;
	private boolean team;
	private boolean exhausted;
	public Unit(){
		name = "TEST";
		health = strength = movement = 1;
		defense = 0;
	}
	public Unit(String n, int h, int str, int def, int mov){
		setName(n);
		setHealth(h);
		setMaxHealth(h);
		setStrength(str);
		setDefense(def);
		setMovement(mov);
	}
	public void attack(Unit u){
		if(this.strength >0)
			u.setHealth(u.getHealth() - (this.strength - u.getDefense()));
		else
			u.setHealth(u.getHealth() - this.strength);
		
		if(u.getHealth()>u.getMaxHealth())
			u.setHealth(u.getMaxHealth());
		if(u.getHealth()<=0)
			u.onDeathBy(this);
	}
	public void onDeathBy(Unit u){
		
	}
	
	public void exhaust() {
		exhausted = true;
	}
	public void refresh() {
		exhausted = false;
	}
	
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMovement() {
		return movement;
	}
	public void setMovement(int movement) {
		this.movement = movement;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean getTeam() {
		return team;
	}
	public void setTeam(boolean team) {
		this.team = team;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public boolean isExhausted() {
		return exhausted;
	}

	
	
}
