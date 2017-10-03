package ptit.nhunh.tool;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import ptit.nhunh.model.Cmt;
import ptit.nhunh.utils.Utils;

/**
 * Chay tu file
 * 
 * @author uhn
 *
 */
public class AssignLabel2 extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel ID;
	private javax.swing.JButton but_dongy;
	private javax.swing.JButton b2;
	private javax.swing.JButton but_gopy;
	private javax.swing.JButton but_khongdongy;
	private javax.swing.JButton b5;
	private javax.swing.JButton b6;
	private javax.swing.JButton reload;
	private javax.swing.JButton but_ykienkhav;
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
	private int ma;
	private int chay = 0;
	private ArrayList<Cmt> ac;
	private BufferedWriter bw;
	private BufferedWriter bw2;

	public AssignLabel2() throws SQLException, IOException {
		Scanner scan = new Scanner(new File("current.txt"));
		try {
			this.chay = scan.nextInt();
		} catch (Exception e) {

		}
		scan.close();

		this.ac = new ArrayList<>();
		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("LogAssignLabel.txt", true), StandardCharsets.UTF_8));

		this.bw2 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("result.txt", true), StandardCharsets.UTF_8));

		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("comment.txt"), StandardCharsets.UTF_8));
		String line = "";
		while ((line = br.readLine()) != null) {
			Cmt c = new Cmt();
			c.setId(Integer.parseInt(line.substring(0, 12).trim()));
			c.setUrl(line.substring(12, 212).trim());
			c.setContent(line.substring(212).trim());
			this.ac.add(c);
		}
		br.close();
		this.initComponents();

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					AssignLabel2.this.bw.close();
					AssignLabel2.this.bw2.close();
					BufferedWriter bw3 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream("current.txt")));
					bw3.write(AssignLabel2.this.chay + "");
					bw3.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		this.load();
	}

	public static void main(String[] args) throws SQLException, IOException {
		new AssignLabel2().process();
	}

	public void process() throws SQLException, IOException {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(AssignLabel2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(AssignLabel2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(AssignLabel2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AssignLabel2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new AssignLabel2().setVisible(true);
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
		this.but_dongy = new javax.swing.JButton();
		this.jLabel1 = new javax.swing.JLabel();
		this.jLabel2 = new javax.swing.JLabel();
		this.jLabel3 = new javax.swing.JLabel();
		this.url = new javax.swing.JTextField();
		this.jScrollPane1 = new javax.swing.JScrollPane();
		this.cmt = new javax.swing.JTextArea();
		this.id = new javax.swing.JTextField();
		this.ID = new javax.swing.JLabel();
		this.but_gopy = new javax.swing.JButton();
		this.but_khongdongy = new javax.swing.JButton();
		this.b5 = new javax.swing.JButton();
		this.b6 = new javax.swing.JButton();
		this.reload = new javax.swing.JButton();
		this.jLabel4 = new javax.swing.JLabel();
		this.ulb = new javax.swing.JTextField();
		this.uid = new javax.swing.JTextField();
		this.jButton1 = new javax.swing.JButton();
		this.jButton2 = new javax.swing.JButton();
		this.did = new javax.swing.JTextField();
		this.but_ykienkhav = new javax.swing.JButton();
		this.b2 = new javax.swing.JButton();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setLocation(new java.awt.Point(150, 100));
		this.setMinimumSize(new java.awt.Dimension(700, 400));
		this.setPreferredSize(new java.awt.Dimension(1050, 500));

		this.but_dongy.setText("đồng ý");
		this.but_dongy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b1ActionPerformed(evt);
				} catch (SQLException | IOException e) {
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

		this.but_gopy.setText("góp ý");
		this.but_gopy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b3ActionPerformed(evt);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		this.but_khongdongy.setText("không đồng ý");
		this.but_khongdongy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b4ActionPerformed(evt);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		this.b5.setText("write log");
		this.b5.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b5ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		this.b6.setText("6");
		this.b6.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b6ActionPerformed(evt);
				} catch (SQLException | IOException e) {
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
					AssignLabel2.this.jButton1ActionPerformed(evt);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		this.jButton2.setText("delete");

		this.but_ykienkhav.setText("ý kiến khác");
		this.but_ykienkhav.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.butdifActionPerformed(evt);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
			}
		});

		this.b2.setText("open url");
		this.b2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.b2ActionPerformed(evt);
				} catch (SQLException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		this.reload.setText("load");
		this.reload.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					AssignLabel2.this.butreloadActionPerformed(evt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.jLabel1)).addGroup(
								layout.createSequentialGroup().addContainerGap().addComponent(this.ID))
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel3))
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel2)))
				.addGap(18, 18,
						18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
						.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(this.id, javax.swing.GroupLayout.PREFERRED_SIZE, 229,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(343, 343, 343).addComponent(this.jLabel4))
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(this.jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 606,
												Short.MAX_VALUE)
										.addComponent(this.url)))
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(36, 36, 36)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(this.jButton2).addComponent(this.did,
														javax.swing.GroupLayout.PREFERRED_SIZE, 77,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(layout.createSequentialGroup().addGap(18, 18, 18)
										.addComponent(this.uid, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(this.ulb, javax.swing.GroupLayout.PREFERRED_SIZE, 62,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(this.b2).addComponent(this.jButton1))
										.addContainerGap(98, Short.MAX_VALUE))))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(this.label, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup().addComponent(this.but_dongy)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(this.but_gopy)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(this.but_khongdongy).addGap(27, 27, 27)
												.addComponent(this.but_ykienkhav).addGap(102, 102, 102)
												.addComponent(this.b5).addGap(102, 102, 102).addComponent(this.reload)
												.addComponent(this.b6).addGap(102, 102, 102)))
								.addContainerGap()))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(58, 58, 58).addComponent(this.jLabel1))
						.addGroup(layout.createSequentialGroup().addGap(66, 66, 66)
								.addComponent(this.did, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18).addComponent(this.jButton2))
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(this.id, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(this.ID))
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(this.jLabel4)
								.addComponent(this.uid, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(this.ulb, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(this.jButton1)))
				.addGap(28, 28, 28)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(this.url, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(this.jLabel3))
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(this.jLabel2).addComponent(this.label, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(this.but_dongy).addComponent(this.but_gopy).addComponent(this.but_khongdongy)
						.addComponent(this.b5).addComponent(this.reload).addComponent(this.but_ykienkhav)
						.addComponent(this.b2).addComponent(this.reload).addComponent(this.b6))
				.addGap(24, 24, 24)));

		this.pack();
	}

	protected void b6ActionPerformed(ActionEvent evt) throws IOException, SQLException {
		this.bw2.write(String.format("%-12s", this.ma) + "6");
		this.bw2.newLine();
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	protected void butreloadActionPerformed(ActionEvent evt) throws SQLException {
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	protected void butdifActionPerformed(ActionEvent evt) throws IOException, SQLException {
		this.bw2.write(String.format("%-12s", this.ma) + "4");
		this.bw2.newLine();
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException, IOException {
		this.bw2.write(String.format("%-12s", this.ma) + "1");
		this.bw2.newLine();
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b2ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		Runtime.getRuntime()
				.exec(new String[] { "cmd", "/c",
						"start \"C:\\Users\\admin\\AppData\\Local\\CocCoc\\Browser\\Application\\browser.exe\" "
								+ this.url.getText() });
	}

	private void b3ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException, IOException {
		this.bw2.write(String.format("%-12s", this.ma) + "3");
		this.bw2.newLine();
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void b4ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException, IOException {
		this.bw2.write(String.format("%-12s", this.ma) + "2");
		this.bw2.newLine();
		if (this.chay < this.ac.size()) {
			this.chay++;
			this.load();
		} else {
			JOptionPane.showMessageDialog(this, "het roi");
		}
	}

	private void load() throws SQLException {
		this.ma = this.ac.get(this.chay).getId();
		String s = this.ac.get(this.chay).getContent();
		String line = "";
		while (s.length() > 80) {
			line += s.substring(0, 80) + "\n";
			s = s.substring(80);
		}
		line += s;
		this.cmt.setText(line);
		try {
			this.url.setText(this.ac.get(this.chay).getUrl());
			if (this.ac.get(this.chay).getUrl().indexOf("thanhnien") > 0) {
				this.did.setText(Utils.getThanhNienPageId(this.ac.get(this.chay).getUrl()));
			} else {
				this.did.setText(Utils.getVnExpressPageId(this.ac.get(this.chay).getUrl()));
			}
		} catch (Exception e) {

		}
		this.id.setText(this.ma + "");
	}

	private void b5ActionPerformed(java.awt.event.ActionEvent evt)
			throws SQLException, IOException, InterruptedException {
		this.bw.write(String.format("%-6s", this.ac.get(this.chay).getId()));
		this.bw.write(String.format("%-200s", this.url.getText()));
		this.bw.write(this.ac.get(this.chay).getContent() + " ");
		this.bw.newLine();
		this.bw.flush();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException, IOException {
		this.bw2.write("update:" + String.format("%-12s", this.uid.getText()) + this.ulb.getText() + "\n");
	}

}
