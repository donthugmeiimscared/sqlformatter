package SQLFormatter.TheMainClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Frame {

	private JFrame frame; // Frame of application
	private final JSeparator separator = new JSeparator(); // Separator object for Interface
	private ImageIcon img = new ImageIcon("src/Images/uulogo.jpg"); // Display given image
	private JTextField textFieldIn; // Left hand text field (Input)
	private JTextField textFieldOut; // Right hand text field (Output)
	public static String sqlIn; // Variable used to get text from Left hand text field (Input)
	public static String[] words; // String list of the input text after splitting see sqlSplit()
	public static ArrayList<String> colList = new ArrayList<String>(); // Array list of words
	public static int changes = 0; // Counter for number of changes made when the formatting has completed
	public static long start = System.nanoTime(); // Gets start time in real time
	public static long finish = System.nanoTime(); // Gets finish time in real time
	public static long timeElapsed = finish - start; // Generates elapsed time in nano seconds
	public static double elapsedTimeInSecond = (double) timeElapsed / 1_000_000; // converts to seconds

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame() {
		initialise();
	}

	public static void sqlSplit() {

		// split the input into individual words and convert to default lowercase
		words = sqlIn.split("\\s+");
	}

	public static void sqlCaseChange() {
		// iterate over words and apply changes where necessary
		for (int x = 0; x < words.length; x++) {
			System.out.println("sqlCaseChange1: " + words[x]);
			if ("select".equals(words[x]) || "from".equals(words[x]) || "where".equals(words[x])
					|| "in".equals(words[x]) || "is".equals(words[x]) || "not".equals(words[x])
					|| "and".equals(words[x]) || "or".equals(words[x]) || "case".equals(words[x])
					|| "when".equals(words[x]) || "then".equals(words[x]) || "as".equals(words[x])
					|| "else".equals(words[x]) || "end".equals(words[x]) || "order".equals(words[x])
					|| "by".equals(words[x]) || "inner".equals(words[x]) || "outer".equals(words[x])
					|| "right".equals(words[x]) || "left".equals(words[x]) || "full".equals(words[x])
					|| "join".equals(words[x]) || "on".equals(words[x]) || "like".equals(words[x])
					|| "group".equals(words[x]) || "union".equals(words[x]) || "left(".startsWith(words[x])
					|| "count(".startsWith(words[x]) || "trim(".startsWith(words[x]) || "add_hours".startsWith(words[x])
					|| "now(".startsWith(words[x]) || "local.".startsWith(words[x]) || "length(".startsWith(words[x])
					|| "decode(".startsWith(words[x]) || "(select".startsWith(words[x]) || "join(".equals(words[x])
					|| "to_char(".startsWith(words[x]) || "nvl(".startsWith(words[x]) || "'".startsWith(words[x])
					|| "'".endsWith(words[x]) || words[x].contains("add_days(") || words[x].contains("now(")
					|| words[x].contains("desc") || words[x].contains("count(")) {
				words[x] = words[x].toLowerCase();
			} else {
				words[x] = words[x].toUpperCase();
				System.out.println("sqlCaseChange2: " + words[x]);
			}
		}
	}

	// Adds the indentation to the column names separately, easier to read in here
	public static void colNameIndent() {

		for (int z = 0; z < words.length; z++) {
			if ("select".equals(words[z])) {
				words[z] = words[z] + "\n\t";
			} else if ("from".equals(words[z])) {
				break;
			} else if ("as".equals(words[z])) {
				words[z - 1] = words[z - 1].trim();
				continue;
			} else {
				words[z] = words[z] + "\n\t";
			}
		}
	}

	// Add indentation before or after certain words
	public static void sqlIndent() {
		for (int y = 0; y < words.length; y++) {
			if ("from".equals(words[y]) || "where".equals(words[y]) || "on".equals(words[y])) {
				words[y] = "\n" + words[y] + "\n\t";
			} else if ("inner".equals(words[y]) || "outer".equals(words[y]) || "left".equals(words[y])
					|| "right".equals(words[y]) || "full".equals(words[y]) || "order".equals(words[y])) {
				words[y] = "\n" + words[y];
			} else if ("join".equals(words[y]) || "by".equals(words[y]) || "and".equals(words[y])
					|| "join(".equals(words[y])) {
				words[y] = words[y] + "\n\t";
			} else if ("on".equals(words[y])) {
				words[y] = "\n" + words[y] + "\n\t";
			} else if ("case".equals(words[y])) {
				words[y] = "\n\t" + words[y] + "\n\t\t";
			}
		}
	}

	public void initialise() {
		frame = new JFrame("IQM SQL Formatter");

		frame.setBounds(100, 100, 745, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(separator);
		frame.setResizable(false);
		frame.setIconImage(img.getImage());


		separator.setBounds(0, 0, 0, 0);

		JTextPane txtPaneIn = new JTextPane();
		txtPaneIn.setToolTipText("Paste your SQL");
		txtPaneIn.setBounds(10, 67, 349, 635);
		frame.getContentPane().add(txtPaneIn);

		JTextPane txtPaneOut = new JTextPane();
		txtPaneOut.setToolTipText("Formatted SQL output");
		txtPaneOut.setBounds(369, 67, 349, 635);
		frame.getContentPane().add(txtPaneOut);

		// Format button functionality
		JButton btnFormat = new JButton("Format SQL");
		btnFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				changes = 0;
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				sqlIn = txtPaneIn.getText().toLowerCase();
				sqlIn = sqlIn.replace("\n", " ").replace("\t", " ").replace("\r", " ").replace("    ", " ")
						.replace("   ", " ").replace("  ", " ").replaceAll("\\s+(?=[),])", "");
				sqlSplit();
				sqlCaseChange();
				colNameIndent();
				sqlIndent();
				sqlIn = Arrays.toString(words).replace(", ", " ").replace("[", "").replace("]", "").replace("\t ", "\t")
						.trim();
				// Hacky formatting to remove some blank lines
				txtPaneOut.setText(sqlIn.replaceAll("(?m)^[ \t]*\r?\n", ""));
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		btnFormat.setBounds(202, 33, 328, 23);
		frame.getContentPane().add(btnFormat);

		JButton btnClearIn = new JButton("Clear");
		btnClearIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				txtPaneIn.setText(null);
			}

		});
		btnClearIn.setBounds(10, 7, 135, 23);
		frame.getContentPane().add(btnClearIn);

		JButton btnClearOut = new JButton("Clear");
		btnClearOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				txtPaneOut.setText(null);
			}

		});
		btnClearOut.setBounds(581, 7, 135, 23);
		frame.getContentPane().add(btnClearOut);

		textFieldIn = new JTextField("Input");
		textFieldIn.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldIn.setEditable(false);
		textFieldIn.setBounds(161, 6, 198, 24);
		frame.getContentPane().add(textFieldIn);

		textFieldOut = new JTextField("Output");
		textFieldOut.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldOut.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textFieldOut.setEditable(false);
		textFieldOut.setColumns(10);
		textFieldOut.setBounds(369, 6, 198, 24);
		frame.getContentPane().add(textFieldOut);
	}
}
