
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;



public class SquareRotate extends JFrame {
	public SquareRotate() {
		super("Square Rotation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Square square = new Square(20, 20, 100, 100);
		getContentPane().add(square);

		pack();
		setLocationRelativeTo(null);

		setVisible(true);
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new SquareRotate();
				
			}
		});
	}
}

class Square extends JPanel 
{
	private static final int PREF_W = 600;
	private static final int PREF_H = PREF_W;
	
	private Rectangle square;
	int i = 0, j = 0;
	int x = 0, y = 0;
	boolean flag = true;
	Timer timer = new Timer(50, new SpinTimerListener());
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		square = new Rectangle(200 + i, 200 + j, 100, 100);

		double angle = -Math.toRadians(x);

		g2.setColor(Color.red);
		g2.rotate(angle, 200, 200);
		g2.draw(square);

	}

	public Square(int x, int y, int width, int height) {
		Rectangle rect = new Rectangle(x, y, width, height);
		this.square = rect;
		setupKeyBinding();
	}

	public void rotate(boolean isRotation) {
		if (isRotation) { // rotate the square
			timer.start();
			System.out.println("Rotation started");		
			
		} else { // stop rotation
			timer.stop();
			System.out.println("Rotation stopped");		
		}
	}

	private void setupKeyBinding() {
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inMap = getInputMap(condition);
		ActionMap actMap = getActionMap();

		for (RotationController direction : RotationController.values()) {
			int key = direction.getKey();
			String name = direction.name();

			inMap.put(KeyStroke.getKeyStroke(key, 0), name);
			inMap.put(KeyStroke.getKeyStroke(key, InputEvent.SHIFT_DOWN_MASK),
					name);
			actMap.put(name, new KeyAction(this, direction));
		}
	}
	

	private class SpinTimerListener implements ActionListener {
	@Override
		public void actionPerformed(ActionEvent e) {
			y += 5;
			x += 5;
			repaint();
		}
	}
}

enum RotationController {
	START(KeyEvent.VK_UP), STOP(KeyEvent.VK_DOWN);

	private int key;

	private RotationController(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}
}

// Actions for the key binding
@SuppressWarnings("serial")
class KeyAction extends AbstractAction {
	private Square draw;
	private RotationController direction;

	public KeyAction(Square draw, RotationController direction) {
		this.draw = draw;
		this.direction = direction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (direction) {
		case START:
			draw.rotate(true);
			break;
		case STOP:
			draw.rotate(false);
			break;
		default:
			break;
		}
	}
}

