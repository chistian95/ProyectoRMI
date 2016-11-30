package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceServidor extends Remote {
	public List<DatoCoche> getDatosCoches() throws RemoteException;
	
	public int obtenerCodigo(String nombre) throws RemoteException;
	public void desconectarCliente(int codigo) throws RemoteException;
	
	public void moverArriba(int codigo) throws RemoteException;
	public void moverAbajo(int codigo) throws RemoteException;
	public void moverIzquierda(int codigo) throws RemoteException;
	public void moverDerecha(int codigo) throws RemoteException;
	public void turbo(int codigo) throws RemoteException;
	public double getTurbo(int codigo) throws RemoteException;
}
