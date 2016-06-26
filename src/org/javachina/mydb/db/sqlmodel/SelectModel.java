package org.javachina.mydb.db.sqlmodel;

import java.util.Map;

public class SelectModel {
	private String tableName;
	private String[] queryParams;
	private Map<String,String> pairMap;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(String[] queryParams) {
		this.queryParams = queryParams;
	}
	public Map<String, String> getPairMap() {
		return pairMap;
	}
	public void setPairMap(Map<String, String> pairMap) {
		this.pairMap = pairMap;
	}
	
}
