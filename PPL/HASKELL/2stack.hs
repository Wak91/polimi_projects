--https://wiki.haskell.org/99_questions
--http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
--https://wiki.haskell.org/Haskell/Lazy_evaluation
--https://www.haskell.org/hoogle/?hoogle=flip
--http://learnyouahaskell.com/syntax-in-functions
--http://echo.rsmw.net/n00bfaq.html
--https://github.com/elleFlorio/PL2015/blob/master/eshs3.hs
--https://github.com/elleFlorio/PL2015/blob/master/eshs3_complete.hs
--http://brandon.si/code/the-state-monad-a-tutorial-for-the-confused/
--See notes for a simple expansion of these rules

import Control.Applicative
import Control.Monad



newtype StateMaybe s a = StateMaybe {runState :: s -> Maybe (a,s)}

instance Monad (StateMaybe s) where
	return x = StateMaybe $ \s -> (Just (x,s))
	m  >>= f = StateMaybe $ \s -> let computation = runState m s   -- in computation we have Nothing or a Just (value,newState)
							      in  computation >>= (\(value,newState)-> runState (f value) newState)  --here we are composing the Maybe monads 


type DoubleStack = ([Int],[Int])


push :: Int -> StateMaybe DoubleStack ()
push a = StateMaybe (\pair -> Just ((), (a:(fst pair), snd(pair))))


move :: StateMaybe DoubleStack ()
move = StateMaybe (\pair -> if (length (fst pair)) == 0 then Nothing else Just ( () , ( (tail (fst pair)) , (head (fst pair)):(snd pair)))) 


pop :: StateMaybe DoubleStack Int
pop = StateMaybe (\pair -> if (length (snd pair)) == 0 then Nothing else Just  ( head (snd pair) , ( (fst pair) , tail (snd pair)))) 



stackManip = do
			 push 7
			 move
			 move
			 pop
			 pop



stackManip'' = do
			   push 7
			   push 19
			   pop
			   move
			   move
			   pop
			   push 1

