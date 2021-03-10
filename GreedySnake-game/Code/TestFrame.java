import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class TestFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 417);
		
		GameJPanel contentPane = new GameJPanel(getWidth(), getHeight());
		addKeyListener(contentPane);
		new Thread(contentPane).start();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
}

class GameJPanel extends JPanel implements KeyListener, Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static int SIZE = 20;
	final static int DELAY = 1000;

	int width, height;
	LinkedList<Point> bodies = new LinkedList<Point>();
	HashSet<Point> blocks = new HashSet<Point>();
	
	public GameJPanel(int w, int h) {
		// TODO Auto-generated constructor stub
		width = w;
		height = h;
		bodies.add(new Point(200, 200));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		for(Point p: bodies) {
			g.fillRect(p.x, p.y, SIZE, SIZE);
		}
		for(Point p: blocks) {
			g.fillRect(p.x, p.y, SIZE, SIZE);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int x = bodies.getFirst().x;
		int y = bodies.getFirst().y;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			y -= SIZE;
			break;
		case KeyEvent.VK_DOWN:
			y += SIZE;
			break;
		case KeyEvent.VK_LEFT:
			x -= SIZE;
			break;
		case KeyEvent.VK_RIGHT:
			x += SIZE;
			break;
		default:
			return;
		}
		Point p = new Point(x, y);
		bodies.addFirst(p);
		if(blocks.contains(p)) {
			blocks.remove(p);
		}else {
			bodies.removeLast();
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random r = new Random();
		while(true) {
			while(true) {
				int x = r.nextInt(width) / SIZE * SIZE;
				int y = r.nextInt(height) / SIZE * SIZE;
				Point p = new Point(x, y);
				if(!blocks.contains(p)) {
					blocks.add(p);
					repaint();
					break;
				}
			}
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}