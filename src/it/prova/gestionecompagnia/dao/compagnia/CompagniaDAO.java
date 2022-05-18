package it.prova.gestionecompagnia.dao.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia>{
	//findAllBuDataAssunzioneMaggioreDi(Date dataInput) vglio la lista di tutte le compagnie che abbiano persone assunte dopo questa data
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataInput);
	//findAllByRaggioneSocialeContiene(String pattern) %s%
	public List<Compagnia> findAllByRagioneSocialeContiene(String inputDaRicercare);
	//findAllByCodiceFiscaleImpiegatoContiene(String patter) codice fiscale del impiegato %cs%
	public List<Compagnia> findAllByCodiceFiscaleImpiegatoContiene(String codiceFiscaleDaRicercare);
	
	public void findByIdEager(Compagnia input);
	
	
}
