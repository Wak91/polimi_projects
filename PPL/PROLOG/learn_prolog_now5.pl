/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse24
*/

%--------------------------------------------------------------------------
% The append definition
% ?-  append(X,Y,[a,b,c,d]). "can be used to split the list into every possible two list" 
%--------------------------------------------------------------------------

append([],L,L). 
append([H|T],L2,[H|L3])  :-  append(T,L2,L3).


%--------------------------------------------------------------------------
% prefix and suffix of lists and sub-lists
%--------------------------------------------------------------------------

prefix(P,L) :- append(P,_,L).
suffix(S,L):-  append(_,S,L).
sublist(SubL,L):-  suffix(S,L),  prefix(SubL,S).


%--------------------------------------------------------------------------
% naive reverse using append
%--------------------------------------------------------------------------

naiverev([],[]). 
naiverev([H|T],R):-  naiverev(T,RevT),  append(RevT,[H],R).

%--------------------------------------------------------------------------
% reversing with accumulator
%--------------------------------------------------------------------------

accRev([],A,A).
accRev([H|T],A,R):-  accRev(T,[H|A],R). 

rev(L,R):-  accRev(L,[],R).


%--------------------------------------------------------------------------
% Double lists are boring...
%--------------------------------------------------------------------------

doubled(L) :- append(X,Y,L) , X=Y.


%--------------------------------------------------------------------------
% Palindrome are not boring...
%--------------------------------------------------------------------------

palindrome(L) :- rev(L,R) , R=L. 


%--------------------------------------------------------------------------
% I can't stand the first and last one elements in a list!
%--------------------------------------------------------------------------


toptail([],[]).
toptail([X],[]).
toptail([X,Y],[]).
toptail([X|Y],L) :- rev(Y,[P|P1]), rev(P1,D) , L=D.


%--------------------------------------------------------------------------
% I want X in the last position of the list ( exploiting rev ) 
%--------------------------------------------------------------------------

lastX(X,X).
lastX(X,L) :- rev(L,[Last|Rev]) , lastX(X,Last). 

%--------------------------------------------------------------------------
% I want X in the last position of the list ( exploiting only recursion ) 
%--------------------------------------------------------------------------

lastX1(X,[X]).
lastX1(X,[Y|L]) :- lastX1(X,L).


%--------------------------------------------------------------------------------
% Check if L1 is identical to L2 except the first and last element are exchanged
% L1 = [ 1,2,3,4,5,6 ]
% L2 = [ 6,2,3,4,5,1 ]
% yes.
%--------------------------------------------------------------------------------

swapfl([],[]).
swapfl([XH|Y], [ZH|W]) :- rev(Y,[YL|YR]) , rev(W,[XL|XR]) , XH = XL , ZH = YL , YR=XR.


%--------------------------------------------------------------------------
% Member one liner 
%--------------------------------------------------------------------------

memberoneliner(X,L) :- append(_,[X|_],L).


%--------------------------------------------------------------------------
% Delete duplicates in the list 

% set([2,2,foo,1,foo,[],[]],X).
% X = [2,foo,1,[]].

%NOTE:
%####################################################################
% the 'not(member...)' is essential in order to avoid wrong solution
% because if we ask other solution with ; , prolog backtracks
% at the first checking of member predicate, it fails and so it
% will try to hit the goal with OH=X leading to wrong solution.
% ALWAYS REMEMBER that the clauses aren't exclusive! If you need
% exclusivility you have to enforce it like in this exercise.
% OR you can use the '!' at the end of the first clause of member! 
%####################################################################

%--------------------------------------------------------------------------

member(X,[X|_]). 
member(X,[_|T]) :- member(X,T).

set([],[]).
set([X|Y],O) :- member(X,Y) , set(Y,O).
set([X|Y],[OH|L]) :- not(member(X,Y)) , OH=X , set(Y,L).  

%--------------------------------------------------------------------------
% Flatten a list 
%--------------------------------------------------------------------------

% TO DO HOLY SHIT 




