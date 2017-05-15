package ptit.nhunh.prepare2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;

public class CollectWord {
	private CommentDAO commentDAO;
	private WordDAO wordDAO;
	
	public CollectWord() {
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
	}
	
	public void process(int limit) throws SQLException, IOException {
		collectWord(limit);
	}
	
	/**
	 * Collect các từ ở trong limit comments đầu tiên. Và thêm vào Database.
	 * 
	 * @param limit
	 * @throws SQLException
	 * @throws IOException
	 */
	private void collectWord(int limit) throws SQLException, IOException {
		ArrayList<Word> listWord = new ArrayList<>();
		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
		ResultSet rs = null;
		rs = this.commentDAO.getData("select top " + limit + " * from TblComment order by id");
		int dem = 0;
		int percent = 0;
		while (rs.next()) {
			String segmentCmt = rs.getString(4);

			ArrayList<Word> aw = Utils.string2ListWord(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listWord, aw.get(i));

				if (pos == -1) {
					aw.get(i).setDF(1);
					listWord.add(aw.get(i));
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
			dem++;
			if ((int) (dem / (float) limit * 100) > percent) {
				percent = (int) (dem / (float) limit * 100);
				System.out.println("\t\t" + percent + "%");
			}
		}

		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		// và đánh số thứ tự
		for (int i = 0; i < listWord.size(); i++) {
			String sql = "";
			try {
				listWord.get(i).setIDF((float) Math.log10((limit) / (1 + listWord.get(i).getDF())));

				sql = "insert into TblWord values(N'" + listWord.get(i).getWord() + "', "
						+ listWord.get(i).getDF() + ", " + listWord.get(i).getIDF() + ", 0)";

				this.wordDAO.updateData(sql);
			} catch (SQLException e) {
				System.out.println(sql);
				e.printStackTrace();
			}
		}
		checkStopWord();
	}

	private void checkStopWord() throws IOException, SQLException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("stopword.txt")));
		String s = "";
		while ((s = br.readLine()) != null) {
			ResultSet rs = wordDAO.getData("select * from TblWord where word = N'" + s + "'");
			while (rs.next()) {
				wordDAO.updateData("update TblWord set isStop = 1 where id = " + rs.getInt(1));
			}
		}
		br.close();
	}
}
