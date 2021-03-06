package servidor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Servidor extends UnicastRemoteObject implements InterfaceServidor, Runnable {
	private static final long serialVersionUID = -7837459943188684283L;
	private static int clientes = 0;
	
	private List<Coche> coches; 
	private BufferedImage pista;
	private BufferedImage pistaInterior;
	private Area areaPista;
	private Area areaPistaInterior;
	
	private Rectangle meta_uno;
	private Rectangle meta_dos;
	private Rectangle meta_tres;
	private Rectangle meta_cuatro;

	protected Servidor() throws RemoteException {
		super();	
		
		System.out.println("Abriendo servidor...");
		coches = new ArrayList<Coche>();
		
		try {
			String ip = JOptionPane.showInputDialog("Introduce la ip local");
			if(ip == null || ip.length() <= 0) {
				ip = "127.0.0.1";
			}
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmi://" + ip + "/serverProyecto", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Generando pista...");
		try {
			pista = ImageIO.read(this.getClass().getClassLoader().getResource("pista.png"));
			pistaInterior = ImageIO.read(this.getClass().getClassLoader().getResource("pistaInterior.png"));
		} catch(IOException e) {
			System.out.println("Error al cargar texturas");
			System.exit(0);
		}
		areaPista = calcularArea(pista, new Color(134, 134, 134));
		areaPistaInterior = calcularArea(pistaInterior, new Color(255, 0, 0));
		areaPista.subtract(areaPistaInterior);
		
		meta_uno = new Rectangle(480, 220, 80, 25);
		meta_dos = new Rectangle(280, 20, 25, 80);		
		meta_tres = new Rectangle(240, 487, 25, 80);
		meta_cuatro = new Rectangle(202, 260, 25, 80);
		
		(new Thread(this)).start();
		System.out.println("Servidor abierto");
	}
	
	public static void main(String[] args) {
		try {
			new Servidor();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Area calcularArea(BufferedImage imagen, Color target) {
		GeneralPath gp = new GeneralPath();
		
	    boolean cont = false;
	    int targetRGB = target.getRGB();
	    for (int xx=0; xx<imagen.getWidth(); xx++) {
	        for (int yy=0; yy<imagen.getHeight(); yy++) {
	            if (imagen.getRGB(xx,yy)==targetRGB) {
	                if (cont) {
	                    gp.lineTo(xx,yy);
	                    gp.lineTo(xx,yy+1);
	                    gp.lineTo(xx+1,yy+1);
	                    gp.lineTo(xx+1,yy);
	                    gp.lineTo(xx,yy);
	                } else {
	                    gp.moveTo(xx,yy);
	                }
	                cont = true;
	            } else {
	                cont = false;
	            }
	        }
	        cont = false;
	    }
	    gp.closePath();

	    return new Area(gp);
	}
	
	public List<Coche> getCoches() {
		return coches;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				for(Coche coche : coches) {
					coche.mover();
				}
				Thread.sleep(20);
			} catch(InterruptedException e) {
				
			}
		}
	}

	@Override
	public List<DatoCoche> getDatosCoches() throws RemoteException {
		List<DatoCoche> datos = new ArrayList<DatoCoche>();
		for(Coche coche : coches) {
			datos.add(coche.getDatosCoche());
		}
		return datos;
	}

	@Override
	public int obtenerCodigo(String nombre) throws RemoteException {		
		int codigo = clientes;
		clientes++;
		
		if(nombre == null || nombre.length() <= 0) {
			nombre = "Jugador " + codigo;
		}
		
		Color color;
		switch(codigo%5) {
		case 0:
			color = new Color(244, 75, 66);
			break;
		case 1:
			color = new Color(98, 244, 66);
			break;
		case 2:
			color = new Color(241, 244, 66);
			break;
		case 3:
			color = new Color(66, 134, 244);
			break;
		case 4:
			color = Color.WHITE;
			break;
		default:
			color = Color.BLACK;
		}
		Coche c = new Coche(this, color, codigo, nombre);
		coches.add(c);	
		
		return codigo;
	}

	@Override
	public void desconectarCliente(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coches.remove(coche);
				return;
			}
		}
	}

	@Override
	public void moverArriba(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coche.moverAlante();
				return;
			}
		}
	}

	@Override
	public void moverAbajo(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coche.moverAtras();
				return;
			}
		}
	}

	@Override
	public void moverIzquierda(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coche.girarIzquierda();
				return;
			}
		}
	}

	@Override
	public void moverDerecha(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coche.girarDerecha();
				return;
			}
		}
	}
	
	@Override
	public void turbo(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				coche.turbo();
				return;
			}
		}
	}
	
	public Area obtenerPista() {
		return areaPista;
	}

	@Override
	public double getTurbo(int codigo) throws RemoteException {
		for(Coche coche : coches) {
			if(coche.getCodigoCliente() == codigo) {
				return coche.getTurbo();
			}
		}
		return 0.0;
	}
}
