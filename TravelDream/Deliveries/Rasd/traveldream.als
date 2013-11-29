open util/integer

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//---SIGNATURE---
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//--- Utility Signature -----------------------------------------------------------------------
sig Data{
d_value: one Int
}
 {
  d_value > 0
 }

sig Testo{}

abstract sig Type{}

sig Invito extends Type {}
sig GiftListed extends Type{}
//-----------------------------------------------------------------------------------------------

//--- Actors Signature ----------------------------------------------------------------------
abstract sig Utente{}

sig UtenteRegistrato extends Utente
{
username:one Testo,
ur_viaggio:set Viaggio,  //viaggi creati utente ma non  ancora acquistati
ur_prenotazioni: set Prenotazione, // viaggi già acquistati
ur_condivisioni: set Condivisione // insieme dei pacchetti condivisi con qualcuno di tipo gift_listed o invito
}

sig UtenteNonRegistrato extends Utente
{}

sig Impiegato 
{
i_pacchetti: set Pacchetto,
i_hotel: set Hotel,
i_trasporto: set Trasporto,
i_escursione: set Escursione
}

sig Admin 
{
ad_impiegati: set Impiegato
}
//------------------------------------------------------------------------------------------------

// --- Resources Signature -----------------------------------------------------------------
abstract sig Trasporto
{
//t_available: one Int
t_partenza: one Testo,
t_arrivo: one Testo,
t_data: one Data,
}{
   t_partenza != t_arrivo
  }

sig Volo extends Trasporto
{

}

/* testing dei trasporti
sig Treno extends Trasporto
{}
*/

sig Escursione
{
 //e_nome: one Testo,
// e_price: one Int
   e_luogo: one Testo,
   e_data: one Data
}

sig Hotel
{
h_luogo: one Testo,
h_data1: one Data,
h_data2: one Data
//h_nome: one Testo,
//h_available: one Int,
//h_dailyPrice: one Int 
} { 
     h_data1.d_value < h_data2.d_value
   }

sig Pacchetto
{
//p_nome: one Testo,
p_destinazione: one Testo,
p_Data1 : one Data,
p_Data2:  one Data,
p_Hotel: some Hotel,
p_Escursione: set Escursione,
p_Trasporto_andata: some Trasporto,
p_Trasporto_ritorno:  some Trasporto
} { 
     #p_Trasporto_andata >=2
     #p_Trasporto_ritorno  >=2
     p_Data1.d_value < p_Data2.d_value
   }



//il viaggio seleziona elementi da un paccheto
sig Viaggio
{
v_pacchetto: one Pacchetto,
v_hotel: one Hotel,
v_escursione: set Escursione,
v_trasporto_a: one Trasporto,
v_trasporto_r: one Trasporto,
v_data1: one Data,
v_data2: one Data
}{
    v_data1.d_value < v_data2.d_value // la data di partenza è minore di quella di arrivo
    v_trasporto_a.t_data.d_value < v_trasporto_r.t_data.d_value // la data del trasporto di partenza è minore di quella di arrivo
  }

sig Prenotazione{
p_viaggio: one Viaggio,
p_id: one Testo
}


sig Condivisione
{
 c_utenti: some Utente, // rappresenta l'insieme di utenti a cui viene notificato il pacchetto 
 c_viaggio: one Viaggio, 
 c_type: one Type
}
-----------------------------------------------------------------------------------------------------------

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//---FACT---
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

// --- Fact1 : Nulla si crea da solo -----------------------------------------------------------------

//Gli hotel sono stati inseriti da un impiegato 
fact HotelInseriti
{
all h1: Hotel | one im: Impiegato | h1 in im.i_hotel
}
//I trasporti sono stati inseriti da un impiegato 
fact TrasportiInseriti
{
all t1: Trasporto | one im: Impiegato | t1 in im.i_trasporto
}
//Le escursioni sono state inserite da un impiegato
fact EscursioniInserite
{
all h1: Hotel | one im: Impiegato | h1 in im.i_hotel
}

//Le condivisioni devono essere fatte da utenti 
fact CondivisioniFatteDaUtenti
{
all c1 : Condivisione | some ur: UtenteRegistrato | c1 in ur.ur_condivisioni
}

//Ogni pacchetto è stato aggiunto da un'impiegato
fact PacchettoAggiuntoDaImpiegato
{
all p: Pacchetto | one im : Impiegato | p in im.i_pacchetti
}

//Ogni impiegato è stato aggiunto dall'admin
fact ImpiegatoAggiuntoDaAdmin
{
all im: Impiegato | one ad: Admin | im in ad.ad_impiegati
}

//dei viaggi creati corrispondono ad almeno un'utente registrato
fact NoViaggiSoli
{
all v1 : Viaggio | some ur: UtenteRegistrato | ( v1 in ur.ur_viaggio || v1 in ur.ur_condivisioni.c_viaggio )  
}

// Un utente non registrato deve essere stato invitato da un utente registrato 
fact NoNRegistratiInvitati{
all unr: UtenteNonRegistrato | some ur :UtenteRegistrato | unr in ur.ur_condivisioni.c_utenti
}
//----------------------------------------------------------------------------------------------------------

// --- Fact2 : Eliminazione duplicati e relazioni di appartenenza -----------------------------

//non esistono 2 utenti con lo stesso username
fact NoStessoUsernameUtenti{
	all disj ur1,ur2:UtenteRegistrato | ur1.username != ur2.username
}

//tutti i pacchetti proposti differiscono per almeno un'elemento
fact PacchettiDifferenti
{
all p1,p2 : Pacchetto |  ( p1!=p2 implies !SamePacket[p1,p2] )
}

//Tutte le condivisioni sono diverse 
fact CondivisioniDifferenti
{
all ur: UtenteRegistrato | no disj c1,c2: Condivisione | SameCondivisione[c1,c2]  and c1 in ur.ur_condivisioni && c2 in ur.ur_condivisioni
}

//un viaggio può essere condiviso se creato dall'utente che lo vuole condividere
fact CondivisioneViaggiCreatoDaStessoUtente{
all ur: UtenteRegistrato | all v1:Viaggio | ( v1 in ur.ur_condivisioni.c_viaggio implies v1  in ur.ur_viaggio ) 
}



//non compaio tra gli utenti invitati 
fact NonMiAutoInvito
{
all ur : UtenteRegistrato | ur not in ur.ur_condivisioni.c_utenti
}


fact AdminUnico
{
#Admin = 1
}

//Un viaggio contiene solo gli elementi del pacchetto da cui è stato creato
fact CorrispondenzaViaggioPacchetto
{
all v1: Viaggio | let pacchetto = v1.v_pacchetto| 
     v1.v_trasporto_a in pacchetto.p_Trasporto_andata && 
     v1.v_trasporto_r in pacchetto.p_Trasporto_ritorno && 
     v1.v_escursione in pacchetto.p_Escursione && 
     v1.v_hotel in pacchetto.p_Hotel 
}

// --- Fact3 : Controllo coerenza date -----------------------------------------------------------------------------------

// Per ogni viaggio nessuna barbonata 
fact CoerenzaDateHotelViaggio
{
 all h1: Hotel | all v1: Viaggio | 
      h1 in v1.v_hotel implies h1.h_data1.d_value = v1.v_data1.d_value  && 
      h1.h_data2.d_value = v1.v_data2.d_value
}

fact CoerenzaDateHotePacchetto
{
all h1 : Hotel | all p1: Pacchetto | 
     h1 in p1.p_Hotel implies h1.h_data1.d_value >= p1.p_Data1.d_value &&
     h1.h_data2.d_value<=p1.p_Data2.d_value
}

//Un'escursione selezionata in un viaggio deve cadere all'interno delle date del viaggio
fact CoerenzaDateEscursioneViaggio
{
all e1: Escursione | all v1 : Viaggio | e1 in v1.v_escursione implies e1.e_data.d_value >= v1.v_data1.d_value && e1.e_data.d_value <= v1.v_data2.d_value
}

//La disponibilità di un'escursione cade nelle date di validità del pacchetto 
fact CoerenzaDateEscursionePacchetti
{
all e1: Escursione | all p1 : Pacchetto | 
    e1 in p1.p_Escursione implies e1.e_data.d_value >= p1.p_Data1.d_value && 
    e1.e_data.d_value <= p1.p_Data2.d_value
}

//Tutte le date sono diverse
fact DateDiverse
{
all d1,d2 : Data | d1!=d2 implies d1.d_value != d2.d_value
}

//I trasporti di un pacchetto hanno date comprese nel periodo di validità del pacchetto 
fact TrasportiInDateCoerenti
{
all p1: Pacchetto | let all_trasporti = p1.p_Trasporto_andata + p1.p_Trasporto_ritorno | 
all t1 : all_trasporti | t1.t_data.d_value >= p1.p_Data1.d_value 
                                  && t1.t_data.d_value <= p1.p_Data2.d_value
}

//Le date del viaggio sono corrispondenti a quelle di partenza del trasporto di andata e di arrivo del trasporto di ritorno
fact ViaggiDateCoerentiTrasporto
{
all v1: Viaggio | v1.v_data1.d_value = v1.v_trasporto_a.t_data.d_value && v1.v_data2.d_value = v1.v_trasporto_r.t_data.d_value
}
// --- Fact4 : Controllo coerenza luoghi -----------------------------------------------------------------------------------

//Il luogo dell'escursione è identico a quello della destinazione del pacchetto
fact CoerenzaLuogoEscursione
{
all p1 : Pacchetto | all e1: Escursione | e1 in p1.p_Escursione implies e1.e_luogo = p1.p_destinazione
}

//il trasporto dell'andata deve essere diverso da quello di ritorno 
fact TrasportoAndataDiversoDalRitorno
{
 all p1 : Pacchetto | all tr1: Trasporto  | (tr1 in p1.p_Trasporto_andata  implies  tr1 not in p1.p_Trasporto_ritorno )
}

//la destinazione del pacchetto è coerente l'arrivo del trasporto di andata
fact ArriviCoerentiTrasportoAndata
{
all p1: Pacchetto |   all t1: Trasporto | t1 in p1.p_Trasporto_andata implies t1.t_arrivo = p1.p_destinazione 
}

//la destinazione del pacchetto è uguale alla partenza dell trasporto di ritorno
fact RitorniCoerenti
{
all p1: Pacchetto | all t1: Trasporto | t1 in p1.p_Trasporto_ritorno implies t1.t_partenza = p1.p_destinazione
} 

//la destinazione del pacchetto è uguale a quella dell'hotel
fact ArriviCoerentiHotel
{
all p1:Pacchetto | all h1: Hotel | h1 in p1.p_Hotel implies h1.h_luogo = p1.p_destinazione
}

//I 2 trasporti scelti nel viaggio sono coerenti tra di loro
fact TrasportiCoerentiInViaggio
{
all v1: Viaggio | v1.v_trasporto_a.t_partenza = v1.v_trasporto_r.t_arrivo && v1.v_trasporto_a.t_arrivo = v1.v_trasporto_r.t_partenza
}

// --- Fact5 : Controllo coerenza Prenotazioni-----------------------------------------------------------------------------------

//fact Prenotazioni
//la prenotazione appartiene solo ad un utente
fact PrenotazioneAppartieneAUnSoloUtente{
	all disj u1,u2:Utente | all p:Prenotazione | p in u1.ur_prenotazioni implies p not in u2.ur_prenotazioni
}

//la prenotazione può essere fatta prenotando un viaggio creato dallo stesso utente
fact PrenotoViaggioCreatodaStessoUtente{
	all u:UtenteRegistrato | all v:Viaggio | v in u.ur_prenotazioni.p_viaggio implies v in u.ur_viaggio	
}

//le prenotazioni hanno tutti ID diversi
fact NoPrenotazioniStessoIdoViaggio{
	all disj p1,p2:Prenotazione | p1.p_id != p2.p_id and not SameViaggio[p1.p_viaggio,p2.p_viaggio]
}

//a ogni prenotazione è associato un e un solo utente
fact OgniPrenotazioneHaUnUtente{
	all p:Prenotazione | one u:Utente | p in u.ur_prenotazioni
}

//Se viaggio è condiviso non può essere in prenotazione e viceversa
fact NoViaggioInPrenotazioneECondivisione{
	all v :Viaggio | all p:Prenotazione | all c:Condivisione |
		v in p.p_viaggio implies v not in c.c_viaggio and
		v in  c.c_viaggio implies v  not in  p.p_viaggio
}

//-------------------------------------------------------------------------------------------------------------------------

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//---PRED---
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//---Predicati utility----------------------------------------------------------------------------------------------------

//predicato utilizzato per verificare l'uguaglianza completa tra 2 pacchetti
pred SamePacket[p1,p2:Pacchetto]
{
p1.p_Data1 = p2.p_Data1 && p1.p_Data2 = p2.p_Data2 && p1.p_Hotel = p2.p_Hotel &&
 p1.p_Trasporto_andata = p2.p_Trasporto_andata &&  p1.p_Trasporto_ritorno = p2.p_Trasporto_ritorno  &&
 p1.p_Escursione = p2.p_Escursione &&p1.p_destinazione = p2.p_destinazione
}

//Predicato utilizzato per verificare l'uguaglianza tra 2  viaggi
pred SameViaggio[v1,v2:Viaggio]
{v1.v_data1 = v2.v_data1 && v1.v_data2= v2.v_data2 &&  v1.v_pacchetto= v2.v_pacchetto && v1.v_escursione = v2.v_escursione && 
  v1.v_trasporto_a = v2.v_trasporto_a && v1.v_trasporto_r = v2.v_trasporto_r
}
//Predicato utilizzato per verificare l'uguaglianza tra 2 condivisioni
//considero solo questi due campi palmenoerchè non voglio che ci sia una condivisione di 2 tipi diversi ma con stesse identiche cose
pred SameCondivisione[c1,c2:Condivisione]
{
c1.c_utenti = c2.c_utenti && c1.c_viaggio = c2.c_viaggio 
} 

//###########################################
//PRED REQUISITI FUNZIONALI
//###########################################

//personalizzazione pacchetto
//v1,v2,e1,e2,h sono le personalizzazioni dell'utente
//v è il viaggio creato
//p  è il pacchetto da cui è stato creato
//u1,u2 sono l'utente prima e dopo
pred PersonalizzazionePacchetto(u1,u2:Utente,v1,v2:Volo,e1,e2:Escursione,h:Hotel,v:Viaggio,p:Pacchetto){
	v.v_pacchetto =p
	h in p.p_Hotel
	e1 in p.p_Escursione
	e2 in p.p_Escursione
	v.v_trasporto_a=v1
	v.v_trasporto_r=v2
	v.v_hotel=h
	v.v_escursione = e1 +e2
	v not in u1.ur_viaggio 
	
	u2.ur_viaggio = u1.ur_viaggio +v 
}

assert PersonalizzazioneOK{
	all u1,u2:Utente | all e1,e2:Escursione | all h:Hotel| all v1,v2:Volo | all v:Viaggio |all  p:Pacchetto |
		v.v_pacchetto =p and
		v.v_trasporto_a =v1 and
		v.v_trasporto_r =v2 and
		v.v_hotel=h and
		v.v_escursione = e1 +e2 and
		PersonalizzazionePacchetto[u1,u2,v1,v2,e1,e2,h,v,p]
		implies v not in u1.ur_viaggio and v in u2.ur_viaggio and #u2.ur_viaggio = #u1.ur_viaggio+132

}


//predicato che descrive l'acquisto di un viaggio
//v viaggio acquistato
//p prenotazione creata
//u1,u2 utente prima e utente dopo
pred Acquisto(v:Viaggio,p:Prenotazione, u1,u2:UtenteRegistrato){
	v not in u1.ur_prenotazioni.p_viaggio
	p.p_viaggio = v

	u2.ur_prenotazioni = u1.ur_prenotazioni +p

}
//verifico che l'acquisto è andato a buon fine
assert AcquistoOk{
	all v:Viaggio | all u1,u2:UtenteRegistrato | all p:Prenotazione|
	Acquisto[v,p,u1,u2] implies #u2.ur_prenotazioni=#u1.ur_prenotazioni +1			
}

// Predicato che descrive la creazione di un pacchetto da parte dell'impiegato

 pred CreazionePacchetto(i1,i2:Impiegato, p:Pacchetto){
	p not in i1.i_pacchetti
	
	i2.i_pacchetti= i1.i_pacchetti +p
	i2.i_hotel = i1.i_hotel
	i2.i_escursione = i1.i_escursione
	i2.i_trasporto = i1.i_trasporto	
}

assert VerificoCreazione{
	all i1,i2:Impiegato |all p:Pacchetto |
	CreazionePacchetto[i1,i2,p] implies p not in i1.i_pacchetti and 
														p in i2.i_pacchetti and 
														#i1.i_pacchetti=#i2.i_pacchetti+1 and 
														all p2:Pacchetto |p2 in i1.i_pacchetti implies p2 in i2.i_pacchetti														
}

//predicato che permettere di aggiungere un amico ad una gift list di pacchetti
//u è l'utente che condivide
//u3 è l'amico che viene aggiunto alla Gift
//c1,c2 sono i 2 stati (prima e dopo la condivisione)
pred AggiuntaAmicoCondivisioneGift(u:UtenteRegistrato,u3:Utente,c1,c2:Condivisione){
u3 not in c1.c_utenti
c1 in u.ur_condivisioni
c1.c_type=GiftListed
c2.c_viaggio =c1.c_viaggio
c2.c_utenti = c1.c_utenti + u3
c2.c_type= c1.c_type	
c2 in u.ur_condivisioni
}
//controllo che l'amico prima della condivisione non c'era e dopo c'è nella Condivisione e che questa appartiene ancora a u
assert VerificaCondivisione{
all u:UtenteRegistrato | all u2:Utente | all c1,c2:Condivisione |
c1 in u.ur_condivisioni and AggiuntaAmicoCondivisioneGift[u,u2,c1,c2] implies
u2 not in c1.c_utenti and u2 in c2.c_utenti  and c2 in u.ur_condivisioni
}

pred RegistrazioneUtente(unr: UtenteNonRegistrato )
{
 unr in UtenteNonRegistrato 
 

}

//--- Check dei requisiti funzionali ---

//check VerificoCreazione
//check VerificaCondivisione
//check PersonalizzazioneOK

//###########################################

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//---ASSERT---
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//---Utility Assert--------------------------------------------------------------------------------------------------------------------
assert ArriviCoerentiTrasportoAndata
{
all p1: Pacchetto |   all t1: Trasporto | t1 in p1.p_Trasporto_andata implies t1.t_arrivo = p1.p_destinazione
}


----------------------------------------------------TESTING--------------------------------------------------------------------------
//check LuoghiCoerenti
pred show
{
#Impiegato=1
#UtenteRegistrato=2
#UtenteNonRegistrato=2
#Pacchetto=1
#Viaggio=2
#Hotel=2
#Escursione=2
#Trasporto=4
#Data=6
#Pacchetto.p_Escursione =1
#Condivisione = 1
#Prenotazione =1
}

run show for 7
