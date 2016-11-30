package servidor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import cliente.Pantalla;
import mates.Vector2D;

public class Coche {	
	private Vector2D posicion;
	private int ancho;
	private int alto;
	private int codigoCliente;
	private double angulo;
	private double velocidad;
	private double turbo;
	private Servidor servidor;
	private Color color;
	private BufferedImage texturaCoche;		
	private String nombre;
	
	public Coche(Servidor servidor, Color color, int codigoCliente, String nombre) {
		this(servidor, color, 525, 225, 10, 20, codigoCliente, 0, nombre);
	}
	
	public Coche(Servidor servidor, Color color, double x, double y, int ancho, int alto, int codigoCliente, double angulo, String nombre) {
		posicion = new Vector2D(x, y);
		
		this.color = color;
		this.servidor = servidor;
		this.ancho = ancho;
		this.alto = alto;
		this.codigoCliente = codigoCliente;
		this.angulo = angulo;
		this.nombre = nombre;
		this.turbo = 5.0;
		
		velocidad = 0.0;
		
		if(servidor == null) {
			try {
				texturaCoche = ImageIO.read(this.getClass().getClassLoader().getResource("coche.png"));
				texturaCoche = pintarImagen(texturaCoche, color);
			} catch(Exception e) {
				System.out.println("Error al cargar textura del coche");
				System.exit(0);
			}
		}
	}
	
	public void actualizar(DatoCoche datos) {
		posicion.setLocation(datos.getX(), datos.getY());
		angulo = datos.getAngulo();
	}
	
	public void mover() {
		if(servidor == null) {
			return;
		}
		if(turbo < 5) {
			turbo += 0.05;
		}
		double x = posicion.getX() + Math.sin(angulo) * velocidad;
		double y = posicion.getY() + Math.cos(angulo) * velocidad * -1;
		
		posicion.setLocation(limiteAncho(x), limiteAlto(y));
		
		if(!servidor.obtenerPista().contains(posicion.getX(), posicion.getY())) {
			chocar();
		}
		
		if(servidor != null) {
			for(Coche coche : servidor.getCoches()) {
				if(coche.getCodigoCliente() == this.codigoCliente) {
					continue;
				}
				if(coche.obtenerHitbox().getBounds2D().contains(this.posicion.getX(), this.posicion.getY())) {
					this.chocar();
					break;
				}
			}
		}
		
		if(velocidad > 0) {
			velocidad -= 0.05;
		} else if(velocidad < 0) {
			velocidad += 0.05;
		}
		
		velocidad = velocidad > 5 ? 5 : velocidad;
		velocidad = velocidad < -5 ? -5 : velocidad;
	}
	
	public void pintar(Graphics2D g) {
		AffineTransform trans = g.getTransform();
		
		g.rotate(angulo, posicion.getX(), posicion.getY());
		
		int x = (int) (posicion.getX() - ancho / 2);
		int y = (int) (posicion.getY() - alto / 2);
		g.drawImage(texturaCoche, x, y, ancho, alto, null);
		
		g.setTransform(trans);
		
		x = (int) (posicion.getX() - g.getFontMetrics().stringWidth(nombre) / 2);
		g.setColor(Color.WHITE);
		g.drawString(nombre, x, y - 10);
	}
	
	private void chocar() {
		velocidad *= -1.05;
		
		double x = posicion.getX() + Math.sin(angulo) * velocidad;
		double y = posicion.getY() + Math.cos(angulo) * velocidad * -1;
		
		posicion.setLocation(limiteAncho(x), limiteAlto(y));
	}
	
	private BufferedImage pintarImagen(BufferedImage img, Color color) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
	    Graphics2D graphics = res.createGraphics();
	    graphics.drawImage(img, 0, 0, null);
	    graphics.dispose();

	    float r = color.getRed() / 255.0F;
	    float g = color.getGreen() / 255.0F;
	    float b = color.getBlue() / 255.0F;
	    float a = color.getAlpha() / 255.0F;
	    
	    for (int i = 0; i < res.getWidth(); i++)
	    {
	      for (int j = 0; j < res.getHeight(); j++)
	      {
	        int ax = res.getColorModel().getAlpha(res.getRaster().getDataElements(i, j, null));	        
	        if(ax == 0) {
	        	continue;
	        }
	        
	        int rx = res.getColorModel().getRed(res.getRaster().getDataElements(i, j, null));
	        int gx = res.getColorModel().getGreen(res.getRaster().getDataElements(i, j, null));
	        int bx = res.getColorModel().getBlue(res.getRaster().getDataElements(i, j, null));
	        
	        rx *= r;
	        gx *= g;
	        bx *= b;
	        ax *= a;
	        
	        res.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
	      }
	    }
	    return res;
	}
	
	public void moverAlante() {
		if(velocidad < 3) {
			velocidad += 0.1;
		}
	}
	
	public void moverAtras() {
		if(velocidad > -3) {
			velocidad -= 0.1;
		}
	}
	
	public void girarIzquierda() {
		angulo -= 0.1;
	}
	
	public void girarDerecha() {
		angulo += 0.1;
	}
	
	public void turbo() {
		if(turbo >= 0.2) {
			turbo -= 0.2;
			if(velocidad < 5) {
				velocidad += 0.1;
			}
		}
	}
	
	public DatoCoche getDatosCoche() {
		return new DatoCoche(color, posicion.getX(), posicion.getY(), ancho, alto, codigoCliente, angulo, nombre);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigoCliente;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coche other = (Coche) obj;
		if (codigoCliente != other.codigoCliente)
			return false;
		return true;
	}
}
