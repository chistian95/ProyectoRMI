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
	
	public DatoCoche(Color color, double x, double y, int ancho, int alto, int codigoCliente, double angulo) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.codigoCliente = codigoCliente;
		this.angulo = angulo;
	}

	public Coche construirCoche() {
		return new Coche(null, color, x, y, ancho, alto, codigoCliente, angulo);
	}
}
