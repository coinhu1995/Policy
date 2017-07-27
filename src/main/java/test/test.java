package test;

import java.io.IOException;
import java.sql.SQLException;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.prepare.SentenceSegment;

public class test {
	public static void main(String[] args) throws IOException, SQLException {
		SentenceSegment ss = new SentenceSegment();
		SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		SQLDAO cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		Comment c = new Comment();
		c.setContent("thật là nực cười quá");
		c.setCmt_segment(ss.sentence2SegmentSentence(c.getContent(), SentenceSegment.VNTOKENIZER));
		c.setLabel(2);
		cmtDao.insert(c);
		System.out.println("done");
	}
}
