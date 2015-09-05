/*
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse9
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse11
*/

%------------------------------------------------------------
% Recursively food chain! 
%------------------------------------------------------------

is_digesting(X,Y)  :-  just_ate(X,Y). % "if x has just ate y so x is digesting y!"
is_digesting(X,Y)  :- 
                   just_ate(X,Z), 
                   is_digesting(Z,Y). 
    
just_ate(mosquito,blood(john)). 
just_ate(frog,mosquito). 
just_ate(stork,frog).


%------------------------------------------------------------
% Recursively  numbers definition
%------------------------------------------------------------

numeral(0).     
numeral(succ(X))  :-  numeral(X).


%------------------------------------------------------------
% Recursively addition 
% ?- add(succ(succ(0)) , succ(0) , Y) asks: "exists an Y s.t. it's the sum of the previous two numbers?"
%------------------------------------------------------------

add(0,Y,Y). 
add(succ(X),Y,succ(Z)) :- add(X,Y,Z).


child(anne,bridget). 
child(bridget,caroline). 
child(caroline,donna). 
child(donna,emily). 
    
descend(X,Y)  :-  child(X,Y). 
descend(X,Y)  :-  child(Z,Y), descend(X,Z).
 
%-------------------------------------------------------
% Russian dolls 
%-------------------------------------------------------

directlyin(katarina,olga).
directlyin(olga,natasha).
directlyin(natasha,irina).


in(X,Y) :- directlyin(X,Y). % "if Y is included in X , Y is in X" 
in(X,Y) :- directlyin(X,Z) ,  in(Z,Y).


%------------------------------------------------------
% I hate trains...
%------------------------------------------------------

directTrain(saarbruecken,dudweiler). 
directTrain(forbach,saarbruecken). 
directTrain(freyming,forbach). 
directTrain(stAvold,freyming). 
directTrain(fahlquemont,stAvold). 
directTrain(metz,fahlquemont). 
directTrain(nancy,metz).


travelFromTo(X,Y) :- directTrain(X,Y).
travelFromTo(X,Y) :- directTrain(Z,Y) , travelFromTo(X,Z).

%------------------------------------------------------
% I'm bigger than you 
%------------------------------------------------------

greater_than(succ(X),0).
greater_than(succ(X),succ(Y)) :- greater_than(X,Y).


%------------------------------------------------------
% Tree, Tree, Tree 
%------------------------------------------------------


leaf(X).

tree(leaf(X),leaf(Y)) :- leaf(X) , leaf(Y).
tree(tree(X,Y),leaf(W)) :- tree(X,Y) , leaf(W).
tree(leaf(W),tree(X,Y)) :- leaf(W) , tree(X,Y).

swap(leaf(X),leaf(X)) :- leaf(X) . %fact of life: swapping a leaf it's the leaf 
swap(tree(X,Y) , tree(Y1,X1)) :- swap(X,X1), swap(Y,Y1).

%------------------------------------------------------
% A maze
%------------------------------------------------------

connected(1,2). 
connected(3,4). 
connected(5,6). 
connected(7,8). 
connected(9,10). 
connected(12,13). 
connected(13,14). 
connected(15,16). 
connected(17,18). 
connected(19,20). 
connected(4,1). 
connected(6,3). 
connected(4,7). 
connected(6,11). 
connected(14,9). 
connected(11,15). 
connected(16,12). 
connected(14,17). 
connected(16,19).

path(X,Y) :- connected(X,Y).
path(X,Y) :- connected(X,Z),path(Z,Y).

%------------------------------------------------------
% Let's travel
%------------------------------------------------------

byCar(auckland,hamilton). 
byCar(hamilton,raglan). 
byCar(valmont,saarbruecken). 
byCar(valmont,metz). 
 
byTrain(metz,frankfurt). 
byTrain(saarbruecken,frankfurt). 
byTrain(metz,paris). 
byTrain(saarbruecken,paris). 
    
byPlane(frankfurt,bangkok). 
byPlane(frankfurt,singapore). 
byPlane(paris,losAngeles). 
byPlane(bangkok,auckland). 
byPlane(singapore,auckland). 
byPlane(losAngeles,auckland).


travel(X,Y)  :- byCar(X,Y).
travel(X,Y)  :- byTrain(X,Y).
travel(X,Y)  :- byPlane(X,Y).
travel(X,Y)  :- byCar(X,Z) , travel(Z,Y).
travel(X,Y)  :- byTrain(X,Z), travel(Z,Y).
travel(X,Y)  :- byPlane(X,Z) , travel(Z,Y).


traveldir(X,Y,(X,Y))   :- byCar(X,Y)  .
traveldir(X,Y,(X,Y))   :- byTrain(X,Y).
traveldir(X,Y,(X,Y))   :- byPlane(X,Y).
traveldir(X,Y,(X,P))   :- byCar(X,Z)  , traveldir(Z,Y,P).
traveldir(X,Y,(X,P))   :- byTrain(X,Z), traveldir(Z,Y,P).
traveldir(X,Y,(X,P))   :- byPlane(X,Z), traveldir(Z,Y,P).

 
