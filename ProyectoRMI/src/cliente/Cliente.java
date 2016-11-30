package cliente;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import servidor.Coche;
import servidor.DatoCoche;
import servidor.InterfaceServidor;

public class Cliente extends Thread implements KeyListener {
	private InterfaceServidor server;
	private int codigo;
	private List<Coche> coches;
	
	private boolean arriba;
	private boolean abajo;
	private boolean izquierda;
	private boolean derecha;
	
	private BufferedImage fondo;
	private MediaPlayer sonido;
	
	public Cliente() {
		try {
			String ip = JOptionPane.showInputDialog("Introduce la IP");
			if(ip == null || ip.length() <= 0) {
				ip = "127.0.0.1";
			}
			server = (InterfaceServidor) Naming.lookup("rmi://" + ip + "/serverProyecto");
			codigo = server.obtenerCodigo();
			
			fondo = ImageIO.read(this.getClass().getClassLoader().getResource("fondo.jpg"));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}		
		
		try {
			new JFXPanel();
	        Media hit;
			hit = new Media(this.getClass().getClassLoader().getResource("musica.mp3").toURI().toString());
			sonido = new MediaPlayer(hit);
			sonido.setVolume(0.5);
	        sonido.setOnEndOfMedia(new Runnable() {
	            public void run() {
	            	sonido.seek(Duration.ZERO);
	            }
	        });
	        sonido.play();
		} catch (URISyntaxException e) {
		}  
		
		coches = new ArrayList<Coche>();
		Pantalla pantalla = new Pantalla(this);
		pantalla.addKeyListener(this);
		
		start();
	}
	
	public static void main(String[] args) {
		new Cliente();
	}
	
	private void actualizarDatosCoches() {
		List<DatoCoche> cochesServer = new ArrayList<DatoCoche>();
		try {
			cochesServer = server.getDatosCoches();
			for(DatoCoche datos : cochesServer) {
				boolean encontrado = false;
				for(Coche coche : coches) {
					if(coche.getCodigoCliente() == datos.getCodigoCliente()) {
						encontrado = true;
						coche.actualizar(datos);
						break;
					}
				}
				if(!encontrado) {
					coches.add(datos.construirCoche());
				}
			}
		} catch (RemoteException e) {
		}
		if(coches.size() > cochesServer.size()) {
			for(DatoCoche dato : cochesServer) {
				boolean encontrado = false;
				for(Coche coche : coches) {
					if(coche.getCodigoCliente() == dato.getCodigoCliente()) {
						encontrado = true;
					}
				}
				if(!encontrado) {
					cochesServer.remove(dato);
					break;
				}
			}
		}
	}
	
	public void desconectar() {
		try {
			server.desconectarCliente(codigo);
		} catch(Exception e) {
			
		}
		System.exit(0);
	}
	
	public List<Coche> getCoches() {
		actualizarDatosCoches();
		return coches;
	}
	
	public void pintarPista(Graphics2D g) {
		g.drawImage(fondo, 0, 0, Pantalla.WIDTH, Pantalla.HEIGHT, null);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if(arriba) {
					server.moverArriba(codigo);
				}
				if(abajo) {
					server.moverAbajo(codigo);
				}
				if(izquierda) {
					server.moverIzquierda(codigo);
				}
				if(derecha) {
					server.moverDerecha(codigo);
				}
				Thread.sleep(20);
			} catch(InterruptedException | RemoteException e) {
				
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba = true;
			break;
		case KeyEvent.VK_S:
			abajo = true;
			break;
		case KeyEvent.VK_A:
			izquierda = true;
			break;
		case KeyEvent.VK_D:
			derecha = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba = false;
			break;
		case KeyEvent.VK_S:
			abajo = false;
			break;
		case KeyEvent.VK_A:
			izquierda = false;
			break;
		case KeyEvent.VK_D:
			derecha = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
