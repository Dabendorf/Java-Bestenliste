package bestenliste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Diese Klasse laedt und speichert alle Highscores des Projekts verschluesselt in xml-Dateien.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class LoadFile {
	/**Pfad zur Speicherdatei fuer Highscores*/
	private String fileHighscore = "files/highscores.xml";
	/**Schluessel fuer die Vigenereverschluesselung*/
	private char[] vigKey = "Heizoelrueckstossabdaempfung".toCharArray();
	/**Zu speichernde Propertieselemente*/
	private Properties props = new Properties();
	/**Die geladene Speicherdatei*/
	private File file;
	
	public LoadFile() {
		file = new File(fileHighscore);
	}
	
	/**
	 * Diese Methode fuegt einen Highscore zur HighscoreDatei hinzu.
	 * @param hsc Der hinzugefuegte Highscore.
	 * @param num Die Nummer des Highscores.
	 */
	public void addHighscore(Highscore hsc,int num) {
		try {
			String temp = (String.valueOf(hsc.getSystemzeit())+","+hsc.getRekordzeit()+","+hsc.getName()+","+hsc.getZeilen());
			props.setProperty("highscore"+num, encrypt(temp));
			props.setProperty("numOfHighscores", encrypt(String.valueOf(num+1)));
			FileOutputStream fileOut = new FileOutputStream(file);
			props.storeToXML(fileOut, "SameGame Highscores");
			fileOut.close();
		} catch(IOException e) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				fileDamage(fileHighscore);
			}
		}
	}
	
	/**
	 * Diese Methode gibt einen Array aller abgespeicherten Highscores zurueck.
	 * @return Gibt Highscore-Array zurueck.
	 */
	public Highscore[] getAllHighscores() {
		try {
			FileInputStream fileInput = new FileInputStream(file);
			props.loadFromXML(fileInput);
			fileInput.close();
		} catch(IOException e) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				fileDamage(fileHighscore);
			}
		}
		
		int numOfHighscores;
		try {
			numOfHighscores = Integer.valueOf(decrypt(props.getProperty("numOfHighscores")));
		} catch(Exception e) {
			numOfHighscores = 0;
		}
		
		Highscore[] highscores = new Highscore[numOfHighscores];
		for(int i=0;i<highscores.length;i++) {
			highscores[i] = getHighscore("highscore"+i);
		}
		return highscores;
	}
	
	/**
	 * Diese Methode gibt einen einzelnen Highscore zurueck.
	 * @param key Key unter dem der Highscore intern abgespeichert ist.
	 * @return Gibt Highscore zurueck.
	 */
	private Highscore getHighscore(String key) {
		try {
			String temp = decrypt(props.getProperty(key));
			String[] temp2 = temp.split(",");
			Highscore hsc = new Highscore(Long.valueOf(temp2[0]),Long.valueOf(temp2[1]),temp2[2],Integer.valueOf(temp2[3]));
			return hsc;
		} catch (NullPointerException np) { 
			return null;
		}
	}
	
	/**
	 * Diese Methode verschluesselt den eingegebenen String.
	 * @param original Nimmt den Originalstring entgegen.
	 * @return Gibt den verschluesselten String aus.
	 */
	private String encrypt(String original) {
		char[] temp = original.toCharArray();
		String crypt = new String("");
		for(int i=0;i<temp.length;i++) {
			int result = (temp[i] + vigKey[i%vigKey.length]) % 256;
			crypt += (char) result;
		}
		return crypt;
	}
 	
 	/**
	 * Diese Methode entschluesselt den eingegebenen String.
	 * @param encrypted Nimmt den verschluesselten String entgegen.
	 * @return Gibt den entschluesselten String aus.
	 */
 	private String decrypt(String encrypted) {
  		char[] temp = encrypted.toCharArray();
  		String decrypt = new String("");
  		for(int i=0;i<temp.length;i++) {
  			int result;
  			if(temp[i] - vigKey[i%vigKey.length] < 0) {
  				result =  (temp[i] - vigKey[i%vigKey.length]) + 256;
  			} else {
  				result = (temp[i] - vigKey[i%vigKey.length]) % 256;
  			}
  			decrypt += (char) result;
  		}
  		return decrypt;
 	}
 	
 	/**
	 * Diese Methode gibt eine Meldung ueber eine fehlerhaft angelegte oder nicht vorhandene Speicherdatei aus.
	 * @param filename Pfad der fehlerhaften Datei.
	 */
	private void fileDamage(String filename) {
		String linebreak = System.getProperty("line.separator");
		JOptionPane.showMessageDialog(null, "Die Speicherdatei /"+filename+" ist nicht vorhanden oder beschädigt."+linebreak+"Die Spielfunktion ist nur eingeschränkt möglich."+linebreak+"Stelle die Speicherdatei wieder her und versuche es erneut.", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
	}
}