
package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import service.RGBUtil;

public class Recognition extends Neure {

	private RecMap okMap;
	private RecMap passMap;
	private String value = "p";
	private double threshold = 0.65;
	private double score = 0.0;
	private Map<Neure, Boolean> history;

	public Recognition(BufferedImage model,String name) {
		value=name;
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
				passSk.addPix(new Pixel(i, j, Color.GRAY.getRGB()));
			}
		}
		Set<Pixel> okSet=okSk.trim();
		Set<Pixel> passSet=passSk.trim(okSk.getMinWitdh(),okSk.getMinHeight(),okSk.getMaxWidth(),okSk.getMaxHeight());
		okMap = new RecMap(okSk.getTrimWidth(),okSk.getTrimHeight());
		passMap = new RecMap(passSk.getTrimWidth(),passSk.getTrimHeight());
		for(Pixel pix:okSet){
			okMap.put(pix, 1.0);
		}
		for(Pixel pix:passSet){
			passMap.put(pix, 0.2);
		}
		RGBUtil.genImg("ok-"+value, okMap.keySet(), okMap.getWidth(), okMap.height);
		RGBUtil.genImg("pass-"+value, passMap.keySet(), passMap.width, passMap.height);
	}

	@Override
	public void stimulated(Neure neure, boolean flag) {
		Skeleton sk = (Skeleton) neure;
		double okRate = getRate(sk.getTrimWidth(), sk.getTrimHeight(), okMap.getWidth(), okMap.getHeight());
		double passRate = getRate(sk.getTrimWidth(), sk.getTrimHeight(), passMap.getWidth(), passMap.getHeight());
		List<Pixel> pixs = new ArrayList<>();
		double okd=0.0,passd=0.0;
		Set<Pixel> trimSet=sk.getTrimSet();
		for (Pixel pix : trimSet) {
				okd+= getMatchScore(pix, okMap, sk) * okRate /okMap.size() ;
				passd+=getMatchScore(pix, passMap, sk)*passRate / passMap.size();
		}
		score=okd-passd;
		System.out.println(Integer.toHexString(sk.getRgb()) + " " + okd+" "+passd);
//		System.out.println(Integer.toHexString(sk.getRgb()) + " " + score);
		 this.complete();
		// history.put(neure, flag);
		// if(history.size()>=okMap.size()+passMap.size()){
		// this.complete();
		// }
	}
	private double getMatchScore(Pixel pix,RecMap recMap,Skeleton sk){
		int x = new Double(pix.getX() *new Double(recMap.getWidth())/sk.getTrimWidth()).intValue();
		int y = new Double(pix.getY() * new Double(recMap.getHeight())/sk.getTrimHeight()).intValue();
		Pixel newPix = new Pixel(x, y, pix.getRgb());
		if (recMap.containsKey(newPix)) {
			return recMap.get(newPix);
		}
		return 0;
	}
	private double getRate(int width,int height,int recWidth,int recHeight){
//		return Math.sqrt(new Double(recWidth*recWidth+recHeight*recHeight)/new Double(width*width+height*height));
		return new Double(recWidth*recHeight)/(width*height);
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
		private int width=0,height=0;
		
		public RecMap(int width, int height) {
			this.width = width>0?width:1;
			this.height = height>0?height:1;
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
