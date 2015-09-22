#lang racket
;;CALL/CC

;;-------------------------------------------
;;EXCEPTION HANDLING
;;-------------------------------------------
(define *handlers* (list))

(define (push-handler proc)
  (set! *handlers* (cons proc *handlers*)))

(define (pop-handler)
  (let ((h (car *handlers*)))
    (set! *handlers* (cdr *handlers*))
    h))

(define (throw x)
  (if (pair? *handlers*)
      ((pop-handler) x)
      (apply error x)))


(define (foo x)
  (display x) (newline)
  (throw "hello"))


(define-syntax catch
  (syntax-rules ()
    (( _ what handler exp1 ...)
     ( call/cc (lambda (exit)
                 (push-handler
                  (lambda(x)
                    (if (equal? x what)
                        (exit handler)
                        (throw x))))
                 (let ((res
                        (begin exp1 ...)))
                   (pop-handler)
                   res))))))

( + 3
       (catch "hello"
         (begin
           (display "I caught a throw\n")
           2)
         (display "Before foo")
         (newline)
         (foo "hi!")
         (display "After foo"))) ;;the continuation is at the end of this snippet of code, by passing the handler we will execute the handler



;;----------------------------------------------
;; EXAMPLE 2
;;----------------------------------------------

(define saved-cont #f)

(define (test-cont)
  (let ((x 0))
    (call/cc
     (lambda (k)
       (set! saved-cont k)))
    (set! x (+ x 1))
    (display x)
    (newline)))

(test-cont) ;=> 1 
(saved-cont) ;=> 2
(define other-cont saved-cont)
(test-cont) ;=> 1 here we reset saved-cont 
(other-cont) ;=> 3 other is still going...
(saved-cont) ;=> 2


;;----------------------------------------------
;; EXAMPLE 3
;;----------------------------------------------

(define (my-exists-opt P L)
  (call/cc (lambda (exit)
	     (not (null? (filter 
			  (lambda (t) 
			    (if (P t)
				(exit (display "motherfucker")) ; in order to exit from the filter function from the lambda
				#f)) 
			  L))))))

(my-exists-opt (lambda(x) (if ( > x 3) #t #f)) '(1 2 2 4 1 2 ))

;;----------------------------------------------
;; EXAMPLE 4
;;----------------------------------------------

(define (re-map f L cond?)
  (let loop ((res '())
             (cur L))
    (if (null? cur)
        res
        (let* ((k #f)
               (v (call/cc
                   (lambda (cont)
                     (set! k cont)
                     (f (car cur))))))
          (if (cond? v)
              (cons k v)
              (loop (append res (list v))
                    (cdr cur)))))))


(define V
  (re-map (lambda (x) (+ x 1))
          '(0 1 -4 3 -6 5)
          negative?))

;((car V) 4) ;; la continuation è dall'assegnamento di v in let* in poi, se non passo il 4 non sa a cosa deve assegnare v!


