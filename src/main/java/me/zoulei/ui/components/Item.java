package me.zoulei.ui.components;

import java.io.File;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表名选择下拉框
 */
@Data
@AllArgsConstructor
public class Item {
	private String key;
	private Properties prop;
	private File file;
	public String toString(){
		return key;
	}
}