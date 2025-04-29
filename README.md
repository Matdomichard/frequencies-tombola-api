# Frequencies Tombola API

## 🚀 Configuration via Variables d'Environnement

### Base de données (PostgreSQL)

| Variable | Description | Exemple |
|:--|:--|:--|
| DATABASE_URL | URL JDBC PostgreSQL | `jdbc:postgresql://localhost:5432/tombola` |
| DATABASE_USERNAME | Utilisateur PostgreSQL | `tombola_user` |
| DATABASE_PASSWORD | Mot de passe PostgreSQL | `secret` |

### HelloAsso (API de paiements)

| Variable | Description | Exemple |
|:--|:--|:--|
| HELLOASSO_CLIENT_ID | ID client fourni par HelloAsso | `xxxxx` |
| HELLOASSO_CLIENT_SECRET | Secret client fourni par HelloAsso | `xxxxx` |
| HELLOASSO_API_URL | (Optionnel) URL API HelloAsso (sandbox ou production) | `https://api.helloasso-sandbox.com` |
| HELLOASSO_ORGANIZATION_SLUG | Slug de votre association HelloAsso | `mon-association` |

### 🌍 Déploiement

- Azure, Heroku : il suffit de définir ces variables dans l'interface de déploiement.
- Aucun changement de code requis.
