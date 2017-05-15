package ptit.nhunh.prepare;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;

import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.tool.WordSegment;

/**
 * Phân tích các câu comment thành các từ, đưa vào cơ sở dữ liệu, tạo id cho mỗi
 * từ.
 * 
 * @author uhn
 * 
 */
public class CollectWord {
	private WordSegment wordSegment;
	private WordDAO wordDAO;
	private Collator collator;

	public CollectWord() {
		this.wordSegment = new WordSegment();
		this.wordDAO = new WordDAO("Capstone");
		this.collator = Collator.getInstance();
	}

	public CollectWord(WordSegment wordSegment) {
		this.wordSegment = wordSegment;
		this.wordDAO = new WordDAO("Capstone");
		this.collator = Collator.getInstance();
	}

	public void process(int length) throws FileNotFoundException, SQLException, IOException {
		this.collator.setStrength(Collator.TERTIARY);
		System.out.println("\n\n\n----------Starting collect word---------");

		collectWord(1, length);

		System.out.println("---------Done!------------");
	}

	public int collectWord(int limit) throws UnsupportedEncodingException, SQLException {
		ArrayList<Word> listWord = new ArrayList<>();
		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
		ResultSet rs = null;
		rs = this.wordDAO
				.getData("select top " + limit + " * from TblCmt where label2 = 1 order by id");
		int dem = 0;

		while (rs.next()) {
			String s = rs.getString(3);
			s = s.toLowerCase();

			ArrayList<Word> aw = this.wordSegment.string2Vector(s);
			for (int i = 0; i < aw.size(); i++) {
				int pos = contain(listWord, aw.get(i));

				if (pos == -1) {
					if (aw.get(i).getWord().length() > 1) {
						aw.get(i).setDF(1);
						listWord.add(aw.get(i));

						System.out.println(++dem + " : [" + aw.get(i).getWord() + "]");
					}
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}

		// rs = this.dbc.getData(
		// "select top "+limit+" * from TblCmt where label = 2 order by id");
		// dem = 0;
		//
		// while (rs.next()) {
		// String s = rs.getString(3);
		// s = s.toLowerCase();
		//
		// ArrayList<Word> aw = this.wordSegment.string2Vector(s);
		// for (int i = 0; i < aw.size(); i++) {
		// int pos = contain(listWord, aw.get(i));
		//
		// if (pos == -1) {
		// if (aw.get(i).getWord().length() > 1) {
		// aw.get(i).setDF(1);
		// listWord.add(aw.get(i));
		//
		// System.out.println(++dem + " : [" + aw.get(i).getWord() + "]");
		// }
		// } else {
		// listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
		// }
		// }
		// }

		rs = this.wordDAO.getData("select top " + limit + " * from TblCmt where label = 2 order by id");
		dem = 0;

		while (rs.next()) {
			String s = rs.getString(3);
			s = s.toLowerCase();

			ArrayList<Word> aw = this.wordSegment.string2Vector(s);
			for (int i = 0; i < aw.size(); i++) {
				int pos = contain(listWord, aw.get(i));

				if (pos == -1) {
					if (aw.get(i).getWord().length() > 1) {
						aw.get(i).setDF(1);
						listWord.add(aw.get(i));

						System.out.println(++dem + " : [" + aw.get(i).getWord() + "]");
					}
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}

		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		// và đánh số thứ tự
		for (int i = 0; i < listWord.size(); i++) {
			try {
				listWord.get(i).setIDF((float) Math.log10((limit) / (1 + listWord.get(i).getDF())));
				this.wordDAO.updateData("insert into TblWord values(N'" + listWord.get(i).getWord()
						+ "', " + listWord.get(i).getDF() + ", " + listWord.get(i).getIDF() + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listWord.size();
	}

	public int collectWord(int start, int end) throws UnsupportedEncodingException, SQLException {
		ArrayList<Word> listWord = new ArrayList<>();
		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
		ResultSet rs = null;
		rs = this.wordDAO.getData("select * from TblCmt where id >= " + start + " and id <= " + end
				+ " and label2 = 1 or label2 = 3 order by id");
		int dem = 0;

		while (rs.next()) {
			String s = rs.getString(3);
			s = s.toLowerCase();

			ArrayList<Word> aw = this.wordSegment.string2Vector(s);
			for (int i = 0; i < aw.size(); i++) {
				int pos = contain(listWord, aw.get(i));

				if (pos == -1) {
					aw.get(i).setDF(1);
					listWord.add(aw.get(i));

					System.out.println(++dem + " : [" + aw.get(i).getWord() + "]");
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		// và đánh số thứ tự
		for (int i = 0; i < listWord.size(); i++) {
			try {
				listWord.get(i)
						.setIDF((float) Math.log10((end - start) / (1 + listWord.get(i).getDF())));
				this.wordDAO.updateData("insert into TblWord values(N'" + listWord.get(i).getWord()
						+ "', " + listWord.get(i).getDF() + ", " + listWord.get(i).getIDF()
						+ ", 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listWord.size();
	}

	/**
	 * 
	 * Collect word ở tất cả các nhãn. Từ comment đầu tiên cho đến comment 'end'
	 * 
	 * @param end
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws SQLException
	 */

	public int collectWord1(int end) throws UnsupportedEncodingException, SQLException {
		ArrayList<Word> listWord = new ArrayList<>();
		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
		ResultSet rs = null;
		rs = this.wordDAO.getData("select * from TblCmt where id <= " + end + "order by id");
		int dem = 0;

		while (rs.next()) {
			String s = rs.getString(3);
			s = s.toLowerCase();

			ArrayList<Word> aw = this.wordSegment.string2Vector(s);
			for (int i = 0; i < aw.size(); i++) {
				int pos = contain(listWord, aw.get(i));

				if (pos == -1) {
					if (aw.get(i).getWord().length() > 1) {
						aw.get(i).setDF(1);
						listWord.add(aw.get(i));

						System.out.println(++dem + " : [" + aw.get(i).getWord() + "]");
					}
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		// và đánh số thứ tự
		for (int i = 0; i < listWord.size(); i++) {
			try {
				listWord.get(i).setIDF((float) Math.log10((end) / (1 + listWord.get(i).getDF())));
				this.wordDAO.updateData("insert into TblWord values(N'" + listWord.get(i).getWord()
						+ "', " + listWord.get(i).getDF() + ", " + listWord.get(i).getIDF()
						+ ", 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listWord.size();
	}

	private int contain(ArrayList<Word> listWord, Word w) {
		for (int i = 0; i < listWord.size(); i++) {
			if (this.collator.equals(listWord.get(i).getWord(), w.getWord())) {
				return i;
			}
		}
		return -1;
	}
}
