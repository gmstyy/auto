
package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import model.Pixel;

public class RGBUtil {

	public static String imageDir = "C:/Users/Administrator/Desktop/img";

	public static BufferedImage rotateImg(BufferedImage src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage res = null;
		// res = new BufferedImage(rect_des.width, rect_des.height,
		// BufferedImage.TYPE_INT_RGB);
		res = new BufferedImage(src_width, src_height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		g2.setBackground(new Color(0xFFFFFF));
		g2.clearRect(0, 0, src_width, src_height);
		g2.setColor(Color.BLACK);
		// transform
		// g2.translate((rect_des.width - src_width) / 2,
		// (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
		g2.drawImage(src, null, null);
		int res_width = res.getWidth(null);
		int res_height = res.getHeight(null);
		return res;
	}

	public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}
		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}

	public static void genImg(String name, BufferedImage bi) {
		File file = new File(imageDir + "/" + name + ".png");
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void genImg(String path, String name, Collection<Pixel> pixels, int width, int height) {
		File file = new File(path + "/" + name + ".png");
		if (!file.exists()) {
			file.mkdirs();
		}
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(new Color(0xFFFFFF));
		g2.clearRect(0, 0, width, height);
		for (Pixel p : pixels) {
			g2.setPaint(new Color(p.getRgb()));
			g2.drawLine(p.getX(), p.getY(),p.getX(), p.getY());
		}
		try {
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void genImg(String name, Collection<Pixel> pixels, int width, int height) {
		genImg(imageDir, name, pixels, width, height);
	}

	public static double getDistance(int rbg1, int rbg2) {
		LAB lab1 = new LAB(rbg1);
		LAB lab2 = new LAB(rbg2);
		double deltaL = lab1.l - lab2.l;
		double deltaA = lab1.a - lab2.a;
		double deltaB = lab1.b - lab2.b;
		double deltaE = Math.pow((Math.pow(deltaL, 2) + Math.pow(deltaA, 2) + Math.pow(deltaB, 2)), 0.5);
		return deltaE;
	}

	public static class RGB {

		public int r, g, b, rgb;

		public RGB(int rgb) {
			this.rgb = rgb;
			this.r = rgb >> 16;
			this.g = (rgb & 0x00FF00) >> 8;
			this.b = rgb & 0x0000FF;
		}
	}

	public static class XYZ {

		public double x, y, z;

		public XYZ(RGB rgb) {
			if (rgb.rgb == -16777216) {
				x = 0;
				y = 0;
				z = 0;
			}
			x = (0.490 * rgb.r + 0.310 * rgb.g + 0.200 * rgb.b) / (0.667 * rgb.r + 1.132 * rgb.g + 1.200 * rgb.b);
			y = (0.117 * rgb.r + 0.812 * rgb.g + 0.010 * rgb.b) / (0.667 * rgb.r + 1.132 * rgb.g + 1.200 * rgb.b);
			z = (0.000 * rgb.r + 0.010 * rgb.g + 0.990 * rgb.b) / (0.667 * rgb.r + 1.132 * rgb.g + 1.200 * rgb.b);
		}
	}

	public static class LAB {

		public double l, a, b;

		public LAB(int rgb) {
			RGB rgb1 = new RGB(rgb);
			XYZ xyz = new XYZ(rgb1);
			init(xyz);
		}

		public LAB(XYZ xyz) {
			init(xyz);
		}

		private void init(XYZ xyz) {
			double x = xyz.x / 95.047;
			double y = xyz.y / 100.000;
			double z = xyz.z / 108.883;
			// Adjust the x, y and z value. (Optional)
			if (x > 0.008856)
				x = Math.pow(x, 1.0 / 3.0);
			else
				x = (7.787 * x) + (16 / 116);
			if (y > 0.008856)
				y = Math.pow(y, 1.0 / 3.0);
			else
				y = (7.787 * y) + (16 / 116);
			if (z > 0.008856)
				z = Math.pow(z, 1.0 / 3.0);
			else
				z = (7.787 * z) + (16 / 116);
			// Calculate l,a and b from x,y and z.
			l = 116 * Math.pow(y, 1.0 / 3.0) - 16;
			a = 500 * (Math.pow(x, 1.0 / 3.0) - Math.pow(y, 1.0 / 3.0));
			b = 200 * (Math.pow(y, 1.0 / 3.0) - Math.pow(z, 1.0 / 3.0));
		}
	}
	// private static int getMixRbg(int rgb1, int rgb2, Double rate1, Double
	// rate2) {
	// RGB rgb11=new RGB(rgb1);
	// RGB rgb22=new RGB(rgb2);
	// int r = new Double((rgb11.r * rate1 + getR(rgb2) * rate2) / (rate1 +
	// rate2)).intValue();
	// int g = new Double((getG(rgb1) * rate1 + getG(rgb2) * rate2) / (rate1 +
	// rate2)).intValue();
	// int b = new Double((getB(rgb1) * rate1 + getB(rgb2) * rate2) / (rate1 +
	// rate2)).intValue();
	// return (r << 16) + (g << 8) + b;
	// }
	//
	// private static int getDistance1(int rgb1, int rgb2) {
	// int r = rgb1 >> 16 - rgb2 >> 16;
	// int g = (rgb1 & 0x00FF00) >> 8 - (rgb2 & 0x00FF00) >> 8;
	// int b = rgb1 & 0x0000FF - rgb2 & 0x0000FF;
	// return r * r + g * g + b * b;
	// }
}
