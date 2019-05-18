(ns day8.part12)

(defn -main []
  (let [input (line-seq (java.io.BufferedReader. *in*))
        statements (for [line input]
                     (let [[_ register op arg cond-reg comp value] (or (re-matches #"(\S+) (\S+) (-?\d+) if (\S+) (\S+) (-?\d+)" line)
                                                                       (-> "Bad input: %s" (format line) Exception. throw))]
                      {:register register
                       :op (case op
                             "inc" +
                             "dec" -)
                       :arg (Long. arg)
                       :cond-reg cond-reg
                       :comp (case comp
                               "==" =
                               "!=" not=
                               "<=" <=
                               ">=" >=
                               "<" <
                               ">" >)
                       :value (Long. value)}))
        registers (into {}
                    (for [statement statements
                          register ((juxt :register :cond-reg) statement)]
                      [register 0]))
        run (fn [registers {:keys [register op arg cond-reg comp value]}]
              (if (comp (get registers cond-reg) value)
                (update registers register op arg)
                registers))
        registers-seq (reductions run registers statements)
        max-value #(apply max (vals %))]
    (println (max-value (last registers-seq)))
    (println (apply max (map max-value registers-seq)))))

; NB:
; `registers` must be initialized with zeros instead of just keeping it empty and adding it as we go.
; The reason is that if we have a program that only decreases values, and only uses one register
; in a conditional position, or its conditional evaluates to false, we will have a zero left. Zero
; should then be the largest value, but if it is not initialized and never written to we will not see
; it in `registers`.
;
; This interpretation of the problem may be questioned. It could be argued that the answer is always
; at least zero since you could argue that there is a register that the program does not use at all
; which will be zero. That would imply that there are infinite registers though, which is why I favor
; the interpretation above.
