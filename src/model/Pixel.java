package model;



public class Pixel{

	private int x,y;
	private int rgb;
	
	public Pixel(int x, int y,int rgb) {
		super();
		this.x = x;
		this.y = y;
		this.rgb = rgb;
	}
	public Pixel(String str) {
		String[] arr=str.split(",");
		if(arr.length>1){
			this.x = Integer.parseInt(arr[0]);
			this.y = Integer.parseInt(arr[1]);
		}
		if(arr.length>2){
			this.rgb = Integer.parseInt(arr[2]);
		}
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
	@Override
	public String toString() {
		return x+","+y+","+rgb;
	}
}
