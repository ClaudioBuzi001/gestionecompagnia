package it.prova.gestionecompagnia.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from impiegato")) {

			// Lazy Fetching
			while (rs.next()) {
				// Costruisco l oggetto compagnia
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setId(rs.getLong("id"));
				impiegatoTemp.setNome(rs.getString("nome"));
				impiegatoTemp.setCognome(rs.getString("cognome"));
				impiegatoTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				impiegatoTemp.setDataNascita(rs.getDate("datanascita"));
				impiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				// Non prendo la compagnia, no lazy fetching
				// result.add compagnia
				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id = ?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));
					// Non prendo la compagnia, no lazy fetching

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
	public int update(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"update impiegato set nome=?, cognome=?, codicefiscale=?, datanascita=?, dataassunzione=?, compagnia_id=? where id = ?; ")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getCompagnia().getId());
			ps.setLong(7, input.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"insert into impiegato(nome, cognome, codicefiscale, datanascita, dataassunzione, compagnia_id) values(?,?,?,?,?,?);")) {

			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getCompagnia().getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() < 1)
			throw new RuntimeException("Errore: input non valido!");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("delete from impiegato where id = ?;")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato temp = null;

		// Iniziamo il corpo della costruzione della query
		String query = "select * from impiegato where 1=1 ";

		// Se nome esiste
		if (input.getNome() != null || !input.getNome().isEmpty()) {
			query += " and nome like '" + input.getNome() + "%' ";
		}

		// Se cognome
		if (input.getCognome() != null || !input.getCognome().isEmpty()) {
			query += " and cognome like '" + input.getCognome() + "%' ";
		}

		// se codice fiscale
		if (input.getCodiceFiscale() != null || !input.getCodiceFiscale().isEmpty()) {
			query += " and codicefiscale like '" + input.getCodiceFiscale() + "%' ";
		}

		// se data nascita
		if (input.getDataNascita() != null) {
			query += " and nascita ='" + new java.sql.Date(input.getDataNascita().getTime()) + "' ";
		}

		// se data assunzione
		if (input.getDataAssunzione() != null) {
			query += " and dataassunzione ='" + new java.sql.Date(input.getDataAssunzione().getTime()) + "' ";
		}

		// se compagnia DA VERIFICARE SE GIUSTO
		if (input.getCompagnia() != null && input.getCompagnia().getId() > 1) {
			query += " and compagnia_id = " + input.getCompagnia().getId();
		}

		query += ";";

		// Abbiamo finito di costruire la query
		// La executiamo
		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				temp = new Impiegato();
				temp.setNome(rs.getString("nome"));
				temp.setCognome(rs.getString("cognome"));
				temp.setCodiceFiscale(rs.getString("codicefiscale"));
				temp.setDataNascita(rs.getDate("datanascita"));
				temp.setDataAssunzione(rs.getDate("dataassunzione"));
				// non settiamo compangia in qualunque caso, sia se c e la abbiamo sia no

				result.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia) {

		// data
		if (compagnia == null || compagnia.getId() < 1)
			throw new RuntimeException("ERRORE: input non valido");

		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on i.compagnia_id = c.id where c.ragionesociale = ?;")) {

			ps.setString(1, compagnia.getRagioneSociale());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));

					result.add(temp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public int countByDataFondazioneCompagniaGratherThan(Date dateConfronto) {
		//cotenggio di quanti impiegati lavorano in compagnie fondate dal 2000 in poi
		if (dateConfronto == null )
			throw new RuntimeException("ERRORE: input non valido");

		int contatore = 0;

		try (PreparedStatement ps = connection.prepareStatement("select count(*) from impiegato i inner join compagnia c on i.compagnia_id = c.id where c.datafondazione > ?;")){
			
		
			ps.setDate(1, new java.sql.Date(dateConfronto.getTime())); 
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					contatore = rs.getInt("count(*)");
				}//while

			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}

		return contatore;
		
	}

	@Override
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput) {
		if (fatturatoInput < 1)
			throw new RuntimeException("ERRORE: input non valido");

		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on i.compagnia_id = c.id where c.fatturatoannuo > ?;")) {

			ps.setInt(1, fatturatoInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));

					result.add(temp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public List<Impiegato> findAllByErroriAssunzione() {
		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on i.compagnia_id = c.id where i.dataassunzione < c.datafondazione ;")) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));

					result.add(temp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}

		return result;
	}

}
