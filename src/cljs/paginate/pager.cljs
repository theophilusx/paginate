(ns paginate.pager
  (:require [reagent.session :as session]
            [goog.string :as gstring]))

(defn set-current-page! [pg]
  (session/assoc-in! [:pager :current-page] pg))

(defn get-current-page []
  (session/get-in [:pager :current-page]))

(defn set-current-index! [idx]
  (session/assoc-in! [:pager :current-index] idx))

(defn get-current-index []
  (session/get-in [:pager :current-index]))

(defn is-active [i]
  (= i (get-current-index)))

(defn idx-item [i]
  (let [pg (get-current-page)]
    [:li (when (is-active i) {:class "Active"})
     [:a {:on-click #(set-current-index! i)} (str i)]]))

(defn current-index-list []
  (get (session/get-in [:pager :index-data]) (get-current-page)))

(defn next-index-list []
  (get (session/get-in [:pager :index-data])
       (inc (get-current-page))))

(defn previous-index-list []
  (get (session/get-in [:pager :index-data])
       (dec (get-current-page))))

(defn has-a-next? []
  (let [current (get-current-index)
        current-page (get-current-page)
        current-list (current-index-list)
        next-list (next-index-list)]
    (cond
      (< current (last current-list)) [current-page (inc current)]
      (seq next-list) [(inc current-page) (first next-list)]
      :else nil)))

(defn has-a-previous? []
  (let [current (get-current-index)
        current-page (get-current-page)
        current-list (current-index-list)
        prev-list (previous-index-list)]
    (cond
      (> current (first current-list)) [current-page (dec current)]
      (seq prev-list) [(dec current-page) (last prev-list)]
      :else nil)))

(defn previous-page []
  (let [[pg idx] (has-a-previous?)]
    (if pg
      [:li
       [:a {:on-click (fn []
                        (set-current-page! pg)
                        (set-current-index! idx))}
        (str (gstring/unescapeEntities "&laquo;") " Prev")]]
      "")))

(defn next-page []
  (let [[pg idx] (has-a-next?)]
    (if pg
      [:li
       [:a {:on-click (fn []
                        (set-current-page! pg)
                        (set-current-index! idx))}
        (str "Next " (gstring/unescapeEntities "&raquo;"))]]
      "")))


(defn init-pager [page-size index-size col]
  (let [idx (apply merge (map-indexed
                          (fn [i v]
                            {(inc i) (vec v)})
                        (partition-all page-size col)))
        page-idx (apply merge (map-indexed
                               (fn [i v]
                                 {(inc i) (vec v)})
                               (partition-all index-size (sort (keys idx)))))
        pg-count (count (keys idx))]
    (session/assoc-in! [:pager :index-data] page-idx)
    (session/assoc-in! [:pager :page-count] pg-count)
    (set-current-page! 1)
    (set-current-index! 1)
    idx))

(defn header []
  (let [pg-idx (into [:ul.pagination (previous-page)]
                     (for [i (current-index-list)]
                       ^{:key (str "idx" i)} (idx-item i)))]
    [:div
     [:nav
      (conj pg-idx (next-page))]
     [:p (str "Page " (get-current-index) " of "
              (session/get-in [:pager :page-count]) " pages")]
     [:hr]]))

;; (defn header []
;;   [:nav
;;    (into [:ul.pagination]
;;          (for [i (current-index-list)]
;;            (idx-item i)))])
