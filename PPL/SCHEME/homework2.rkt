#lang racket

;HOMEWORK-2


;ES-1

;define the "unzip" function.
;unzip takes a zipped list (see exercise 5 of homework 1) and returns the a list that contains the original two lists.

;examples:
;(unzip '((1 4) (2 5) (3 6))) -> '((1 2 3) (4 5 6))
;(unzip '((a 2) (4 c))) -> '((a 4) (2 c))


(define (unzip l)
  (define (unzip-aux l1 l2 l)
    (if (null? l)
        (append (list l1) (list l2))
        (unzip-aux (append l1 (list(caar l))) (append l2 (cdr (car l))) (cdr l))))
  (unzip-aux '() '() l))

(unzip '((1 4) (2 5) (3 6)))
(unzip '((a 2) (4 c)))


;ES-1a

(define (test . l1)  ;a function with unlimited number of parameters 
  (cdr l1))

(test '(1 2 3) '(4 5 6) '(7 8 9)) ; => (4 5 6) (7 8 9)

;ES-2
;define "zip*", a zip function that takes an indefinite number of lists to be zipped instead of only two.

;(zip* '(1 2 3) '(4 5 6) '(7 8 9)) -> '((1 4 7) (2 5 8) (3 6 9))
;(zip* '(1 2 3)) -> '((1) (2) (3))


(define (zip* . l)
  (define (zip*-aux my-list l1 )
    (if (null? (car l1))
        my-list
        (let label ((my-list-tmp '()) (lr l1))  ;my-list-tmp will be a portion of the final list , l-slice a list from which extract an element
          (if (null? lr)
              (zip*-aux (append my-list (list my-list-tmp)) (map (lambda(l) (cdr l)) l1)) ;map is useful in order to remove all the car from the list 
              (label (append my-list-tmp (list (car (car lr)))) (cdr lr))))))
  (zip*-aux '() l))

(zip* '(1 2 3) '(4 5 6) '(7 8 9))
(zip* '(1 2 3))
(zip* '(1 2 3) '(4 5 6) '(7 8 9) '(8 8 8) '(@ ? * ) '(@ ? *) ) ;;=> For now all the list must have the same length ( we will fix it )

;ES-3
;define the "make-iterator" function that takes a list as argument and returns a closure. 
;this closure is an iterator object with the two well-known methods has-next? and next. 
;use the 'closures as objects' technique seen in class.

;example:
;(define my-it (make-iterator '(1 2 3)))
;(my-it 'has-next?) -> #t
;(my-it 'next) -> 1
;(my-it 'next) -> 2
;(my-it 'has-next?) -> #t
;(my-it 'next) -> 3
;(my-it 'has-next?) -> #f
;(my-it 'next) -> error


(define (make-iterator l)
  (let ((l1 l))
    (define (has-next)
      (if (null? l1)  ;l1 is the closure!
          #f
          #t))
    (define (next)
      (if (null? l1)
          '()
          (let ((el (car l1)))
            (set! l1 (cdr l1)) el)))
  (lambda (message)
    (cond [(equal? message "has-next")  (has-next)]
          [(equal? message "next")  (next)]
          [else  (display "no method found")]))))

(define l (make-iterator '(1 2 3 4)))

(l "has-next")             ;Basically you are calling the lambda defined above with the arguments "has-next". 
(display (l "next"))
(newline)                  ;since this is a closures that embedd all the other definition the right method will be called 
(display (l "next"))
(newline)
(l "has-next")
(newline)  
(display (l "next"))
(l "has-next")
(newline)
(display (l "next"))
(newline)  
(l "has-next")
(newline)
(display (l "next"))
(newline)
(display (l "next"))
(l "nex")
(newline)

;ES-4

;define the "iter" macro.
;iter uses a closure-iterator to iterate through a list. iter has the following syntax:
;(iter <the iterator> -> <a variable symbol> <code>)

;example:
;(define my-it (make-iterator '(1 2 3)))
;(iter my-it -> x
;      (display x)
;      (newline))

(newline)

(define-syntax iter
  (syntax-rules (->)
    (( _ itr -> e body ... ) ;body ... is the code to perform for each element in the list 
     (let loop ((e (itr "next")))
       (if (null? e)
           (newline)
           (begin
             body ... 
             (loop (itr "next"))))))))

(define my-it (make-iterator '(1 2 3)))
 
(iter my-it -> x
     (display x)
     (display " doubled-> ")
     (display (+ x 1))
     (newline))


;ES-5

;given this code:

(define (list-iter-cc lst)
  (call/cc 
   (lambda (return) 
    (for-each               
      (lambda (x)
       (call/cc (lambda (next-step)
                   (return (cons x next-step))))) ;;in next-step I have the continuation
      lst)
     'end)))

 (cdr (list-iter-cc '(1 2 3 4)))  ;=> in this way I extract the continuation from the cons ( in this case the other three cycle of the for-each )



;((cdr (list-iter-cc '(1 2 3 4)))) ;=> in this way I evaluate the continuation. ( the continuation saved in return is the cdr! )
                                   ;   basically in this way you are doing the cdr of all the cons returned by the list-iter-cc and with the ( ) you are evaluating the continuation



;enrich the iter macro using the list-iter-cc function with the following syntax.

;(iter2 <a variable symbol> in <a list> <code>)

;example:

;(iter2 x in '(1 2 3) 
;	(display x)
;	(newline)) 

(define-syntax iter2
  (syntax-rules (in)
    (( _ e in l body ... )
     (let label ((cc (list-iter-cc l)))
       (if (equal? 'end cc)
           (newline)
           (begin
              ((lambda (e)
              (begin body ...))
            (car cc))
            (label ((cdr cc)))))))))

(iter2 x in '(1 2 3) 
	(displayln (+ x 1))
        (display "cc")
	(newline))                 

