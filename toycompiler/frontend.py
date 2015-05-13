#!/usr/bin/python
# _*_ coding: iso-8859-15 _*_

__doc__ = '''PL/0 recursive descent parser adapted from Wikipedia'''

from ir import *
from logger import logger
import sys


symbols =  [ 'ident', 'lsquare' , 'rsquare' , 'number', 'mod' , 'lparen', 'rparen', 'times', 'slash', 'plus', 'minus', 'eql', 'neq', 'lss', 'leq', 'gtr', 'geq', 'callsym', 'beginsym', 'semicolon', 'endsym', 'ifsym', 'whilesym', 'becomes', 'thensym', 'dosym', 'constsym', 'comma', 'varsym', 'procsym', 'period', 'oddsym' ]

sym = None  # simbolo corrente   ( se il sym ident )
value = None  #valore del simbolo corrente  ( il valore il nome della variabile )

new_sym = None  #simbolo successivo 
new_value = None   #valore del simbolo successivo 

#calling getsym() we populate the sym,value,new_sym,new_value with the tokens from the lexer
def getsym():
	'''Update sym'''
	global new_sym 
	global new_value
	global sym
	global value
	try :
		sym=new_sym
		value=new_value
		new_sym, new_value=the_lexer.next() # it exploits the yeld in the lexer, it will return the next TOKEN
	except StopIteration :
		return 2
	print 'getsym:', new_sym, new_value
	return 1
	
def error(msg):
	print msg, new_sym, new_value
	
def accept(s):
	print 'accepting', s, '==', new_sym
	return getsym() if new_sym==s else 0 #ritorna il simbolo successivo a questo se il corrente e' uguale alla speculazione sul aprametro s
 
def expect(s) : 
	print 'expecting', s
	if accept(s) : return 1  #se tutto ok la accept fa switchare tutto al prossimo token
	error("expect: unexpected symbol")
	return 0 #altrimenti aspetta a far switchare i token
 
#Questa funzione ritorna sempre e solo oggetti di tipo Var o Const 
@logger
def factor(symtab) :  #factor controlla l'operando di un'espressione. ad esempio a := 5 +3 controlla cosa è 5 ( che può essere anche un'espressione complessa )

	if accept('ident'): #se è un identificatore allora lo cerco nella symbol table e lo ritorno creando l'oggetto Var
		return Var(var=symtab.find(value), symtab=symtab)

	if accept('number') : return Const(value=value, symtab=symtab) # se è un numero ritorno l'oggetto costante che rappresenta quel numero

	elif accept('lparen') : #se trovo una parentesi devo risolvere la sotto-espressione in modo ricorsivo
		expr = expression(symtab) # una volta risolta 
		expect('rparen') # mi aspetto un chiusa tonda 
		return expr
	else :
		error("factor: syntax error")
		getsym()

 
@logger
def term(symtab) :
	op=None

	expr = factor(symtab)

	while new_sym in [ 'times', 'slash' , 'mod'] :
		getsym() #restituisce prossimo simbolo 
		op = sym #salvo che operatore sto parsando 
		expr2 = factor(symtab) # valuto il secondo operando dell'operazione risolvendo l'eventuale espressione
		expr = BinExpr(children=[ op, expr, expr2 ], symtab=symtab)
	return expr
 
@logger
def expression(symtab) :
	op=None

	#print "BEFORE GETSYM()" + str(new_sym);
	#OMG
	#print new_sym in [ 'plus' , 'minus' ]
	#print 'plus' or 'minus'
	#print type(new_sym)
	
	if new_sym in [ 'plus' , 'minus' ] :
		getsym()
		op = sym

	expr = term(symtab) 

	#CAMBIATO initial_op in op, giusto?????
	if op : 
		expr = UnExpr(children=[op, expr], symtab=symtab) #se avevo già trovato un operando il codice di prima ha già settato op e quindi sono

	while new_sym in [ 'plus' , 'minus' ] :
		getsym()
		op = sym
		expr2 = term(symtab)
		expr = BinExpr(children=[ op, expr, expr2 ], symtab=symtab) #se avevo risolto in una unary ma c'era altra roba, qua sovrascrivo con una binary 
	
	#print "ESPRESSIONE RISOLTA " + str(expr)
	return expr
 
@logger
def condition(symtab) :
	if accept('oddsym') : 
		return UnExpr(children=['odd', expression(symtab)], symtab=symtab)
	else :
		expr = expression(symtab);
		if new_sym in [ 'eql', 'neq', 'lss', 'leq', 'gtr', 'geq' ] :
			getsym()
			print 'condition operator', sym, new_sym
			op=sym
			expr2 = expression(symtab)
			return BinExpr(children=[op, expr, expr2 ], symtab=symtab)
		else :
			error("condition: invalid operator")
			getsym();
 
@logger
def statement(symtab) : #da qua in poi la symbolTable non può più essere aggiornata, il tempo per le definizioni è finito! il tempo degli orchi è iniziato
	
	if accept('ident') : # sto vedendo se è un assegnamento a := 5+4+3+b;		if(accept('lsquare')):
		name = value
		target = symtab.find(name)
		if accept('lsquare'):
			index = expression(symtab)
			expect('rsquare')
			expect('becomes')
			expr = expression(symtab)
			return AssignArrayStat(target=target, expr=expr, index = index , symtab=symtab) 
		else:
			expect('becomes')
			expr=expression(symtab)
			return AssignStat(target=target, expr=expr, symtab=symtab) #creo e ritorno la struttura dell'assegnamento

	elif accept('callsym') : # call a 
		expect('ident')
		return CallStat(call_expr=CallExpr(function=symtab.find(value), symtab=symtab), symtab=symtab)

	elif accept('beginsym') :

		statement_list = StatList(symtab=symtab)
		statement_list.append(statement(symtab))

		while accept('semicolon') :
			if(new_sym == 'endsym'):
				break
			statement_list.append(statement(symtab))
		print new_sym
		expect('endsym');
		statement_list.print_content()
		return statement_list

	elif accept('ifsym') :
		expect('lparen')
		cond=condition(symtab)
		expect('rparen')
		expect('thensym')
		then=statement(symtab)
		return IfStat(cond=cond,thenpart=then, symtab=symtab)

	elif accept('whilesym') :
		cond=condition(symtab)
		expect('dosym')
		body=statement(symtab)
		return WhileStat(cond=cond, body=body, symtab=symtab)

	elif accept('print') :
		expect('ident')
		return PrintStat(symbol=symtab.find(value),symtab=symtab)
 
@logger
def block(symtab) :
	local_vars = SymbolTable()
	defs = DefinitionList() #row 418 in ir.py
	
	if accept('constsym') :  #se trovo una dichiarazione di costante, i.e. symbol='constsym' value='const' | new_symbol='ident' new_value='a'
		expect('ident')  #mi aspetto che il prossimo token sia un identifier. Se tutto va a buon fine ora ho symbol ='ident'  value='a' | new_symbol='number'  new_value='2'
		name=value # name = 'a'
		expect('eql')
		expect('number') 

		#ERRORE
		local_vars.append(Symbol(name, standard_types['int'], value)) #se tutto ok con le expect, aggiungi alla SymbolTable il nuovo oggettino Symbol

		#print "ZAZAZAZAZAZAZAZAZAZ" + str(new_sym)

		while accept('comma') :  #costanti dichiarate come CONST a=5,b=9,c=0;
			expect('ident')
			name=value
			expect('eql')
			expect('number')
			local_vars.append(Symbol(name, standard_types['int']), value)

		expect('semicolon'); #mi aspetto alla fine di tutto un semicolon 

	if accept('varsym') : # VAR x,y,z;
		expect('ident')
		name = value #salviamo il nome dell'array
		if(new_sym == 'lsquare'):
			expect('lsquare')
			expect('number')
			size = value # salviamo la grandezza dell'array
			expect('rsquare')
			local_vars.append(Symbol(name,ArrayType(name, size , standard_types['int'] ) ))
		else:	
			a = Symbol(value, standard_types['int'])
			local_vars.append(a)  #differenza con variabili è che qua non metto un valore di partenza 
		while accept('comma') :
			expect('ident')
			name = value
			if(new_sym == 'lsquare'):
				expect('lsquare')
				expect('number')
				print sym 
				print value
				print new_sym
				size = value # salviamo la grandezza dell'array
				print size
				expect('rsquare')

				b = ArrayType(name, size , standard_types['int'] )
				a = Symbol(name, b )
				local_vars.append(a)
			else:	
				local_vars.append(Symbol(value, standard_types['int']))  #differenza con variabili è che qua non metto un valore di partenza 
			print local_vars
			expect('semicolon');

	while accept('procsym') : #procedure a;
		expect('ident') 
		fname=value
		expect('semicolon');  
		local_vars.append(Symbol(fname, standard_types['function'])) #appende alla lista SymTable
		fbody=block(local_vars) # parsa il corpo della funzione. DOMANDA: perchè passa symtable se poi ne usa una locale dentro?   
		expect('semicolon')  #si aspetta semicolon di chiusura del body

		defs.append(FunctionDef(symbol=local_vars.find(fname), body=fbody))

	stat = statement(SymbolTable(symtab[:]+local_vars)) #appende la symbTable locale a quella passata all'inizio

	return Block(gl_sym=symtab, lc_sym=local_vars, defs=defs, body=stat)
 
@logger
def program() :
	'''Axiom'''
	
	global_symtab=SymbolTable()  # in ir.py, it return an object that represent the SymbolTable
	getsym() #here, row <17>; it populate the new_sym and new_value with the next_token 

	the_program = block(global_symtab) #popola la systemTable con la funzione block
	expect('period')
	return the_program


#ENTRY POINT 
if __name__ == '__main__' :
	from lexer import lexer
	from program import __test_program


	the_lexer=lexer(__test_program) #call the lexer on the program 

	res = program() 




	print '\n', res, '\n'

		
	res.navigate(print_stat_list)
	from support import *


	node_list=get_node_list(res)
	for n in node_list :
		print type(n), id(n), '->', type(n.parent), id(n.parent)
	print '\nTotal nodes in IR:', len(node_list), '\n'

	res.navigate(lowering)


	print res 

	'''


	node_list=get_node_list(res)
	print '\n', res, '\n'
	for n in node_list :
		print type(n), id(n)
		try :	n.flatten()
		except Exception :	pass
	#res.navigate(flattening)
	print '\n', res, '\n'

	print_dotty(res,"log.dot")

	from cfg import *
	cfg=CFG(res)
	cfg.liveness()
	cfg.print_liveness()
	cfg.print_cfg_to_dot("cfg.dot")
	from regalloc import *
	ra = minimal_register_allocator(cfg,8)
	reg_alloc = ra()
	print reg_alloc

'''