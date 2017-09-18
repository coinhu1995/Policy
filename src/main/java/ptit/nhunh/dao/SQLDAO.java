package ptit.nhunh.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SQLDAO {
	public boolean insert(Object obj) throws SQLException;
	public ArrayList<Object> getAll() throws SQLException;
	public ArrayList<Object> getData(String sql) throws SQLException;
	public boolean update(Object obj, int field);
	public boolean update(String sql);
	public boolean update(Object obj) throws SQLException;
	public Object findByItemId(String id) throws SQLException;
	Object findById(String id) throws SQLException;
}
