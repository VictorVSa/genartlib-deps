(ns genartlib.plotter
  (:require
    [quil.core :as q]))

(defn ^:private get-closest-curve
  [[x y] curves]
  (loop [curves curves
         closest-curve nil
         reverse-closest? false ; track whether we're close to the start or end of the closest curve
         previous-closest-dist nil]

    (if (empty? curves)
      [closest-curve reverse-closest?]

      (let [next-curve (first curves)
            [start-x start-y] (first next-curve)
            [end-x end-y] (last next-curve)
            start-dist (q/dist start-x start-y x y)
            end-dist (q/dist end-x end-y x y)
            closest-dist (min start-dist end-dist)]

        (if (or (nil? closest-curve) (< closest-dist previous-closest-dist))
          ; the start or end of the curve is closer than previous best
          (recur (rest curves) next-curve (< end-dist start-dist) start-dist)
          ; both ends of curve are farther away
          (recur (rest curves) closest-curve reverse-closest? previous-closest-dist))))))

(defn sort-curves-for-plotting
  "Takes a seq of curves and optimizes them for plotting. This involves removing
   any curves that have fewer than two points, as well as sorting all curves so
   that the distance between two consecutive curves is minimized."
  [curves]
  ; remove empty and single-point curves
  (let [curves (remove #(< (count %) 2) curves)]

    (loop [unsorted-curves (set (rest curves)) ; can't be transient, we need to iterate
           sorted-curves (transient [(first curves)])
           previous-point (-> curves first last)]

      (if (zero? (count unsorted-curves))
        (persistent! sorted-curves)

        (let [[curve should-reverse?] (get-closest-curve previous-point unsorted-curves)]
          (recur (disj unsorted-curves curve)
                 (conj! sorted-curves (if should-reverse? (reverse curve) curve))
                 (if should-reverse? (first curve) (last curve))))))))
