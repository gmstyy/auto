
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.RGBUtil;

public class Skeleton extends Neure {

	private List<Recognition> resultList = new ArrayList<>();
	protected int rgb;
	protected String name;
	protected String value;
	private Double rate;
	protected int width, height, trimWidth, trimHeight;
	protected Pixel[][] pixArr;
	private Set<Pixel> pixSet;
	private Set<Pixel> trimSet;
	private int minWitdh, maxWidth, minHeight, maxHeight;

	public Skeleton(int rgb, int width, int height) {
		this.rgb = rgb;
		this.name = "sk" + Integer.toHexString(rgb);
		this.width = width;
		this.height = height;
		this.minWitdh = width;
		this.maxWidth = 0;
		this.minHeight = height;
		this.maxHeight = 0;
		this.pixArr = new Pixel[width][height];
		this.pixSet = new HashSet<>();
	}

	public Skeleton(String name, int width, int height) {
		this(0, width, height);
		this.name = name;
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
	public void stimulated(Neure neure, boolean enhance) {
		trim();
		RGBUtil.genImg(name, trimSet, width, height);
		for (Neure n : frontSet) {
			n.stimulated(this, true);
		}
		Collections.sort(resultList, new Comparator<Recognition>() {

			@Override
			public int compare(Recognition o1, Recognition o2) {
				return new Double(o2.getScore()).compareTo(o1.getScore());
			}
		});
		System.out.println(name + " " + (resultList.size() > 0 ? resultList.get(0).getValue() + "  " + resultList.get(0).getScore() : "没有"));
	}

	public Set<Pixel> trim(int x, int y, int maxX, int maxY) {
		trimSet = new HashSet<>();
		int minX = minWitdh > x ? minWitdh : x;
		int minY = minHeight > y ? minHeight : y;
		maxX = maxWidth > maxX ? maxX : maxWidth;
		maxY = maxHeight > maxY ? maxY : maxHeight;
		this.trimWidth = maxX - minX + 1;
		this.trimHeight = maxY - minY + 1;
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				Pixel pix = pixArr[i][j];
				if (pix != null) {
					trimSet.add(new Pixel(pix.getX() - minX, pix.getY() - minY, pix.getRgb()));
				}
			}
		}
		return trimSet;
	}

	public Set<Pixel> trim() {
		return this.trim(minWitdh, minHeight, maxWidth, maxHeight);
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
		return trimSet;
	}

	@Override
	public void feedback(Neure neure, boolean enhance) {
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

	public List<Recognition> getResultList() {
		return resultList;
	}

	public void setResultList(List<Recognition> resultList) {
		this.resultList = resultList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
