
package service;

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
import model.TotalSkeleton;

public class run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			delFolder(RGBUtil.imageDir);
			Set<Neure> recNeure = initRec();
			File file = new File("C:\\Users\\Administrator\\Desktop\\img1\\m2.png");
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
			double maxDis=0.0;
			out: for (int i=0;i<skList.size();i++) {
				TotalSkeleton sk=skList.get(i);
				if (sk.getRate() > 50 || sk.getRate()<0.5) {
					continue;
				}
				for (int j=0;j<skList.size();j++) {
					if(i==j){
						continue;
					}
					TotalSkeleton sk1 =skList.get(j);
					double distance = RGBUtil.getDistance(sk.getRgb(), sk1.getRgb());
					maxDis=maxDis>distance?maxDis:distance;
				}
			}
			out: for (TotalSkeleton sk1 : skList) {
				if (sk1.getRate() > 50) {
					continue;
				}
				for (Integer color : newMap.keySet()) {
					double distance = RGBUtil.getDistance(sk1.getRgb(), color);
					if (distance <= 45) {
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

	private static Set<Neure> initRec() throws IOException {
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
		set.add(new Recognition(bi, imgName));
		BufferedImage bi1 = RGBUtil.rotateImg(bi, 15);
		set.add(new Recognition(bi1, imgName + 1));
		BufferedImage bi2 = RGBUtil.rotateImg(bi, -30);
		set.add(new Recognition(bi2, imgName + 2));
		return set;
	}

	private void train() {
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}