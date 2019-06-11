(ns day23.part2
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader StringReader]))

(defn factors [n]
  (filter #(zero? (mod n %)) (range 2 n)))

(defn prime? [n]
  (empty? (factors n)))

(->> (range 109900 (inc 126900) 17)
     (filter (complement prime?))
     count
     println)

; This solution is comes from seeing that there is a nested loop over registers `d` and `e` such that `f` becomes 0 if b is a product of
; any of the values of `d` and `e`. That is, `f = 1` if `b` is prime. If `b` is not prime, then `h` is inc:ed.

; set b 99
; set c b
; jnz a 2
; jnz 1 5
; mul b 100
; sub b -100000
; set c b
; sub c -17000
;     set f 1
;     set d 2 # for d in range(2, b + 1):
;         set e 2 # for e in range(2, b + 1):
;             set g d
;             mul g e
;             sub g b
;             jnz g 2 # if d * e == b:
;                 set f 0
;             sub e -1
;             set g e
;             sub g b
;             jnz g -8
;         sub d -1
;         set g d
;         sub g b
;         jnz g -13
;     jnz f 2 # if f == 0:
;         sub h -1
;     set g b
;     sub g c
;     jnz g 2
;         jnz 1 3
;     sub b -17
;     jnz 1 -23
