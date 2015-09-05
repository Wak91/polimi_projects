--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://echo.rsmw.net/n00bfaq.html


--QUESTIONS 21->28

{-

ES-21 Insert an element at a given position into a list.

Example:

insertAt 'X' "abcd" 2
"aXbcd"

-}

insertAt :: a -> [a] -> Int -> [a]

insertAt _ [] _ = []
insertAt w l i = let t = splitAt (i-1) l 
				 in (fst t) ++ [w] ++ (snd t)


{-

ES-22 Create a list containing all integers within a given range.

Example:

range 4 9
[4,5,6,7,8,9]

-}

range :: Int -> Int -> [Int]

range s e = [s .. e]


{-

ES-23 Extract a given number of randomly selected elements from a list.

Example:

"abcdefgh" 3 >>= putStrLn
eda
-}









