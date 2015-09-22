% acceptance
config(State, []) :- final(State).

% move
config(State, [C|String]) :- delta(State, C, NewState), config(NewState, String).

run(Input) :- initial(Q), config(Q, Input).

a(1).
a(2).												   

test(X) :- a(X) ,  X = 2.

betweenAB([a|Y]) :- betweenABa(Y),!.

betweenABa([a|R]) :- betweenABa(R) ,!.
betweenABa(J) :- betweenAB1(J), !.

betweenAB1([b|Z]) :- betweenAB2(Z),!.
betweenAB1([T|Z]) :- not(T=b) , not(T=a) , betweenAB1(Z).

betweenAB2([]).
betweenAB2([b|R]) :- betweenAB2(R),!.




deep(L) :- betweenAB(L) , deepaux(L).

deepaux([]).
deepaux([X|Y]) :- is_list(X) , betweenAB(X) , deepaux(Y).
deepaux([X|Y]) :- not(is_list(X)) , deepaux(Y).


% this eats only 'aaaaa...'
as([]) :- !.
as([a|Xs]) :- !, as(Xs).

% this eats only 'bbbbb....'
bs([]) :- !.
bs([b|Xs]) :- !, bs(Xs).


b3tweenAB(X) :- append(A,Y,X), append(C,B,Y), as(A), bs(B), \+ member(a,C), \+ member(b,C).



atomOrAB([X]) :- !, (atom(X) ; deepBetweenAB(X)).
atomOrAB([X|Xs]) :- (atom(X) ; deepBetweenAB(X)), !, atomOrAB(Xs).

deepBetweenAB(X) :- b3tweenAB(X), !, atomOrAB(X).


triparting([] , P1, P2, [], [],[]).
triparting([X|Y], P1, P2 , [X|L1T] , L2 , L3 ) :- X < P1 , X < P2 , triparting( Y , P1 , P2 , L1T , L2 , L3 ),!.
triparting([X|Y], P1, P2 , L1 , [X|L2T] , L3 ) :- X >= P1 , X =< P2 , triparting( Y , P1 , P2 , L1 , L2T , L3 ),!.
triparting([X|Y], P1, P2 , L1 , L2, [X|L3T]  ) :- triparting( Y , P1 , P2 , L1 , L2 , L3T ),!.




duplicates(Tree,X) :- treeToList(Tree,Y), onlydup(Y,X).
treeToList(Atom, [Atom]) :- atomic(Atom), !.
treeToList(Tree, [X|Xs]) :- Tree =.. [X|Args], maplist(treeToList,Args,Ys), flatten(Ys,Xs).

% defined also in Exercise 3 of the exam of 2013.02.13

onlydup([],[]).
onlydup([Y|Xs],[Y|Ys]) :- member(Y,Xs), onlydup(Xs,Ys).
onlydup([X|Xs],Ys) :- \+ member(X,Xs), onlydup(Xs,Ys).

/*My solution--------------------------------------------------------------------------------------------*/

termToList(T,L) :- T =.. X  ,  termToList1(X,Ltf) , flatten(Ltf,L).

termToList1([],[]).
termToList1([X|Y] , [L1|L2]) :- atomic(X) , L1=X , termToList1(Y,L2) .
termToList1([X|Y] , [L1|L2]) :- not(atomic(X)) , X=.. X1 , termToList1(X1,L3) , L1 = L3, termToList1(Y,L2).


dupli([],[]).
dupli([Y|Ys], [Y|Ds]) :- member(Y,Ys), dupli(Ys,Ds).
dupli([X|Xs], L) :- not(member(X,Xs)) , dupli(Xs,L).

duplicates(T,L) :- termToList(T,L1), dupli(L1,L).



/*My solution--------------------------------------------------------------------------------------------*/


multipleapply(P,0,X,R) :- R , !.
multipleapply(P,N,X,R) :- call(P,R,R1) , N1 is N-1 , multipleapply(P,N1,X,R1).


remove(X,[X|Y],Y).
remove(X,[Y|Z],[Y|L2]) :- remove(X,Z,L2).



demux( Ul,Acc,V ) :- demux1(Ul,0,[],Acc,V).

demux1([],A,L,A,L).
demux1([end|Y] , A , L , A , L ) :- !.
demux1([X|Y] , A , L ,Acc,V) :- integer(X) , A1 is A+X , demux1(Y,A1,L,Acc,V).
demux1([X|Y] , A , L ,Acc,V) :- not(integer(X)) , append(L,[X],L1) , demux1(Y,A,L1,Acc,V).



shuffle([],[],[]).
shuffle([X|Y] , A , [X|L2] ) :- shuffle(Y,A,L2).
shuffle(N, [X|Y] , [X|L2] ) :- shuffle(N,Y,L2).




prod-list(L,R) :- prod-list1(L,1,R).
prod-list1([],Acc,Acc).
prod-list1([X|Y],Acc,R) :- Acc1 is Acc*X , prod-list1(Y,Acc1,R), !.


freeM([],A).
freeM([X|Y],A) :- member(X,A) , freeM(Y,A).


%"calculate the number of different colors in every sub-tree"

col_tree(T) :- col_tree1(T,U).
col_tree1([X,Y,Z] , U ) :- atom(Y) , atom(Z) , union([Y],[Z],U) , length(U,L) , X=L , !.
col_tree1([X,Y,Z] , U) :- atom(Y) , not(atom(Z)) , col_tree1(Z,U1) , union([Y],U1,U),length(U,L),X=L ,!.
col_tree1([X,Y,Z] , U) :- atom(Z) , not(atom(Y)) , col_tree1(Y,U1) , union([Z],U1,U),length(U,L),X=L ,!.
col_tree1([X,Y,Z] , U) :- not(atom(Y)) , not(atom(Z)) ,col_tree1(Y,U1) , col_tree1(Z,U2), union(U2,U1,U),length(U,L),X=L , !.

%"conver the * in + and + in *"
% ?- mixUp(3*2+4,R).
% ?- R = 20.

mixUp(V,V) :- atomic(V), !.
mixUp(A1+A2,R) :- !, mixUp(A1,R1), mixUp(A2,R2), R is R1*R2.
mixUp(A1*A2,R) :- !, mixUp(A1,R1), mixUp(A2,R2), R is R1+R2.




prefix(S,P) :- append(P,_,S) , P\=[].
suffix(S,Su) :- append(_,Su,S) , Su\=[].

infix(S,Sub) :- suffix(S,Suff) , prefix(Suff,Sub).

overlap(S1,S2) :- infix(S1,S2).
overlap(S1,S2) :- infix(S2,S1).
overlap(S1,S2) :- suffix(S1,P) , prefix(S2,P),!.

prefix1(L,P) :- append(P,_,L).
suffix1(L,S) :- append(_,S,L).
subList(L,SubL) :- suffix1(L,Suff) , prefix1(Suff,SubL) , SubL \= [].

subsetsum(L,R) :- subList(L,SubL) , checkSum(SubL,R,0), ! .

checkSum([],R,R).
checkSum([X|Y] , R,A):- A1 is A+X , checkSum(Y,R,A1).


lile([]).
lile(L) :- length(L,L1) , member(L1,L).


lileg(L) :- lile(L) , lileg1(L).

lileg1([]).
lileg1([X|Y]) :- is_list(X) , lileg(X) , ! , lileg1(Y).
lileg1([X|Y]) :- not(is_list(X)) , ! , lileg1(Y).



takeout(X,[X|R],R).
takeout(X,[F|R],[F|S]):-takeout(X,R,S).

perm([],[]).
perm([X|Y],P) :- perm(Y,W) ,  takeout(X,P,W) .




flatten(List, FlatList) :- flatten(List, [], FlatList), ! .


flatten([], Tl, Tl) :- !.
flatten([Hd|Tl], Tail, List) :- !, flatten(Hd, FlatHeadTail, List), flatten(Tl, Tail, FlatHeadTail).
flatten(NonList, Tl, [NonList|Tl]). 



last_but_one([X,_] , X).
last_but_one([X|Y],L) :- last_but_one(Y,L), !.


nestedlastb1(L,LB1) :- last_but_one(L,LBT) , not(is_list(LBT)) , LB1=LBT.
nestedlastb1(L,LB1) :- last_but_one(L,LBT) , is_list(LBT) , nestedlastb1(LBT,LB1).


mult(X,R) :- R is X*2.
revmap(P,L,LR,LF) :- reverse(L,LR) , maplist(P,LR,LF).


revmaplinear(P,L,R) :- revmaplinearaux(P,L,[],R).

revmaplinearaux(P,[],LT,LT).
revmaplinearaux(P,[L1|L2],LT,R) :- call(P,L1,Res) , append([Res],LT,LT1) , revmaplinearaux(P,L2,LT1,R) , !.


countpreds(X,T,R) :- T=.. L , countpredsaux(X,L,0,R).
countpredsaux(X,[],Acc,Acc).
countpredsaux(X,[L1|L2],Acc,T) :- atomic(L1) , L1=X ,Acc1 is Acc+1 , countpredsaux(X,L2,Acc1,T) , !.
countpredsaux(X,[L1|L2],Acc,T) :- atomic(L1) , X \= L1 , countpredsaux(X,L2,Acc,T) , !.
countpredsaux(X,[L1|L2],Acc,T) :- not(atomic(L1)) , L1=..LF , countpredsaux(X,LF,0,Tt) , Acc1 is Acc+Tt , countpredsaux(X,L2,Acc1,T), !.

% unique removes duplicates element from a list
unique(L,R) :- unique1(L,[],R).  

unique1([],Acc,Acc).
unique1([X|Y],T,R) :- not(member(X,T)) , unique1(Y,[X|T],R),!.
unique1([X|Y],T,R) :- member(X,T) , unique1(Y,T,R),!.


% duplicate keeps only the elements that are duplicate!
duplicate(L,R) :- duplicate1(L,[],R).  

duplicate1([], Acc , Acc).
duplicate1([X|Y] , Acc , R ) :- member(X,Y) , not(member(X,Acc)) , duplicate1(Y , [X|Acc] , R),!.
duplicate1([X|Y] , Acc , R ) :- member(X,Y) , member(X,Acc) , duplicate1(Y,Acc,R),!.
duplicate1([X|Y] , Acc , R ) :- not(member(X,Y)) ,duplicate1(Y,Acc,R),!.





% TO FINISH!!!

% this predicate continuosly produce random indexes 
myrandom_member(M1,M2,M3,L) :- length(L,N) , N1 is N-1 , random_between(0,N1,R1) , nth0(R1, L , M1) ,  random_between(0,N1,R2) , R1 =\= R2 , nth0(R2, L, M2), random_between(0,N1,R3) , R3 =\= R2 , R3 =\= R1 , nth0(R3, L, M3).
myrandom_member(M1,M2,M3,L) :- myrandom_member(M1,M2,M3,L).  

subset([L1,L2,L3] , [P1,P2,P3]) :- P1 = [L1,L2] , P2 = [L2,L3] , P3 = [L1,L3].

rtris(L,[T1,T2,T3],LR2) :- myrandom_member(T1,T2,T3,L) , remove(T1,L,LR) ,  remove(T2,LR,LR1) ,  remove(T3,LR1,LR2). 

bigUnion([T1,T2,T3] , B ) :- subset(T1,S1) , subset(T2,S2) , subset(T3,S3) , append(S1,S2,St) , append(St,S3,B).


include([X|Y] , Bu ) :- member(X,Bu).
include([X|Y] , Bu ) :- not(member(X,Bu)) , reverse(X,XR ) , not(member(XR,Bu)), include(Y,Bu).


weekorganized(L,[T11,T12,T13] , [T21,T22,T23] , [T31,T32,T33] ) :- rtris(L,T11,LR) , rtris(LR,T12,LR1) , rtris(LR1,T13,LR2) ,bigUnion([T11,T12,T13],Bu) , print(Bu), 
																   rtris(L,T21,LR21) ,  subset(T21,S21) ,not(include(S21,Bu)) , rtris(LR21,T22,LR22) , subset(T22,S22), not(include(S22,Bu))  , rtris(LR22,T23,LR23) ,
																   subset(T23,S23) , not(include(S23,Bu)) , bigUnion([T21,T22,T23] , Bu2) , append(Bu,Bu2,Bu3) , rtris(L,T31,LR31) , subset(T31,S31)  ,not(include(S31,Bu3)) ,
																   rtris(LR31,T32,LR32) , subset(T32,S32) ,print(S32) , not(include(S32,Bu3))  , rtris(LR32,T33,LR33),subset(T33,S33)  , not(include(S33,Bu3)).
																   


