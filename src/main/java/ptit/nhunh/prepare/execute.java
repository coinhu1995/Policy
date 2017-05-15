package ptit.nhunh.prepare;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class execute {
	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException{
		//-----------Collect words-----------
		new CollectWord().process(700);
		//-----------Collect words-----------
		
		//-----------Generate Traning Data File-----------
//		new GenTraningData().process();
		//-----------Generate Traning Data File-----------
	}
}
