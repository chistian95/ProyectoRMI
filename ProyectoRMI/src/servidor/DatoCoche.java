package servidor;

import java.io.Serializable;

public class DatoCoche implements Serializable {
	private static final long serialVersionUID = 5677594733314160776L;
	
	private double x;
	private double y;
	private int ancho;
	private int alto;
	
	public DatoCoche(double x, double y, int ancho, int alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
	}

	public Coche construirCoche() {
		return new Coche(x, y, ancho, alto);
	}
}
