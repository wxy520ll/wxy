package cn.net.xinyi.xmjt.model;

import android.graphics.drawable.Drawable;

public class ZipDocSlidMenuItem {

	private String menuName;
	private String menuIconName;
	private int menuId;
	private Drawable menuIconId;
	
	
	public Drawable getMenuIconId() {
		return menuIconId;
	}
	public void setMenuIconId(Drawable menuIconId) {
		this.menuIconId = menuIconId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuIconName() {
		return menuIconName;
	}
	public void setMenuIconName(String menuIconName) {
		this.menuIconName = menuIconName;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	
	
}