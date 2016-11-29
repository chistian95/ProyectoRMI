package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceServidor extends Remote {
	public List<DatoCoche> getDatosCoches() throws RemoteException;
	
	public int obtenerCodigo() throws RemoteException;
	public void desconectarCliente(int codigo) throws RemoteException;
}
