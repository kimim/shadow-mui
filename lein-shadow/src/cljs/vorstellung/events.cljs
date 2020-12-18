(ns vorstellung.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [ajax.edn :refer [edn-request-format edn-response-format]]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]))

;;dispatchers
(rf/reg-event-db
  :common/navigate
  (fn [db [_ match]]
    (let [old-match (:common/route db)
          new-match (assoc match :controllers
                                 (rfc/apply-controllers (:controllers old-match) match))]
      (assoc db :common/route new-match))))

(rf/reg-fx
  :common/navigate-fx!
  (fn [[k & [params query]]]
    (rfe/push-state k params query)))

(rf/reg-event-fx
  :common/navigate!
  (fn [_ [_ url-key params query]]
    {:common/navigate-fx! [url-key params query]}))

(rf/reg-event-db
 :common/set-navbar-visible?
 (fn [db [_ status]]
   (assoc db :common/navbar-visible? status)))

(rf/reg-sub
 :common/navbar-visible?
 (fn [db _]
   (:common/navbar-visible? db)))

(rf/reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(rf/reg-event-fx
  :fetch-docs
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/docs"
                  :response-format (ajax/raw-response-format)
                  :on-success       [:set-docs]}}))

(rf/reg-event-db
  :set-edn
  (fn [db [_ edn]]
    (assoc db :edn edn)))

(rf/reg-event-fx
  :fetch-edn
  (fn [_ _]
    {:http-xhrio {:method          :get
                  :uri             "/api/edn"
                  :response-format (edn-response-format)
                  :on-success       [:set-edn]
                  :on-failure       [:set-edn]}}))

(rf/reg-event-db
  :common/set-error
  (fn [db [_ error]]
    (assoc db :common/error error)))

(rf/reg-event-fx
  :page/init-home
  (fn [_ _]
    {:dispatch [:fetch-docs]}))

;;subscriptions

(rf/reg-sub
  :common/route
  (fn [db _]
    (-> db :common/route)))

(rf/reg-sub
  :common/page-id
  :<- [:common/route]
  (fn [route _]
    (-> route :data :name)))

(rf/reg-sub
  :common/page
  :<- [:common/route]
  (fn [route _]
    (-> route :data :view)))

(rf/reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(rf/reg-sub
  :common/error
  (fn [db _]
    (:common/error db)))

;; user signup and signin

(rf/reg-event-db
  :set-user
  (fn [db [_ field value]]
    (assoc-in db [:user field] value)))

(rf/reg-sub
  :user
  (fn [db _]
    (:user db)))

(rf/reg-event-db
  :signup-status
  (fn [db [_ status]]
    (assoc db :signup-status status)))

(rf/reg-sub
  :signup-status
  (fn [db [_]]
    (db :signup-status)))

(rf/reg-event-fx
  :signup
  (fn [_ [_ form-data]]
    {:http-xhrio {:method          :post
                  :uri             "/signup"
                  :params          form-data
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format)
                  :on-success      [:signup-status true]
                  :on-failure      [:signup-status false]}}))
