const express = require("express");

const app = express();

app.use(express.json());

// ======================================
// 1. ROUTE PUBLIQUE DE TEST DU SERVEUR
// ======================================

app.get("/", (req, res) => {
    res.json({
        name: "VULN-LUDO LAB API",
        status: "online",
        security: "IDOR protection enabled"
    });
});

// ======================================
// 2. UTILISATEURS FICTIFS
// ======================================

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

// ======================================
// 3. PORTEFEUILLES FICTIFS
// ======================================

const wallets = {
    "101": {
        balance: 10000
    },

    "102": {
        balance: 5000
    }
};

// ======================================
// 4. AUTHENTIFICATION
// ======================================

app.use((req, res, next) => {

    const token = req.headers.authorization;

    const user = users[token];

    if (!user) {
        return res.status(401).json({
            error: "Non autorisé",
            message: "Jeton invalide ou absent"
        });
    }

    req.user = user;

    next();
});

// ======================================
// 5. ROUTE PROFIL UTILISATEUR
// ======================================

app.get("/me", (req, res) => {

    res.json({
        id: req.user.id,
        name: req.user.name
    });

});

// ======================================
// 6. ROUTE PORTEFEUILLE SÉCURISÉE
// ======================================

app.get("/wallet/:userId", (req, res) => {

    // Vérification de propriété du portefeuille
    if (req.params.userId !== req.user.id) {

        return res.status(403).json({
            error: "Accès interdit",
            message: "Vous ne pouvez pas consulter le portefeuille d'un autre utilisateur"
        });

    }

    const wallet = wallets[req.user.id];

    if (!wallet) {

        return res.status(404).json({
            error: "Portefeuille introuvable"
        });

    }

    res.json({
        userId: req.user.id,
        balance: wallet.balance
    });

});

// ======================================
// 7. ROUTES INCONNUES
// ======================================

app.use((req, res) => {

    res.status(404).json({
        error: "Route introuvable"
    });

});

// ======================================
// 8. DÉMARRAGE DU SERVEUR
// ======================================

const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {

    console.log(
        "VULN-LUDO API démarrée sur le port " + PORT
    );

});
