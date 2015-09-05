/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse43
*/



%--------------------------------------------------------------------------
% First useful example of the cut  ( a green cut ) 
% If the first rule succeed , never backtrack on this, it makes no sense!
% [ If you already knows that the number was minor-equal to Y it makes
%   no sense to check if it is greater ]  
% The clauses are mutually exclusive!!
%--------------------------------------------------------------------------

maxg(X,Y,Y)  :-  X  =<  Y,!. 
maxg(X,Y,X)  :-  X>Y.

%-------------------------------------------------------------------------
% This is instead a red cut, if you remove it the program is wrong
%-------------------------------------------------------------------------

maxr(X,Y,Z)  :-  X  =<  Y,!,  Y  =  Z. 
maxr(X,Y,X).



%-------------------------------------------------------------------------
% Introducing Failure
% cut-fail combination 
% ?- enjoys(vincent,b) => no. 
%-------------------------------------------------------------------------


enjoys(vincent,X)  :-  big_kahuna_burger(X),!,fail. 
enjoys(vincent,X)  :-  burger(X). 
    
burger(X)  :-  big_mac(X). 
burger(X)  :-  big_kahuna_burger(X). 
burger(X)  :-  whopper(X). 
    
big_mac(a). 
big_kahuna_burger(b). 
big_mac(c). 
whopper(d).


%-------------------------------------------------------------------------
% Negation with cut-fail
% For any Prolog goal, neg(Goal) will succeed precisely if Goal does not succeed.
% in standard prolog \+ is negation as failure! 
% NEGATION AS FAILURE IS NOT LOGICAL NEGATION!
%-------------------------------------------------------------------------

neg(Goal)  :-  Goal,!,fail. 
neg(Goal).


%-------------------------------------------------------------------------
% Vincent with clearer ideas...
%-------------------------------------------------------------------------

enjoys1(vincent,X)  :-  burger(X), neg(big_kahuna_burger(X)).


%-------------------------------------------------------------------------
% test!
%-------------------------------------------------------------------------

p(1).
p(2) :- !.
p(3).


%-------------------------------------------------------------------------
% adding red cuts  e green cuts 
%-------------------------------------------------------------------------

class(Number,positive)  :-  Number  >  0. 
class(0,zero). 
class(Number,negative)  :-  Number  <  0.


classr(Number,C) :- Number > 0 , ! , C=positive.   % red cuts 
classr(0,C):- ! , C=zero . 
classr(Number,negative). 


classg(Number,positive)  :-  Number  >  0 , !.  %green cuts 
classg(0,zero) :- !.  
classg(Number,negative)  :-  Number  <  0 , !.   



%-------------------------------------------------------------------------
% split splits a list of integers into two lists: one containing the positive ones 
% (and zero), the other containing the negative ones. For example:
%-------------------------------------------------------------------------

split([],[],[]).
split([X|Y],[Z|W],N) :- X>=0, ! , Z=X , split(Y,W,N).
split([X|Y],P,[Z|W]) :- X<0 , ! , Z=X , split(Y,P,W).


%-------------------------------------------------------------------------
% I hate trains part II 
% /www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse46
%-------------------------------------------------------------------------

directTrain(saarbruecken,dudweiler). 
directTrain(forbach,saarbruecken). 
directTrain(freyming,forbach). 
directTrain(stAvold,freyming). 
directTrain(fahlquemont,stAvold). 
directTrain(metz,fahlquemont). 
directTrain(nancy,metz).


directTrain(dudweiler,saarbruecken). 
directTrain(saarbruecken,forbach). 
directTrain(forbach,freyming). 
directTrain(freyming,stAvold). 
directTrain(stAvold,fahlquemont). 
directTrain(fahlquemont,metz). 
directTrain(metz,nancy).



