/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse6
*/
%------------------------------------------------------------
% This is a Knowledge base 
%------------------------------------------------------------

woman(mia).  %fact1
woman(jody).  %fact2
woman(yolanda).  %fact3
playGuitar(jody).   %fact4
party.  %proposition

%------------------------------------------------------------
% Another Knowledge base 
% 5 clauses
%------------------------------------------------------------

happy(yolanda).  %fact1
listens2Music(mia).  %fact2
listens2Music(yolanda):-  happy(yolanda).  %rule1 with one goal 
playsAirGuitar(mia):-  listens2Music(mia). %rule2 with one goal 
playsAirGuitar(yolanda):-  listens2Music(yolanda). %rule3 with one goal 

%------------------------------------------------------------
% Yet another party
%------------------------------------------------------------

happy1(vincent). 
listens2Music1(butch). 
playsDrums(vincent):- listens2Music1(vincent), happy1(vincent).  %two goal in the body of this rule 
playsDrums(butch):- happy1(butch); listens2Music1(butch).

%------------------------------------------------------------
% A novelty for us
% ?- woman(X) will asks: "tell me which of the individuals you know about is a woman"
% X = mia ;  with the ; we are asking "other alternatives?" 
% loves(marsellus,X),  woman(X).  asking "exist a woman that is loved by marsellus?" 
%------------------------------------------------------------

woman1(katerina). 
woman1(anna). 
woman1(july). 
    
loves(vincent,katerina). 
loves(marsellus,katerina). 
loves(pumpkin,honey_bunny). 
loves(honey_bunny,pumpkin).


%------------------------------------------------------------
% Let's introduce some pepper in the novelty
% ?- jealous(marsellus,W).  asks: "can you find an individual W such that Marsellus is jealous of W ?" 32
%------------------------------------------------------------

loves1(vincent,mia). 
loves1(marsellus,mia). 
loves1(pumpkin,honey_bunny). 
loves1(honey_bunny,pumpkin). 
    
jealous(X,Y) :- loves1(X,Z),  loves1(Y,Z).

%------------------------------------------------------------
% Let's add some geometry...
%------------------------------------------------------------
vertical(line(point(X,Y),point(X,Z))). 
horizontal(line(point(X,Y),point(Z,Y))).

%------------------------------------------------------------
% Nazigrammar
% ?-sentence(X,Y,Z,W,P) generates all the possible sentence that we can create 
%------------------------------------------------------------
word(determiner,a). 
word(determiner,every). 
word(noun,criminal). 
word(noun,shit). 
word(verb,eats). 
word(verb,likes). 
    
sentence(Word1,Word2,Word3,Word4,Word5):- 
         word(determiner,Word1), 
         word(noun,Word2), 
         word(verb,Word3), 
         word(determiner,Word4), 
         word(noun,Word5).


%------------------------------------------------------------
%Puzzle
%------------------------------------------------------------

wordp(astante,  a,s,t,a,n,t,e). 
wordp(astoria,  a,s,t,o,r,i,a). 
wordp(baratto,  b,a,r,a,t,t,o). 
wordp(cobalto,  c,o,b,a,l,t,o). 
wordp(pistola,  p,i,s,t,o,l,a). 
wordp(statale,  s,t,a,t,a,l,e).

crossword(H1,H2,H3,Z1,Z2,Z3) :- wordp(H1,_,B,_,D,_,F,_) , wordp(H2,_,I,_,M,_,O,_) ,wordp(H3,_,R,_,T,_,V,_) ,wordp(Z1,_,B,_,I,_,R,_) ,wordp(Z2,_,D,_,M,_,T,_) ,wordp(Z3,_,F,_,O,_,V,_).
