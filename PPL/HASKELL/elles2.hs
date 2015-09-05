--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://echo.rsmw.net/n00bfaq.html
--https://github.com/elleFlorio/PL2015/blob/master/eshs2.hs
--https://github.com/elleFlorio/PL2015/blob/master/eshs2_complete.hs

{-
-- boolean guards
-- Write the myRange function using boolean guards
-}
myRange :: Int->Int-> [Int]

myRange a b 
		| a > b = error "the first number must be lower than second" 
		| a == b = [b]
		| otherwise =  a:myRange (a+1) b

{-
-- Define the myRange function using only one input parameter
Usage: take 3 $ myRange' 4
-}

myRange' :: Int -> [Int]
myRange' a = a : myRange' (a+1)


{- 
   case expression
-- Define a function that describes a list (it says if the list is empty, 
-- it's a singleton or a longer list)
-}
describeList :: [a] -> String
describeList [] = "that's an empty list"
describeList [a] =  "List with a single element"
describeList (a:b:[]) =  "List with two elements"
describeList (x:y:xs) =  "Long list!" 


{-
-- Define myTakeWhile function that receive as input a condition and a list
-- and copies in a new list the elements of the first one until the 
-- condition became false
-}

myTakeWhile :: (a -> Bool) -> [a] -> [a]

myTakeWhile _ [] = []
myTakeWhile f (x:xs) = if f x == True 
					   then x:myTakeWhile f xs
					   else []


{-
-- Define myFilter function that receives as input a condition and a list
-- and return a new list containing only the elements of the input list
-- that satisfy the condition
-}

myFilter :: (a -> Bool ) -> [a] -> [a] 

myFilter _ [] = []
myFilter f (x:xs) = if f x == True
					then x:myFilter f xs
					else myFilter f xs


{-

-- Folds
-- Define myReverse function that reverse the elements of a list
   myReverse :: [a] -> [a]
	
   USE FOLD!
-}

myReverse :: [a] -> [a]

myReverse [] = []
myReverse l = foldl (\acc x -> x:acc) [] l


{-
-- Define mySum function that sums the numbers in a list
-}

mySum :: (Num a)=>[a] -> a
mySum [] = 0
mySum (x:xs) = x + mySum xs


{-
or more elegant with foldl and currying
-}

mySum' :: (Num a) => [a] -> a 
mySum' = foldl (+) 0 


{-
-- Define myFilter2 function using "foldr"
-}

myFilter2 :: (a -> Bool) -> [a] -> [a]

myFilter2 _ [] = []
myFilter2 f l = foldr (\x acc -> if f x == True then x:acc else acc) [] l


{-
-- list comprehension / let
-- Define a function that creates righth triangles ( triangoli rettangoli ) 
-}

rightTriangles :: [(Integer, Integer, Integer)]

rightTriangles = [(x,y,z) | z <- [1,2..] , y <- [1..z] , x <- [1..y] , x^2+y^2 == z^2]

{-
-- Define a function that orders a list using the quicksort algorithm
-- https://www.youtube.com/watch?v=ywWBy6J5gz8
-- Quick-sort with Hungarian (Küküllőmenti legényes) folk dance [ xD ] 
-- THINK WHAT IS AN ORDINATE LIST, NOT HOW TO CONSTRUCT IT.
-}

quicksort :: (Ord a) => [a] -> [a]

quicksort [] = []
quicksort (x:xs) = 
	let smallerSorted = quicksort [ a | a <- xs , a <= x];
		biggerSorted  = quicksort [ a | a <- xs , a > x ]
	in smallerSorted ++ [x] ++ biggerSorted

{-
-- Define a TrafficLight that implements Eq and Show typeclasses
-}


data TrafficLight = Red | Yellow | Green

instance Eq TrafficLight where
	Red == Red = True
	Yellow == Yellow = True 
	Green == Green = True 
	_ == _  = False 


instance Show TrafficLight where
	show Red = "Red light"
	show Yellow = "Yellow light"
	show Green = "Green light" 



{-
Binary tree
-}
data Tree a = EmptyTree | Node a (Tree a) (Tree a) deriving (Eq,Show,Read) 


{-
-- Define a function that creates a singleton tree (one node with 2 empty leaf)
-}

singleton :: a -> Tree a
singleton a = Node a EmptyTree EmptyTree

{-
-- Define a function to insert an element into a tree
-}

treeInsert :: (Ord a) => a -> Tree a -> Tree a

treeInsert x EmptyTree = singleton x
treeInsert x (Node a left right )
		   | x <= a = Node a (treeInsert x left ) right 
		   | x > a = Node a left (treeInsert x right)

{-
-- Define a function to check if a tree contains an element
-}

treeElem :: (Ord a) => a -> Tree a -> Bool

treeElem x EmptyTree = False
treeElem x (Node a left right)
		 | x == a  = True
		 | x <= a  = treeElem x left 
		 | otherwise = treeElem x right 


{-
--  Define a function that sum the elements of a tree composed by numbers

treeSum (Node 8 (Node 7 EmptyTree EmptyTree) (Node 9 EmptyTree EmptyTree))
8 + treeSum (Node 7 EmptyTree EmptyTree) + treeSum (Node 9 EmptyTree EmptyTree)
8 + 7 + treeSum EmptyTree + treeSum EmptyTree + 9 + treeSum EmptyTree + treeSum EmptyTree
8 + 7 + 0 + 0 + 9 + 0 + 0 = 24 

-}

treeSum :: Num a => Tree a -> a

treeSum EmptyTree = 0
treeSum (Node a left right) = a + treeSum left + treeSum right  


{- 
-- Define a function that returns a list containing all the values in a tree
-}

treeValues :: Tree a -> [a]

treeValues EmptyTree = []
treeValues (Node a left right) = [a] ++ treeValues left ++ treeValues right 

{-
-- Define the map function on tree
-}

treeMap :: (a -> b) -> Tree a -> Tree b

treeMap f EmptyTree = EmptyTree
treeMap f (Node a left right ) = Node (f a) (treeMap f left) (treeMap f right) 


{-
-- Define a filter function on tree
-}

treeFilter :: (a-> Bool ) -> Tree a -> [a]

treeFilter f EmptyTree = [] 
treeFilter f (Node a left right)  = if f a == True 
									then [a] ++ (treeFilter f left) ++ (treeFilter f right) 
									else (treeFilter f left) ++ (treeFilter f right)


{-
-- Define foldl on tree
-- in order left tree walk
   1- all the left nodes
   2- root
   3- all the right nodes 
-}

treeFoldl :: (a -> b -> a) -> a -> Tree b -> a
treeFoldl f acc EmptyTree = acc 
treeFoldl f acc (Node a left right) = treeFoldl f ( f (treeFoldl f acc left) a ) right 


{-
-- Define foldr on tree
-- in order right tree walk
   1- all the right nodes
   2- root
   3- all the left nodes 
-}

treeFoldr :: (b -> a -> a) -> a -> Tree b -> a
treeFoldr f acc EmptyTree = acc
treeFoldr f acc (Node b left right) = treeFoldr f (f b (treeFoldr f acc right)) left


{-
-- Define the max of the tree
-}

treeMax :: (Num a) => Tree a -> a 
treeMax EmptyTree = (-1)
treeMax (Node a _ EmptyTree) = a 
treeMax (Node a left right ) = treeMax right



{-
-- Define the minimum of the tree
-}

treeMin :: (Num a) => Tree a -> a 
treeMin EmptyTree = (-1)
treeMin (Node a _ EmptyTree) = a 
treeMin (Node a left right ) = treeMin left




