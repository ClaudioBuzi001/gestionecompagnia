package it.prova.gestionecompagnia.dao.impiegato;

import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato>{
	//findAllByCompagnia(Compagnia )
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia);
	//countByDataFondazioneCompagniaGratherThan(Date )  cotenggio di quanti impiegati lavorano in compagnie fondate dal 2000 in poi
	public int countByDataFondazioneCompagniaGratherThan(Date dateConfronto);
	//findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput) lista di tutti i lavoratori che lavorano in societa che hanno un fatturato maggiore di
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput);
	//findAllByErroriAssunzione() lista impiegati dove c è stato un errore di data entry, dataAssunzione < di dataFondazione
	public List<Impiegato> findAllByErroriAssunzione();
	 
	
}
