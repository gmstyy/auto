package model;


public class Pixel {

	private int x,y;
	private int rgb;
	
	public Pixel(int x, int y,int rgb) {
		super();
		this.x = x;
		this.y = y;
		this.rgb = rgb;
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

	
	public int getRgb() {
		return rgb;
	}

	
	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Pixel)){
			return false;
		}
		Pixel p=(Pixel)obj;
		return x==p.x?y==p.y:false;
	}
	@Override
	public int hashCode() {
		return (new Integer(x)+"|"+new Integer(y)).hashCode();
	}
}
