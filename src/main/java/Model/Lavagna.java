package Model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Lavagna definisce un arrayList di quotazioni, in pratica raggruppa tutte e 6 le quotazioni delle scuderie
 * in una struttura dati comoda da usare per effettuare i confronti con la classifica in uscita da 
 * corsa. 
 * Implementata usando il pattern SINGLETONE
 */
public class Lavagna implements Serializable {
	
	private  ArrayList <Quotazione> lavagna = new ArrayList <Quotazione>() ;
	private static Lavagna lavagnaQuotazioni;
		
	/**
	 * il costruttore e' privato, nessuno lo vede, nessuno lo usa.
	 */
	private Lavagna(){
		
	}
	public static Lavagna creaLavagna(){
		if (lavagnaQuotazioni==null){
			lavagnaQuotazioni = new Lavagna();
		}
		return lavagnaQuotazioni;
	}
	
	/**
	 * aggiunge una quotazione alla lavagna, questo metodo sar� chiamato all'inizio del gioco
	 * per riempire la lavagna di quotazioni, poi non sar� pi� utilizzato.
	 * ( potremmo valutare l'idea di farlo statico come per mazzo ) 
	 * @param q
	 */
	public  void aggiungi_quotazione(Quotazione q)
	{
	lavagna.add(q);	
	}
	
	/**
	 * data una posizione(quotazione) ritorna il colore della scuderia corrente che occupa quella posizione.
	 * ( in realt� questo metodo viene usato solo all'inizio quando devo assegnare la scuderia proprietario ad un
	 * personaggio, quindi viene chiamato solo quando ad ogni quotazione corrisponde UNA SOLA scuderia )
	 * 
	 * @param posizione
	 * @return
	 */
	public  String getColore(int posizione)
	{
		for(Quotazione quote : lavagna)
		  {
			if(quote.getIndex()==posizione)
				return quote.getColore();
		  }
		return "";
	}
	
	/**
	 * dato un colore questo metodo ricerca tra gli oggetti "Quotazione" contenuti in lavagna
	 * la quotazione corrispondente al colore c, una volta trovato ritorna il suo index ( la sua quotazione )  
	 * @param c: il colore da ricercare 
	 * @return	l'indice ritornato è 1:1 con le posizioni in classifica NON TOCCARE 
	 */
	public  int getPosizione(String c)
	{
	for(Quotazione q : lavagna)
		{if(c.equals(q.getColore()))
		   {return q.getIndex();}
		}
	return -1;
	}
	
	/**
	 * Questo metodo serve per aggiornare la quotazione di una scuderia all'interno di una lavagna,
	 * dati in ingresso il colore 'c' e un incremento / decremento di posizione 'numb' il metodo ricerca
	 * l'oggetto Quotazione corrispondente al colore 'c' passato e utilizza il metodo setIndex per
	 * aggiornare la quotazione
	 * @param c
	 * @param numb
	 */
	public  void setPosizione(String c, int numb)
	{
		for(Quotazione q : lavagna)
			if(c.equals(q.getColore()))
			   { q.setIndex(q.getIndex()+numb); }
	}
	
	/**
	 * Metodo che banalmente visualizza la lavagna a schermo.
	 */
	public  void visualizzaLavagna()
	{
		for(Quotazione q : lavagna)
			{ q.visualizza(); }
	}
	/**
	 * 
	 * FUNZIONAMENTO DI UPDATELAVAGNA
	 * Scanning ( for i ) della classifica da 0 fino all'ultima posizione ( 6 ) 
	 * se l'indice corrente � minore della posizione che il colore contenuto in classifica[i]
	 * ha nella lavagna delle quotazioni, allora significa che la scuderia � migliorata quindi
	 * il metodo setPosizione fa scalare la sua quotazione di una posizione verso l'alto ( -1 ) 
	 * 
	 *  se invece i > della posizione della scuderia in lavagna, allora � peggiorata e aggiorno
	 *  la quotazione scalando la posizione verso il basso ( +1 ) 
	 * 
	 * 	
	 * @param classifica
	 */
	
	public  void updateLavagna(ArrayList<String> classifica)
	{
	 int i;
	 for(i=0;i<classifica.size();i++)
	    {
		 if(i < getPosizione(classifica.get(i)))  
            { setPosizione(classifica.get(i), -1 );	}
		 else
			if(i > getPosizione(classifica.get(i)))
				{ setPosizione(classifica.get(i), +1); }
	   }
	
	}
	public static void deleteLavagna(){
		Lavagna.lavagnaQuotazioni=null;
	}
}
