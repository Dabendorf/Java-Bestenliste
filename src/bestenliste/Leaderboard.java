package bestenliste;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Diese Klasse erstellt die Bestenliste (Spiel: Mastermind) und ordnet selbige in eine primitive Tabelle ein.
 * 
 * @author Lukas Schramm
 * @version 1.0
 */
public class Leaderboard {

	/**JFrame auf dem die Tabelle dargestellt wird*/
	private JFrame frame1 = new JFrame("Bestenliste");
	/**Highscoretabelle*/
	private JTable table1 = new JTable();
	/**ArrayList mit Highscores*/
	private ArrayList<Highscore> highscorelist = new ArrayList<Highscore>();
	/**Objekt der Klasse zum Aufrufen der Highscores*/
	private LoadFile lf = new LoadFile();

	public Leaderboard() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(600,400));
        frame1.setMinimumSize(new Dimension(300,200));
	    frame1.setResizable(true);
	    frame1.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
            	frame1.dispose();
            }
        });
	    
	    Container cp = frame1.getContentPane();
	    cp.setLayout(new GridLayout());
	    
	    loadHighscores();
	}
	
	/**
	 * Hier werden alle alten Highscores aus der Highscoreliste geladen und in die Highscoreliste integriert.
	 */
	private void loadHighscores() {
		for(Highscore hsc:lf.getAllHighscores()) {
			highscorelist.add(hsc);
		}
	}
	
	/**
	 * Hier wird ein neuer Highscore eingefuegt und in die Highscoreliste integriert.
	 * @param systemzeit Dies ist die Zeit zu welcher der Rekord aufgestellt wurde
	 * @param rekordzeit So lange hat der Spieler zum Gewinnen gebraucht
	 * @param name Dies ist der Name des Spielers
	 * @param zeilen Dies ist die Anzahl von Zeilen nach welchen der Spieler siegte
	 */
	public void addHighscore(long systemzeit,long rekordzeit,String name,int zeilen) {
		highscorelist.add(new Highscore(systemzeit,rekordzeit,name,zeilen));
	}
	
	/**
	 * Diese Methode sortiert die Highscores und ueberfuert sie in die Tabelle.
	 */
	public void sort() {
		Collections.sort(highscorelist);
		Vector<Object> eintraege = new Vector<Object>();
		for(Highscore hsc:highscorelist) {
			if(highscorelist.indexOf(hsc)<25) {
				Vector<Object> zeile = new Vector<Object>();
				zeile.add(hsc.getName());
				zeile.add(hsc.getZeilen());
				double tempRekord = hsc.getRekordzeit();
				double tempRekord2 = tempRekord/1000;
				DecimalFormat deziformat = new DecimalFormat("###0.##");
				zeile.add(deziformat.format(tempRekord2)+" sek.");
				long tempSystem = hsc.getSystemzeit();
				Date date = new Date(tempSystem);
			    Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				zeile.add(format.format(date));
				eintraege.add(zeile);
				lf.addHighscore(hsc, highscorelist.indexOf(hsc));
			}
		}

		Vector<String> titel = new Vector<String>();
		titel.add("Name");
		titel.add("Versuche");
		titel.add("Zeit");
		titel.add("Erreicht");
		table1 = new JTable(eintraege, titel);
		
		table1.getColumn("Name").setPreferredWidth(40);
	    table1.getColumn("Versuche").setPreferredWidth(15);
	    table1.getColumn("Zeit").setPreferredWidth(20);
	    table1.getColumn("Erreicht").setPreferredWidth(25);
	    table1.getTableHeader().setBackground(Color.lightGray);
	    table1.setEnabled(false);
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	    for(int x=0;x<table1.getColumnCount();x++) {
	    	table1.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	    	table1.getTableHeader().getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
	    }
	    
	    table1.setDefaultRenderer(String.class, centerRenderer);
	    
	    frame1.pack();
	    frame1.setLocationRelativeTo(null);
	    table1.setVisible(true);
	    frame1.getContentPane().add(new JScrollPane(table1));
	}
	
	/**
	 * Diese Methode kann die Bestenliste anzeigen oder wieder ausblenden.
	 * @param anzeigen Boolean, ob angezeigt oder ausgeblendet wird.
	 */
	public void show(boolean anzeigen) {
		frame1.setVisible(anzeigen);
	}
	
	public static void main(String[] args) {
		Leaderboard lb = new Leaderboard();
		//lb.addHighscore(System.currentTimeMillis(), 5000, "Lukas", 7);
		lb.sort();
		lb.show(true);
	}
}