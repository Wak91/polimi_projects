--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--QUESTIONS 1->10

{-
ES-1  Find the last element of a list.

Prelude> myLast [1,2,3,4]
4
Prelude> myLast ['x','y','z']
'z'
-}

myLast :: [a] -> a

myLast [] = error "No end for empty list"
myLast [x] = x 
myLast (_:xs) = myLast xs


{-
ES-2 Find the last but one element of a list.

Prelude> myButLast [1,2,3,4]
3
Prelude> myButLast ['a'..'z']
'y'
-}

myButLast :: [a] -> a 

myButLast []  = error "This is an empty list"
myButLast [_] = error "We need at least two elements"
myButLast [x,_] = x 
myButLast (_:xs) = myButLast xs

{-
ES-3 Find the K'th element of a list. The first element in the list is number 1.
 
Prelude> elementAt [1,2,3] 2
2
Prelude> elementAt "haskell" 5
'e'
-}


elementAt :: [a] -> Int -> a

elementAt [] _   = error "Error"
elementAt (x:xs) 0 = x
elementAt (x:xs) i = if i < 0
						then error "Give me a positive index"
						else elementAt xs (i-1)


{-
ES-4
Find the number of elements of a list.

Prelude> myLength [123, 456, 789]
3
Prelude> myLength "Hello, world!"
13
-}
myLength :: [a] -> Int 

myLength [] = error "This is an empty list"
myLength (x:xs) = sum [1 | _ <- (x:xs)] 

{-
ES-5
Reverse a list.

Prelude> myReverse "A man, a plan, a canal, panama!"
"!amanap ,lanac a ,nalp a ,nam A"

Prelude> myReverse [1,2,3,4]
[4,3,2,1]
-}

myReverse :: [a] -> [a] 
myReverse [] = []
myReverse (x:xs) = myReverse xs ++ [x]

{-
ES-6
Find out whether a list is a palindrome. 
A palindrome can be read forward or backward; e.g. (x a m a x).

Main> isPalindrome [1,2,3]
False
Main> isPalindrome "madamimadam"
True

-}

isPalindrome :: (Eq a) => [a] -> Bool  -- remember to enforce the fact that the type must support the Equality! 

isPalindrome [] = error "This is an empty list"
isPalindrome (x:xs) = let reversed = myReverse (x:xs)
					  in if reversed == (x:xs)
						 then True
						 else False



{-
ES-7
Flatten a nested list structure.

Transform a list, possibly holding lists as elements into a `flat' list 
by replacing each list with its elements (recursively).

Example:

We have to define a new data type, because lists in Haskell are homogeneous.

data NestedList a = Elem a | List [NestedList a]

Main> flatten (Elem 5)
[5]

Main> flatten (List [Elem 1, List [Elem 2, List [Elem 3, Elem 4], Elem 5]])
[1,2,3,4,5]

Main> flatten (List [])
[]

-}


{-

example of building a nested list 

List [NestedList a]  --- this is a list of NestedList!
List [Elem a , Elem a , List [NestedList a] , Elem a ]
List [Elem a , Elem a , List [ Elem a ] , Elem a] 


-}
data NestedList a = Elem a | List [NestedList a] 

flatten :: NestedList a -> [a] 

flatten (Elem f) = [f]
flatten (List (x:xs)) = flatten x ++ flatten ( List xs )
flatten (List []) = [] 


{-
ES-8 Eliminate consecutive duplicates of list elements.

If a list contains repeated elements they should be replaced with a single copy 
of the element. The order of the elements should not be changed.

Example:

> compress "aaaabccaadeeee"
"abcade"

-}

compress :: (Eq a) => [a] -> [a]

compress [] = []
compress [x] = [x]
compress (x:xs) = if x == head xs
				  then compress xs
				  else x : compress xs



{-
ES-9  Pack consecutive duplicates of list elements into sublists. 
If a list contains repeated elements they should be placed in 
separate sublists.

Main> pack ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 
             'a', 'd', 'e', 'e', 'e', 'e']

["aaaa","b","cc","aa","d","eeee"]
-}

pack :: (Eq a) => [a] -> [[a]]

pack [] = []
pack (x:xs) = let (first,rest) = span (==x) xs
			  in (x:first) : pack rest 



pack' :: (Eq a) => [a] -> [[a]]
pack' [] = []
pack' (x:xs) = (x : takeWhile (==x) xs) : pack' (dropWhile (==x) xs)


{-
ES-10  Run-length encoding of a list. Use the result of problem P09 to implement 
the so-called run-length encoding data compression method. 
Consecutive duplicates of elements are encoded as lists (N E) 
where N is the number of duplicates of the element E.

encode "aaaabccaadeeee"
[(4,'a'),(1,'b'),(2,'c'),(2,'a'),(1,'d'),(4,'e')]

-}

encode ::  (Eq a) => [a] -> [[(Int,a)]]
encode (x:xs) = let list = pack (x:xs)
				in [ [(length l , head l)] | l <- list] 


