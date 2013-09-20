package com.heliumv.api.worktime;

import java.util.List;

import javax.ws.rs.core.Response;

import com.heliumv.api.item.ItemEntry;
import com.heliumv.api.order.OrderApi;
import com.heliumv.api.production.ProductionApi;
import com.heliumv.api.project.ProjectApi;


/**
 * Funktionalit�t rund um die Zeit(daten)erfassung</br>
 *
 * @author Gerold
 */
public interface IWorktimeApi {
	/**
	 * Eine KOMMT-Buchung durchf�hren.</br>
	 * @param entry ist die Standardzeitbuchung Datenstruktur <code>TimeRecordingEntry</code>
	 * @return
	 */
	Response bookComing(TimeRecordingEntry entry) ;
	
	/**
	 * Eine GEHT-Buchung durchf�hren.</br>
	 * @param entry ist die Standardzeitbuchung Datenstruktur <code>TimeRecordingEntry</code>
	 * @return
	 */
	Response bookGoing(TimeRecordingEntry entry) ;

	/**
	 * Eine PAUSE (Unterbrechung)-Buchung durchf�hren.</br>
	 * <p>Eine Pause (zum Beispiel Mittagspause) wird durch <b>zwei</b> PAUSE
	 * Buchungen erzielt.</p>
	 * @param entry ist die Standardzeitbuchung Datenstruktur <code>TimeRecordingEntry</code>
	 * @return
	 */
	Response bookPausing(TimeRecordingEntry entry) ;

	/**
	 * Eine ENDE Buchung durchf�hren</br>
	 * <p>Eine Belegbuchung wie beispielsweise Auftrags-, Projekt oder Los-Buchung beenden</p>
	 * @param entry
	 * @return
	 */
	Response bookStopping(TimeRecordingEntry entry) ;

//	Response bookComing(String userId,
//			Integer year, Integer month, Integer day,
//			Integer hour, Integer minute, Integer second) ;

	/** 
	 * Eine (Beginn) Buchung eines Los durchf�hren.
	 * 
	 * @param entry ist die Datenstruktur zur Speicherung einer Los-Buchung</br>
	 * <p>Die anzugebende Los-Id kann �ber die Resource <code>production</code> ermittelt werden @see {@link ProductionApi}
	 * {@link ProductionApi} </p>
	 * 
	 * @return
	 */
	Response bookProduction(ProductionRecordingEntry entry) ;

	/** 
	 * Eine (Beginn) Buchung eines Projekts durchf�hren.
	 * 
	 * @param entry ist die Datenstruktur zur Speicherung einer Projekt-Buchung</br>
	 * <p>Die anzugebende Project-Id kann �ber die Resource <code>project</code> ermittelt werden
	 * @see {@link ProjectApi} </p>
	 * @return
	 */
	Response bookProject(ProjectRecordingEntry entry) ;
	
	/**
	 * Eine (Beginn) Buchung mit Auftragsbezug erzeugen.</br>
	 * <p>Die anzugebende Order-Id kann �ber die Resource <code>order</code> ermittelt werden
	 * @see {@link OrderApi} </p>
	 *
	 * @param entry ist dabei die Auftragszeit Datenstruktur
	 * @return
	 */
	Response bookOrder(OrderRecordingEntry entry) ;

	/**
	 * Liefert eine Liste aller verf�gbaren T�tigkeiten(Artikel) die innerhalb der Zeiterfassung 
	 * durchgef�hrt werden k�nnen. </br>
	 * 
	 * @param userId des am HELIUM V Servers angemeldeten Benutzers
	 * @param limit (optional) die maximale Anzahl von gelieferten Eintr�gen. Default ist 50.
	 * @param startIndex (optional) die Id (eines <code>ItemEntry</code> Eintrags, mit dem die Liste beginnen soll
	 * @param filterCnr (optional) die Sondert�tigkeiten auf diese Kennung einschr�nken
	 * @return eine (leere) Liste der f�r den Benutzer verf�gbaren Sondert�tigkeiten
	 */
	List<ItemEntry> getActivities(
			String userId,
			Integer limit,
			Integer startIndex,String filterCnr);
	
	/**
	 * Liefert eine Liste aller verf�gbaren Belegarten die f�r die Zeiterfassung verwendet werden k�nnen.</br>
	 * <p>Belegarten sind typischerweise Angebot, Auftrag, Los oder Projekt</p>
	 * 
	 * @param userId der am HELIUM V Server angemeldete Benutzer
	 * @return eine (leere) Liste von verfuegbaren/bebuchbaren Belegarten
	 */
	List<DocumentType> getDocumentTypes(String userId) ; 

	/**
	 * Liefert alle <code>ZeitdatenEntry</code> f�r den angegebenen Tag.</br>
	 * 
	 * @param userId enth�lt den angemeldeten Benutzer
	 * @param year ist das Jahr f�r das die Zeitdaten abgerufen werden sollen
	 * @param month ist das Monat (1-12) 
	 * @param day der Tag (1-31)
	 * @param forStaffId ist jene Benutzer-Id f�r welche die Zeitdaten abgerufen werden sollen. Kann auch leer sein
	 * @param limit Ist die maximale Anzahl an Datens�tzen. Default 50.
	 * @return eine (leere) Liste von <code>ZeitdatenEntry </code> f�r den gew�nschten Tag
	 */
	List<ZeitdatenEntry> getWorktimeEntries(
			String userId,
			Integer year,
			Integer month,
			Integer day,
			Integer forStaffId,
			Integer limit) ;
	
	/**
	 * Eine Zeitbuchung l�schen
	 * 
	 * @param userId der angemeldete Benutzer
	 * @param worktimeId die Id der zu l�schenden Buchung.</br><p>Die Zeitbuchungen (und damit auch deren Id)
	 * kann �ber die GET ermittelt werden.</p>
	 * @param forStaffId ist die optionale PersonalId f�r die gel�scht werden soll. 
	 * <p>Der angemeldete Benutzer kann f�r jene Personen f�r die er ausreichend Rechte hat Zeitbuchungen l�schen.
	 * </p>
	 * <p>Wird sie nicht angegeben, so wird der Zeitbuchung mit der angemeldeten Person verkn�pft.
	 */
	void removeWorktime(
			String userId,
			Integer worktimeId,
			Integer forStaffId) ;
}
