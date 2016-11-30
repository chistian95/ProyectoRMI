package servidor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import cliente.Pantalla;
import mates.Vector2D;

public class Coche {
	private Vector2D posicion;
	private int ancho;
	private int alto;
	private int codigoCliente;
	private double angulo;
	private double velocidad;
	private Servidor servidor;
	private Color color;
	
	public Coche(Servidor servidor, Color color, int codigoCliente) {
		this(servidor, color, 525, 225, 10, 20, codigoCliente, 0);
	}
	
	public Coche(Servidor servidor, Color color, double x, double y, int ancho, int alto, int codigoCliente, double angulo) {
		posicion = new Vector2D(x, y);
		
		this.color = color;
		this.servidor = servidor;
		this.ancho = ancho;
		this.alto = alto;
		this.codigoCliente = codigoCliente;
		this.angulo = angulo;
		
		velocidad = 0.0;
	}
	
	public void mover() {
		if(servidor == null) {
			return;
		}
		double x = posicion.getX() + Math.sin(angulo) * velocidad;
		double y = posicion.getY() + Math.cos(angulo) * velocidad * -1;
		
		posicion.setLocation(limiteAncho(x), limiteAlto(y));
		
		Rectangle2D rec = obtenerHitbox().getBounds2D();
		if(!servidor.obtenerPista().contains(rec)) {
			if(velocidad > 0) {
				velocidad = -2;
			} else if(velocidad < 0) {
				velocidad = 2;
			}
			x = posicion.getX() + Math.sin(angulo) * velocidad;
			y = posicion.getY() + Math.cos(angulo) * velocidad * -1;
			
			posicion.setLocation(limiteAncho(x), limiteAlto(y));
		}
		
		if(velocidad > 0) {
			velocidad -= 0.05;
		} else if(velocidad < 0) {
			velocidad += 0.05;
		}
	}
	
	public void pintar(Graphics2D g) {
		AffineTransform trans = g.getTransform();
		
		g.rotate(angulo, posicion.getX(), posicion.getY());
		
		int x = (int) (posicion.getX() - ancho / 2);
		int y = (int) (posicion.getY() - alto / 2);
		g.setColor(color);
		g.fillRect(x, y, ancho, alto);
		
		g.setTransform(trans);
	}
	
	public void moverAlante() {
		velocidad = velocidad > 3 ? 3 : velocidad + 0.1;
	}
	
	public void moverAtras() {
		velocidad = velocidad < -3 ? -3 : velocidad - 0.1;
	}
	
	public void girarIzquierda() {
		angulo -= 0.1;
	}
	
	public void girarDerecha() {
		angulo += 0.1;
	}
	
	public DatoCoche getDatosCoche() {
		return new DatoCoche(color, posicion.getX(), posicion.getY(), ancho, alto, codigoCliente, angulo);
	}
	
	public Shape obtenerHitbox() {
		Rectangle2D rect = new Rectangle2D.Double(posicion.getX() - ancho / 2, posicion.getY() - alto / 2, ancho, alto);
		AffineTransform at = AffineTransform.getRotateInstance(angulo, posicion.getX(), posicion.getY());
		return at.createTransformedShape(rect);
	}
	
	private double limiteAncho(double dx) {
		dx = dx < 0 ? 0 : dx;
		dx = dx >= Pantalla.WIDTH ? Pantalla.WIDTH - 1 : dx;
		
		return dx;
	}
	
	private double limiteAlto(double dy) {
		dy = dy < 0 ? 0 : dy;
		dy = dy >= Pantalla.HEIGHT ? Pantalla.HEIGHT - 1 : dy;
		
		return dy;
	}
	
	public int getCodigoCliente() {
		return codigoCliente;
	}
	
	public Vector2D getPosicion() {
		return posicion;
	}
}
