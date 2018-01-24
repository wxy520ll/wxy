package cn.net.xinyi.xmjt.utils.IdCard;

import java.util.Observable;

//划线识别相关（银行卡等）
public class DrawLineViewState extends Observable{
	
	public float x1,y1;
	public float x2,y2;
	
	public DrawLineViewState(){
		
	}
	
	public DrawLineViewState(float x1,float y1,float x2,float y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public float getX1() {
		return x1;
	}
	public void setX1(float x1) {
		if(x1!=this.x1){
			this.x1 = x1;
			setChanged();
		}
	}
	public float getY1() {
		return y1;
	}
	public void setY1(float y1) {
		if(y1!=this.y1){
			this.y1 = y1;
			setChanged();
		}
	}
	public float getX2() {
		return x2;
	}
	public void setX2(float x2) {
		if(x2!=this.x2){
			this.x2 = x2;
			setChanged();
		}
	}
	public float getY2() {
		return y2;
	}
	public void setY2(float y2) {
		if(y2!=this.y2){
			this.y2 = y2;
			setChanged();
		}
	}
	
	
}
