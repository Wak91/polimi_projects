#!/usr/bin/python
# _*_ coding: iso-8859-15 _*_


__doc__='''Simple lexer for PL/0 using generators'''

# Tokens can have multiple definitions if needed
symbols =  { 
	'lparen' : ['('], 
	'rparen' : [')'], 
	'times'  : ['*'], 
	'slash'  : ['/'], 
	'plus'   : ['+'], 
	'minus'  : ['-'], 
	'eql'    : ['='], 
	'neq'    : ['!='], 
	'lss'    : ['<'], 
	'leq'    : ['<='],   
	'gtr'    : ['>'], 
	'geq'    : ['>='], 
	'mod' 	: ['%'],
	'callsym': ['call'], 
	'beginsym'  : ['begin'], 
	'semicolon' : [';'], 
	'endsym'    : ['end'], 
	'ifsym'     : ['if'], 
	'whilesym'  : ['while'], 
	'becomes'   : [':='], 
	'thensym'   : ['then'], 
	'dosym'     : ['do'], 
	'constsym'  : ['const'], 
	'comma'     : [','], 
	'varsym'    : ['var'], 
	'procsym'   : ['procedure'],	 
	'period'    : ['.'], 
	'oddsym'    : ['odd'],
	'print'			: ['!', 'print'],
	'lsquare'	: ['['],
	'rsquare'   : [']'],

}

#This function return the correct token
#that match the word.
def token(word):
	'''Return corresponding token for a given word'''
	for s in symbols : 
		if word in symbols[s] :
			return s # matching and returning the token equivalent to this word
	try : # If a terminal is not one of the standard tokens but can be converted to float, then it is a number, otherwise, an identifier
		float(word)
		return 'number' #number token 
	except ValueError, e :
		return 'ident'  #identifier token

def lexer(text) :
	import re
	from string import split, strip, lower, join
	import sys 

	t=re.split('(\W+)',text) # splitta sugli spazi, in pratica ho poi un array di parole in t 

	print t # this is the array of words and spaces 

	text=join(t,' ') #removing spaces and '\n'
	print text 

	words=[ strip(w) for w in split(lower(text)) ] 
	#in words I have this : ['var', 'x', ',', 'y', ',', 'z', ';', 'begin', 'x', ':=', '3', ';', 'y', ':=', '2', ';', 'z', ':=', '3', '%', '2', 'end', '.']

	print words

	for word in words :
		yield token(word), word  # a pair i.e. <SEMICOLON ;>


if __name__ == '__main__' :
	from program import __test_program
	for t,w in lexer(__test_program) :
		print t, w
