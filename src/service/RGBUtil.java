
package service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import model.Pixel;

public class RGBUtil {

	public static String imageDir = "C:/Users/Administrator/Desktop/img";

	public static BufferedImage rotateImg(BufferedImage image, int degree) throws IOException {
		BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
		gs.rotate(Math.toRadians(degree), image.getWidth() >> 1, image
			     .getHeight() >> 1); 
		gs.drawImage(rotatedImage, 0, 0, null);
		 return rotatedImage;
//		int iw = image.getWidth();// 原始图象的宽度
//		int ih = image.getHeight();// 原始图象的高度
//		int w = 0;
//		int h = 0;
//		int x = 0;
//		int y = 0;
//		degree = degree % 360;
//		if (degree < 0)
//			degree = 360 + degree;// 将角度转换到0-360度之间
//		double ang = Math.toRadians(degree);// 将角度转为弧度
//		/**
//		 * 确定旋转后的图象的高度和宽度
//		 */
//		if (degree == 180 || degree == 0 || degree == 360) {
//			w = iw;
//			h = ih;
//		} else if (degree == 90 || degree == 270) {
//			w = ih;
//			h = iw;
//		} else {
//			int d = iw + ih;
//			w = (int) (d * Math.abs(Math.cos(ang)));
//			h = (int) (d * Math.abs(Math.sin(ang)));
//		}
//		x = (w / 2) - (iw / 2);// 确定原点坐标
//		y = (h / 2) - (ih / 2);
//		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
//		Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
////		if (bgcolor == null) {
////			rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
////		} else {
//			gs.setColor(Color.WHITE);
//			gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
////		}
//		AffineTransform at = new AffineTransform();
//		at.rotate(ang, w / 2, h / 2);// 旋转图象
//		at.translate(x, y);
//		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
//		op.filter(image, rotatedImage);
//		return rotatedImage;
////		image = rotatedImage;
////		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
////		ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut);
////		ImageIO.write(image, "png", iamgeOut);
////		InputStream inputStream = new ByteArrayInputStream(byteOut.toByteArray());
////		return inputStream;
	}

	public static void genImg(String name, BufferedImage bi) {
		File file = new File(imageDir + "/" + name + ".jpg");
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			ImageIO.write(bi, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void genImg(String name, Collection<Pixel> pixels, int width, int height) {
		File file = new File(imageDir + "/" + name + ".jpg");
		if (!file.exists()) {
			file.mkdirs();
		}
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(new Color(0xFFFFFF));
		g2.clearRect(0, 0, width, height);
		for (Pixel p : pixels) {
			g2.setPaint(new Color(p.getRgb()));
			g2.drawRect(p.getX(), p.getY(), 1, 1);
		}
		try {
			ImageIO.write(bi, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
