(ns latte.rel
  "A **relation** between elements of
  a given type `T`, is formalized with type `(==> T T :type)`.

  The type `(==> T U :type)` for arbitrary types `T` and `U` gives
  the relations between elements of `T` and elements of `U`.

  With an extra property, this is also the type of **functional
  relations**, given by the type `(==> T U)`.

  This namespace provides some important properties about such
  relations."

  (:refer-clojure :exclude [and or not])
  (:require-macros [latte.core :as latte :refer [definition defaxiom defthm proof assume have]])
  (:require [latte.core-init :refer [forall ==>]]
            [latte.prop :as p :refer [and or not]]
            [latte.equal :as eq :refer [equal]]
            [latte.quant :as q :refer [exists]]))

(definition rel
  "The type of relations."
  [[T :type]]
  (==> T T :type))

(definition reflexive
  "A reflexive relation."
  [[T :type] [R (rel T)]]
  (forall [x T] (R x x)))

(definition symmetric
  "A symmetric relation."
  [[T :type] [R (rel T)]]
  (forall [x y T]
    (==> (R x y)
         (R y x))))

(definition transitive
  "A transitive relation."
  [[T :type] [R (rel T)]]
  (forall [x y z T]
    (==> (R x y)
         (R y z)
         (R x z))))

(definition equivalence
  "An equivalence relation."
  [[T :type] [R (rel T)]]
  (and (reflexive T R)
       (and (symmetric T R)
            (transitive T R))))

(definition injective
  "An injective function."
  [[T :type] [U :type] [F (==> T U)]]
  (forall [x y T]
    (==> (equal U (F x) (F y))
         (equal T x y))))

(definition surjective
  "A surjective function."
  [[T :type] [U :type] [F (==> T U)]]
  (forall [y U] (exists [x T] (equal U (F x) y))))

(definition bijective
  "A bijective function."
  [[T :type] [U :type] [F (==> T U)]]
  (and (injective T U F)
       (surjective T U F)))

(defthm bijective-is-surjective
  "A bijection is a surjection."
  [[T :type] [U :type] [F (==> T U)]]
  (==> (bijective T U F)
       (surjective T U F)))

(proof bijective-is-surjective :script
  (assume [H (bijective T U F)]
    (have a (surjective T U F) :by (p/%and-elim-right H))
    (qed a)))

(defthm bijective-is-injective
  "A bijection is an injection."
  [[T :type] [U :type] [F (==> T U)]]
  (==> (bijective T U F)
       (injective T U F)))

(proof bijective-is-injective :script
  (assume [H (bijective T U F)]
    (have a (injective T U F) :by (p/%and-elim-left H))
    (qed a)))

