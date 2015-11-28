package bestenliste;

/**
 * Diese Klasse fasst Highscores mit Spielername, Rekordzeit, Systemzeit und der Anzahl benoetigter Zeilen zusammen.
 * Dies ist eine Beispielklasse und verwendet einen Highscore im Spiel MasterMind als Beispiel.
 * @author Lukas Schramm
 */
public class Highscore implements Comparable<Highscore> {
	
	private long systemzeit;
	private long rekordzeit;
	private String name;
	private int zeilen;
	
	public Highscore(long systemzeit, long rekordzeit, String name, int zeilen) {
		this.systemzeit = systemzeit;
		this.rekordzeit = rekordzeit;
		this.name = name;
		this.zeilen = zeilen;
	}

	public long getSystemzeit() {
		return systemzeit;
	}

	public long getRekordzeit() {
		return rekordzeit;
	}

	public String getName() {
		return name;
	}

	public int getZeilen() {
		return zeilen;
	}

	/**
	 * Diese compareTo-Methode vergleicht die Highscores.
	 * Zuerst wird bevorzugt, wer am wenigsten Zeilen benoetigt hat und bei Gleichstand, wer weniger Zeit benoetigte.
	 */
	public int compareTo(Highscore o) {
		int rueckgabe;
		if(zeilen != o.zeilen) {
			rueckgabe = ((Integer)zeilen).compareTo(o.zeilen);
		} else {
			rueckgabe = ((Long)rekordzeit).compareTo((Long)o.rekordzeit);
		}
		return rueckgabe;
	}

}