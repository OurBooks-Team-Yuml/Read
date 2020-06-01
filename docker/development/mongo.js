db = db.getSiblingDB('readbooks')
db.createUser({user: "user", pwd: "secret", roles: [{role: "readWrite", db: "readbooks"}]})