/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse18
*/

%------------------------------------------------------------
% Rocket science math...
%------------------------------------------------------------
add_3_and_double(X,Y)  :-  Y  is  (X+3)*2.



%------------------------------------------------------------
% MyLength classic ( this is not tail recursive ) 
%------------------------------------------------------------

len([],0). 
len([_|T],N)  :-  len(T,X),  N  is  X+1.


%------------------------------------------------------------
% MyLength with accumulator ( this is tail recursive ) 
%------------------------------------------------------------

accLen([],A,A). % "in an empty list the accumulator is equal to the length!"
accLen([_|T],A,L) :- Anew  is  A+1,  accLen(T,Anew,L). 

%------------------------------------------------------------
% Max of a list 
%------------------------------------------------------------

accMax([],A,A).
accMax([H|T],A,Max)  :- H  >  A, accMax(T,H,Max). 
accMax([H|T],A,Max)  :- H  =<  A, accMax(T,A,Max). 
    
max(List,Max)  :- List  =  [H|_], accMax(List,H,Max). % the list will be always a list of integer
													  % so we can unify List with the head of the list and
													  % call then the predicate accMax. 


%------------------------------------------------------------
% Lazy increment
%------------------------------------------------------------

increment(X,Y) :- Y is X+1.

%------------------------------------------------------------
% Saying the truth...
%------------------------------------------------------------

sum(X,Y,Z) :- Z is X+Y.


%------------------------------------------------------------
% Addone
%------------------------------------------------------------

addone([],[]).
addone([X|Y],[Z|L]) :- Z is X+1 , addone(Y,L).


%------------------------------------------------------------
% Min of a list 
%------------------------------------------------------------

accMin([],A,A).
accMin([H|T],A,Min)  :- H  >  A, accMin(T,A,Min). 
accMin([H|T],A,Min)  :- H  =<  A, accMin(T,H,Min). 
    
min(List,Min)  :- List  =  [H|_], accMin(List,H,Min).


%------------------------------------------------------------
% Messing with vectors
%------------------------------------------------------------


scalarMult(_,[],[]).
scalarMult(X,[Y|Z],[R|P]) :- R is X*Y , scalarMult(X,Z,P).


%------------------------------------------------------------
% Messing with vectors seriously...
%------------------------------------------------------------

dotMult([],[],0).
dotMult([X|Y],[Z|W],R) :- Rnew is X*Z , dotMult(Y,W,S) , R is Rnew + S.




