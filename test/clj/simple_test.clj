(ns simple-test
  (:use expectations))

(expect ArithmeticException (/ 1 0))

(expect 0 (+ 1 2 -3))

(expect 0 0)