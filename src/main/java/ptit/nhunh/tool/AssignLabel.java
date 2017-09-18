package ptit.nhunh.tool;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Article;
import ptit.nhunh.utils.Utils;

public class AssignLabel extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel ID;
	private javax.swing.JButton b1;
	private javax.swing.JButton b2;
	private javax.swing.JButton b3;
	private javax.swing.JButton b4;
	private javax.swing.JButton b5;
	private javax.swing.JButton b6;
	private javax.swing.JTextArea cmt;
	private javax.swing.JTextField did;
	private javax.swing.JTextField id;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField label;
	private javax.swing.JTextField uid;
	private javax.swing.JTextField ulb;
	private javax.swing.JTextField url;
	private SQLDAO commentDAO;
	private SQLDAO urlDAO;
	private int ma;
	private int chay = 0;
	private ArrayList<Object> listCmt;
	private BufferedWriter bw;

	public AssignLabel() throws SQLException, IOException {
		this.bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\coinh\\Desktop\\LogAssignLabel.txt", true),
				StandardCharsets.UTF_8));
		this.commentDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
		this.listCmt = this.commentDAO.getAll();

		this.initComponents();
		this.load();
	}

	public static void main(String[] args) throws SQLException, IOException {
		new AssignLabel().process();
	}

	public void process() throws SQLException, IOException {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(AssignLabel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(AssignLabel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(AssignLabel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AssignLabel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new AssignLabel().setVisible(true);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void initComponents() {

		this.label = new javax.swing.JTextField();
		this.b1 = new javax.swing.JButton();
		this.b2 = new javax.swing.JButton();
		this.jLabel1 = new javax.swing.JLabel();
		this.jLabel2 = new javax.swing.JLabel();
		this.jLabel3 = new javax.swing.JLabel();
		this.url = new javax.swing.JTextField();
		this.jScrollPane1 = new javax.swing.JScrollPane();
		this.cmt = new javax.swing.JTextArea();
		this.id = new javax.swing.JTextField();
		this.ID = new javax.swing.JLabel();
		this.b3 = new javax.swing.JButton();
		this.b4 = new javax.swing.JButton();
		this.b5 = new javax.swing.JButton();
		this.b6 = new javax.swing.JButton();
		this.jLabel4 = new javax.swing.JLabel();
		this.ulb = new javax.swing.JTextField();
		this.uid = new javax.swing.JTextField();
		this.jButton1 = new javax.swing.JButton();
		this.jButton2 = new javax.swing.JButton();
		this.did = new javax.swing.JTextField();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setLocation(new java.awt.Point(150, 50));
		this.setMinimumSize(new java.awt.Dimension(700, 400));
		this.setPreferredSize(new java.awt.Dimension(1050, 500));

		this.b1.setText("1");
		this.b1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b1ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		this.b2.setText("open url");
		this.b2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b2ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		this.jLabel1.setText("Comment");

		this.jLabel2.setText("Label");

		this.jLabel3.setText("Url");

		this.cmt.setColumns(20);
		this.cmt.setRows(5);
		this.jScrollPane1.setViewportView(this.cmt);

		this.ID.setText("id");

		this.b3.setText("2");
		this.b3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b3ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		this.b4.setText("3");
		this.b4.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b4ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		this.b5.setText("write log");
		this.b5.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b5ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		this.b6.setText("close log file");
		this.b6.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.b6ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		this.jLabel4.setText("update");

		this.jButton1.setText("update");
		this.jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.jButton1ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		this.jButton2.setText("delete");
		this.jButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel.this.jButton2ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(2, 2, 2)
										.addComponent(this.jLabel1))
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(this.ID))
								.addGroup(layout.createSequentialGroup()
										.addContainerGap().addComponent(this.jLabel3))
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(this.jLabel2)))
						.addGap(18, 18, 18)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(this.label,
												javax.swing.GroupLayout.PREFERRED_SIZE, 91,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(layout.createSequentialGroup().addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(this.jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE, 606,
												Short.MAX_VALUE)
										.addComponent(this.url))
										.addGroup(layout
												.createParallelGroup(
														javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(this.b6))
												.addGroup(layout.createSequentialGroup()
														.addGap(95, 95, 95)
														.addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(this.jButton2)
																.addComponent(this.did,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		77,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
														.addContainerGap())))
								.addGroup(layout.createSequentialGroup().addComponent(this.b1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(this.b3)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(this.b4).addGap(84, 84, 84)
										.addComponent(this.b5)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(this.b2).addGap(83, 83, 83))
								.addGroup(layout.createSequentialGroup()
										.addComponent(this.id,
												javax.swing.GroupLayout.PREFERRED_SIZE, 229,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												347, Short.MAX_VALUE)
										.addComponent(this.jLabel4).addGap(43, 43, 43)
										.addComponent(this.uid,
												javax.swing.GroupLayout.PREFERRED_SIZE, 56,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(this.ulb,
												javax.swing.GroupLayout.PREFERRED_SIZE, 62,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(this.jButton1).addContainerGap()))));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addGroup(layout.createSequentialGroup().addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(58, 58, 58)
										.addComponent(this.jLabel1))
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(this.jScrollPane1,
												javax.swing.GroupLayout.PREFERRED_SIZE, 217,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGap(18, 18, 18))
						.addGroup(layout.createSequentialGroup().addComponent(this.b6)
								.addGap(47, 47, 47)
								.addComponent(this.did, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(this.jButton2).addGap(83, 83, 83)))
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(this.id,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(this.ID))
								.addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(this.ulb,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(this.uid,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(this.jButton1))
								.addComponent(this.jLabel4))
						.addGap(27, 27, 27)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(this.url, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(this.jLabel3))
						.addGap(18, 18, 18)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(this.jLabel2).addComponent(this.label,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(this.b2).addComponent(this.b1).addComponent(this.b3)
								.addComponent(this.b4).addComponent(this.b5))
						.addGap(24, 24, 24)));

		this.pack();
	}

	private void b1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		this.commentDAO.update("update TblComment set label = 1 where id = " + this.ma);
		if (this.chay < this.listCmt.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b2ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		Runtime.getRuntime()
				.exec(new String[] { "cmd", "/c", "start chrome " + this.url.getText() });
	}

	private void b3ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		this.commentDAO.update("update TblComment set label = 2 where id = " + this.ma);
		if (this.chay < this.listCmt.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b4ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		this.commentDAO.update("update TblComment set label = 3 where id = " + this.ma);
		if (this.chay < this.listCmt.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void load() throws SQLException {
		Comment c = (Comment) this.listCmt.get(this.chay);
		this.ma = c.getId();
		String sql = "select * from TblUrl where url_id = '" + c.getPage_id() + "'";
		ArrayList<Object> list = this.urlDAO.getData(sql);

		String s = c.getContent();
		String line = "";
		while (s.length() > 80) {
			line += s.substring(0, 80) + "\n";
			s = s.substring(80);
		}
		line += s;
		this.cmt.setText(line);
		try {
			Article u = (Article) list.get(0);
			this.did.setText(Utils.getThanhNienPageId(u.getUrl_id()));
		} catch (Exception e) {

		}
		this.id.setText(this.ma + "");
	}

	private void b5ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		Comment c = (Comment) this.listCmt.get(this.chay);
		this.bw.write(c.getContent() + "\n");
		this.bw.write(this.url.getText() + "\n");
	}

	private void b6ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		this.bw.close();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		this.commentDAO.update("update TblComment set label = " + this.ulb.getText()
				+ " where id = " + this.uid.getText());
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		this.commentDAO
				.update("delete from TblUrl where url_id = '" + this.did.getText() + "'");
		this.commentDAO
				.update("delete from TblComment where page_id = '" + this.did.getText() + "'");
		this.listCmt = this.commentDAO.getAll();
		this.chay = 0;
		this.load();
	}
}
