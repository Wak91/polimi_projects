--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://learnyouahaskell.com/syntax-in-functions
--http://echo.rsmw.net/n00bfaq.html
--https://github.com/elleFlorio/PL2015/blob/master/eshs3.hs
--https://github.com/elleFlorio/PL2015/blob/master/eshs3_complete.hs


import qualified Data.Map as M
import Control.Applicative
import Control.Monad
import Control.Monad.State

-- Monads
-- class Monad m where
--     return :: a -> m a
--     (>>=) :: m a -> (a -> m b) -> m b
--     (>>) :: m a -> m b -> m b
--     x >> y = x >>= \_ -> y
--     fail :: String -> m a
--     fail msg = error msg

-- Maybe monad

-- instance Monad Maybe where
--     return x = Just x
--     Nothing >>= f = Nothing
--     Just x >>= f  = f x
--     fail _ = Nothing

type Stack = [Int]


--define the pop function
pop :: Stack -> (Int,Stack)
pop [] = ((-1),[])
pop (x:xs) = (x,xs) 


-- Define the push function 
push :: Int -> Stack -> ((),Stack)  
push a xs = (() , a:xs)


-- Define a function that executes the following operations on the stack:
-- pop an element
-- pop another element
-- push 100
-- pop an element
-- pop another element

-- pop and push are doing stateful computation! 

stackManip :: Stack -> (Int, Stack)  
stackManip stack = let  
    (a,newStack1) = pop stack  
    (b ,newStack2) = pop newStack1
    ((),newStack3) = push 100 newStack2
    (c, newStack4) = pop newStack3  
    in pop newStack4



-- newtype State s a = State { runState :: s -> (a,s) }
-- instance Monad (State s) where  
-- return x = State $ \s -> (x,s)  
-- (State h) >>= f = State $ \s -> let (a, newState) = h s  
--                                     (State g) = f a  
--                                 in  g newState

-- get :: m s
-- Return the state from the internals of the monad.

-- put :: s -> m ()
-- Replace the state inside the monad.




