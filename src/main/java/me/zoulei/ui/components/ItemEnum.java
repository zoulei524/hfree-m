package me.zoulei.ui.components;

public enum ItemEnum {
	/**
	 * 显示且维护
	 */
	A("显示且维护"),
	/**
	 * 不显示且不维护
	 */
	B("不显示且不维护"),
	/**
	 * 显示但不维护
	 */
	C("显示但不维护"),
	/**
	 * 不显示但维护
	 */
	D("不显示但维护");

	String name;
	ItemEnum(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
