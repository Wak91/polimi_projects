#lang racket
;http://www.ic.unicamp.br/~meidanis/courses/mc336/2006s2/funcional/L-99_Ninety-Nine_Lisp_Problems.html
;http://jscheme.sourceforge.net/jscheme/doc/R4RSprimitives.html

;ES-1
;Find the last box of a list.

(define (my-last l)
  (if (null? (cdr l))
      (car l)
      (my-last (cdr l))))

(my-last '(1 2 3))


;ES-2
;Find the last but one box of a list.
;how does it works?
;( 1 2 3 4 )
;----------
;    | foldl cons '()
;    v
;( 4 3 2 1 )
;----------
;    | cdr 
;    v
;( 3 2 1 )
;----------
;    |  foldl cons '()
;    v
;( 1 2 3 )
;----------
;    |  my-last
;    v
;    3
(define (my-but-last l)
  (my-last (foldl cons '() (cdr (foldl cons '() l)))))
        
(my-but-last '(1 2 3))

;ES-3
;Find the K'th element of a list.
;The first element in the list is number 1.

(define (element-at l i)
  (if (eqv? i 1)
      (car l)
      (element-at (cdr l) (- i 1))))

(element-at '(a b e f q w z) 6)


;ES-4
;Find the number of elements of a list

(define (number-of-el l)
  (define (noe-aux l n)
    (if (null? l)
        n
        (noe-aux (cdr l) (+ n 1))))
  (noe-aux l 0))

(number-of-el '(1 2 3 4 5 12342 34 234 234 234 2))

;ES-5
;Reverse a list
(define (reverse l)
  (foldl cons '() l))

(reverse '(1 2 3 4 5 12342 34 234 234 234 2))


;ES-6
;Find out whether a list is a palindrome.

(define (palindrome-test l)
  (equal? l (reverse l)))

(palindrome-test '(1 2 4 4 3 2 1))


;ES-6a

(append '(1 2 3) '(1 4 1 4)) ; => Unify two lists 
(foldr cons '(4 5 6) '(1 2 3));=> 1 2 3 4 5 6 
(define (test-l l)
  (cdr l))

(if (null? (test-l '(4)))  ;=> null
    (display "null")
    (display "Not null"))



;ES-7
;Flatten a nested list structure
;Transform a list, possibly holding lists as elements into a `flat' list
;by replacing each list with its elements (recursively).


(define (flatten l)
  (cond [(empty? l)      null]
        [(not (list? l)) (list l)]
        [else            (append (flatten (car l)) (flatten (cdr l)))]))

          
(flatten '( 1 2 3 (4 5) 5 (7 ( 8 ( 9 ( 10 ))))))


;ES-8
;Eliminate consecutive duplicates of list elements.
;If a list contains repeated elements they should be replaced with a single copy of the element. *
;The order of the elements should not be changed.

(define (del-copy l)
  (define (del-copy-aux my-list l)
    (if ( null? l)
        my-list
        (if ( equal? (car (foldl cons '() my-list)) (car l) ) ;=> foldl cons '() my-list is for reverse the list in order to extract the last element inserted previously.
               (del-copy-aux my-list (cdr l))
               (begin (del-copy-aux  (append my-list ( list (car l))) (cdr l))))))
  (del-copy-aux (list (car l)) (cdr l)))

(del-copy  '(1 1 2 2 1 1 3 3 3 3 5 5 6 7))

;ES-9
;Pack consecutive duplicates of list elements into sublists.
;If a list contains repeated elements they should be placed in separate sublists.
;   Example:
;   (pack '(a a a a b c c a a d e e e e))
;   ((A A A A) (B) (C C) (A A) (D) (E E E E))


(define (pack l)
  (define (pack-aux my-list-final l)
    (define (pack-aux-2 my-list-tmp l)
      (if (null? l)
          (append my-list-final (list my-list-tmp) )
          (if ( equal? (car (foldl cons '() my-list-tmp)) (car l))
              ( pack-aux-2 (append my-list-tmp (list (car l))) (cdr l))
              ( pack-aux (append my-list-final (list my-list-tmp) ) l))))   ;list of equal element reduced, let's proceed with the next elements 
   (pack-aux-2 (list (car l)) (cdr l)))
  (pack-aux '() l))

(pack '(a a a b c c a a d e e e e))


;ES-9a

(define my-list '(1 2))
(eq? (append my-list '(3 3)) my-list) ;=> Since my-list is immutable, every time we do an append a new object is created, in fact this [eq?] is #f

;ES-10
;Run-length encoding of a list.
;Use the result of problem P09 to implement the so-called run-length encoding data compression method.
;Consecutive duplicates of elements are encoded as lists (N E) where N is the number of duplicates of the element E.


(define (encode l)
  (define (encode-aux my-list l)
    (if (null? l)
        my-list
        (encode-aux (append my-list (list(list (number-of-el (car l)) (caar l)))) (cdr l))  
        ))
  (encode-aux '() l))

(encode (pack '(a a a b c c a a d e e e e)))


; ES-11
; Modified run-length encoding.
; Modify the result of problem P10 in such a way that if an element has no duplicates it is simply copied into the result list.
; Only elements with duplicates are transferred as (N E) lists.
; Example:
; (encode-modified '(a a a a b c c a a d e e e e))
; ((4 A) B (2 C) (2 A) D (4 E))

(define (encode-modified l)
  (define (encode-aux my-list l)
    (if (null? l)
        my-list
        (if (equal? (number-of-el (car l)) 1)
            (encode-aux (append my-list (list (caar l))) (cdr l))  ;this is in case of a single element, we join it to the main list without create the (#,e)
            (encode-aux (append my-list (list(list (number-of-el (car l)) (caar l)))) (cdr l)))))  ;;this will create the list with (#,e)
  (encode-aux '() l))

(encode-modified (pack '(a a a b c c a a d e e e e)))


;ES-12
;Decode a run-length encoded list.
;Given a run-length code list generated as specified in problem ES-11.
;Construct its uncompressed version.

(define (decode l)
  (define (decode-aux my-list l)  ;my-list will be (a a a) and sub-l is ( 3 a ) , if it is a single element this aux never call decode-aux-2 otherwise it call it 
    (define (decode-aux-2 my-list-tmp n e )
      (if (zero? n)
          my-list-tmp
          (decode-aux-2 (append my-list-tmp e) (- n 1) e)))
    (if (null? l)
        my-list
        (if (list? (car l))
            (decode-aux (append my-list (decode-aux-2 '() (caar l) (cdr(car l)))) (cdr l) )
            (decode-aux (append my-list (list (car l))) (cdr l)))))
  (decode-aux '() l))

(decode (encode-modified (pack '(a a a b c c a a d e e e e))))
              

;ES-13
;Run-length encoding of a list (direct solution).
;Implement the so-called run-length encoding data compression method directly.
;I.e. don't explicitly create the sublists containing the duplicates, as in problem ES-9, but only count them.
;As in problem ES-11, simplify the result list by replacing the singleton lists (1 X) by X.
; (encode-direct '(a a a a b c c a a d e e e e))
; ((4 A) B (2 C) (2 A) D (4 E))


(define (encode-direct l)
  (define (encode-direct-aux my-list l)
    (define (encode-direct-aux-2 n e l)
      (if (null? l)
          (if (equal? n 1)
              (encode-direct-aux (append my-list (list e)) '())
              (encode-direct-aux (append my-list (list (list n e))) '()))  
          (if (equal? e (car l))
              (encode-direct-aux-2 (+ 1 n) e (cdr l))
              (if (equal? n 1)
                  (encode-direct-aux (append my-list (list e)) l)
                  (encode-direct-aux (append my-list (list (list n e))) l)))))
    (if (null? l)
        my-list
        (encode-direct-aux-2 1 (car l) (cdr l))))
  (encode-direct-aux '() l))

(encode-direct '(a a a b c c a a d e e e e f))

;ES-14 Duplicate the elements of a list.
;  Example:
;  (dupli '(a b c c d))
;  (A A B B C C C C D D)

(define (dupli l)
  (map (lambda(x) (list x x)) l ))

(flatten (dupli '(a a a a b c c d)))

;ES-15 Replicate the elements of a list a given number of times.
; Example:
;  (repli '(a b c) 3)
;  (A A A B B B C C C)

(define (repli l n)
  (define (repli-aux my-list l)
    (define (repli-aux-2 my-list-tmp n e)
      (if (zero? n)
          (repli-aux (append my-list my-list-tmp) (cdr l))
          (repli-aux-2 (append my-list-tmp (list e)) (- n 1) e)))
    (if (null? l)
        my-list
        (repli-aux-2 '() n (car l))))
  (repli-aux '() l))

(repli '(a a b c) 3)

;ES-16 Drop every N'th element from a list.
; Example:
; (drop '(a b c d e f g h i k) 3)
; (A B D E G H K)

(define (drop l np)
  (define (drop-aux my-list l n)
    (if (null? l)
        my-list
        (if (zero? n)
            (drop-aux my-list (cdr l) (- np 1))
            (drop-aux (append my-list (list(car l))) (cdr l) (- n 1)))))
  (drop-aux '() l (- np 1)))

(drop '(a b c d e f g h i k l ) 3)


;ES-17  Split a list into two parts; the length of the first part is given.
; Do not use any predefined predicates.
; Example:
; (split '(a b c d e f g h i k) 5)
; ( (A B C) (D E F G H I K))

(define (split l n)
  (define (split-aux my-list n l )
    (if (zero? n)
        (append (list my-list) (list l))
        (split-aux (append my-list (list(car l))) (- n 1) (cdr l))))
  (if (< (number-of-el l) n)
      (display "error, to split n must be < of number of el")
      (split-aux '() n l)))

(split '(a b c d e f g h i k) 3)

;ES-18 Extract a slice from a list.
;  Given two indices, I and K, the slice is the list containing the elements between the I'th and K'th element of the original list (both limits included).
;  Start counting the elements with 1.
;  Example:
;  (slice '(a b c d e f g h i k) 3 7)
;  (C D E F G)

(define (slice l s e)
  (define (slice-aux my-list c l)
    (if (> c e)
        my-list
        (if (>= c s) 
            (slice-aux (append my-list (list (car l))) (+ c 1) (cdr l))
            (slice-aux my-list (+ c 1) (cdr l)))))
  (if (< (number-of-el l) e)
      (display "error")
      (slice-aux '() 1 l)))

(slice '(a b c d e f g h i k) 1 7)

;ES-19 Rotate a list N places to the left.
; Examples:
; (rotate '(a b c d e f g h) 3)
; (D E F G H A B C)
; (rotate '(a b c d e f g h) -2)
; (G H A B C D E F)
; Hint: Use the predefined functions length and append,
; as well as the result of problem ES-17.


(define (fix-n l n)
  (cond [(< n 0) (fix-n l (+ l n))]
        [(> n l) (fix-n l (- n l))]
        [else n]))
   
(define (rotate l n)
  (let ((i (fix-n (number-of-el l) n)))
    (flatten (append (cdr (split l i)) (car (split l i))))))

(rotate '(a b c d e f g h) +22)


;ES-20 Remove the K'th element from a list.
;Example:
; (remove-at '(a b c d) 2)
; (A C D)

(define (remove-at l n)
  (define (remove-at-aux my-list n l )
    (if (zero? n)
        (append (list my-list) (list (cdr l)))
        (remove-at-aux (append my-list (list(car l))) (- n 1) (cdr l))))
  (if (< (number-of-el l) n)
      (display "error, to remove n must be < of number of el")
      (remove-at-aux '() (- n 1) l)))

(flatten (remove-at '(a b c d) 5))

;ES-21 Insert an element at a given position into a list.
; Example:
; (insert-at 'alfa '(a b c d) 2)
; (A ALFA B C D)

(define (insert-at e l i)
  (if (<= i (number-of-el l))
      (flatten (list (car (split l (- i 1))) e (cdr (split l (- i 1)))))
      (display "error")))

(insert-at 'alfa '(a b c d) 4)


