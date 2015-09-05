/*
http://www.ic.unicamp.br/~meidanis/courses/mc336/2009s2/prolog/problemas/
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse1
http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse6
*/


/*
ES-1
Find the last element of a list.
Example:
?- my_last([a,b,c,d],X).
X = d
*/

my_last(X,[X]).
my_last(X,[_|L]) :- my_last(X,L).


/*
ES-2
Find the last but one element of a list.
Example:
?- lastbut1([a,b,c,d],X).
X = c
*/

last_but_one(X,[X,_]).
last_but_one(X,[_,Y|Ys]) :- last_but_one(X,[Y|Ys]).


/*
ES-3
The first element in the list is number 1.
Example:
?- element_at([a,b,c,d,e],3,X).
X = c
*/

element_at(X,0,[X]).
element_at(X,N,[_|L]) :- N1 is N - 1 , element_at(X,N1,L).  


/*
ES-4
Find the number of elements of a list.
Example:
?- my_length([a,b,c,d,e],X).
X = 5
*/

my_length([],0).
my_length([_|L],X) :- my_length(L,X1), X is X1 + 1.  


/*
ES-5
Reverse a list
Example:
?- my_rev([a,b,c,d,e],X).
X = [e,d,c,b,a]
*/


my_rev([], []).
my_rev([H|T], Rev) :- my_rev(T, RT), append(RT, [H], Rev).

/*
ES-6
Find out whether a list is a palindrome.
Example:
?- pali([a,b,c,d,e],X).
X = False
*/

pali(X) :- my_rev(X,X).


/*
ES-7
Flatten a nested list structure
Example:
?- my_flatten([a, [b, [c, d], e]], X).
X = [a, b, c, d, e]

Hint: Use the predefined predicates is_list/1 and append/3
*/


