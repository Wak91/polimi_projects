#!/usr/bin/python

'''
1) Dichiarare sempre prima le CONST e poi le VAR
'''

__test_program='''CONST a=5; VAR o,x,y,l[5] ; 


BEGIN
y:= -a+5+3+x;
END.
'''


#l'assegnamento ad array va con tutti gli spazi che ci sono altrimenti il parser scazza a capire ]:=
#
#x:= 3+1+2+y;
#l[5+y+55] := -a;
#if(x=3) then y:=4;
#for( j:=0; j<10; 6 ) BEGIN o:=5; END
