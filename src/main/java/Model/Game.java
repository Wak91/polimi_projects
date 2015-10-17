package Model;


import it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti.GameOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/**
 * Model principale del gioco, raggruppa tutte le strutture dati e le funzioni che
 * permettono di portare avanti una partita.
 *
 */
public class Game implements Model_interface{
	
	private  Card_handler card_h;
	private Lavagna lavagna;
	
	//---NUMERO TURNI DELLA PARTITA
	private  int turni;
	//-----------------------------------------------
	
	private int giocatore_corrente;  
	
	int numero_giocatore_turno; //usata per sapere quando tutto i giocatori hanno giocato
	
	//---servono per gara---
	private int spostamento;
	private boolean state;
	private ArrayList <Cavallo> elenco_pedine = new ArrayList <Cavallo>();
	private String colore1,colore2; // i colori delle scuderie che sprintano
	private int sprint=GameOption.sprint_value; 
	private ArrayList <Cavallo> finalisti = new ArrayList <Cavallo>();
	private ArrayList <String> classifica = new ArrayList <String>();
	private int num_cavalli; //numero di cavalli in gioco
	private Carta_movimento current_card;
	int fase_corsa=0; // 0 = partenza 1=continua 2=sprint 3=check_traguardo
	boolean inizio = true;
	private  int lunghezza_percorso = GameOption.lunghezza_percorso;
	//-----------------------------------------------
	
	//---DISPONIBILITA DI SEGNALINI PER SCUDERIA
	private  HashMap <String , Integer> disp_scuderia;
	//-----------------------------------------------
	
	//---COLORI SCUDERIE
	private  ArrayList <String> colori;
	//-----------------------------------------------
	
	//---MAPPA GIOCATORI-SEGNALINI SCUD-NUMERO TURNI
	private  HashMap<Integer,Integer[]> map_segnalino; 	
	//-----------------------------------------------
	
	//---ELENCO GIOCATORI
	private  ArrayList <Giocatore> elenco_giocatori = new ArrayList <Giocatore>();
	//-----------------------------------------------
	
	//---INIZIALIZZAZIONE MAZZI---
	private  Mazzo mAzione = new Mazzo();
	private  Mazzo mPersonaggio = new Mazzo();
	private  Mazzo mMovimento = new Mazzo();
	//-----------------------------------------------
		
	
	public Game () 
	        {
		    INIT();
		    card_h = new Card_handler();
	        } 
	
	/**
	 * ritorna l'hash map che mappa per ogni scuderia i segnalini disponibili
	 */
	public HashMap<String, Integer> getDisp_scuderia() {
		return disp_scuderia;
	}


	/**
	 * ritorna la lavagna delle quotazioni
	 */
	public Lavagna getLavagna() {
		return lavagna;
	}

    /**
    * Aggiunge 2 carte azione casuali al player passato come parametro.
    */
	public void add_card_player(Giocatore player){
		player.add_action_card((Carta_azione) mAzione.pesca());
		player.add_action_card((Carta_azione) mAzione.pesca());
	}
	
	public String getColore1(){return this.colore1;}
	public String getColore2(){return this.colore2;}
	
	//------FUNZIONI UTILIZZATE PER SCORRERE SULLA LISTA DEI GIOCATORI TENENDO TRACCIA DI UN GIOC. CORRENTE------
	
	public void set_gioc_turno() {
		numero_giocatore_turno=elenco_giocatori.size()-1;
	}
	public boolean has_next_gioc_turno() {
		return numero_giocatore_turno>0;
	}
	public int get_gioc_turno() {
		return numero_giocatore_turno;
	}
	public	void init_num_gioc_turno() {
		numero_giocatore_turno=elenco_giocatori.size()-1;
	}
	
	public Giocatore next_giocatore(String senso) {
		if (senso.equals("orario")){
			if (giocatore_corrente==elenco_giocatori.size()-1){
				giocatore_corrente=0;}
			else {
				giocatore_corrente++;
			}	
		}
		if (senso.equals("antiorario")){
			if (giocatore_corrente==0){
				giocatore_corrente=elenco_giocatori.size()-1;
			}
			else {
				giocatore_corrente--;
			}
		}
		numero_giocatore_turno--;
		return get_giocatore_corrente();
	}
	
	public Giocatore get_giocatore_corrente() {
		return elenco_giocatori.get(giocatore_corrente);
	}
	
	public void set_primo_giocatore(String senso) {
		if (senso.equals("orario")){
			giocatore_corrente=0;
		}
		else if (senso.equals("antiorario")) {
			giocatore_corrente=elenco_giocatori.size()-1;
		}
	}
	
	public Giocatore get_primo_giocatore(String senso){
		if (senso.equals("orario")){
			return elenco_giocatori.get(0);
		}
		else if (senso.equals("antiorario")) {
			return elenco_giocatori.get(elenco_giocatori.size()-1);
		}
		return null;
	}
		
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * ritorna la fase della corsa in cui mi trovo
	 */
	public Integer getFaseCorsa()
	{
		return this.fase_corsa;
	}
	
	/**
	 * ritorna l'ultima carta movimento estratta
	 */
	public Carta_movimento getCartaMovimento()
	{
		return this.current_card;
	}
	
	
	/**
	 * se la scuderia ha ancora segnalini disponibili permetto la scommessa altrimenti denied
	 */
	public boolean check_disp_scuderia(String colore){
		
		if(disp_scuderia.get(colore)!=0)
			{
			sub_segnalino_color(colore);
			return true;
			}
		else
		    {return false;}
	}
	

	/**
	 * Esclusione sul fatto che se ho fatto una vincente poi devo fare una piazzata.
	 */
	public boolean check_tipo_scommessa(Giocatore player , String  bet_type , String colore)
	{
		for (Scommessa scommessa : player.get_scommesse()) {
			if (scommessa.get_tipo()== bet_type && scommessa.get_colore()==colore)
			{ 
				return false; }
		}
		return true;	
	}
	
	/**
	 * dato un giocatore, un valore , un tipo ed un colore aggiunge al giocatore una scommessa di quel tipo.
	 * Metodo richiamato dal controller.
	 */
	public void piazza_scommessa(Giocatore player , int value , String type , String color)
	{
		player.Scommette(value, color, type);
	}
	
	/**
	 * Creazione di un giocatore estraendo una carta personaggio e dandogli un nome; viene subito aggiunto
	 * alla lista dei player in gioco.
	 */
	public Carta_personaggio create_player(String nome)
	{
		Carta_personaggio card = (Carta_personaggio) mPersonaggio.pesca();
    	elenco_giocatori.add(new Giocatore (nome,card));    	
    	return card;
	}
	/**
	 * paga i giocatori e ritorna hashmap che ha 
	 * nel primo campo il nome giocatore ,nel secondo i soldi vinti scommessa,nel terzo pv vinti e nel quartosoldi scuderia vinti
	 */
	public HashMap<Giocatore, ArrayList<Integer>> pay_price() {
		HashMap<Giocatore, ArrayList<Integer>> payments =new HashMap<Giocatore, ArrayList<Integer>>();
		ArrayList<Integer> price_from_bet =new ArrayList<Integer>();
		int price_scud;
		for (Giocatore player : get_giocatori()) {
			price_from_bet = make_payment_bet(player);
			price_scud=make_payment_scud(player);
			price_from_bet.add(price_scud);
			payments.put(player, price_from_bet); //nel primo campo c'e il nome giocatore nel secondo i soldi vinti scommessa pv vinti e soldi scuderia vinti
		}
		return payments;
	}
	
	/**
	 * ritorna dato un player ed un indice, la carta azione a quell'indice.
	 */
	public Carta_azione card_at_index(int i,Giocatore player){
		return player.get_card_at_index(i);
	}
	
	/**
	 * Dato un colore ritorna la pedina ( cavallo ) di quel colore.
	 */
	public Cavallo horse_from_color(String color) {
		for (Cavallo horse : elenco_pedine) {
			if (horse.get_color().equals(color)){
				return horse;
			}
		}
		return null; //non viene mai eseguito

	}

	//<-------------------------------------------------GETTER----------------------------------------------------------------->
	
    public int getLunghezzaPercorso()	
    {
    	return lunghezza_percorso;
    }
	
	/**
	 * Ritorna il reference al mazzo dei personaggi
	 * @return
	 */
	public  Mazzo getMazzoPersonaggi()
	{
		return mPersonaggio;
	}
	
	/**
	 * Ritorna il reference al mazzo delle carte azione
	 * @return
	 */
	public  Mazzo getMazzoAzione()
	{
		return mAzione;
	}
	
	
	/**
	 * Interroga la hash table che modella le regole del gioco per estrarre la corrispondenza
	 * tra numero-giocatori e numero turni di gioco
	 * @param num_players
	 * @return
	 */
	public  int get_num_turni(int num_players)
	{
		Integer arry[] = map_segnalino.get(num_players);
		return arry[1];
	}
	
	/**
	 * Interroga la hash table che modella le regole del gioco per estrarre la corrispondenza
	 * tra numero-giocatori e numero segnalini scommessa per scuderia
	 * @param num_players
	 * @return
	 */
	public  int get_num_segnalini(int num_players)
	{
		Integer arry[] = map_segnalino.get(num_players);
		return arry[0];
	}
	
	/**
	 * Ritorna il reference all'arraylist di colori che rappresentano
	 * le nostre scuderie
	 * @return
	 */
	public  ArrayList <String> get_colori()
	{
		return colori;
	}
	
	/**
	 * Ritorna l'elenco delle pedine cavallo in gioco
	 * @return
	 */
	public  ArrayList <Cavallo> get_cavalli()
	{
		return elenco_pedine;
	}
	
	/**
	 * Ritorna la lista di giocatori senza esporre il rep
	 * @return
	 */
	public  ArrayList <Giocatore> get_giocatori()
	{
		ArrayList <Giocatore> gio = new ArrayList <Giocatore>();
		for(Giocatore g : elenco_giocatori)
		   {
			gio.add(g);
		   }
	    return gio;
	}
	
	/**
	 * torna tutte le scommesse di un player passato come parametro.
	 */
	public ArrayList <Scommessa> get_player_bets(Giocatore player)
	{
		return player.get_scommesse();
	}
	
	public ArrayList <String> getClassifica()
	{
		return classifica;
	}
	
	//<-----------------INIZIO METODI DI INIZIALIZZAZIONE-------------------------------------------------->
	
	/**
	 * per ogni colore presente nell'array iniziale crea una pedina di quel colore e la aggiunge all'elenco di
	 * pedine in gioco.
	 */
	public  void init_cavalli(){
		for (String colore : colori) {
			Cavallo c = new Cavallo(colore);
			elenco_pedine.add(c);
		}		
	}
	
	/**
	 * inizializzazione casuale delle quotazioni.
	 * (vedi oggetto lavagna )
	 */
	public  void init_quotazioni()
	{	lavagna=Lavagna.creaLavagna();
		 Collections.shuffle(colori);
	        int key=0;
	        for(String s : colori)
	        {
	        Quotazione q = new Quotazione(key,s);
	        lavagna.aggiungi_quotazione(q);
	        key++;
	        }	
	}
	
	/**
	 *inizializza la hashtable da cui ricaviamo il numero segnalini 
	 *in funzione del numero di giocatori ed il numero di turni da giocare.
	 */
	public  void init_hash(){
		map_segnalino=new HashMap<Integer,Integer[]>();
		map_segnalino.put(0,new Integer[] {-1,-1});
		map_segnalino.put(1,new Integer[] {-1,-1});
		map_segnalino.put(2,new Integer[] {1,6});
		map_segnalino.put(3,new Integer[] {2,6});
		map_segnalino.put(4,new Integer[] {3,4});
		map_segnalino.put(5,new Integer[] {4,5});
		map_segnalino.put(6,new Integer[] {4,6});
		//gia che ci sono istanzio anche la hash da cui pesco i segnalini ( questa hash � variabile )
		disp_scuderia = new HashMap <String, Integer>();
	}
	
	/**
	 * Inizializzazione di tutti i colori in gioco ( tutte le scuderie ) 
	 */
	public  void init_colori(){
		colori = new ArrayList <String>();
		colori.add("nero");
		colori.add("blu");
		colori.add("verde");
		colori.add("rosso");
		colori.add("giallo");
		colori.add("bianco");
		
	}
	
	
	
	/**
	 * In base al numero di giocatori in gioco setto il numero di segnalini
	 * scommessa per ogni scuderia
	 * ( questo metodo viene richiamato anche quando viene eliminato un giocatore )
	 */
	public  void init_segnalini(){
		Integer num_segnalini = map_segnalino.get(elenco_giocatori.size())[0];
		for (String color : colori) {
			disp_scuderia.put(color, num_segnalini);
		}
	}
	
	/**
	 * inizializzo i turni in base al numero gi giocatori in gioco
	 * ( fatto solo una volta all'inizio )
	 */
	public void init_turni(){
		Integer turns = map_segnalino.get(elenco_giocatori.size())[1];
		this.turni = turns;
	}
	
	
	
	/**
	 * Metodo utilizzato come entry point del gioco, che effettua le 
	 * operazioni di inizializzazione di tutte le variabili globali
	 */
	public  void INIT()
	  {
		Carta_azione.Extract_all_in(mAzione);
		Carta_movimento.Extract_all_in(mMovimento);
		init_colori();
		init_cavalli();
		init_hash();
		init_quotazioni();
		Carta_personaggio.Extract_all_in(mPersonaggio);
		Collections.shuffle(elenco_giocatori);
	  }
	
//<-----------------FINE METODI DI INIZIALIZZAZIONE---------------------------------------->

	/**
	 * Toglie un sengalino scommessa dalla scuderia passata come parametro.
	 * @param color
	 */
	public void sub_segnalino_color(String color) {
		Integer old = disp_scuderia.get(color);
		disp_scuderia.put(color, old-1);
	}
	
	/**
	 * Aggiunge una carta azione ad un cavallo controllando che non si annulli con
	 * un'altra preesistente, se si annulla le butta tutte e 2.
	 * @param horse
	 * @param played_card
	 */
	public  void add_card_horse(Cavallo horse, Carta_azione played_card){
		int deleted=0;
		for (Carta_azione carta_azione : horse.get_carte_azione()) {
			if (carta_azione.get_classe()==played_card.get_classe()){
				horse.delete_action_card(carta_azione);
				deleted=1;
			}		
		}
		if(deleted==0) {
			horse.add_carta_azione(played_card);
		}
	}	 
	
	public void setFase(int fase){this.fase_corsa=fase;}
	
	
	/**
	 * Utile per capire prima della gara quali carte azione buone/cattive vengono eliminate
	 * ed eventualmente mettere un malus/bonus alla quotazione di un cavallo prima che tutto
	 * cominci ( utile sopratutto in GUI )
	 */
	public void pre_check_partenza()
	{
		for( Cavallo horse : elenco_pedine)
		   {card_h.check_special_card(horse);}
	}
	
	/**
	 * Quando viene chiamato partenza dal controller per ogni cavallo viene controllato se sono presenti
	 * carte-azione che agiscono in questa fase, se sono presenti la funzione di card_handler provvede a 
	 * modificare la posizione del cavallo e ritorna true segnalando al model di aver gi� mosso; 
	 * se ritorna false invece � il model che deve provvedere a muovere il cavallo usando lo spostamento
	 * tirato fuori dalla carta azione.
	 * quando ha finito modifica la fase della corsa a sprinting.
	 * 	 
	 */
	public void partenza()
	{
		current_card = (Carta_movimento) mMovimento.pesca();
		num_cavalli = elenco_pedine.size();
		for( Cavallo horse : elenco_pedine)
		  {
			spostamento = current_card.get_movimento(lavagna.getPosizione(horse.get_color()));
			state = card_h.check_carte_partenza(horse,spostamento); //check sugli effetti delle carte partenza
			if(state==false) //qua ci vado solo se il check sulle carte ha dato esito negativo
				{ horse.set_posizione(horse.get_posizione()+spostamento); }
		  }
		this.fase_corsa = 2; // switch in sprinting
	}
	
	/**
	 * La fase running copre la parte di gara in cui esistono cavalli in gioco, finita questa fase vado in sprint.
	 */
	public void running () 
	{
		current_card = (Carta_movimento) mMovimento.pesca();
		
		// mi salvo l'elenco delle posizioni per testare felix e vigor
		 ArrayList <Integer> posizioni = new ArrayList <Integer>();
         for(Cavallo c  : elenco_pedine)
            { 
        	 if(c.get_in_game() == 1)
        	   {posizioni.add(c.get_posizione());} // prendo solo le posizioni di chi e' in gioco
        	 }
         
		for( Cavallo horse : elenco_pedine)
		  {
			if(horse.get_in_game()==1) // se il cavallo su cui sto ciclando � ancora in gioco ( non ha tagliato il traguardo )
			  {
				   state = card_h.check_position_card(horse,posizioni); // check sulle carte che agiscono in base alla posizione
			       if(state == false)
			         {
			          int pos = horse.get_posizione();
			          spostamento = current_card.get_movimento(lavagna.getPosizione(horse.get_color()));
			          horse.set_posizione(pos+spostamento);
			         }
			     }
			   
		   }
		  
		posizioni.clear();
		
		//QUA CONTROLLO XIII E FUSTIS ET RADIX.
		for ( Cavallo horse : elenco_pedine)
		   {
			if(horse.get_in_game()==1)
			   {
				card_h.check_final_card(horse,lunghezza_percorso); //check sugli effetti di fine gara se qualcuno ci e' gia' arrivato
			   }
		   }
		this.fase_corsa=2; // vado in sprint 
	}
	
	/**
	 * Estrae 2 colori e li salva come attributi del model ( per permettere al controller di estrarli e creare l'evento 
	 * da spedire alla gui)
	 */
	public void sprinting(){
		colore1 = colori.get((int) (Math.random()*colori.size()));
		colore2 = colori.get((int) (Math.random()*colori.size()));
		for ( Cavallo horse : elenco_pedine )
		    {
			if(horse.get_in_game()==1)
			{
			 if((colore1.equals(horse.get_color())) || colore2.equals(horse.get_color()))
				 {state = card_h.check_carte_sprint(horse,sprint);
				  if(state == false)
					  { horse.set_posizione(horse.get_posizione()+sprint); }
				 }
			} 
		    }
		if(inizio == true )
		  {this.fase_corsa = 1;
		   inizio = false;
		   } // se arrivo allo sprint dalla partenza allora torno in fase normale
		else
			{ this.fase_corsa = 3; } //altrimenti vado in check traguardo 
}
	
	/**
	 * check se qualcuno ha tagliato il traguardo, se si lo inserisco in una lista di finalisti,
	 * se la lista di finalisti contiene pi� di un cavallo allora la ordino ( utilizzando la compareTo  definita in Cavallo )
	   Una volta ordinata la lista inserisco in fila i cavalli nella lista classifica e svuoto finalisti.
	 */
	public boolean check_traguardo() {
		
		for  ( Cavallo horse : elenco_pedine)
		{
		if(horse.get_posizione()>lunghezza_percorso && horse.get_in_game()==1)
			{
			horse.out_of_game(); 
			finalisti.add(horse);
			}
		}
	if(finalisti.size()>1)
		{ Collections.sort(finalisti); } //ordina i cavalli nella tabella finalisti sulla base del compare
	for(Cavallo horse : finalisti)
		{ classifica.add(horse.get_color()); }
	num_cavalli-=finalisti.size();
	finalisti.clear();
	this.fase_corsa = 1; // torno in situazione normale
	if(num_cavalli==0) // se la gara � finita ritorno false al controller che smette di chiamare gara
		{ return false; }
	return true;	 // altrimenti gli dico di continuare
	
	}
		
	
	/**
	 * Metodo che paga le scommesse vinte/piazzate del giocatore passato come parametro
	 * @param classifica
	 * @param player
	 */
	private ArrayList<Integer> make_payment_bet(Giocatore player) {
	int win_money=0;
	int win_pv=0;
	ArrayList <Integer> price = new ArrayList <Integer> ();		
	  
	for(Scommessa scommessa : player.get_scommesse()){
		
	    if (scommessa.get_colore().equals(classifica.get(0))) {
		   if (scommessa.get_tipo().equals("Vincente")){
			win_pv+=3;
			win_money+=(lavagna.getPosizione(classifica.get(0))+2)*scommessa.getDenaro();
			}
		else {
			  if (scommessa.get_tipo().equals("Piazzata")){
				win_pv+=1;
				win_money+=2*scommessa.getDenaro();
				}
			}
	  }
		
	  else if ((scommessa.get_colore().equals(classifica.get(2))||scommessa.get_colore().equals(classifica.get(1)))&&scommessa.get_tipo().equals("Piazzata")) {
		        win_money+=2*scommessa.getDenaro();
				win_pv+=1;
			}
	}
	 player.add_pv(win_pv);
	 player.set_money(player.get_money()+win_money); 
	 price.add(win_money);
	 price.add(win_pv);
	return price;	
		
	}
	
	/**
	 * Metodo che paga il player passato come parametro in base alla sua scderia
	 * I premi sono dichiarati come variabile locale in modo da poter 
	 * cambiare in futuro.
	 * @param classifica
	 * @param player
	 */
	private int make_payment_scud(Giocatore player){
		final int primo_premio=GameOption.primo_premio;
		final int secondo_premio=GameOption.secondo_premio;
		final int terzo_premio=GameOption.terzo_premio;
		if(player.get_scuderia().equals(classifica.get(0))){
			player.set_money(player.get_money()+primo_premio);
			return primo_premio;
		}
		else if(player.get_scuderia()==classifica.get(1)){
			player.set_money(player.get_money()+secondo_premio);
			return secondo_premio;
		}
		else if(player.get_scuderia()==classifica.get(2)){
			player.set_money(player.get_money()+terzo_premio);
			return terzo_premio;
		}	
	return 0;
	}
	
	/**
	 * metodo che sistema la situazione del giocatore eliminando le sue scommesse fatte in questo turno
	 * e le sue carte azione. 
	 * se non ha pi� soldi viene inserito in un arraylist di rimossi_temporanei ed usciti dal ciclo su ogni giocatore
	 * rimuovo dalla lista principale i giocatori finiti in questo arraylist.
	 */
	public ArrayList <Giocatore> sistema_giocatori()
	{	rearrange_player();
		ArrayList <Giocatore> rimossi = new ArrayList <Giocatore>();

		for (Giocatore player : elenco_giocatori) {
			player.drop_card(); // butto le carte del giocatore 
			player.drop_scommesse(); //elimino scommesse questo giro
			boolean enough = check_enough_money(player);
			if(enough == false)
			  {rimossi.add(player);}
			}
		for (Giocatore player : rimossi) {
			elenco_giocatori.remove(player);
		}
		return rimossi;
	}
	
	/**
	 * ripristina le variabili del gioco per poter ricominciare un nuovo turno
	 * controlla anche che si possa cominciare un nuovo turno basandosi sul numero di giocatori 
	 * rimasti e sul numero di turni rimanenti.
	 */
	public boolean sistema_gioco()
	{
		turni--;
		lavagna.updateLavagna(classifica);
		classifica.clear(); 
		elenco_pedine.clear();
		init_colori();
		init_cavalli();
        this.colore1="";//reset parametri corsa sprint e carta azione
		this.colore2="";
		this.current_card=null;
		this.fase_corsa=0;
		this.inizio = true;
		num_cavalli = elenco_pedine.size();
		init_segnalini();
		mMovimento.mischia();
		mAzione.mischia();
		if (elenco_giocatori.size()==0){
			return true;
		}
		if (elenco_giocatori.size()==1) {
			return true;
		}
		if (turni==0) {
			sort_by_pv();
			return true;
		}
		return false;
		
	}
	
	/**
	 * Voglio che ora il primo giocatore ad iniziare sia quello successivo a quello che 
	 * ha cominciato all'inizio del turno precedente.
	 */
	private void rearrange_player() {
		Giocatore player = elenco_giocatori.get(0);
		elenco_giocatori.remove(0);
		elenco_giocatori.add(player);
	}
	
	
	/**
	 * controlla se il giocatore ha abbastanza soldi per la min_bet 
	 * altrimenti sottrae 2 pv se non ne ha lo elimina dai giocatori 
	 * Questo metodo continua a togliere pv fino a che non ci sono abbastanza
	 * soldi per coprire la scommessa minima
	 * @param player giocatore da analizzare
	 * @return true se ha abbastanza soldi o false se non ne ha
	 */
	private  boolean check_enough_money(Giocatore player) {
		boolean togliPv;
		if (player.get_money()<player.get_min_bet()) {
			togliPv = player.sub_pv(2);
			if (!togliPv) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Ordina l'elenco dei giocatori sulla base del compareTo definito in Giocatore
	 * per decidere chi ha vinto la partita.
	 */
	private  void sort_by_pv(){
		Collections.sort(elenco_giocatori);
	}
}