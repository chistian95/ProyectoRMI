package cliente;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

import servidor.Coche;

public class Pantalla extends JFrame implements Runnable, KeyListener {
	private static final long serialVersionUID = 4782356132041659314L;
	
	public final int WIDTH = 600;
	public final int HEIGHT = 600;
	
	private Cliente cliente;
	private BufferedImage bf;
	
	public Pantalla(Cliente cliente) {
		this.cliente = cliente;
		bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        
        addKeyListener(this);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		dispose();
        		System.exit(0);
        	}
        });
        
       	(new Thread(this)).start();
	}
	
	public void paint(Graphics g) {
		Graphics2D bff = (Graphics2D) bf.getGraphics();
		
		bff.setColor(Color.GREEN.darker());
		bff.fillRect(0, 0, WIDTH, HEIGHT);	
		
		try {
			for(Coche coche : cliente.getCoches()) {
				coche.pintar(bff);
			}
		} catch(ConcurrentModificationException e) {
			
		}		
		
		g.drawImage(bf, 0, 0, null);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				repaint();
				Thread.sleep(20);
			} catch(InterruptedException e) {
				
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			cliente.desconectar();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
