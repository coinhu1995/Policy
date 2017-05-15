package ptit.nhunh.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.WordDAO;

public class CheckStopWord {
	public void check() throws IOException, SQLException {
		WordDAO wordDAO = new WordDAO("Capstone");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("stopword.txt")));
		String s = "";
		while((s = br.readLine()) != null){
			ResultSet rs = wordDAO.getData("select * from TblWord where word = N'"+s+"'");
			while(rs.next()){
				wordDAO.updateData("update TblWord set isStop = 1 where id = "+rs.getInt(1));
			}
		}
		br.close();
		System.out.println("DOne");
	}
}
