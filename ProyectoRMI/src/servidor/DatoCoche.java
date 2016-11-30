package servidor;

import java.awt.Color;
import java.io.Serializable;

public class DatoCoche implements Serializable {
	private static final long serialVersionUID = 5677594733314160776L;
	
	private Color color;
	private double x;
	private double y;
	private int ancho;
	private int alto;
	private int codigoCliente;
	private double angulo;
	private String nombre;
	private double turbo;
	
	public DatoCoche(Color color, double x, double y, int ancho, int alto, int codigoCliente, double angulo, String nombre, double turbo) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.codigoCliente = codigoCliente;
		this.angulo = angulo;
		this.nombre = nombre;
		this.turbo = turbo;
	}

	public Coche construirCoche() {
		return new Coche(null, color, x, y, ancho, alto, codigoCliente, angulo, nombre, turbo);
	}

	public Color getColor() {
		return color;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public int getCodigoCliente() {
		return codigoCliente;
	}

	public double getAngulo() {
		return angulo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public double getTurbo() {
		return turbo;
	}
}
