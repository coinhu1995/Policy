package ptit.nhunh.tool;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.model.Comment;
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
	private CommentDAO commentDAO;
	private int ma;
	private int chay = 0;
	private ArrayList<Comment> ac;
	private BufferedWriter bw;

	public AssignLabel() throws SQLException, IOException {
		bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\uhn\\Desktop\\LogAssignLabel.txt", true),
				StandardCharsets.UTF_8));
		commentDAO = new CommentDAO("Capstone");
		ac = commentDAO.getAllComment();

		initComponents();
		load();
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

		label = new javax.swing.JTextField();
		b1 = new javax.swing.JButton();
		b2 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		url = new javax.swing.JTextField();
		jScrollPane1 = new javax.swing.JScrollPane();
		cmt = new javax.swing.JTextArea();
		id = new javax.swing.JTextField();
		ID = new javax.swing.JLabel();
		b3 = new javax.swing.JButton();
		b4 = new javax.swing.JButton();
		b5 = new javax.swing.JButton();
		b6 = new javax.swing.JButton();
		jLabel4 = new javax.swing.JLabel();
		ulb = new javax.swing.JTextField();
		uid = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		did = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setLocation(new java.awt.Point(150, 50));
		setMinimumSize(new java.awt.Dimension(700, 400));
		setPreferredSize(new java.awt.Dimension(1050, 500));

		b1.setText("1");
		b1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b1ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		b2.setText("open url");
		b2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b2ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		jLabel1.setText("Comment");

		jLabel2.setText("Label");

		jLabel3.setText("Url");

		cmt.setColumns(20);
		cmt.setRows(5);
		jScrollPane1.setViewportView(cmt);

		ID.setText("id");

		b3.setText("2");
		b3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b3ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		b4.setText("3");
		b4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b4ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		b5.setText("write log");
		b5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b5ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		b6.setText("close log file");
		b6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					b6ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		jLabel4.setText("update");

		jButton1.setText("update");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton1ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		jButton2.setText("delete");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton2ActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(2, 2, 2)
								.addComponent(jLabel1))
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(ID))
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addComponent(jLabel3))
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addComponent(jLabel2)))
						.addGap(18, 18, 18)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE,
												91, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(layout.createSequentialGroup().addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE, 606,
												Short.MAX_VALUE)
										.addComponent(url))
										.addGroup(layout
												.createParallelGroup(
														javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(b6))
												.addGroup(layout.createSequentialGroup()
														.addGap(95, 95, 95)
														.addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(jButton2)
																.addComponent(did,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		77,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
														.addContainerGap())))
								.addGroup(layout.createSequentialGroup().addComponent(b1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(b3)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(b4).addGap(84, 84, 84).addComponent(b5)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(b2).addGap(83, 83, 83))
								.addGroup(layout.createSequentialGroup()
										.addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE,
												229, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												347, Short.MAX_VALUE)
										.addComponent(jLabel4).addGap(43, 43, 43)
										.addComponent(uid, javax.swing.GroupLayout.PREFERRED_SIZE,
												56, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(ulb, javax.swing.GroupLayout.PREFERRED_SIZE,
												62, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton1).addContainerGap()))));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addGroup(layout.createSequentialGroup().addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(58, 58, 58)
										.addComponent(jLabel1))
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(jScrollPane1,
												javax.swing.GroupLayout.PREFERRED_SIZE, 217,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGap(18, 18, 18))
						.addGroup(layout.createSequentialGroup().addComponent(b6).addGap(47, 47, 47)
								.addComponent(did, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jButton2).addGap(83, 83, 83)))
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(ID))
								.addGroup(layout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(ulb, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(uid, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jButton1))
								.addComponent(jLabel4))
						.addGap(27, 27, 27)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(url, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3))
						.addGap(18, 18, 18)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(label,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(b2).addComponent(b1).addComponent(b3).addComponent(b4)
								.addComponent(b5))
						.addGap(24, 24, 24)));

		pack();
	}

	private void b1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		commentDAO.updateData("update TblComment set label = 1 where id = " + ma);
		if (chay < ac.size()) {
			chay++;
			load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b2ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start chrome " + url.getText() });
	}

	private void b3ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		commentDAO.updateData("update TblComment set label = 2 where id = " + ma);
		if (chay < ac.size()) {
			chay++;
			load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b4ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		commentDAO.updateData("update TblComment set label = 3 where id = " + ma);
		if (chay < ac.size()) {
			chay++;
			load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void load() throws SQLException {
		ma = ac.get(chay).getId();
		String sql = "select * from TblUrl where url_id = '" + ac.get(chay).getPage_id() + "'";
		ResultSet rs2 = commentDAO.getData(sql);
		rs2.next();
		String s = ac.get(chay).getContent();
		String line = "";
		while (s.length() > 80) {
			line += s.substring(0, 80) + "\n";
			s = s.substring(80);
		}
		line += s;
		cmt.setText(line);
		try {
			url.setText(rs2.getString(2));
			if(rs2.getString(6).equals("thanhnien")){
				did.setText(Utils.getThanhNienPageId(rs2.getString(2)));
			}else{
				did.setText(Utils.getVnExpressPageId(rs2.getString(2)));
			}
		} catch (Exception e) {

		}
		id.setText(ma + "");
	}

	private void b5ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		bw.write(ac.get(chay).getContent() + "\n");
		bw.write(url.getText() + "\n");
	}

	private void b6ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		bw.close();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		commentDAO.updateData(
				"update TblComment set label = " + ulb.getText() + " where id = " + uid.getText());
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
		commentDAO.updateData("delete from TblUrl where url_id = '" + did.getText() + "'");
		commentDAO.updateData("delete from TblComment where page_id = '" + did.getText() + "'");
		ac = commentDAO.getAllComment();
		chay = 0;
		load();
	}
}
