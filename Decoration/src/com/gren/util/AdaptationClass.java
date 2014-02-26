package com.gren.util;

/*
 * 用来适配不同手机分辨率的工具类
 * 
 */
public class AdaptationClass {
	private final static float FINAL_WIDTH = 800;  //手机的基准宽度
	private final static float FINAL_HEIGHT = 1333;  //手机的基准高度
	
	float Coefficient_X;
	float Coefficient_Y;
	
	float width;
	float height;
	float density;

	public AdaptationClass(float width, float height,float density) {
		this.Coefficient_X = width / FINAL_WIDTH;
		this.Coefficient_Y = height / FINAL_WIDTH;
		this.width = width;
		this.height = height;
		this.density = density;
	}
	
	public float getCurWidth(){
		return width;
	}
	public float getCurHeight(){
		return height;
	}
	public float getCurDensity(){
		return density;
	}


	public int changeImageXByHeight(int w) {
		return Math.round(Coefficient_Y * w);
	}

	public int changeImageYByHeight(int h) {
		return Math.round(Coefficient_Y * h);
	}

	public int changeImageX(int w) {
		return Math.round(Coefficient_X * w);
	}

	public int changeImageY(int h) {
		return Math.round(Coefficient_X * h);
	}

	public int changeCoordinateX(int x) {
		return Math.round(Coefficient_X * x);
	}

	public int changeCoordinateY(int y) {
		return Math.round(Coefficient_Y * y);
	}

	public int changeCoordinateAnimY(int y) {
		float h = 800 - y;
		int h1 = Math.round(height - (Coefficient_X * h));
		return h1;
	}
}
