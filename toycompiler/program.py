#!/usr/bin/python

'''
1) Dichiarare sempre prima le CONST e poi le VAR
'''

__test_program='''CONST a=5; VAR x,y,l[5] ; 


BEGIN
x:=0;
y:= -a+5+3+x; 
x:= 3+1+2+y;
l[5+y+55] := -a;
END.
'''


#l'assegnamento ad array va con tutti gli spazi che ci sono altrimenti il parser scazza a capire ]:=