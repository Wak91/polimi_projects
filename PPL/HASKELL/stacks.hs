--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://learnyouahaskell.com/syntax-in-functions
--http://echo.rsmw.net/n00bfaq.html
--https://github.com/elleFlorio/PL2015/blob/master/eshs3.hs
--https://github.com/elleFlorio/PL2015/blob/master/eshs3_complete.hs
--http://brandon.si/code/the-state-monad-a-tutorial-for-the-confused/


import Control.Applicative
import Control.Monad



type Stack = [Maybe Int]   -- create an alias for [Maybe Int]


{-the stuff in the brakets is the record syntax. ( http://learnyouahaskell.com/making-our-own-types-and-typeclasses ) 
  we are automatically defininf a function that given a state, returs 
  a tuple with a value of type a and the state -}
newtype State s a = State {runState :: s -> (a,s)}  -- A  [ State s a ] is a stateful computation that manipulates a state of type s and has a result of type a 


 

instance Monad (State s) where
	return x = State $ \s -> (x,s)
	m >>= f = State $ \s -> let (a,newState) = runState m s
							in 	runState (f a) newState 



-- pop returns a stateful computation that manipulates a Stack and has a result of type Int 
pop :: State Stack (Maybe Int)  
pop = State (\(x:xs) -> (x ,xs))

push :: Maybe Int -> State Stack (Maybe Int)
push a = State (\xs -> (Nothing , a:xs))

--With StackManip and the use of do we are gluing together different stateful computation
--and obtaining a big stateful computation
 
stackManip = do 
			 pop
			 push (Just 7)
			 pop
			 pop
			 pop
			 push (Just 7)
{-
*Main Control.Applicative> runState stackManip [(Just 1) ,(Just 2)]
(Just 13,[Just 17,Just 1,Just 2])
*Main Control.Applicative> 
-}



