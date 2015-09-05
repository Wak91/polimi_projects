/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse13
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse17
*/
%------------------------------------------------------------
% Am I in a list? 
% member(X,[yolanda,trudy,vincent,jules]).  asks: "exist an X s.t. is an element of the list?" 
%------------------------------------------------------------

member(X,[X|_]). %fact: X is a member of the list 
member(X,[_|T]) :- member(X,T).


%------------------------------------------------------------
% Recursive list of a and b
%------------------------------------------------------------

a2b([],[]). % a list that contain 0 a has got the same element as a list that contains 0 b!
a2b([a|Ta],[b|Tb]) :- a2b(Ta,Tb).


%------------------------------------------------------------
% The second element of a list
%------------------------------------------------------------

second(X,[_,X|_]).


%------------------------------------------------------------
% Swapping
%------------------------------------------------------------

swap12([X,Y|R], [Y,X|R]).


%------------------------------------------------------------
% German-English dictionary
%------------------------------------------------------------
tran(eins,one). 
tran(zwei,two). 
tran(drei,three). 
tran(vier,four). 
tran(fuenf,five). 
tran(sechs,six). 
tran(sieben,seven). 
tran(acht,eight). 
tran(neun,nine).

listtran([],[]).
listtran([X|T0],[Y|T1]) :- tran(X,Y) , listtran(T0,T1). 


%------------------------------------------------------------
% Double element in a list 
%------------------------------------------------------------

double(X,X,X).

twice([],[]).
twice([X|R],[Y,Y1|R1]) :- double(X,Y,Y1) , twice(R,R1).


%------------------------------------------------------------
% Combine lists 
%------------------------------------------------------------

combining(X,Y,X,Y).

combine([],[],[]).
combine([X|R],[Y|S],[Z,P|L]) :- combining(X,Y,Z,P) , combine(R,S,L).


%------------------------------------------------------------
% Combine lists again
%------------------------------------------------------------

combining2(X,Y,[X,Y]).

combine2([],[],[]).
combine2([X|R],[Y|S],[Z|L]) :- combining2(X,Y,Z) , combine2(R,S,L).


%------------------------------------------------------------
% Combine lists again, again...
%------------------------------------------------------------


combining3(X,Y,j(X,Y)).

combine3([],[],[]).
combine3([X|R],[Y|S],[Z|L]) :- combining3(X,Y,Z) , combine3(R,S,L).
