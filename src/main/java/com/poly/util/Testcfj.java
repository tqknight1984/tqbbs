package com.poly.util;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Testcfj {
	
	public static void main(String[] args) {
		String m1 = "F:/img/tt.jpg";
		String m2 = "F:/img/tt2.jpg";
		MarvinImage img = MarvinImageIO.loadImage(m1);
		MarvinImage image = img.clone();
		image.resize(image.getWidth(), image.getHeight());
		MarvinImageIO.saveImage(image, m2);
	}
}
