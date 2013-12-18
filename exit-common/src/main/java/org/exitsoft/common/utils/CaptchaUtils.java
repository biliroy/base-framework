/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exitsoft.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 简单的验证码工具类
 * 
 * @author vincent
 *
 */
public class CaptchaUtils {

	public static String FONT_FAMILY = "Arial Black";
	
	public static Integer FONT_SIZE = 16;
	
	/**
	 * 生成图形验证码，并返回code值
	 * 
	 * @param width 宽度
	 * @param height 高度
	 * @param num 验证码位数
	 * @param out OutputStream
	 * 
	 * @return String
	 */
	public static String getCaptcha(int width, int height, int num,
			OutputStream out) {
		// 防止非负数
		width = width > 0 ? width : 60;
		height = height > 0 ? height : 18;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Font font = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE);
		// 绘制图片
		Random random = new Random();
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 103));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(font);
		// 随机生成线条，让图片看起来更加杂乱
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(6) + 1;
			int y1 = random.nextInt(12) + 1;
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 该变量用于保存系统生成的随机字符串
		String sRand = "";
		for (int i = 0; i < num; i++) {
			// 取得一个随机字符
			String tmp = getRandomChar();
			sRand += tmp;
			// 将系统随机字符添加到图形验证码图片上
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, FONT_SIZE * i + 5, FONT_SIZE + 5);
		}
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", out);
			out.close();
		} catch (IOException e) {
			sRand = null;
			e.printStackTrace();
		}
		return sRand;
	}

	/**
	 * 生成随机颜色
	 * 
	 * @param fc 从随机的颜色位置 (0-255)
	 * @param bc 到随机的颜色位置（0-255）
	 * 
	 * @return {@link Color}
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	/**
	 * 生成随机字符的方法
	 */
	public static String getRandomChar() {
		int rand = (int) Math.round(Math.random() * 2);
		long itmp = 0;
		char ctmp = '\u0000';
		// 根据rand的值来决定是生成一个大写字母，小写字母和数字
		switch (rand) {
		// 生成大写字母的情形
		case 1:
			itmp = Math.round(Math.random() * 25 + 65);
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
			// 生成小写字母的情形
		case 2:
			itmp = Math.round(Math.random() * 25 + 97);
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
			// 生成数字的情形
		default:
			itmp = Math.round(Math.random() * 9);
			return String.valueOf(itmp);
		}

	}
	
}
