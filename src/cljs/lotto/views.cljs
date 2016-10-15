(ns lotto.views
  (:require [lotto.utils :refer [state-viewer]]
            [re-frame.core :as re-frame]
            [lotto.cards :as cards]))

(.clear js/console)

(defn card-back []
  [:div.back
    [:div.card]])

(defn card-face [face]
  [:div.front
    [:div.card {:style {:font-size "240px"}}
     (get cards/mahjong face)]])

(defn oriented-card [card]
  [:div.flip-container
   {:class (if (not (cards/front? card)) "back-of-card")}
   [:div.flipper
    [card-back]
    [card-face (cards/face card)]]])

(defn flipper [row column]
  (let [card (re-frame/subscribe [:card-at row column])]
    [:button
     {:on-click (fn [] (re-frame/dispatch [:flip-up row column]))}
     [oriented-card @card]]))

(defn grid []
  (let [
        grid-size (re-frame/subscribe [:grid-size])
        height (get @grid-size :height)
        width (get @grid-size :width)]
    [:table
     [:tbody
      (for [row (range height)]
        [:tr
         (for [column (range width)]
           [:td [flipper row column]])])]]))

(defn main-panel []
  (let [current-player (re-frame/subscribe [:current-player])]
    [:div
     [:h2 "Current State" [state-viewer]]
     [:button {:on-click
               (fn [] (re-frame/dispatch
                       [:deal-cards (keys cards/mahjong) 4 5]))}
      "Deal it up"]
     [grid]]))
