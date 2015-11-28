package bestenliste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Diese Klasse liest saemtliche Highscores ein und speichert sie auch wieder verschluesselt ab
 * @author Lukas Schramm
 */
public class Spielstand {
	
	BufferedReader br;
	BufferedWriter bw;
	Properties prop;
	private char[] schluessel = "Heizoelrueckstossabdaempfung".toCharArray();
	
	public Spielstand() {
		ladeDatei("highscores.txt");
	}
	
	/**
	 * Diese Methode nimmt die aktuelle Systemzeit in Millisekunden auf.
	 * @return Gibt die Systemzeit in Millisekunden zuruck
	 */
	public long zeitnehmen() {
		long zeit = System.currentTimeMillis();
		return zeit;
	}
	
	/**
	 * Diese Methode laedt die Datei, in welcher zu schreiben ist.
	 * @param dateiname Hier steht der Dateiname der Datei
	 */
	private void ladeDatei(String dateiname) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dateiname), Charset.forName("UTF-8")));
			prop = new Properties();
			prop.load(br);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Diese Methode schreibt einen neuen Highscore und gibt ihm eine Nummer.
	 * @param hsc Der zu uebergebende Highscore
	 * @param num Die Nummer des Highscores
	 */
	public void schreiben(Highscore hsc,int num) {
		try {
			String temp = (String.valueOf(hsc.getSystemzeit())+","+hsc.getRekordzeit()+","+hsc.getName()+","+hsc.getZeilen());
			prop.setProperty("highscore"+num, verschluesseln(temp));
			prop.setProperty("anzahlHighscores", verschluesseln(String.valueOf(num+1)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("highscores.txt"), Charset.forName("UTF-8")));
			prop.store(bw, "Gespeicherte Highscores");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt einen Highscore anhand seines Keys aus.
	 * @param key Hier gibt man den key ein
	 * @return Gibt den Highscore zurueck
	 */
	public Highscore aufrufen(String key) {
		try {
			String temp = entschluesseln(prop.getProperty(key));
			String[] temp2 = temp.split(",");
			Highscore hsc = new Highscore(Long.valueOf(temp2[0]),Long.valueOf(temp2[1]),temp2[2],Integer.valueOf(temp2[3]));
			return hsc;
		} catch (NullPointerException np) { 
			return null;
		}
	}
	
	/**
	 * Diese Methode laedt alle verfuegbaren Highscores aus der Datei und gibt sie in einem Array aus.
	 * @return Gibt einen Highscore[] zurueck
	 */
	public Highscore[] allesLaden() {
		int anz;
		try {
			anz = Integer.valueOf(entschluesseln(prop.getProperty("anzahlHighscores")));
		} catch(NullPointerException e) {
			anz = 0;
		}
		
		Highscore[] highscores = new Highscore[anz];
		for(int i=0;i<anz;i++) {
			highscores[i] = aufrufen("highscore"+i);
		}
		return highscores;
	}
	
	/**
	 * Diese Methode verschluesselt den eingegebenen String.
	 * @param original Nimmt den Originalstring entgegen.
	 * @return Gibt den verschluesselten String aus.
	 */
	private String verschluesseln(String original) {
		char[] temp = original.toCharArray();
		String crypt = new String("");
		for(int i=0;i<temp.length;i++) {
			int result = (temp[i] + schluessel[i%schluessel.length]) % 256;
			crypt += (char) result;
        }
        return crypt;
    }
	
	/**
	 * Diese Methode entschluesselt den eingegebenen String.
	 * @param original Nimmt den verschluesselten String entgegen.
	 * @return Gibt den entschluesselten String aus.
	 */
	private String entschluesseln(String verschluesselt) {
		char[] temp = verschluesselt.toCharArray();
		String decrypt = new String("");
		for(int i=0;i<temp.length;i++) {
			int result;
            if(temp[i] - schluessel[i%schluessel.length] < 0) {
            	result =  (temp[i] - schluessel[i%schluessel.length]) + 256;
            } else {
            	result = (temp[i] - schluessel[i%schluessel.length]) % 256;
            }
            decrypt += (char) result;
		}
		return decrypt;
	}

}