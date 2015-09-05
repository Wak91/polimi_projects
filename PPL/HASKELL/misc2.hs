{- 
--http://learnyouahaskell.com/syntax-in-functions
--pure/unpure function 
-}


--do glue together I/O action 
main = do 
    putStrLn "Hello, what's your name?"
    name <- getLine   ---unboxing the I/O action inside the do I/O action ( unpure things in unpure environment ) 
    putStrLn $ "Read this carefully " ++ name  
 
    
-- in this example the function tellFortune doesn't have to know anything about I/O, it's 
-- just a normal String -> String function, it is pure.

main = do 
    putStrLn "Hello, what's your name?"
    name <- getLine   
    putStrLn $ "Read this carefully because it is your future" ++ tellFortune name 


-- This example is WRONG![XXX] 
-- (++) request that both parameters are String, and getLine is a IO String.
-- you have to unbox it inside an I/O operation before use it!

nameTag = "Hello, my name is" ++ getLine 



-- Another stupid thing [XXX] 
-- this code give the getLine I/O action a different name called, 'name'.

name = getLine 


--In this example we can see the use of the let inside a block of I/O operation.
--REMEMBER: use let to bound pure expression to name
--          use <-  to bound result of I/O actions to name
 
main = do 
	   putStrLn "What's your first name?" 
	   firstName <- getLine
	   putStrLn "What's your second name?"
	   lastName <- getLine  --the result of this I/O action is binded to lastName insinde a block of I/O ops.
	   let bigFirstName = map toUpper firstName  --toUpper is a pure function String -> String 
		   bigLastName  = map toUpper lastName   --the result is binded to the name specified
	   putStrLn $ "hey " ++ bigFirstName ++ " " ++ bigLastName ++ ", how are you?" 



-- REMEMBER: 'return' in Haskell (in I/O actions specifically), makes an I/O action from a pure value!!! 
--            Basically it takes a value and wraps it in a box.
main = do 
     line <- getLine  --let's bound the result of the getLine to 'line'
     if null line 
          then return ()  -- I/O action in case of true 
          else do   -- I/O action in case of false ( in a n I/O block if has always I/O action for both branches ) 
               putStrLn $ reverseWords line  -- equivalent to putStrLn ( reverseWords line ) 
               main 
 
-- in reverseWords there is a good use of the function composition
-- in fact, by using it we don't have to write a pattern matching
-- REMEMBER: let dd = (*2) . (1+)
-- 			 dd 6 => 14 
reverseWords :: String -> String 
reverseWords = unwords . map reverse . words   -- words: "hey there man" -> ["hey" , "there" , "man"]
											   -- map reverse ["hey" , "there", "man"] ->  ["yeh" , "ereht", "nam"]
											   -- unwords "yeh ereht nam" 
											   
											   
											 
-- Another example that returns doesn't cause the end in execution.
-- all these returns make I/O actions that don't really do anything except have
-- an encapsulated result and that result is throw away because it isn't bound to a name 
main = do 
	   return () 
	   return "HAAHAH"
	   line <- getLine
	   return "BLAH CLA"
	   return 4
	   putStrLn line 
	   
-- You can see return as OPPOSITE of <- 
main = do 	
	a <- return "hell"
	b <- return "yeah"
	putStrLn $ a ++ " " ++ b
	   
{-	   
--Let's make here an example of the evaluation of a do
  Just 3 >>= (\x -> Just "!" >>= (\y -> Just 3 >> Just $ show x ++ y))
-}

foo = do
	  x <- Just 3
	  y <- Just "!"
	  Just 3
	  Just ( show x ++ y )

