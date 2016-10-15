(ns lotto.handlers
    (:require [re-frame.core :as re-frame]
              [lotto.db :as db]
              [lotto.cards :as cards]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(defn deal-cards [card-names height width]
  (let [number-of-cards (* width height)
        number-of-pairs (/ number-of-cards 2)
        shuffled-card-names (shuffle card-names)
        chosen-card-names (take number-of-pairs shuffled-card-names)
        chosen-card-name-doubled (concat chosen-card-names chosen-card-names)
        shuffled-chosen-card-names (shuffle chosen-card-name-doubled)
        shuffled-cards (for [card-name shuffled-chosen-card-names]
                         (cards/card card-name :back))
        row-of-cards (partition width shuffled-cards)
        grid-of-cards (vec (for [row row-of-cards]
                             (vec row)))]
    grid-of-cards))

(re-frame/reg-event-db
 :deal-cards
 (fn [db [_ card-names height width]]
   (assoc db
          :cards (deal-cards card-names height width)
          :current-player :A)))


(re-frame/reg-event-db
 :flip-up
 (fn [db [_ row column]]
   (update-in db [:cards row column] cards/flip-up)))
