import Data.Array
import Data.Char
import Data.List (sort)

-- duofold apply two function f and g in this way: g(f(g(f(e1,i),e2,e3))),e4) 

duofold :: (Int->Int->Int) -> (Int -> Int -> Int ) -> Int -> [Int] -> Int 

duofold f g i l  = duofold' f g i l
	where 
		duofold' _ _ v [] = v 
		duofold' f g v (x:xs) = duofold' g f (f v x) xs 

--Make [Int] an instance of Num 
----------------------------------------------------------------------------
{-
sum_list [] [] = [] 
sum_list [] (y:ys) = y:sum_list [] ys
sum_list (y:ys) [] = y:sum_list [] ys
sum_list (x:xs) (y:ys) = s:sum_list xs ys
					   where s = x+y

diff_list [] [] = []
diff_list (x:xs) (y:ys) = d:diff_list xs ys
					    where d = x-y
					    
mult_list [] [] = []
mult_list (x:xs) (y:ys) = d:mult_list xs ys
					    where d = x*y
	
abs_list :: (Ord a,Num a) => [a] -> [a] 
abs_list [] = []
abs_list (x:xs) = if x > 0 then x:abs_list xs else x*(-1):abs_list xs 		    

sig_list  :: (Ord a,Num a) => [a] -> [a] 
sig_list [] = []
sig_list (x:xs) = if x > 0 then 1:sig_list xs else (-1):sig_list xs 


instance (Ord a,Num a) => Num [a] where
	x + y = sum_list x y 
	x - y = diff_list x y
	x * y  = mult_list x y
	abs l  = abs_list l
	signum l = sig_list l 
	fromInteger l = [fromInteger l] 

-}
----------------------------------------------------------------------------
--Make [Int] an instance of Num ( A SMARTER SOLUTION ) 
----------------------------------------------------------------------------


instance (Num f) => (Num [f]) where
	a + [] = a
	[] + b = b
	(x:xs) + (y:ys) = (x+y):(xs + ys)
	a - [] = a
	[] - b = -b
	(x:xs) - (y:ys) = (x-y):(xs - ys)
	a * [] = map (\x -> 0) a
	[] * b = map (\x -> 0) b
	(x:xs) * (y:ys) = (x*y):(xs * ys)
	abs = map abs
	signum = map signum
	fromInteger a = [fromInteger a]

----------------------------------------------------------------------------

data TT = E Int | Ly [TT] deriving (Eq)

lile :: TT -> Bool 

lile (E x) = if x/= 1 then False else True
lile (Ly l) = let len = length l
			  in if length (filter (==(E len)) l) /=0 then True else False  -- otherwise  foldr (||) True ( map (== ( E len)) l) 



lileg :: TT -> Bool

lileg ( E 1 ) = True
lileg ( E x ) = False
lileg ( Ly l ) = if (lile (Ly l)) /= True then False else lilegaux l 


lilegaux :: [TT] -> Bool 

lilegaux [] = True 
lilegaux ((E x):xs ) = lilegaux xs
lilegaux ((Ly l):xs) = (lileg (Ly l)) && lilegaux xs
----------------------------------------------------------------------------


split :: [a] -> Int -> ([a],[a])

split l n = ( take n l , drop n l )


-- Use split to define the function 3-factors, which, given a list L, returns all the possible contiguous sublists
-- A, B, C, such that (equal? L (append A B C)). A,B,C, cannot be empty

tfactors :: [a] -> [ ([a],[a],[a]) ]

tfactors l = tfactors1 [] 1 
	      where tfactors1 l1 c
							 | c <= ( (length l) - 2 ) = tfactors2 [] 1 (fst (split l c)) (snd (split l c))
							 | otherwise = l1  
							 where tfactors2 l2 cont prefix suffix 
																  | cont <= ( (length suffix) - 1 ) = tfactors2 (l2 ++ [ ( prefix , fst (split suffix cont) , snd (split suffix cont)) ]) (cont + 1)  prefix suffix 
															      | otherwise = tfactors1 (l1++l2) (c + 1)
		           
		           
		           
smap :: [Int] -> (Int->Int) -> (Int -> Int -> Int ) -> Int -> [Int] 
smap l f op t = smap' l f op t 0 [] 
			  where smap' (x:xs) f op t k my_list 
												| (k >= t) = reverse my_list
												| otherwise = smap' xs f op t ( op k (f x)) [f x] ++ my_list												
												
												

class Blup a where
	fisto :: (a b c) -> Maybe b
	fosto :: (a b c) -> Maybe c

data Blargh a b = Bip a b | Bop a | Bup deriving (Show, Eq)

instance Blup Blargh where
	fisto (Bip a b) = Just a
	fisto (Bop a) = Just a
	fisto Bup = Nothing
	fosto (Bip a b) = Just b
	fosto _ = Nothing

data Blarf a b = La [a] | Lb [b] deriving (Show, Eq)

instance Blup Blarf where
	fisto (La (x:xs)) = Just x
	fisto _ = Nothing
	fosto (Lb (x:xs)) = Just x
	fosto _ = Nothing


data Either a b = LFeft a | Right b  



rep :: Eq(a) => [a] -> [a] 

rep l = rep' l []
	  where 
		   rep' [] my_list = my_list 
		   rep' (x:xs) my_list = if (x `elem` xs) && not (x `elem` my_list) then rep' xs (x:my_list) else rep' xs my_list 

comprep :: (Eq(a),Eq(b)) => ( Int -> Int -> Bool ) -> [a] -> [b] -> Bool 
comprep pred x y = pred ( length ( rep x ) ) ( length ( rep y ) )





multipleapply :: (Int -> Int) -> Int -> Int -> Int 
multipleapply f 0 x = x 
multipleapply f n x = multipleapply f (n-1) (f x)


positionofmax :: (Eq(a) , Ord(a)) => [a] -> Int 
positionofmax l = positionofmax' l (maximum l) 0
			    where positionofmax' (x:xs) m c = if x == m then c else positionofmax' xs m (c+1)


{-
*Main> maxofthelongest [[N 99,N 0],[N 2,N 3,S "hi, there!"],[N 3,S "hi there",N 1,N 2,N 2]]
*Main> 8
-}

data StrNum = S String | N Int deriving (Show,Eq) 

modda :: StrNum -> Int 
modda (S x) = length x 
modda (N i) = i


maxofthelongest :: [[StrNum]] -> Int
maxofthelongest lst = let (x:xs) = (map modda (lst !! positionofmax (map length lst)))
					  in foldl max x xs



data Valn a = Valn a ( a -> Bool ) 

instance Eq(a) => Eq (Valn a) where
	Valn t f == Valn r f' = t == r && (f t) == (f' r)
	
instance Show(a) => Show (Valn a) where
	show (Valn x f) = "Valn with value " ++ show x ++ " " ++ show (f x)

instance (Ord a , Num a) => Num (Valn a) where
	(Valn x f) + (Valn y f') = Valn (x+y) (\x -> f x && f' x)
	(Valn x f) - (Valn y f') = Valn (x-y) (\x -> f x && f' x)
	(Valn x f) * (Valn y f') = Valn (x*y) (\x -> f x && f' x)
	abs (Valn x f) = if x < 0 then Valn (-x) f else Valn x f 
    



data Junk a = A a | L [a] | End deriving (Eq,Show)

data Demuxed a = Demuxed !a ![[a]] deriving (Eq,Show)

demux :: Num(a) => [Junk a] -> Demuxed a 
demux ul = demux' ul 0 []
		where 
			demux' [] acc list = Demuxed acc list 
			demux' ( (A a) : xs ) acc list = demux' xs (acc+a) list
			demux' ( (L l) : xs ) acc list = demux' xs acc ( list++[l] )
			demux' ( (End) : xs ) acc list = Demuxed acc list 
				


data TreeM a = Leaf a | Branch a [TreeM a] deriving (Show)


visit :: TreeM a -> [a] 

visit (Branch x l) = x : foldl (++) [] ( map visit l ) -- map visit l return a list of list , we have to fold it before
													   -- the cons with x 
visit (Leaf x) = [x] 

instance Eq(a) => Eq (TreeM a) where
	x == y = visit x == visit y


zipToList :: [ (a,a) ] -> [a] 

zipToList [] = []
zipToList (x:xs) = [fst x] ++ [snd x] ++ zipToList xs

-- generate all the string of the language {a,b}^* in the infinite list fm 
-- trick to create infinite list: 
-- 1- inizializza la lista al valore iniziale ( in questo caso "" )
-- 2- ora pensa: "qualè il passaggio che devo fare ogni volta per creare il
-- 				  prossimo elemento partendo da quelli che ho fino ad ora?" 
fm = "" : zipToList [(x ++ "a", x ++ "b") | x <- fm]




infixes :: [a] -> [[a]]

infixes l = infixes1 l 1 0
		  where 
				infixes1 l t d 
							  | t < (length l ) && d < ((length l)-1) = [ take t (drop d l) ] ++ infixes1 l t (d + 1)
							  | t < (length l ) && d == ((length l)-1) = infixes1 l (t+1) 0
							  | t == (length l) = [take (length l) l ] 




data Node = Parent Int | Root deriving (Show, Eq)
type ParentTree a = Array Int (Node, a)

{-

Usage:

findRoot (array (0,2) [(0,(Root,'c')), (1,(Parent 0 , 'b')) ,(2,(Root, 'd'))]) (Parent 0,'b')
(Root,'c')

-}

findRoot :: Eq(a) =>  (ParentTree a) -> (Node , a) -> (Node , a) 

findRoot ppt (Root , x ) = (Root , x ) 
findRoot ppt (Parent x , y ) = findRoot ppt (ppt ! x)

{-

Usage:

union (array (0,3) [(0,(Root,'c')), (1,(Parent 0 , 'b')) ,(2,(Root, 'd')) , (3,(Parent 2 , 'e'))]) (Parent 0 , 'b') (Parent 2 , 'e')

array (0,3) [(0,(Root,'c')),(1,(Parent 0,'b')),(2,(Parent 0,'d')),(3,(Parent 2,'e'))]

-}

union :: Eq(a) => (ParentTree a) -> (Node , a ) -> (Node , a) -> (ParentTree a)

union ppt n1 n2 = let 
					r1 = (findRoot ppt n1)
					r2 = (findRoot ppt n2)
					i1 = (getIndex ppt r1)
					i2 = (getIndex ppt r2)
				  in if r1 == r2 then ppt else ppt // [(i2,(Parent i1,snd(r2)))]
				  
				  
				  
getIndex :: Eq(a) =>  (ParentTree a) -> (Node,a) -> Int 

getIndex ppt n = getIndex1 ppt n 0 
			   where getIndex1 ppt node i 
										| (ppt ! i ) == node = i
										| otherwise = getIndex1 ppt node (i+1)



{-
Another possible solution for the previous problem
exploiting case and other smart stuff
-}

parent :: ParentTree a -> Int -> Node
parent tree node = fst $ tree ! node

nodeName :: ParentTree a -> Int -> a
nodeName tree node = snd $ tree ! node

parentSet :: ParentTree a -> Int -> Int -> ParentTree a
parentSet tree node newRoot = tree // [(node, (Parent newRoot, nodeName tree node))]

findRoot1 :: ParentTree a -> Int -> Int
findRoot1 tree node = case parent tree node of 
	Root -> node
	Parent p -> findRoot1 tree p

union1 :: ParentTree a -> Int -> Int -> ParentTree a
union1 tree node1 node2 = let 
							 r1 = findRoot1 tree node1
							 r2 = findRoot1 tree node2 
						 in if r1 /= r2 
							then parentSet tree r2 r1 
							else tree


-- https://wiki.haskell.org/Tying_the_Knot
-- DList is a circular recursive data structure!!!!


data DList a = Nil | Node (DList a) a (DList a)

instance Eq a => Eq (DList a) where
	Nil == Nil = True
	(Node p c n) == (Node p' c' n') = c == c' && n == n' && p==p'
	_ == _ = False


car Nil = Nothing
car (Node prev head next) = Just head

cdr Nil = Nothing
cdr (Node prev head next) = let Node p c n = next
							in Just $ Node Nil c n


-- cons exploits call by need: new’s definition is recursive:
-- basically cons add an element in front of the list 
-- mental trick to understand the recursive definition:
-- "we are adding a new head for the nested list , so the new list is composed
-- by the new head [ Node Nil x ____ ] and instead of the ____ there is the
-- next Node that has as previous Node the new node and as next node the next 
-- node of the older head" 
-- Remember that cons is used to add element in the list ( x:list ...  )
cons x Nil = Node Nil x Nil
cons x (Node Nil cur next) = let new = Node Nil x (Node new cur next)
							 in new


-------------------------------------------------------------------------------
--PRINTED UNTILL HERE
-------------------------------------------------------------------------------

sumFromString :: String -> Integer
sumFromString s = foldl (+) 0 ( map read $ sumFromString' s [] )
				where 
					sumFromString' [] l = l
					sumFromString' s l = let (n,r) = span isDigit s
										  in if n==[] then sumFromString' (tail s) l else sumFromString' r [n]++l 
					



data Atom = N2 Int | S2 String deriving (Eq,Show)
data Exp =  S3 Atom | C2 Atom [Exp] 

instance Show Exp where
	show ( S3 a ) = show a 
	show ( C2 a (x:xs)) = show a ++ show x



seven :: Integer -> (Integer, Int)
seven n = seven' n 0 
        where seven' n c = if n>99 then let 
										q = div n 10 
										r = mod n 10 
										in seven' ( q - 2*r ) (c+1)
									else if (mod n 7)==0 then (n , c) else (n,c)



scoreTest :: (Integral a) => [a] -> a -> a -> a -> a

scoreTest [] _ _ _ = 0
scoreTest (x:xs) pc po pw 
						| x==0 = pc + scoreTest xs pc po pw 
						| x==1 = po + scoreTest xs pc po pw
						| x==2 = (-pw) + scoreTest xs pc po pw


largest :: Ord a => Int -> [a] -> [a]
largest n l = reverse (take n (sort l))



prova n = if (n+1) == 1 then n else n-1


newtype Text = Text [String] deriving (Eq)


instance Show Text where
	show (Text l)  = foldr (++) "" l 

data Song = Meta Text Int 



instance Show Song where
	show (Meta l t) = "Il testo della canzone è : " ++ show l ++ " e la durata: " ++ show t ++ " minuti"
	

instance Eq Song where 
	(Meta t d) == (Meta t' d') = t==t' && d == d'
	
	
revmap :: ( Int -> Int ) -> [Int] -> [Int] 
revmap f l = revmap' f l []
		  where 
				revmap' f [] r = r 
				revmap' f (x:xs) r = revmap' f xs ([f x] ++ r)

-- holy shit this is beautiful
revmap' f = foldl (\x -> \y -> (f y) : x) []


class Listoid l where
	listoidcons :: a -> l a -> l a
	listoidunit :: a -> l a 
	listoidappend :: l a -> l a -> l a 
	listoidfirst :: l a -> a
	listoidlast :: l a -> a 
	listoidrest :: l a -> l a 

newtype LL a = LL ([a],a) 


extract :: [a] -> a -> (LL a)
extract [] _ = error "unit list" 
extract (x:xs) u = LL ( xs , u)

instance (Eq(a)) => Eq (LL a) where
	( LL (l,e) ) == ( LL (l',e')) = l == l' && e == e'

instance (Show a) => Show (LL a) where
	show (LL (l,x)) = show l ++ "[" ++ show x ++ "]"

{-
here there is only LL in the declaration of instance because
Listoid typeclass is defined with a non concrete type ( note the fact 
that in the type of its function there is a l a, that means that l with a 
create a concrete type and not l alone! 

So LL a is the concrete type, and LL is the partial type and in this case 
we put the partial one.

-}
instance Listoid LL where
	listoidcons e (LL (l,u)) = LL ( [e]++l , u)
	listoidunit e = LL ([],e)
	listoidappend (LL (l',u')) (LL (l'',u'')) = LL (l' ++ l'' ++ [u''],u')
	listoidlast (LL (l,u)) = u
	listoidrest (LL ( l , u)) = extract l u


data Item = Numero Int | Stringa String deriving (Eq,Show)

type Order = [Item] 


allpossibleBikes :: [Order] -> [Order]
allpossibleBikes ( (p1):(p2):(p3):[] ) = [ ([x,y,z]) | x <- p1 , y <- p2 , z <- p3 ]  --limitata a 3 broker soli


allPossibleBikes1 ms = foldr k [[]] ms
				   	 where k m m' = [(x:xs) | x <- m, xs <- m'] --indipendentemente dal numero di broker :-) 
				   	 
				   	 

		
check :: String -> String -> (String,String)
check [] ('W':'U':'B':rs) = ("",rs)
check [] ('W':'U':rs) = ("WU",rs)	
check [] ('W':rs) = ("W",rs)	
check [] rs = ("",rs)	
check p [] = (p,"")
check p ('W':'U':'B':[]) = ((p ++ ""),[])			
check p ('W':'U':'B':rs) = ((p ++ " "),rs)	
check p ('W':'U':rs) = ((p ++ "WU"),rs)	
check p ('W':rs) = ((p ++ "W"),rs)		   	 
check p rs = ((p ++ ""),rs)	
	


	   	 
songDecoder :: String -> String
songDecoder s = songDecoder' s []
			 where 	
					songDecoder' [] o = o
					songDecoder' s o 
								  | (length s) >= 3 = let (p,r) = span (/= 'W') s
													  in let ( otemp, rest ) = check p r 
														  in songDecoder' rest (o ++ otemp)
								  | otherwise = songDecoder' [] (o ++ s)
			



main = do 
	   putStrLn "Hello, what's your name?"
	   name <- getLine 
	   putStrLn("Hey " ++ name ++ "fuck you")
