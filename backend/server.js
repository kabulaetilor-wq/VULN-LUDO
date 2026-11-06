const express = require("express");

const app = express();
app.use(express.json());

app.get("/", (_req, res) => {
  res.json({
    name: "VULN-LUDO LAB API",
    status: "online",
    note: "API de laboratoire. Les exercices de sécurité seront ajoutés étape par étape."
  });
});

const port = process.env.PORT || 3000;
app.listen(port, "0.0.0.0", () => {
  console.log(`VULN-LUDO API listening on ${port}`);
});
