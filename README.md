# ANDROID-VULN-LAB — VULN-LUDO

Projet éducatif pour apprendre la sécurité Android et API dans un environnement personnel autorisé.

## Première version

- Application Android Kotlin + Jetpack Compose.
- Deux profils de démonstration : Leonard et Patrick.
- Soldes fictifs affichés localement.
- Backend Express minimal, prêt pour un déploiement Render Free ultérieur.
- Aucune connexion réelle, aucun paiement et aucune donnée personnelle.

## Construire l'application

Le projet utilise Gradle, Android Gradle Plugin 8.7.3, Kotlin 2.0.21 et compileSdk 35.
Ouvrir le dépôt dans Android Studio, puis lancer la tâche `:android:app:assembleDebug`.

L'APK de débogage sera généré dans :
`android/app/build/outputs/apk/debug/app-debug.apk`

## Important

Cette version est une maquette locale. Les profils et soldes sont codés dans l'interface : ils ne constituent pas une authentification ni un portefeuille réel.

Les exercices IDOR seront ajoutés ensuite au backend de laboratoire, avec des comptes et des données fictifs, puis corrigés.
