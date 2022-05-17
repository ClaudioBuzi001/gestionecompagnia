package it.prova.gestionecompagnia.dao.compagnia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;


public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Compagnia> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				// Costruisco l oggetto compagnia
				compagniaTemp = new Compagnia();
				compagniaTemp.setId(rs.getLong("id"));
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));

				// result.add compagnia
				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Compagnia get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id = ?;")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setId(rs.getLong("id"));
					result.setRagioneSociale(rs.getString("ragionesociale"));
					result.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					result.setDataFondazione(rs.getDate("datafondazione"));

				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Compagnia input) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"update compagnia set ragionesociale = ?, fatturatoannuo = ?, datafondazione = ? where id = ?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			ps.setLong(4, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia input) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"insert into compagnia(ragionesociale, fatturatoannuo, datafondazione) values(?, ?, ?);")) {

			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		if(input == null || input.getId() < 1)
			throw new RuntimeException("Errore: input non valido!");
		
		if(input.getImpiegati().size() > 0)
			throw new RuntimeException("ERRORE: la compagnia data in input ha ancora impiegati");
		
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("delete from compagnia where id = ?;")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;

		// Iniziamo il corpo della costruzione della query
		String query = "select * from compagnia  where 1=1 ";
		
		//Se ragionesociale esiste
		if(input.getRagioneSociale() != null || !input.getRagioneSociale().isEmpty()) {
			query += " and ragionesociale like '" + input.getRagioneSociale()+ "%' ";
		}
		
		//Se fatturatoannuo
		if(input.getFatturatoAnnuo() > 1) {
			query += " and fatturatoannuo > " + input.getFatturatoAnnuo();
		}
		
		// se data esiste
		if (input.getDataFondazione() != null) {
			query += " and datafondazione ='" + new java.sql.Date(input.getDataFondazione().getTime()) + "' ";
		}
		query += ";";

		// Abbiamo finito di costruire la query
		// La executiamo
		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTemp.setId(rs.getLong("ID"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTemp.setDataFondazione(rs.getDate("datafondazione"));
				
				result.add(compagniaTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	//TODO implementare metodo findByIdEager(Compagnia input) alla fine preso l impiegato fai input.getImpiegati().add(impiegatoTemp)
	
	

	@Override
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataInput) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String inputDaRicercare) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Compagnia> findAllByCodiceFiscaleImpiegatoContiene(String codiceFiscaleDaRicercare) {
		// TODO Auto-generated method stub
		return null;
	}

}
