
public class RangedUnit extends Unit {
	private int range;
	public RangedUnit(String n, int h, int str, int def, int mov, int r) {
		super(n, h, str, def, mov);
		setRange(r);
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}

}
