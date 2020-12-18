Two test project in this repo:
- lein-shadow
- shadow-cljs

For lein-shadow, run two cmd in two terminals:

    lein run
    lein shadow watch home app

or

    clj -M:run
    lein shadow watch home app

Then visit: http://localhost:3030

In both methods, material UI works fine.


For shadow-cljs, run two cmd in two terminals:

    clj -M:run

    npm install
    shadow-cljs watch home app

Then visit: http://localhost:3030

If you click the RED menu item, the menu structure is in chaos!
