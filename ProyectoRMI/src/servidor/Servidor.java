package servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends UnicastRemoteObject implements InterfaceServidor, Runnable {
	private static final long serialVersionUID = -7837459943188684283L;
	
	private List<Coche> coches; 

	protected Servidor() throws RemoteException {
		super();		
		
		coches = new ArrayList<Coche>();		
		(new Thread(this)).start();
		
		try {
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmi://127.0.0.1/serverProyecto", this);
		} catch (MalformedURLException e) {
		}
		
		System.out.println("Servidor abierto");
		
		coches.add(new Coche(10, 10, 10, 20));
	}
	
	public static void main(String[] args) {
		try {
			new Servidor();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(20);
				if(coches.size() <= 0) {
					continue;
				}
				int rnd = (int) (Math.random() * coches.size());
				Coche coche = coches.get(rnd);
				coche.mover();
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
	public int obtenerCodigo() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void desconectarCliente(int codigo) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
}
