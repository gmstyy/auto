
package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import service.RGBUtil;

public class Skeleton extends Neure {

	private int rgb;
	private Double rate;
	private int width, height,trimWidth,trimHeight;
	private Pixel[][] pixArr;
	private Set<Pixel> pixSet;
	private Set<Pixel> trimSet;
	private int minWitdh, maxWidth, minHeight, maxHeight;

	public Skeleton(int rgb, int width, int height) {
		this.rgb = rgb;
		this.width = width;
		this.height = height;
		this.minWitdh = width;
		this.maxWidth = 0;
		this.minHeight = height;
		this.maxHeight = 0;
		this.pixArr = new Pixel[width][height];
		this.pixSet = new HashSet<>();
	}

	public void addPix(Pixel pix) {
		pixArr[pix.getX()][pix.getY()] = pix;
		pixSet.add(pix);
		minWitdh = minWitdh > pix.getX() ? pix.getX() : minWitdh;
		maxWidth = maxWidth > pix.getX() ? maxWidth : pix.getX();
		minHeight = minHeight > pix.getY() ? pix.getY() : minHeight;
		maxHeight = maxHeight > pix.getY() ? maxHeight : pix.getY();
	}

	public void addAllPix(Collection<Pixel> pixs) {
		for (Pixel pix : pixs) {
			addPix(pix);
		}
		pixSet.addAll(pixs);
	}

	public Set<Pixel> getPixSet() {
		return pixSet;
	}

	public void setPixSet(Set<Pixel> pixSet) {
		this.pixSet = pixSet;
	}

	public Double getRate() {
		rate = new Double(pixSet.size()) * 100 / new Double(width * height);
		return rate;
	}

	@Override
	public void stimulated(Neure neure, boolean flag) {
		trim();
		RGBUtil.genImg("111" + Integer.toHexString(this.rgb), trimSet, trimWidth,trimHeight);
		for (Neure n : frontSet) {
			n.stimulated(this, true);
		}
	}
	private void trim(){
		trimSet = new HashSet<>();
		this.trimWidth=maxWidth - minWitdh+1;
		this.trimHeight=maxHeight - minHeight+1;
		for (int i = minWitdh; i <= maxWidth; i++) {
			for (int j = minHeight; j <= maxHeight; j++) {
				Pixel pix = pixArr[i][j];
				if (pix != null) {
					trimSet.add(new Pixel(pix.getX() - minWitdh, pix.getY() - minHeight, pix.getRgb()));
				}
			}
		}
	}
	
	public int getTrimWidth() {
		return trimWidth;
	}

	
	public void setTrimWidth(int trimWidth) {
		this.trimWidth = trimWidth;
	}

	
	public int getTrimHeight() {
		return trimHeight;
	}

	
	public void setTrimHeight(int trimHeight) {
		this.trimHeight = trimHeight;
	}

	public Set<Pixel> getTrimSet() {
		trim();
		return trimSet;
	}
	@Override
	public void feedback(Neure neure) {
		// TODO Auto-generated method stub
	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Pixel[][] getPixArr() {
		return pixArr;
	}

	public void setPixArr(Pixel[][] pixArr) {
		this.pixArr = pixArr;
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMinWitdh() {
		return minWitdh;
	}

	public void setMinWitdh(int minWitdh) {
		this.minWitdh = minWitdh;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	

	public void setTrimSet(Set<Pixel> trimSet) {
		this.trimSet = trimSet;
	}
}
