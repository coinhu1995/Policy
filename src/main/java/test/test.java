package test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.WordDAO;

public class test {
	public static void main(String[] args) throws IOException, SQLException {
		WordDAO w = new WordDAO("Capstone");
		ResultSet rs = w.getData("select * from TblWord");
		
		ArrayList<String> a = new ArrayList<>();
		
		while(rs.next()){
			a.add(rs.getString(2));
		}
		
		rs = w.getData("select * from TblWord order by id");
		
		ArrayList<String> b = new ArrayList<>();
		
		while(rs.next()){
			b.add(rs.getString(2));
		}
		
		long s = System.currentTimeMillis();
		for(int i = 0; i < a.size(); i++){
			for(int j = 0; j < b.size(); j++){
				if(a.get(i).equals(b.get(j))){
					System.out.println(j);
					break;
				}
			}
		}
		long e = System.currentTimeMillis();
		for(int i = 0; i < a.size(); i++){
			rs = w.getData("select * from TblWord where word = N'"+a.get(i)+"'");
			rs.next();
			System.out.println(rs.getString(1));
		}
		long e2 = System.currentTimeMillis();
		
		System.out.println("run: "+ (e - s));
		System.out.println("ds : "+ (e2 - e));
	}
}
