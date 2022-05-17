package it.prova.gestionecompagnia.test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.connection.MyConnection;
import it.prova.gestionecompagnia.dao.Constants;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAO;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAO;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class TestCompagnia {

	public static void main(String[] args) {
		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		try (Connection connection = MyConnection.getConnection(it.prova.gestionecompagnia.dao.Constants.DRIVER_NAME,
				Constants.CONNECTION_URL)) {

			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			// TODO TEST
			System.out.println(compagniaDAOInstance.list().size());
			System.out.println(impiegatoDAOInstance.list().size());

			// testInsertCompagnia(compagniaDAOInstance);

			// testInsertImpiegato(impiegatoDAOInstance);

			testGetCompagnia(compagniaDAOInstance);
			testGetImpiegato(impiegatoDAOInstance);

			testUpdateCompagnia(compagniaDAOInstance);
			testUpdateImpiegato(impiegatoDAOInstance);

			// testDeleteCompagnia(compagniaDAOInstance);
			// testDeleteImpiegato(impiegatoDAOInstance);
			
			testFindByExampleCompagnia(compagniaDAOInstance);
			testFindByExampleImpiegato(impiegatoDAOInstance);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// METODI STATICI PER TEST
	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("_------------testInsertCompagnia-------------_");
		Compagnia daInserire = new Compagnia();
		daInserire.setRagioneSociale("Pippo E Baudo");
		daInserire.setFatturatoAnnuo(200000);
		daInserire.setDataFondazione(new Date());
		System.out.println("DAti in tabella compagnia prima del insert: " + compagniaDAOInstance.list().size());

		int result = compagniaDAOInstance.insert(daInserire);
		if (result == 0)
			throw new RuntimeException("_-----------testInsertCompagnia FAILED-------------_");

		System.out.println("DAti in tabella compagnia dopo del insert: " + compagniaDAOInstance.list().size());
		System.out.println("_---------------testInsertCompagnia PASSED-------------------_");

	}

	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {

		System.out.println("_------------testInsertImpiegato-------------_");

		Compagnia daInserire = new Compagnia();
		daInserire.setRagioneSociale("Pippo E Baudo");
		daInserire.setFatturatoAnnuo(200000);
		daInserire.setDataFondazione(new Date());
		daInserire.setId(2L);

		Impiegato impiegatoDaInserire = new Impiegato("Marione", "Rimbombo", "vjbnaih501lcal", new Date(), new Date(),
				daInserire);

		System.out.println("Dati in tabella impiegato prima dell isert: " + impiegatoDAOInstance.list().size());

		int result = impiegatoDAOInstance.insert(impiegatoDaInserire);

		if (result == 0)
			throw new RuntimeException("_-----------testInsertImpiegato FAILED-------------_");

		System.out.println("Dati in tabella impiegato dopo dell isert: " + impiegatoDAOInstance.list().size());
		System.out.println("_---------------testInsertImpiegato PASSED-------------------_");

	}

	private static void testGetCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("_-------------testGetCompagnia------------_");
		Compagnia result = compagniaDAOInstance.get(1L);
		System.out.println(result.getRagioneSociale() + " " + result.getFatturatoAnnuo());

		System.out.println("_-------------testGetCompagniaPassed");
	}

	private static void testGetImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("_-----------testGetImpiegato----------_");
		Impiegato result = impiegatoDAOInstance.get(2L);
		System.out.println(result.getNome() + " " + result.getCognome() + " " + result.getCodiceFiscale());

		System.out.println("_-----------testGetImpiegato PASSED-------------_");

	}

	private static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("_---------testUpdateCompagnia-------------_");
		Compagnia result = compagniaDAOInstance.list().get(1);

		result.setRagioneSociale("Problem solving");
		result.setFatturatoAnnuo(350000);
		int resultFinale = compagniaDAOInstance.update(result);
		System.out.println(compagniaDAOInstance.list().get(1).getRagioneSociale());
		if (resultFinale == 0)
			System.out.println("---------------------TEST FAILED");
		System.out.println("_---------testUpdateCompagnia PASSED-------------_");
	}

	private static void testUpdateImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("_-------testUpdateImpiegato-------------_");
		Impiegato daModificare = impiegatoDAOInstance.get(2L);
		daModificare.setNome("Giuseppe");
		daModificare.setCognome("Giuseppino");
		Compagnia daInserire = new Compagnia();
		daInserire.setRagioneSociale("Pippo E Baudo");
		daInserire.setFatturatoAnnuo(200000);
		daInserire.setDataFondazione(new Date());
		daInserire.setId(2L);
		daModificare.setCompagnia(daInserire);
		int result = impiegatoDAOInstance.update(daModificare);
		System.out.println(impiegatoDAOInstance.get(2L).getNome());
		if (result == 0)
			System.out.println("---------------------TEST FAILED");
		System.out.println("_---------testUpdateImpiegato PASSED-------------_");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		// FIXME Da Implementare inserendo un metodo eager Fething per prenere tutti gli
		// impiegati di daEliminare

		System.out.println("_----------testDeleteCompagnia-----------_");
		Compagnia daEliminare = compagniaDAOInstance.list().get(2);

		System.out.println("Dati in compagnia prima del delete: " + compagniaDAOInstance.list().size());

		int risultato = compagniaDAOInstance.delete(daEliminare);

		System.out.println("Dati in compagnia dopo del delete: " + compagniaDAOInstance.list().size());
		System.out.println("--------testDeleteCompagnia passed---------_");

	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("_---------testDeleteImpiegato-----------_");
		Impiegato daRimuovere = impiegatoDAOInstance.list().get(1);

		System.out.println("DAti in impiegato prima del delete: " + impiegatoDAOInstance.list().size());
		int result = impiegatoDAOInstance.delete(daRimuovere);
		System.out.println("DAti in impiegato dopo del delete: " + impiegatoDAOInstance.list().size());
		System.out.println("_--------testDeleteImpiegato PASSED---------_");
	}

	private static void testFindByExampleCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("_------------testFindByExampleCompagnia-------------_");

		Compagnia daCercare = new Compagnia();
		daCercare.setRagioneSociale("Solv");
		daCercare.setFatturatoAnnuo(2000);

		List<Compagnia> result = compagniaDAOInstance.findByExample(daCercare);
		if (result.size() == 0)
			System.out.println("_-----------testFindByExample FAILED------------_");

		for (Compagnia compagniaItem : result) {
			System.out.println(compagniaItem.getRagioneSociale() + " " + compagniaItem.getFatturatoAnnuo());
		}

		System.out.println("_----------testFindByExampleCompagnia PASSED-------------_");
	}
	
	private static void testFindByExampleImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("_------testFindByExampleImpiegato-------------_");
		
		Impiegato daCercare = new Impiegato();
		daCercare.setNome("Giu");
		daCercare.setCognome("g");
		daCercare.setCodiceFiscale("vjbn");
		
		List<Impiegato> result = impiegatoDAOInstance.findByExample(daCercare);
		if(result.size() == 0 )
			System.out.println("_-----------testFindByExampleImpiegato FAILED");
		
		for(Impiegato impiegatoItem : result)	
			System.out.println(impiegatoItem.getNome()+ " "+ impiegatoItem.getCognome()+ " "+ impiegatoItem.getCodiceFiscale()) ;
		
		System.out.println("_------------testFindByExampleImpiegato PASSED----------_");
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
