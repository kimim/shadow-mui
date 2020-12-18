
-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(email, first_name, last_name, password)
VALUES (:email, :first-name, :last-name, :password)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first-name, last_name = :last-name
WHERE id = :email

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE email = :email

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE email = :email
