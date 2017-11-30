# Evalo

Some experiments fooling around with relational interpretation, using
[clojure](http://clojure.org/) & [core.logic](https://github.com/clojure/core.logic).

## What is this?

An experimental relational interpreter for boolean expressions in a
subset of Clojure.

These experiments were inspired by the relational scheme interpreters
of William Byrd & Dan Friedman et al, and in particular the
outstanding work on barliman to perform program synthesis for a subset
of scheme.

If you're like me after seeing the barliman demonstrations recorded at
clojure conj you rushed to see how it was done, however there's a lot
of code with few comments and it's not always clear what you're
looking at.

Thankfully William Byrd has recorded many excellent video conference
sessions explaining relational programming, however the length and
quantity of videos are duanting to the beginner who just wants to get
a flavour for how it's done.

After a while I decided to set my sights low and dive in and implement
a simple relational interpreter for a subset of Clojure.  It's not
impressive, and probably wrong, but hopefully will provide some
guidance on getting started.

## So what can I do with it?

Evaluate and synthesise a tiny subset of Clojure code corresponding to
fixed arity variants of the functions/macros `and`, `not`, `or`,
`xor`, over the values `true` and `false`.

Let's have a go...

`$ git clone git@github.com:RickMoynihan/evalo.git`

`$ cd evalo`

`$ lein repl`

```clojure
  ;; Lets ask some simple questions in boolean logic...

  ;; What do the boolean values true, and false evaluate to?

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
```

