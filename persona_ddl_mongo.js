db = db.getSiblingDB("persona_db"); // Switch to 'persona_db' database

// No need to authenticate as root if the root user is not required here
// If authentication is needed, ensure the root user has the necessary privileges

try {
  db.createUser({
    user: "persona_db",
    pwd: "persona_db",
    roles: [
      { role: "readWrite", db: "persona_db" },
      { role: "dbAdmin", db: "persona_db" }
    ]
  });
} catch (e) {
  if (e.codeName === "Location51003") {
    print("User persona_db already exists, skipping creation.");
  } else {
    throw e;
  }
}
