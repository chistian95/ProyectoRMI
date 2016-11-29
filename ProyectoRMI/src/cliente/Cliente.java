package cliente;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import servidor.Coche;
import servidor.DatoCoche;
import servidor.InterfaceServidor;

public class Cliente {
	private InterfaceServidor server;
	private int codigo;
	private List<Coche> coches;
	
	public Cliente() {
		try {
			server = (InterfaceServidor) Naming.lookup("rmi://127.0.0.1/serverProyecto");
			codigo = server.obtenerCodigo();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}		
		coches = new ArrayList<Coche>();
		new Pantalla(this);
	}
	
	public static void main(String[] args) {
		new Cliente();
	}
	
	private void actualizarDatosCoches() {
		coches.clear();
		try {
			for(DatoCoche datos : server.getDatosCoches()) {
				coches.add(datos.construirCoche());
			}
		} catch (RemoteException e) {
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
}
