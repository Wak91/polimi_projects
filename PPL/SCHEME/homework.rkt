#lang racket

;HOMEWORK-1

;ES-0
;Find the max of a list

(define (max l)
  (define (max-aux m l)
    (if (null? l)
        m
        (if (> m (car l))
            (max-aux m (cdr l))
            (max-aux (car l) (cdr l)))))
  (max-aux (car l) l))


(max '(1 87 7 8 3 3 0 9 8 3 1 2))


;ES-1
;Find the greatest commond divisor between two numbers

(define (gcd a b)
  (if (zero? b)
      a
      (gcd b (remainder a b))))

(gcd 19 3)

;ES-2
;define the "nested-length" function.
;nested-length takes a list as argument and returns the number of element inside the list and all the nested lists.

;examples:
;(nested-length '(1 2 3)) -> 3
;(nested-length '(1 2 (2 (5 4)) 5 6)) -> 7


(define (nested-length l)
    (define (nested-length-aux-2 n list-tmp )
      (if (null? list-tmp)
          n
          (if (list? (car list-tmp))
              (nested-length-aux-2 (+ n (nested-length-aux-2 0 (car list-tmp)))  (cdr list-tmp))
              (nested-length-aux-2 (+ n 1) (cdr list-tmp)))))
  (nested-length-aux-2 0 l))


(nested-length '(1 2 ( 4 5 ) 3 4 (8 (9) 3) ))
    

;ES-3
;define the "print-matrix" function.

;print-matrix takes a matrix as argument (a matrix is a vector of vectors, e.g. #(#(1 2) #(3 4)) is a 2x2 matrix) 
;and prints out the content of the matrix as in following examples.

;examples:

;> (print-matrix #(#(1 2) #(3 4)))
;1    2
;3    4

;> (print-matrix #(1 2 3))
;1 2 3



(define (print-matrix m)
  (let label ((n (vector-length m)) (i 0))
    (if(equal? i n)
       (display "\n")
       (begin ( if (vector? (vector-ref m i))
                   (print-matrix (vector-ref m i))
                   (begin (display (vector-ref m i))))
              (label n (+ i 1))))))


(print-matrix #(#(1 2) #(3 4)))  ;=> 1 2 \n 3 4   
(print-matrix #(1 2 3)) ;=> 1 2 3
(print-matrix #(#(1) #(4))) ; => 1 \n 4

;ES-4
;define the "vector*!" function.

;vector*! takes three vectors (v1 v2 v3) as arguments. 
;this function will consider v2 a Nx1 matrix, v3 a 1xM matrix; v1 must be a mutable vector of size N.
;this function computes the multiplication between v2 and v3 and saves the resulting NxM matrix in v1.

; example:
; (define v1 (make-vector 2))       
; (vector*! v1 #(3 2) #(4 5))
; (print-matrix v1)
; 12   15
; 8    10
; > (vector*! v1 #(5 2) #(1 1 0))
; > (print-matrix v1)
; 5    5    0
; 2    2    0


(define v1 (vector)) 

(define (vector*! v1 v2 v3)
  (define (vector*!-aux e v-mul)
    (let label ((v-tmp (vector))  (i 0) )
      (if ( equal? i (vector-length v-mul))
          (set! v1 ( vector-append v1 (vector v-tmp))) 
          ( label (vector-append v-tmp (vector (* e (vector-ref v-mul i)))) (+ i 1)))))
  (let label-2 ((j 0) (k (vector-length v2)))
    (if (equal? j k)
        v1
        (begin (vector*!-aux (vector-ref v2 j) v3)
               (label-2 (+ j 1) k)))))

(vector*! v1 #(5 2) #(1 1 0))
(print-matrix (vector*! v1 #(5 2) #(1 1 0)))
(vector*! v1 #(3 2) #(4 5))
(print-matrix (vector*! v1 #(3 2) #(4 5)))                 


;ES-5
;define the "zip" function.
;zip takes two lists as arguments. it returns a list of lists, where the i-th list contains the i-th element from each input list. 
;the returned list is truncated in length to the length of the shortest argument list.

;examples: 
;(zip '(1 2 3) '(4 5 6)) -> '((1 4) (2 5) (3 6))
;(zip '(a 4 d) '(2 c)) -> '((a 2) (4 c))


(define (zip l1 l2)  ;This function create a list with element alternate from the list l1 and l2 => ( 1 4 2 5 3 6 )
  (define (zip-aux my-list l1 l2)
    (if (null? l1)
        (append my-list l2)
        (zip-aux (append my-list (list(car l1))) l2 (cdr l1))))
  (zip-aux '() l1 l2 ))

(define (zip-format l)  ;This function take the input of the previous function and create the pairs ((1 4) (2 5) (3 6)).
  (define (zip-format-aux my-list l )
    (if (<= (length l) 1)  ; We detect the fact that the list has odd number of elements using the length of l.
        my-list
        (zip-format-aux (append my-list (list (list (car l) (car (cdr l))))) (cdr(cdr l)))))
  (zip-format-aux '() l))

(zip-format (zip '(a 4 d) '(2 c)))

;ES-6
;define the "split" function.

;split takes two arguments: a list and a splitter value. 
;it returns a list of lists in which each list is the sublist of the input list between two occurences of the splitter value.

;examples: 
;(split '(a b c x d x e x f) 'x) -> '((a b c) (d) (e) (f))
;(split '(0 1 2 0 2 0 3 0) 0) -> '((1 2) (2) (3))


(define (split l sv)
  (define (split-aux my-list my-list-tmp l)
    (if (null? l)
        (if(null? my-list-tmp)
           my-list
           (append my-list (list my-list-tmp)))  
        (if(equal? sv (car l)) ;=> sv is a closure 
           (split-aux (append my-list (list my-list-tmp)) '() (cdr l))
           (split-aux my-list (append my-list-tmp (list (car l))) (cdr l)))))
  (split-aux '() '() l))

(split '(a b c x d x e x f ) 'x)


;ES-7
;define the "takewhile" function.
;takewhile takes two arguments: a predicate and a list. it returns the elements of the list until the predicate is violated.

;examples: 
;(takewhile odd? '(1 5 7 8 1 4 6 9)) -> '(1 5 7)
;(takewhile positive? '(5 11 4 2 -2 5 6) -> '(5 11 4 2)

(define (takewhile p l)
  (define (takewhile-aux my-list l)
    (if (equal? (p (car l)) #f)
     my-list
     (takewhile-aux (append my-list (list (car l))) (cdr l))))
  (takewhile-aux '() l))

(takewhile odd? '(1 5 7 8 1 4 6 9))

;ES-8
;define the "flatmap" function.
;flatmap is similar to map but it works also for nested lists. it returns a flatten list.

;examples: 
;(flatmap square '(((1 2 3) 4) 0 (6 7) 8)) -> '(1 4 9 16 0 36 49 64)
;(flatmap (lambda(x) (+ x 1)) '(5 (11) ((12 23) 5))) -> '(6 12 13 24 6)

(define (square x)
  (* x x))

(define (flatmap p l)
  (map p (flatten l)))

(flatmap square '(((1 2 3) 4) 0 (6 7) 8))
(flatmap (lambda(x) (+ x 1)) '(5 (11) ((12 23) 5)))


;ES-9
;define the "on-sign" macro.

;on-sign choose between three branches depending if the value considered is greater than zero, equals to zero, or less than zero.
;this macro has the following syntax:
;(on-sign <my expr> pos: <block of code> zero: <block of code> neg: <block of code>) 

;examples:
;(on-sign 2
;        pos: 'a
;        zero: 'b
;        neg: 'c) -> 'a

;(on-sign (- 5 6)
;         pos: (begin (display "positive") (newline))
;         zero: (begin (display "zero") (newline))
;         neg: (begin (display "negative") (newline)))
;=> negative


(define-syntax on-sign
  (syntax-rules (pos zero neg :)
    (( _ expr pos: c1 zero: c2 neg: c3 )
     (if (procedure? expr)
         (let ((n (expr)))
           (cond [(> n 0)  c1]
                 [(equal? 0 n) c2]
                 [(< n 0) c3]))
         (let ((n expr))
           (cond [(> n 0)  c1]
                 [(equal? 0 n) c2]
                 [(< n 0) c3]))))))
 
(on-sign 2
         pos: (begin (display "positive") (newline))
         zero: (begin (display "zero") (newline))
         neg: (begin (display "negative") (newline)))


(on-sign (- 5 6)
        pos: (begin (display "positive") (newline))
        zero: (begin (display "zero") (newline))
        neg: (begin (display "negative") (newline)))


