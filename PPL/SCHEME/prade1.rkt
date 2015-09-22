#lang racket


;; Multiple apply macro 
;; > (multiple-apply (+ - *) to ’(1 2 3) ’(3 4) ’(9 -2))
;; > (6 -1 -18)
(define-syntax multiple-apply
  (syntax-rules (to)
    ((_ (f1 ...) to l1 ...)
     (list (apply f1 l1) ...))))



;; Urmax from tde 24.07.2015

(define (urmax l)
  ( define (urmax-aux l1 index)
     (let label (( i index)
                 (cont 1)
                 (list-aux l1))
       (if (not (equal? cont i))
           (label i (+ cont 1) (cdr list-aux))
           (car list-aux))))
  (define (urmax-aux2 j l2 val)
    ( if (null? (cdr l2))
         val
         ( let (( comp (urmax-aux (car (cdr l2)) j)))
            (if ( < val comp)
                (urmax-aux2 (+ j 1) (cdr l2) comp)  ;tail recursive! 
                (urmax-aux2 (+ j 1) (cdr l2) val)))))
  (urmax-aux2 2 l (caar l)))

(urmax '((-1) (1 2) (1 2 3) (10 2 3 -4)))


;; Same urmax but exploiting high order function + a smart closure

(define count
  (let ((start -1))
    (lambda ()
      (set! start (+ start 1))
      start)))

(define (hurmax LL)
  (apply max (map (lambda (x)
                    (list-ref x (count))) LL)))


;; define the split in scheme that return a vector
;; with the prefix with n elements and the rest of the list 
(define (split l n)
  (define (split-aux l1 l n)
    (if (equal? 0 n)
        (vector l1 l)
        (split-aux (append l1 (list(car l))) (cdr l) (- n 1 ))))
  (split-aux '() l n))


(split '(0 1 2 3 4) 2) ;;=> '#((0 1) (2 3 4))


;; Use split to define the function 3-factors, which, given a list L, returns all the possible contiguous sublists
;; A, B, C, such that (equal? L (append A B C)). A,B,C, cannot be empty

(define (3-factors l)
  (define (3-factors-aux my-list cont1)
    (define (3-factors-aux-2 my-list-tmp cont2 prefix suffix)
      (if ( <= cont2 (- (length suffix) 1))
          (3-factors-aux-2 (append my-list-tmp (list (list prefix (vector-ref (split suffix cont2) 0) (vector-ref (split suffix cont2) 1)))) (+ cont2 1) prefix suffix)
          (3-factors-aux (append my-list my-list-tmp) (+ cont1 1 ))))
    (if ( <= cont1 ( - (length l) 2))
        (3-factors-aux-2 '() 1 (vector-ref (split l cont1) 0) (vector-ref (split l cont1) 1))
        my-list))
  (3-factors-aux '() 1))

(3-factors '(0 1 2 3 4))



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

;((car V) 5)


(define (rep l )
  (let label ((my_list '())
              (l1 l))
    (if (null? l1)
        my_list
        ( let  ( ( ml (member (car l1) (cdr l1))))
           (if (equal? #f ml)
               (label my_list (cdr l1))
               (label (append my_list (list (car l1))) (filter (lambda(x) (not (equal? x (car l1)))) (cdr l1) )))))))

(rep '(3 2 "hi" 2 "hello" hello 2 2 "hello" "hi"))






