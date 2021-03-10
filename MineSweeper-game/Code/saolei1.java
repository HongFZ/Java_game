import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class saolei1 implements MouseListener {
	JFrame frame = new JFrame("É¨À×");
	JPanel panel = new JPanel();
	JButton reset = new JButton("Restart");
	JLabel timelabel = new JLabel("00:00:00:00");
	JLabel LeiNumlabel = new JLabel("       Bombs:");
	JLabel time = new JLabel("      Time:");

	JMenuBar menuBar = new JMenuBar();
	JMenu m = new JMenu("Main Menu");
	JMenuItem i1 = new JMenuItem("Customize");
	JMenuItem i2 = new JMenuItem("Rank");
	JMenuItem i3 = new JMenuItem("History");

	Container container = new Container();

	static int row = 10;
	static int col = 10;
	static int num = 10;
	private int remain;
	private long pStart;
	final int MARK = 50;

	TimingThread thread = new TimingThread();
	JLabel LeiNum;
	JButton[][] buttons;
	int[][] counts;

	public saolei1() {
		frame.setSize(500, 500);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		reset.setBackground(Color.white);
		reset.setOpaque(true);
		reset.addMouseListener(this);

		frame.getContentPane().add(reset, BorderLayout.SOUTH);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 5, 0, 0));

		panel.add(menuBar);
		m.setHorizontalAlignment(SwingConstants.CENTER);
		m.setFont(new Font("Algerian", Font.BOLD, 15));
		menuBar.add(m);
		i1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectLevel1();
			}
		});
		m.add(i1);
		m.add(i2);
		
		m.add(i3);
		panel.add(time);
		panel.add(timelabel);
		panel.add(LeiNumlabel);

		menuBar.setFont(new Font("Algerian", Font.BOLD, 15));
		reset.setFont(new Font("Algerian", Font.BOLD, 15));
		time.setFont(new Font("Algerian", Font.BOLD, 15));
		LeiNumlabel.setFont(new Font("Algerian", Font.BOLD, 15));

		counts = new int[row][col];
		buttons = new JButton[row][col];
		remain = num;
		LeiNum = new JLabel("   " + String.valueOf(remain));
		panel.add(LeiNum);

		addButtons();
		RandAsign();
		cal();

		frame.setVisible(true);
		thread.start();
	}

	public void SelectLevel1() {
		thread.stopped = true;
		int userOption = JOptionPane.showConfirmDialog(null, "Customize", "Default", JOptionPane.YES_NO_CANCEL_OPTION);
		if (userOption == JOptionPane.YES_OPTION) {
			String r = JOptionPane.showInputDialog("Please input row numbers");
			String c = JOptionPane.showInputDialog("Please input column numbers");
			String n = JOptionPane.showInputDialog("Please input bomb numbers");

			row = Integer.parseInt(r);
			col = Integer.parseInt(c);
			num = Integer.parseInt(n);

		} else {
			restart();
			return;
		}
		counts = new int[row][col];
		buttons = new JButton[row][col];
		remain = num;
		LeiNum.setText("   " + String.valueOf(remain));

		resetButtons();
		RandAsign();
		cal();
		timelabel.setText("00:00:00:00");
	}

	public void SelectLevel2() {

		thread.stopped = true;
		SelectLevelWindow w = new SelectLevelWindow();
		Thread wait = new Thread(new Runnable() {

			@Override
			public void run() {
				while (w.flag) {
				}

			}
		});
		try {
			wait.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		row = w.r;
		col = w.c;
		num = w.n;
		counts = new int[row][col];
		buttons = new JButton[row][col];
		remain = num;
		LeiNum.setText("   " + String.valueOf(remain));

		resetButtons();
		RandAsign();
		cal();
		timelabel.setText("00:00:00:00");

	}

	public void RandAsign() {
		Random rand = new Random();
		int rR, rC;
		for (int i = 0; i < num; i++) {
			rR = rand.nextInt(row);
			rC = rand.nextInt(col);
			if (counts[rR][rC] == MARK) {
				i--;
			} else {
				counts[rR][rC] = MARK;
			}
		}
	}

	public void addButtons() {
		frame.getContentPane().add(container, BorderLayout.CENTER);
		container.setLayout(new GridLayout(row, col));

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				JButton button = new JButton();
				button.setBackground(Color.gray);
				button.setOpaque(true);
				button.addMouseListener(this);
				;
				buttons[i][j] = button;
				container.add(button);
			}
		}

	}

	public void resetButtons() {
		container.removeAll();
		container.setLayout(new GridLayout(row, col));

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				JButton button = new JButton();
				button.setBackground(Color.gray);
				button.setOpaque(true);
				button.addMouseListener(this);
				;
				buttons[i][j] = button;
				container.add(button);
			}
		}
	}

	public void cal() {
		int count;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				count = 0;
				if (counts[i][j] == MARK)
					continue;
				if (i > 0 && j > 0 && counts[i - 1][j - 1] == MARK)
					count++;
				if (i > 0 && counts[i - 1][j] == MARK)
					count++;
				if (i > 0 && j < num - 1 && counts[i - 1][j + 1] == MARK)
					count++;
				if (j > 0 && counts[i][j - 1] == MARK)
					count++;
				if (j < num - 1 && counts[i][j + 1] == MARK)
					count++;
				if (i < num - 1 && j > 0 && counts[i + 1][j - 1] == MARK)
					count++;
				if (i < num - 1 && counts[i + 1][j] == MARK)
					count++;
				if (i < num - 1 && j < num - 1 && counts[i + 1][j + 1] == MARK)
					count++;

				counts[i][j] = count;
			}
		}
	}

	public void restart() {
		thread.stopped = true;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				buttons[i][j].setText("");
				buttons[i][j].setEnabled(true);
				buttons[i][j].setBackground(Color.gray);
				counts[i][j] = 0;
			}
		}
		RandAsign();
		cal();
		timelabel.setText("00:00:00:00");

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		if (button.equals(reset)) {
			restart();
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			if (button.getBackground() == Color.gray) {
				button.setBackground(Color.red);
				if (remain != 0) {
					remain--;
					LeiNum.setText("   " + String.valueOf(remain));
				}
			} else if (button.getBackground() == Color.red) {
				button.setBackground(Color.gray);
				if (remain != num) {
					remain++;
					LeiNum.setText("   " + String.valueOf(remain));
				}
			}
		} else {
			if (timelabel.getText().equals("00:00:00:00")) {
				thread.stopped = false;
				pStart = System.currentTimeMillis();

			}
			int count = 0;
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					if (button.equals(buttons[i][j])) {
						count = counts[i][j];
						if (count == MARK) {
							Lose();
						} else {
							open(i, j);
							Win();
						}
						return;
					}
				}
			}

		}
	}

	void Win() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (buttons[i][j].isEnabled() == true && counts[i][j] != MARK)
					return;
			}
		}
		thread.stopped = true;
		JOptionPane.showMessageDialog(frame, "Yeah,you won£¡");
	}

	void open(int i, int j) {
		if (buttons[i][j].isEnabled() == false)
			return;

		buttons[i][j].setEnabled(false);

		if (counts[i][j] == 0) {
			if (i > 0 && j > 0 && counts[i - 1][j - 1] != MARK)
				open(i - 1, j - 1);
			if (i > 0 && counts[i - 1][j] != MARK)
				open(i - 1, j);
			if (i > 0 && j < num - 1 && counts[i - 1][j + 1] != MARK)
				open(i - 1, j + 1);
			if (j > 0 && counts[i][j - 1] != MARK)
				open(i, j - 1);
			if (j < num - 1 && counts[i][j + 1] != MARK)
				open(i, j + 1);
			if (i < num - 1 && j > 0 && counts[i + 1][j - 1] != MARK)
				open(i + 1, j - 1);
			if (i < num - 1 && counts[i + 1][j] != MARK)
				open(i + 1, j);
			if (i < num - 1 && j < num - 1 && counts[i + 1][j + 1] != MARK)
				open(i + 1, j + 1);

			buttons[i][j].setBackground(Color.white);

		} else {
			buttons[i][j].setText(counts[i][j] + "");
			buttons[i][j].setBackground(Color.white);
		}
	}

	void Lose() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				int count = counts[i][j];
				if (count == MARK) {
					buttons[i][j].setText("X");
					buttons[i][j].setBackground(Color.red);
					buttons[i][j].setEnabled(false);
				} else if (count != 0) {
					buttons[i][j].setText(count + "");
					buttons[i][j].setBackground(Color.white);
					buttons[i][j].setEnabled(false);
				} else {
					buttons[i][j].setBackground(Color.white);
					buttons[i][j].setEnabled(false);
				}
			}
		}
		thread.stopped = true;
		JOptionPane.showMessageDialog(frame, "Sorry, you lost...");
	}

	private class TimingThread extends Thread {

		public boolean stopped = true;

		@Override
		public void run() {
			while (true) {
				if (!stopped) {
					long duration = System.currentTimeMillis() - pStart;
					timelabel.setText(format(duration));
				}

				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

		private String format(long duration) {
			int hour, minute, second, milli;

			milli = (int) (duration % 1000);
			duration = duration / 1000;

			second = (int) (duration % 60);
			duration = duration / 60;

			minute = (int) (duration % 60);
			duration = duration / 60;

			hour = (int) (duration % 60);

			return String.format("%02d:%02d:%02d:%03d", hour, minute, second, milli);
		}
	}

	public static void main(String[] args) {
		saolei1 lei = new saolei1();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

}
