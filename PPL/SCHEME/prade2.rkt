#lang racket

(define (multiple-4pply f n x)
  ( let label ( ( val x )
                (cont n ))
     (if (equal? cont 0)
         val
         (label (f val) ( - cont 1)))))



(multiple-4pply (lambda (x) (+ 1 x)) 5 1)



(define (position-of-max l)
  ( let (( maxim (apply max l)))
     (let label1 ((lis l)
                  (index 1))
       (if (equal? (car lis) maxim)
           index
           (label1 (cdr lis) (+ index 1))))))

(position-of-max '(1 2 3 7 5 6))


(define (norm v)
  (if (string? v)
      (string-length v)
      (if (integer? v)
          v
          0)))


(define (max-of-the-longest l)
  (define (max-of-the-longest-aux lm s l)
    (if (null? l)
        lm
        (if ( > (length (car l)) s )
            (max-of-the-longest-aux (car l) (length (car l)) (cdr l))
            (max-of-the-longest-aux lm s (cdr l)))))
  (let ((lis (max-of-the-longest-aux (car l) (length (car l)) (cdr l))))
    (let label1 (( v (norm (car lis)))
                 (listmp (cdr lis)))
      (if (null? listmp)
          v
          (if ( > v ( norm (car listmp)))
              (label1 v (cdr listmp))
              (label1 (norm (car listmp)) (cdr listmp)))))))

(max-of-the-longest '((99 0) (2 3 "hi, there!") (3 "hi there" 1 -1 -1)))




(define v1 ( make-vector 7 #f))

(define (vecstring v l)
  ( let label ((l1 l))
     (if (null? l1)
         v
         (let ((len (string-length (car l1))))
           (if ( > len ( - (vector-length v) 1 ))
               (label (cdr l1))
               (begin ( vector-set! v len (car l1))
                      (label (cdr l1))))))))



(vecstring v1 '("hi" "italiano" "papa" "pollo" "memoria"))

(define v2 ( make-vector 7 #f))



;; same things as previously but with a closure 

(define (make-vecstring v)
  (let (( vector v))
    (define (put s)
      ( if ( > (string-length s) ( - (vector-length v) 1 ))
           (displayln "string too long")
           (vector-set! v (string-length s) s)))
    (define (return)
      (displayln v))
    (lambda (message)
      ( case message
         ((return) (return))
         (else (put message))))))


(define my-v (make-vecstring v2))

(my-v "another")
(my-v "member")
(my-v "no")
(my-v 'return)


(struct demuxed ( (v #:mutable) (list-vector #:mutable)))

(define d ( demuxed 0 '()))

(define (demux-imperative l d)
  (let label ((l1 l)
              (acc 0)
              (lv '()))
    (if (or (null? l1) (equal? 'end (car l1)))
        (begin
          (set-demuxed-v! d acc)
          (set-demuxed-list-vector! d lv)
          (display (demuxed-v d) )
          (display (demuxed-list-vector d)))
        (if (integer? (car l1))
            (label (cdr l1) (+ acc (car l1)) lv)
            (if (vector? (car l1))
                (label (cdr l1) acc (append lv (list (car l1))))
                (label (cdr l1) acc lv))))))


(demux-imperative '(3 "bob" #(6 6 1) 4 #(1 2) -2 end 9) d)

(display "\n")

(define (demux-functional l)
  (let label ((l1 l)
              (dd (demuxed 0 '())))
    (if (or (null? l1) (equal? 'end (car l1)))
        (begin
          (display (demuxed-v dd) )
          (display (demuxed-list-vector dd)))
        (if (integer? (car l1))
            (label (cdr l1) (demuxed (+ (demuxed-v dd) (car l1)) (demuxed-list-vector dd)))
            (if (vector? (car l1))
                (label (cdr l1) (demuxed (demuxed-v dd) (append (demuxed-list-vector dd) (list (car l1)))))
                (label (cdr l1) dd))))))


(demux-functional '(3 "bob" #(6 6 1) 4 #(1 2) -2 end 9))


(define (string-from-strings l)
  (let label ((l1 l)
              (my-string ""))
    (if (null? l1)
        my-string
        (if (string? (car l1))
            (label (cdr l1) (string-append my-string (car l1)))
            (label (cdr l1) my-string)))))

(string-from-strings '(1 "hello" ", " 2 "world"))



(define (string-from-strings-ho l)
  ( let (( lf (filter (lambda(x) (string? x)) l)))
     (apply string-append lf)))

(string-from-strings-ho '(1 "hello" ", " 2 "world")) 


(define (subsets e)
  (let loop ((l e)
             (out '(())))
    (if (null? l)
        out
        (loop (cdr l)
              (append out
                      (map (lambda (x) (cons (car l) x)) out))))))

(define (make-object l)
  ( let (( bag l))
     (define (member? x)
       (let ((res (member x bag)))
         (if (equal? res #f)
             #f
             #t)))
     (define (subsetsum x)
       (let label (( ss (subsets bag)))
         (if (null? ss)
             #f
             ( if (equal? x (apply + (car ss)))
                  #t
                  (label (cdr ss))))))
     (lambda (message . args)
       ( apply (case message
                 ((member?) member?)
                 ((subsetsum) subsetsum)
                 (else (error "semo")))
               args))))


(define ob (make-object '(3 2 7)))
(ob 'member? 9)
(ob 'subsetsum 9)



(define (find-root n v)
  ( let label ((pnode (vector-ref v (car n))))
     (if (equal? '? (car pnode))
         pnode
         (label (vector-ref v (car pnode))))))


(find-root '(7 . X) #((? . R) (0 . A) (0 . B) (1 . C) (1 . D) (1 . E) (2 . F) (? . W) (7 . X) (7 . Y)))



(define (union! n1 n2 v)
  ( let* ((p1 (find-root n1 v))
         (i1 (vector-member p1 v))
         (p2 (find-root n2 v))
         (i2 (vector-member p2 v)))
     (if (equal? p1 p2)
         (display "n1 and n2 are in the same tree")
         (begin
           (vector-set! v i2 ( cons i1 (cdr p2)))
           (display v)))))

(union! '(2 . F) '(7 . X) ( vector '(? . R) '(0 . A) '(0 . B) '(1 . C) '(1 . D) '(1 . E) '(2 . F) '(? . W) '(7 . X) '(7 . Y)))



(display "\n")

(define (exists l p)
  (let label ((l1 l))
    (if (null? l1)
        #f
        (if (equal? #t (p (car l1)))
            #t
            (label (cdr l1))))))


(exists '(1 2 3 -1 9 3 8 -2) (lambda(x) ( if ( < x 0) #t #f)))



(define (my-exists-opt P L)
  (call/cc (lambda (exit)
	     (not (null? (filter 
			  (lambda (t) 
			    (if (P t)
				(exit #t) ; in order to exit from the filter function from the lambda
				#f)) 
			  L))))))



(define (numberlist l iv)
  ( let label (( acc iv)
               (l1 l)
               (my-list '()))
     (if (null? l1)
         my-list
         (label (+ acc (car l1)) (cdr l1) (append my-list (list (cons (car l1) (+ acc (car l1)))))))))

(numberlist '(1 3 22 -5) 0)



(define (num-nodes l)
  ( define (num-nodes-aux sl c )
     ( let label ((sls sl)
                  (acc c))
        (if (null? sls)
            acc
            (if (not (list? (car sls)))
                (label (cdr sls) (+ acc 1))
                (label (cdr sls) (num-nodes-aux (car sls) acc))))))
  (num-nodes-aux l 0))

(num-nodes '(+ 2 3 (/ 1 3) (- 2 2 4 -7)))


(define (num-nodes2 l)
  ( if (not (list? l))
       1
       (apply + (map num-nodes2 l))))


(num-nodes2 '(+ 2 3 (/ 1 3) (- 2 2 4 -7)))


(define (make-matrix r c fill)
  (let label ((v (make-vector r fill))
              (i 0))
    (if (equal? i r)
        v
        (begin
          (vector-set! v i (make-vector c fill))
          (label v (+ 1 i))))))

(make-matrix 4 3 #t)



(define (sfs list)
  ( if ( null? list)
       ""
       (if ( string? (car list))
           (string-append (car list) (sfs (cdr list)))
           (sfs (cdr list)))))

(sfs '("ciao " 1 "fabio " 4 4 #t "come va?"))

(define (sfs2 list)
  (define (sfs l s)
    (if (null? l)
        s
        (if (string? (car l))
            (sfs (cdr l) (string-append s (car l) ))
            (sfs (cdr l) s))))
  (sfs list ""))

(sfs2 '("ciao " 1 "fabio " 4 4 #t "come va?"))


(foldr string-append "" (map (lambda(x) ( if (string? x) x "")) '("ciao " 1 "fabio " 4 4 #t "come va?") ))



(define (make-iter l)
  ( let ((my-list l))
     (lambda ()
       (if (null? my-list )
           'end
           ( let ((e (car my-list)))
              (begin
                (set! my-list (cdr my-list))
                e))))))

(define li ( make-iter '(1 2 3 4)))

(li)
(li)
(li)
(li)
(li)
(li)
(li)


(define (make-v-iter v)
  (let ((my-vector v)
        (index 0))
    (lambda ()
      (if (equal? (vector-length v) index)
          'end
          ( let (( e (vector-ref my-vector index)))
             (begin
               (set! index (+ index 1))
               e))))))


(define lv (make-v-iter #(1 2 3 4 5)))

(lv)
(lv)
(lv)
(lv)
(lv)
(lv)
(lv)
(lv)


(define (make-iterator s)
  (if (list? s)
      (make-iter s)
      (if (vector? s)
          (make-v-iter s)
          (error "!"))))

(define-syntax for
  (syntax-rules (in)
    (( _ e in struct body ... )
     (let ((iter (make-iterator struct)))
       (let label ((e (iter)))
         (if (equal? 'end e)
             (display "\n")
             (begin
               body ...
               (label (iter)))))))))

(for x in #(c a s a) (display x) (display "."))

