(ns paginate.pager
  (:require [reagent.session :as session]))

(defn set-pager-current [i]
  (session/assoc-in! [:pager :current] i))

(defn get-pager-current []
  (session/get-in [:pager :current]))

(defn is-active [i]
  (= i (get-pager-current)))

(defn idx-item [i]
  [:li (when (is-active i) {:class "Active"})
   [:a {:on-click #(set-pager-current i)} (str i)]])

(defn maybe-previous [first-idx]
  (when (< first-idx (get-pager-current))
    (set-pager-current (dec (get-pager-current)))))

(defn maybe-next [last-idx]
  (when (> last-idx (get-pager-current))
    (set-pager-current (inc (get-pager-current)))))

(defn previous-page [first-idx]
  [:li (when (= first-idx (get-pager-current)) {:class "disabled"})
   [:a {:on-click #(maybe-previous first-idx)}
    [:span {:class "glyphicon glyphicon-menu-left"}]]])

(defn next-page [last-idx]
  [:li (when (= last-idx (get-pager-current)) {:class "disabled"})
   [:a {:on-click #(maybe-next last-idx)}
    [:span {:class "glyphicon glyphicon-menu-right"}]]])

(defn header [idx]
  (let [pg-idx (into [:ul.pagination (previous-page (first idx))]
                     (for [i idx]
                       (idx-item i)))]
    [:nav
     (conj pg-idx (next-page (last idx)))]))
