
package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import service.RGBUtil;

public class Recognition extends Neure {

	private RecMap okMap;
	private RecMap passMap;
	private String value = "p";
	private double threshold = 0.8;
	private double score = 0.0;
	private Map<Neure, Boolean> history;

	public Recognition(BufferedImage model) {
		int height = model.getHeight();
		int width = model.getWidth();
		
		Skeleton okSk = new Skeleton(1, width, height);
		Skeleton passSk = new Skeleton(1, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = model.getRGB(i, j) & 0xFFFFFF;
				if (rgb != 0xFFFFFF) {
					okSk.addPix(new Pixel(i, j, rgb));
					continue;
				}
				passSk.addPix(new Pixel(i, j, rgb));
			}
		}
		Set<Pixel> okSet=okSk.getTrimSet();
		Set<Pixel> passSet=passSk.getTrimSet();
		okMap = new RecMap(okSk.getTrimWidth(),okSk.getTrimHeight());
		passMap = new RecMap(passSk.getTrimWidth(),passSk.getTrimHeight());
		for(Pixel pix:okSet){
			okMap.put(pix, 1.0);
		}
		for(Pixel pix:passSet){
			passMap.put(pix, 1.0);
		}
		RGBUtil.genImg("ok", okMap.keySet(), okMap.getWidth(), okMap.height);
		RGBUtil.genImg("pass", passMap.keySet(), passMap.width, passMap.height);
//		int[][] imagePix = new int[width][height];
//		int minWitdh = width, maxWidth = 0, minHeight = height, maxheight = 0;
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < height; j++) {
//				int rgb = model.getRGB(i, j) & 0xFFFFFF;
//				if (rgb != 0xFFFFFF) {
//					imagePix[i][j] = rgb;
//					minWitdh = minWitdh > i ? i : minWitdh;
//					maxWidth = maxWidth > i ? maxWidth : i;
//					minHeight = minHeight > j ? j : minHeight;
//					maxheight = maxheight > j ? maxheight : j;
//				} else {
//					imagePix[i][j] = 0;
//				}
//			}
//		}
//		this.width = maxWidth - minWitdh + 1;
//		this.height = maxheight - minHeight + 1;
//		for (int i = minWitdh; i <= maxWidth; i++) {
//			for (int j = minHeight; j <= maxheight; j++) {
//				if (imagePix[i][j] != 0) {
//					okMap.put(new Pixel(i - minWitdh, j - minHeight, imagePix[i][j]), 1.0);
//					continue;
//				}
//				passMap.put(new Pixel(i - minWitdh, j - minHeight, imagePix[i][j]), 1.0);
//			}
//		}
		
	}

	@Override
	public void stimulated(Neure neure, boolean flag) {
		Skeleton sk = (Skeleton) neure;
		double okRate = getRate(sk.getTrimWidth(), sk.getTrimHeight(), okMap.getWidth(), okMap.getHeight());
		double passRate = getRate(sk.getTrimWidth(), sk.getTrimHeight(), passMap.getWidth(), passMap.getHeight());
		List<Pixel> pixs = new ArrayList<>();
		for (Pixel pix : sk.getTrimSet()) {
				score += getMatchScore(pix, okMap, sk) * okRate / okMap.size();
				score -= getMatchScore(pix, passMap, sk)*passRate / passMap.size();
		}
		System.out.println(Integer.toHexString(sk.getRgb()) + " " + score);
		// this.complete();
		// history.put(neure, flag);
		// if(history.size()>=okMap.size()+passMap.size()){
		// this.complete();
		// }
	}
	private double getMatchScore(Pixel pix,RecMap recMap,Skeleton sk){
		int x = new Double(pix.getX() *new Double(recMap.getWidth()/sk.getTrimWidth())).intValue();
		int y = new Double(pix.getY() * new Double(recMap.getHeight()/sk.getTrimHeight())).intValue();
		Pixel newPix = new Pixel(x, y, pix.getRgb());
		if (recMap.containsKey(newPix)) {
			return recMap.get(newPix);
		}
		return 0;
	}
	private double getRate(int width,int height,int recWidth,int recHeight){
		return Math.sqrt(new Double(recWidth*recWidth+recHeight*recHeight)/new Double(width*width+height*height));
	}
	@Override
	public void complete() {
		if (score >= threshold) {
			System.out.println(value);
		}
		System.out.println(score);
	}

	@Override
	public void feedback(Neure neure) {
		// TODO Auto-generated method stub
	}
	private class RecMap extends HashMap<Pixel, Double>{
		private int width,height;
		
		public RecMap(int width, int height) {
			this.width = width;
			this.height = height;
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
		
	}
}
