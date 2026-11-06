const express = require("express");

const app = express();

app.use(express.json());

// COMPTES FICTIFS
const users = {
    "leonard-token": {
        id: "101",
        name: "Leonard"
    },

    "patrick-token": {
        id: "102",
        name: "Patrick"
    }
};

// PORTEFEUILLES FICTIFS
const wallets = {
    "101": {
        balance: 10000
    },

    "102": {
        balance: 5000
    }
};

// AUTHENTIFICATION SIMPLIFIÉE
app.use((req, res, next) => {

    const token = req.headers.authorization;

    const user = users[token];

    if (!user) {
        return res.status(401).json({
            error: "Non autorisé"
        });
    }

    req.user = user;

    next();
});

// ROUTE DE TEST DU JOUEUR CONNECTÉ
app.get("/me", (req, res) => {

    res.json({
        id: req.user.id,
        name: req.user.name
    });

});

// ROUTE DU PORTEFEUILLE
app.get("/wallet/:userId", (req, res) => {

    const wallet = wallets[req.params.userId];

    if (!wallet) {
        return res.status(404).json({
            error: "Portefeuille introuvable"
        });
    }

    res.json({
        userId: req.params.userId,
        balance: wallet.balance
    });

});

const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {
    console.log("VULN-LUDO API démarrée sur le port " + PORT);
});
