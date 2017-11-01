package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ichen on 2017/10/18.
 */
public class NumLvlVo implements Serializable {

	private Map<String,String> levelData;

	public Map<String, String> getLevelData() {
		return levelData;
	}

	public void setLevelData(Map<String, String> levelData) {
		this.levelData = levelData;
	}
}
