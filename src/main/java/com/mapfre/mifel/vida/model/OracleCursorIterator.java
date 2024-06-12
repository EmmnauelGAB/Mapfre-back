package com.mapfre.mifel.vida.model;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OracleCursorIterator implements Iterator<Map<String,Object>>{
	
	public OracleCursorIterator(CallableStatement call) throws SQLException {
		this.rs = call.executeQuery();
	}

	private final ResultSet rs;
	
	@Override
	public boolean hasNext(){
		// TODO Auto-generated method stub
		boolean res = false;
		try {
			res = rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Map<String, Object> next() {
		Map<String, Object> row = new HashMap<>();
		try {
			for(int i = 1; i<= rs.getMetaData().getColumnCount(); i++) {
				row.put(rs.getMetaData().getColumnLabel(i),rs.getObject(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

}
