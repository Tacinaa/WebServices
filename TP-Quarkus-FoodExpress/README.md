# 🍽️ FoodExpress – Projet Microservices (Quarkus)

## Présentation

Ce projet a été réalisé dans le cadre d’un TP de Master 2 en développement web.
L’objectif était de concevoir une architecture backend basée sur des **microservices REST** avec Quarkus, afin de mettre en pratique la conception d’API, la structuration du code et les bonnes pratiques de développement.

Plusieurs services ont déjà été développés, notamment ceux liés aux clients et aux restaurants.

---

## ⚙️ Technologies utilisées

* Java / Quarkus
* API REST (JAX-RS)
* Hibernate ORM
* Validation Jakarta
* Base de données relationnelle (mode développement)
* Tests d’API via fichiers HTTP

---

## ✨ Fonctionnalités principales

* Gestion d’entités métier via API REST
* Organisation en microservices indépendants
* Structuration backend (ressources, services, persistance)
* Tests d’API reproductibles
* Gestion de différents formats de réponse selon le contexte

---

## 🎯 Objectifs pédagogiques

Ce projet m’a permis de travailler sur :

* l’architecture microservices
* la conception d’API REST propres
* l’organisation d’un backend Java moderne
* la validation des données
* les tests d’API et la qualité du code

---

## 📎 Statut

Projet académique en cours d’évolution dans le cadre de la formation.

## ▶️ Lancement du projet

### Prérequis

* Java 17+
* Maven installé
* IDE recommandé : IntelliJ IDEA

### Étapes

1. Cloner le dépôt :

```bash
git clone <url-du-repo>
cd FoodExpress
```

2. Lancer chaque microservice en mode développement :

```bash
mvn quarkus:dev
```

3. Les API sont ensuite accessibles sur les ports configurés
   (exemple : http://localhost:8082 pour le restaurant-service).

### Tests des API

Des fichiers `.http` sont fournis pour tester les endpoints directement depuis IntelliJ ou tout client HTTP (Postman, Insomnia, etc.).
