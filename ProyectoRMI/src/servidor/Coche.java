package servidor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import mates.Vector2D;

public class Coche {
	private Vector2D posicion;
	private int ancho;
	private int alto;
	
	public Coche(double x, double y, int ancho, int alto) {
		posicion = new Vector2D(x, y);
		this.ancho = ancho;
		this.alto = alto;
	}
	
	public void mover() {
		posicion.setLocation(posicion.getX() + 1, posicion.getY() + 1);
	}
	
	public void pintar(Graphics2D g) {
		AffineTransform trans = g.getTransform();
		
		int x = (int) (posicion.getX() - ancho / 2);
		int y = (int) (posicion.getY() - alto / 2);
		g.setColor(Color.RED);
		g.fillRect(x, y, ancho, alto);
		
		g.setTransform(trans);
	}
	
	public DatoCoche getDatosCoche() {
		return new DatoCoche(posicion.getX(), posicion.getY(), ancho, alto);
	}
}
