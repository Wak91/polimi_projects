
--http://learnyouahaskell.com/syntax-in-functions

maximum' :: (Ord a) => [a] -> a  
maximum' [] = error "maximum of empty list"  
maximum' [x] = x  
maximum' (x:xs) = max x (maximum' xs)  


calcBmis :: (RealFloat a) => [(a,a)] -> [a]
calcBmis xs = [bmi | (w,h) <- xs , let bmi = w / h ^ 2]   --list comprehension with a definition of a function in the predicate 


-- Dealing with infinite lists
-- let's first of all define our take 

take' :: (Num i, Ord i) => i -> [a] -> [a] 
take' n _
	| n <=0 = []   --since there is no otherwise here, if this guard fails, we will evaluate the next pattern 
take' _ []  = []
take' n (x:xs) = x : take' (n-1) xs

-- Now let's define an infinite list made by only one element 

repeat' :: a -> [a] 
repeat' x = x:repeat' x 

--if we call repeat' alone we have this evaluation:
--  repeat' 3
--  3:repeat' 3
--  3:3:repeat' 3 ...
--but calling take' 5 $ repeat' 3
--  take' 5 repeat' 3
--  3:take'     4     3:repeat' 3
--  3:3:take'   3     3:3:repeat' 3
--  3:3:3:take' 2     3:3:3:repeat' 3
--  ... 

quicksort :: (Ord a) => [a] -> [a] 
quicksort [] = []
quicksort (x:xs) =
	let smallerSorted = quicksort [ a |  a <- xs , a <= x]
	    biggerSorted  = quicksort [ a |  a <- xs , a > x ] 
	 in smallerSorted ++ [x] ++ biggerSorted 


newtype State st a = State (st -> (st,a))


{-
the type shape can be a circle or a rectangle.
circle/rectangle are the value constractor that receive parameters. 
They are fucntions that receives the params and return a value of type Shape!

analogy:

data Int = -2147483648 | -2147483647 | ... | -1 | 0 | 1 | 2 | ... | 2147483648  ( this is a nullary type ) 

-}

---we can define a shape like this
data Shape = Circle Float Float Float | Rectangle Float Float Float Float deriving (Show)

--or 

data Point = Point Float Float deriving (Show)  -- here the type constructor has the same name of the value constructor ( != namespaces ) 
data Shape = Circle Point Float | Rectangle Point Point deriving (Show)

--create a data type with record syntax 
--Haskell create automatically the function firstName,lastName,age,height,phoneNumber,flavor.
data Person = Person { firstName :: String  
                     , lastName :: String  
                     , age :: Int  
                     , height :: Float  
                     , phoneNumber :: String  
                     , flavor :: String  
                     } deriving (Show)   



instance Monad (State state) where
  return x = State (\st -> (st,x))
  State f >>= g = State (\oldstate -> 
						   let (newstate,val) = f oldstate
						       State f'       = g val
						   in f' newstate)
						   
						   
esm :: State Int Int 
esm = do 
	  x <- return 5 
	  return (x+1)


getState :: State state state 
getState = State (\state -> (state, state))


esm' :: State Int Int
esm' = do 
	   x <- getState
	   return (x+1)
	   
	   
--- example of the evaluation of the previous State monad
--- based on the following definition 
{-

return 5 >>= (\x -> return (x+1)) 

State (\st -> (st,5)) >>= (\x -> return (x+1))
      |_______f_____|     |________g______|


State (\oldstate -> let (newstate,val) = f oldstate 
						 state f' = g val
					in f' newstate )


let State f = esm   ( in f we have the previous State monad ) 
f 333

(\oldstate -> let (newstate,val) = (\st -> (st,5)) oldstate 
						 state f' = (\x -> return (x+1)) val
					in f' newstate ) 333 
					
					
( 333 -> let (newstate,val) = (\st -> (st,5)) 333
					state f' = (\x -> ret (x+1)) val
		  in f' newstate ) 

( 333 -> let (newstate,val) = (333,5)
		            state f' = State (\st -> (st,6))
		  in f' newstate)

( 333 -> let (newstate,val) = (333,5)
		            state f' = State (\st -> (st,6))
		  in State (\st -> (st,6)) 333)  ===> (333,6)

-}






