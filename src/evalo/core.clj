(ns evalo.core
  "Program synthesis for boolean expressions"
  (:require [clojure.tools.nrepl.server :refer (start-server stop-server)]
            [clojure.core.logic :as l :refer [== != fresh run conde conso]])
  (:gen-class))

(defn oro
  "Relational boolean or."
  [a b o]
  (conde
    [(== a true) (== true o)]
    [(== b true) (== true o)]
    [(== a false) (== b false) (== false o)]))

(defn xoro
  "Relational boolean xor."
  [a b o]
  (conde
    [(== a true) (== b false) (== o true)]
    [(== a false) (== b true) (== o true)]
    [(== a false) (== b false) (== o false)]))

(defn ando
  "Relational boolean and."
  [a b o]
  (conde
    [(== a true) (== b true) (== o true)]
    [(== a false) (== b false) (== o false)]
    [(== a true) (== b false) (== o false)]
    [(== a false) (== b true) (== o false)]))

(defn noto
  "Relational boolean not."
  [a o]
  (conde
    [(== a true) (== o false)]
    [(== a false) (== o true)]))

(defn evalo-1
  "Basic relational evaluator for boolean expressions."
  [e v]
  (conde
    [(== e false) (== v false)]
    [(== e true) (== v true)]
    [(fresh [a ea]
       (conso `not [a] e)
       (evalo-1 a ea)
       (noto ea v))]
    [(fresh [a ea b eb]
       (conso `and [a b] e)
       (evalo-1 a ea)
       (evalo-1 b eb)
       (ando ea eb v))]
    [(fresh [a ea b eb]
       (conso `or [a b] e)
       (evalo-1 a ea)
       (evalo-1 b eb)
       (oro ea eb v))]
    [(fresh [a ea b eb]
       (conso `xor [a b] e)
       (evalo-1 a ea)
       (evalo-1 b eb)
       (xoro ea eb v))]))

(comment

  ;; Lets ask some simple questions in boolean logic...

  ;; What does the boolean values true, and false evaluate to?

  (run 1 [q] (evalo-1 true q)) ;; => (true)

  (run 1 [q] (evalo-1 false q)) ;; => (false)

  ;; Quelle suprise... How about the expression (not true)
  
  (run 1 [q] (evalo-1 `(not true) q)) ;; => (false)

  ;; Ok something harder, how about (not (and true true))

  (run 1 [q] (evalo-1 `(not (and true true)) q)) ;; => (false)

  ;; Let's try it backwards...  Give me ten programs that evaluate to
  ;; true...

  (run 10 [q] (evalo-1 q true))
  ;; => (true
  ;;    (clojure.core/not false)
  ;;    (clojure.core/not (clojure.core/not true))
  ;;    (clojure.core/and true true)
  ;;    (clojure.core/not (clojure.core/and false false))
  ;;    (clojure.core/or false true)
  ;;    (boolo.core/xor false true)
  ;;    (clojure.core/not (clojure.core/not (clojure.core/not false)))
  ;;    (clojure.core/not (clojure.core/and false true))
  ;;    (clojure.core/or true false))

  ;; Ok, given the application (and true ~something) what something
  ;; makes the expression true?

  (run 1 [q] (evalo-1 `(and true ~q) true)) ;; => (true)

  ;; Ok, given the application (~something (and true true)) what
  ;; something makes the expression true?
  
  (run 1 [q] (evalo-1 `(~q (and true true)) true)) ;; => ()

  ;; Doh, looks like there were no answers, because no single argument
  ;; boolean function in our interpreter will cause that expression to
  ;; evaluate to true...  Lets see when given the same
  ;; expression, (~something (and true true)), what
  ;; something makes the expression false?

  (run 1 [q] (evalo-1 `(~q (and true true)) false)) ;; => (clojure.core/not)
  
  ;; Magic!
  )


