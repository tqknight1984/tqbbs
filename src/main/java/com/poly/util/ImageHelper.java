package com.poly.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageHelper {
	private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		// targetW��targetH�ֱ��ʾĿ�곤�Ϳ�
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// ������ʵ����targetW��targetH��Χ��ʵ�ֵȱ����š��������Ҫ�ȱ�����
		// �������if else���ע�ͼ���
		if (sx < sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}


	public static void saveImageAsThumbnail(File file, String outFilePath, int width, int hight) throws Exception {
		InputStream in = new FileInputStream(file);
		File saveFile = new File(outFilePath);
		BufferedImage srcImage = ImageIO.read(in);
		if (width > 0 || hight > 0) {
			int sw = srcImage.getWidth();
			int sh = srcImage.getHeight();
			if (sw > width && sh > hight) {
				srcImage = resize(srcImage, width, hight);
			} else {
				String fileName = saveFile.getName();
				String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
				ImageIO.write(srcImage, formatName, saveFile);
				return;
			}
		}
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		if (w == width) {
			int x = 0;
			int y = h / 2 - hight / 2;
			saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
		}
		else if (h == hight) {
			int x = w / 2 - width / 2;
			int y = 0;
			saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);
		}
		in.close();
	}

	/**
	 * ʵ�����ź�Ľ�ͼ
	 * 
	 * @param image
	 *            ���ź��ͼ��
	 * @param subImageBounds
	 *            Ҫ��ȡ����ͼ�ķ�Χ
	 * @param subImageFile
	 *            Ҫ������ļ�
	 * @throws IOException
	 */
	private static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File subImageFile) throws IOException {
		if (subImageBounds.x < 0 || subImageBounds.y < 0 || subImageBounds.width - subImageBounds.x > image.getWidth() || subImageBounds.height - subImageBounds.y > image.getHeight()) {
			System.out.println("Bad   subimage   bounds");
			return;
		}
		BufferedImage subImage = image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height);
		String fileName = subImageFile.getName();
		String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
		ImageIO.write(subImage, formatName, subImageFile);
	}
}
