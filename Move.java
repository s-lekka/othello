//LEKKA STAVROULA p3090108

public class Move {
	public static final int X = 1; //dark disks
    public static final int O = -1;// light disks

	private int row; 
	private int col;
	private int value; //move value
	private int color; //disk color

	public Move() {
		row = -1;
		col = -1;
		value = 0;
		color = 0;
	}

	public Move(int row, int col) {
		this.row = row;
		this.col = col;
		this.value = -1;
		this.color = 0;
	}

	public Move(int value) {
		this.row = -1;
		this.col = -1;
		this.color = 0;
		this.value = value;
	}

	public Move(Main.Disk disk) {
		this.row = -1;
		this.col = -1;
		if (disk == Main.Disk.DARK) this.color = X;
		else this.color = O;
		this.value = 0;
	}

	public Move(int row, int col, int value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	public Move(int row, int col, Main.Disk clr) {
		this.row = row;
		this.col = col;
		if (clr == Main.Disk.DARK) this.color = X;
		else this.color = O;
		this.value = 0;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getValue() {
		return value;
	}

	public int getColor() {
		return color;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setColor(Main.Disk color) {
		if (color == Main.Disk.DARK) this.color = X;
		else this.color = O;
	}
}
