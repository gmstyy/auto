
package service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import model.Neure;
import model.Pixel;
import model.Recognition;
import model.Skeleton;

public class run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			delFolder(RGBUtil.imageDir);
			File recFile = new File("C:\\Users\\Administrator\\Desktop\\p2.jpg");
			BufferedImage recImg= ImageIO.read(recFile);
			Recognition recNeure=new Recognition(recImg);
			
			File file = new File("C:\\Users\\Administrator\\Desktop\\p.png");
			BufferedImage bufImg= ImageIO.read(file);
			HashSet<Neure> recNuSet=new HashSet<>();
			recNuSet.add(recNeure);
			int height = bufImg.getHeight();
			int width = bufImg.getWidth();
			Map<Integer, Skeleton> colorMap = new TreeMap<>();
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					Integer rgb = bufImg.getRGB(i, j) & 0xFFFFFF;
					if (colorMap.containsKey(rgb)) {
						colorMap.get(rgb).addPix(new Pixel(i, j, rgb));
						continue;
					}
					Skeleton newSk=new Skeleton(rgb, width, height);
					newSk.setFrontSet(recNuSet);
					newSk.addPix(new Pixel(i, j, rgb));
					colorMap.put(rgb, newSk);
					// System.out.println(bufImg.getRGB(i, j) & 0xFFFFFF);
				}
			}
			
			List<Skeleton> skList = new ArrayList<>();
			for (Skeleton sk : colorMap.values()) {
				skList.add(sk);
			}
			Collections.sort(skList, new Comparator<Skeleton>() {

				@Override
				public int compare(Skeleton o1, Skeleton o2) {
					return o2.getRate().compareTo(o1.getRate());
				}
			});
			Map<Integer, Skeleton> newMap = new LinkedHashMap<>();
			out: for (Skeleton sk1 : skList) {
				if(sk1.getRate()>50){
					continue;
				}
				for (Integer color : newMap.keySet()) {
					double distance = RGBUtil.getDistance(sk1.getRgb(), color);
					if (distance <= 7) {
						Skeleton tmp=newMap.get(color);
						tmp.addAllPix(sk1.getPixSet());
						continue out;
					}
				}
				newMap.put(sk1.getRgb(), sk1);
			}
			for (Skeleton list : newMap.values()) {
				RGBUtil.genImg(Integer.toHexString(list.getRgb()),list.getPixSet(), width, height);
				System.out.println(Integer.toHexString(list.getRgb()) + " " + list.getRate());
				list.stimulated(null, true);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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