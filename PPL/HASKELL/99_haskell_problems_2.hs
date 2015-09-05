--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://echo.rsmw.net/n00bfaq.html

--QUESTIONS 11->20
{-

ES-11 Modified run-length encoding. Modify the result of problem 10 in such a way that if 
an element has no duplicates it is simply copied into the result list. Only elements with 
duplicates are transferred as (N E) lists.

Example:

encodeModified "aaaabccaadeeee"
[Multiple 4 'a',Single 'b',Multiple 2 'c',
 Multiple 2 'a',Single 'd',Multiple 4 'e']

-}


pack :: (Eq a) => [a] -> [[a]]

pack [] = []
pack (x:xs) = let (first,rest) = span (==x) xs
			  in (x:first) : pack rest 

data Arity a = Multiple Int a | Single a deriving (Show)

encodedModified :: (Eq a) => [a] -> [Arity a]
encodedModified (x:xs) = let list = pack (x:xs)
						  in [ if length l == 1  then Single (head l) else Multiple (length l) (head l) | l <- list]



{-

ES-12 Decode a run-length encoded list.

Given a run-length code list generated as specified in problem 11. 
Construct its uncompressed version.

Example:

decodeModified 
       [Multiple 4 'a',Single 'b',Multiple 2 'c',
        Multiple 2 'a',Single 'd',Multiple 4 'e']
        
"aaaabccaadeeee"

This is a good example of a partial definition of a function. 
Actually the user input ( the list ) will be putted after decodeHelper
and thanks to the pattern matching inside the where we can distinguish if
the current element that concatMap is analyzing is a Single or a Multiple
and define the decodeHelper function based on this information!
-}

decodeModified :: [Arity a] -> [a]
decodeModified = concatMap decodeHelper
	where
		decodeHelper (Single x) = [x]
		decodeHelper (Multiple n x) = replicate n x


{-

ES-13 Run-length encoding of a list (direct solution).

Implement the so-called run-length encoding data compression method directly. 
I.e. don't explicitly create the sublists containing the duplicates, 
as in problem 9, but only count them. As in problem P11, simplify the result 
list by replacing the singleton lists (1 X) by X.


encodeDirect "aaaabccaadeeee"
[Multiple 4 'a',Single 'b',Multiple 2 'c',
 Multiple 2 'a',Single 'd',Multiple 4 'e']

-}

encodeDirect :: (Eq a) => [a] -> [Arity a]
encodeDirect [] = []
encodeDirect (x:xs) = let y = takeWhile (==x) xs
					  in 
						if length y == 0
							then [Single x] ++ encodeDirect (dropWhile (==x) xs)
							else [Multiple (1 + (length y)) x] ++ encodeDirect (dropWhile (==x) xs)


{-
ES-14 Duplicate the elements of a list.

dupli [1, 2, 3]
[1,1,2,2,3,3]
-}

dupli :: [a] -> [a] 

dupli [] = []
dupli (x:xs) = x:x:dupli xs


dupli' list = concat [[x,x] | x <- list]


{-
ES-15 Replicate the elements of a list a given number of times.

repli "abc" 3
"aaabbbccc"
-}

repli :: [a] -> Int -> [a] 

repli [] _ = []
repli (x:xs) n = replicate n x ++ repli xs n

{-
ES-16  Drop every N'th element from a list.

dropEvery "abcdefghik" 3
"abdeghk"
-}


dropEvery :: [a]->Int->[a]

dropEvery [] _ = []
dropEvery (x:xs) n = dropEvery' (x:xs) n 1 where
	dropEvery' (x:xs) n i = if n==i 
							then dropEvery' xs n 1
							else x:dropEvery' xs n (i+1)
	dropEvery' [] _ _ = []


{-
ES-17 Split a list into two parts; the length of the first part is given.

split "abcdefghik" 3
("abc", "defghik")
-}


split :: [a]->Int->[[a]]

split [] _ = [[]]
split (x:xs) n = split' (x:xs) [] n 0 where
	split' (x:xs) l n i = if n==i
						then [l] ++ [x:xs]
						else split' xs (l++[x]) n (i+1)  


{-
ES-18 Extract a slice from a list.

Given two indices, i and k, the slice is the list containing the elements 
between the i'th and k'th element of the original list (both limits included). 
Start counting the elements with 1.

slice ['a','b','c','d','e','f','g','h','i','k'] 3 7
"cdefg"
-}

slice :: [a] -> Int -> Int -> [a]

slice [] _ _ = []
slice list s e = take (e-s+1) $ drop (s-1) list


{-
ES-19  Rotate a list N places to the left.

Hint: Use the predefined functions length and (++).

Example

*Main> rotate ['a','b','c','d','e','f','g','h'] 3
"defghabc"
 
*Main> rotate ['a','b','c','d','e','f','g','h'] (-2)
"ghabcdef"

-}


fixrotation :: Int -> Int -> Int 
fixrotation r l 
			| r < 0  = fixrotation (l+r) l
		    | r >= l = fixrotation (l-r) l
		    | otherwise = r 

rotate :: [a] -> Int -> [a]

rotate [] _ = []
rotate list r = let rot = fixrotation r (length list)
				  in rotate' list rot 1 where
					 rotate' (x:xs) rot i = if i == rot 
											  then xs ++ [x]
											  else rotate' (xs ++ [x]) rot (i+1)
											  						  
---OR more elegant

rotate'' :: [a] -> Int -> [a] 

rotate'' [] _  = []
rotate'' l r = take len (drop (r  `mod` len )  (cycle l))					  
		where len = length l
											   



{-
ES-20  Remove the K'th element from a list.


Example:

*Main> removeAt 2 "abcd"
('b',"acd")

-}


removeAt :: Int -> [a] -> (Maybe a , [a])

removeAt _ [] = (Nothing,[])
removeAt n l = let t = splitAt (n-1) l  
			   in ( Just (head (snd t))  , ( fst t  ++ tail (snd t)))

