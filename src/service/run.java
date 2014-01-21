
package service;

import static utils.FileUtil.delFolder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import model.Neure;
import model.Pixel;
import model.Recognition;
import model.Result;
import model.TotalSkeleton;
import utils.FileUtil;
import utils.RGBUtil;

public class run {
	private static boolean train=false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			delFolder(RGBUtil.imageDir);
//			Set<Neure> recNeure = revertRec();
			Set<Neure> recNeure = initRec1();
			File file = new File("C:\\Users\\Administrator\\Desktop\\img1\\p.png");
			BufferedImage bufImg = ImageIO.read(file);
			int height = bufImg.getHeight();
			int width = bufImg.getWidth();
			Map<Integer, TotalSkeleton> colorMap = new TreeMap<>();
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					Integer rgb = bufImg.getRGB(i, j) & 0xFFFFFF;
					if (colorMap.containsKey(rgb)) {
						colorMap.get(rgb).addPix(new Pixel(i, j, rgb));
						continue;
					}
					TotalSkeleton newSk = new TotalSkeleton(rgb, width, height);
					if(train){
						newSk.setValue(file.getName().split(".")[0]);
					}
					newSk.setFrontSet(recNeure);
					newSk.addPix(new Pixel(i, j, rgb));
					colorMap.put(rgb, newSk);
					// System.out.println(bufImg.getRGB(i, j) & 0xFFFFFF);
				}
			}
			List<TotalSkeleton> skList = new ArrayList<>();
			for (TotalSkeleton sk : colorMap.values()) {
				skList.add(sk);
			}
			Collections.sort(skList, new Comparator<TotalSkeleton>() {

				@Override
				public int compare(TotalSkeleton o1, TotalSkeleton o2) {
					return o2.getRate().compareTo(o1.getRate());
				}
			});
			Map<Integer, TotalSkeleton> newMap = new LinkedHashMap<>();
//			out: for (int i=0;i<skList.size();i++) {
//				TotalSkeleton sk1=skList.get(i);
//				if (sk1.getRate() > 50) {
//					continue;
//				}
//				for (int j=i+1;j<skList.size();j++) {
//					TotalSkeleton sk2=skList.get(j);
//					double distance = RGBUtil.getDistance(sk1.getRgb(), sk2.getRgb());
//					
//				}
//				for (Integer color : newMap.keySet()) {
//					double distance = RGBUtil.getDistance(sk1.getRgb(), color);
//					if (distance <= 2) {
//						TotalSkeleton tmp = newMap.get(color);
//						tmp.addAllPix(sk1.getPixSet());
//						continue out;
//					}
//				}
//				newMap.put(sk1.getRgb(), sk1);
//			}
			out: for (TotalSkeleton sk1 : skList) {
				if (sk1.getRate() > 65) {
					continue;
				}
				for (Integer color : newMap.keySet()) {
					double distance = RGBUtil.getDistance(sk1.getRgb(), color);
					if (distance <= 4) {
						TotalSkeleton tmp = newMap.get(color);
						tmp.addAllPix(sk1.getPixSet());
						continue out;
					}
				}
				newMap.put(sk1.getRgb(), sk1);
			}
			for (TotalSkeleton list : newMap.values()) {
				RGBUtil.genImg(Integer.toHexString(list.getRgb()), list.getPixSet(), width, height);
				System.out.println(Integer.toHexString(list.getRgb()) + " " + list.getRate());
				list.stimulated(null, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static Set<Neure> initRec1() throws IOException {
		Set<Neure> set=new HashSet<Neure>();
		File trainFile=new File(FileUtil.defaultPath+"\\train");
		Result rt=new Result();
		rt.setTrain(true);
		Set<Neure> rtSet=new HashSet<>();
		rtSet.add(rt);
		for(File file:trainFile.listFiles()){
			if(file.getName().indexOf(".txt")>-1){
				Recognition r=new Recognition(file);
				r.setFrontSet(rtSet);
				set.add(r);
			}
		}
		return set;
	}
	private static Set<Neure> revertRec() throws IOException {
		Set<Neure> set = getFontNeure("a", new HashSet<Neure>());
		getFontNeure1("a", set);
		getFontNeure1("b", set);
		getFontNeure1("c", set);
		getFontNeure1("d", set);
		getFontNeure1("e", set);
		getFontNeure1("f", set);
		getFontNeure1("g", set);
		getFontNeure1("h", set);
		getFontNeure1("i", set);
		getFontNeure1("j", set);
		getFontNeure1("k", set);
		getFontNeure1("l", set);
		getFontNeure1("m", set);
		getFontNeure1("n", set);
		getFontNeure1("o", set);
		getFontNeure1("p", set);
		getFontNeure1("q", set);
		getFontNeure1("r", set);
		getFontNeure1("s", set);
		getFontNeure1("t", set);
		getFontNeure1("u", set);
		getFontNeure1("v", set);
		getFontNeure1("w", set);
		getFontNeure1("x", set);
		getFontNeure1("y", set);
		getFontNeure1("z", set);
		getFontNeure1("1", set);
		getFontNeure1("2", set);
		getFontNeure1("3", set);
		getFontNeure1("4", set);
		getFontNeure1("5", set);
		getFontNeure1("6", set);
		getFontNeure1("7", set);
		getFontNeure1("8", set);
		getFontNeure1("9", set);
		getFontNeure1("0", set);
		Result rt=new Result();
		rt.setTrain(train);
		Set<Neure> rtSet=new HashSet<>();
		rtSet.add(rt);
		rt.setTrain(train);
		for(Neure neure:set){
			neure.setFrontSet(rtSet);
			neure.store();
			neure.load();
		}
		return set;
	}
	
	private static Set<Neure> getFontNeure1(String str, Set<Neure> set) {
		set = getFontNeure(str.toLowerCase(), set);
		set = getFontNeure(str.toUpperCase(), set, "font-" + str + "-up");
		return set;
	}

	private static Set<Neure> getFontNeure(String str, Set<Neure> set, String... name) {
		BufferedImage bi = new BufferedImage(70, 80, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(new Color(0xFFFFFF));
		g2.clearRect(0, 0, 100, 100);
		// 设置大字体
		Font font = new Font("Calibri", Font.ITALIC | Font.BOLD, 60);
		g2.setFont(font);
		g2.setColor(Color.BLACK);
		g2.drawString(str, 10, 45);
		String imgName = name.length > 0 ? name[0] : "font-" + str;
		RGBUtil.genImg(imgName, bi);
		set.add(new Recognition(bi, imgName,str));
		BufferedImage bi1 = RGBUtil.rotateImg(bi, 15);
		set.add(new Recognition(bi1, imgName + 1,str));
		BufferedImage bi2 = RGBUtil.rotateImg(bi, -30);
		set.add(new Recognition(bi2, imgName + 2,str));
		return set;
	}

	private void train() {
		
	}
}