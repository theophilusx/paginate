(ns paginate.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [paginate.pager :as pager]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

;; -------------------------
;; Views

(defonce data (apply merge (map-indexed
                        (fn [i v] {i (vec v)})
                        (partition 10 10 nil
                                   (map #(str "Record " %)
                                        (range 100))))))

(defn page-header [title]
  [:div.row
   [:div.page-header
    [:h1 title]]])

(defn show-page [p]
  [:div
   (into [:ul]
         (for [i (get data p)]
           [:li (str i)]))])

(defn home-page []
  [:div.container
   [page-header "Home Page"]
   [:div.row
    [:div.col-md-12
     [:a {:href "#/about"} "go to about page"]
     [pager/header (sort (keys data))]
     (when (pager/get-pager-current)
       [show-page (pager/get-pager-current)])]]])

(defn about-page []
  [:div.container
   [page-header "About Page"]
   [:div.row
    [:div.col-md-12
     [:a {:href "#/"} "go to home page"]]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
