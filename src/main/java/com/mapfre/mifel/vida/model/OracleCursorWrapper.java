package com.mapfre.mifel.vida.model;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public class OracleCursorWrapper implements Iterable<Map<String,Object>>{
	
	private final CallableStatement call;

	public OracleCursorWrapper(CallableStatement call) {
		this.call = call;
	}

	@Override
	public Iterator<Map<String, Object>> iterator() {
		// TODO Auto-generated method stub
		OracleCursorIterator res = null;
		try {
			res = new OracleCursorIterator(call);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
