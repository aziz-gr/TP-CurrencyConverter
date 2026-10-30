# Currency Converter — Application Android (Kotlin)

**Currency Converter** est une application Android développée en Kotlin permettant de convertir rapidement des montants entre différentes devises à partir de taux de change fixes par rapport à l'Euro (EUR) comme devise pivot.

---

## 📱 Fonctionnalités

- **Interface moderne & ergonomique :**
  - Zone d'affichage turquoise (`#2BB8E8`) occupant 45% de l'écran.
  - Clavier numérique sur fond clair occupant 55% de l'écran.
  - Textes d'affichage du montant et du résultat en grands caractères (40sp), alignés à droite et soulignés.
- **Sélection des devises :**
  - Listes déroulantes (`Spinner`) alimentées dynamiquement depuis `strings.xml`.
  - Devises supportées : **USD**, **EUR**, **TND**, **GBP**, **JPY**, **CAD**.
  - Sélections par défaut : **USD** en devise source, **EUR** en devise cible.
- **Clavier numérique sur-mesure (`TableLayout`) :**
  - Touches 0 à 9 et point décimal `.`.
  - Bouton **« C »** en rouge clair (`#F0727A`) pour effacer le montant et réinitialiser le résultat.
  - Contraintes de saisie :
    - Remplacement du `"0"` initial au premier chiffre tapé.
    - Saisie limitée à **10 caractères**.
    - Un seul point décimal autorisé.
- **Convertisseur & Logique de calcul :**
  - Calcul utilisant l'Euro (EUR) comme devise pivot :
    - $\text{Montant}_{\text{EUR}} = \frac{\text{Montant}_{\text{Source}}}{\text{Taux}_{\text{Source}}}$
    - $\text{Résultat} = \text{Montant}_{\text{EUR}} \times \text{Taux}_{\text{Cible}}$
  - Arrondi automatique à 2 décimales (`%.2f`).
  - Gestion des cas particuliers :
    - Alerte `Toast` (*"Veuillez saisir un montant"*) si le montant est égal à 0 lors du clic sur **Convertir**.
    - Identité des devises (Source = Cible $\rightarrow$ Résultat = Montant).
- **Bouton d'Inversion (`ImageButton`) :**
  - Échange instantané de la sélection des deux devises source et cible.
- **Calcul automatique en temps réel (Bonus) :**
  - Conversion fluide mise à jour dès la saisie d'un chiffre ou le changement de devise.
- **Conservation d'état :**
  - Préservation du montant et du résultat lors de la restauration d'instance (`onSaveInstanceState`).
- **Orientation :**
  - Écran verrouillé en mode portrait (`android:screenOrientation="portrait"`).

---

## 📊 Tableau des Taux de Change (Devise Pivot : EUR)

| Code Devise | Nom de la Devise | Taux pour 1 EUR |
| :--- | :--- | :--- |
| **EUR** | Euro | 1.00 |
| **USD** | Dollar américain | 1.17 |
| **TND** | Dinar tunisien | 3.40 |
| **GBP** | Livre sterling | 0.87 |
| **JPY** | Yen japonais | 172.50 |
| **CAD** | Dollar canadien | 1.62 |

---

## ✅ Cas de Tests de Validation

L'application passe avec succès l'ensemble des tests unitaires validant la précision des conversions :

| Montant | Source | Cible | Résultat Attendu | Statut du Test |
| :---: | :---: | :---: | :---: | :---: |
| `120` | USD | EUR | **102.56** | `PASSED` |
| `100` | EUR | TND | **340.00** | `PASSED` |
| `50` | GBP | USD | **67.24** | `PASSED` |
| `10` | EUR | EUR | **10.00** | `PASSED` |

---

## 🎨 Palette de Couleurs

| Élément | Couleur Hex | Aperçu |
| :--- | :--- | :--- |
| Zone d'affichage (Haut) | `#2BB8E8` | Turquoise vif |
| Chiffres du clavier | `#1BA6C9` | Bleu turquoise |
| Touche d'effacement « C » | `#F0727A` | Rouge clair |
| Textes d'affichage | `#FFFFFF` | Blanc |

---

## 🛠 Structure du Projet

```text
app/src/main/
├── java/com/example/myapplicationcurrencyconverter/
│   ├── MainActivity.kt        # Gestion de l'UI, clavier et événements
│   └── CurrencyConverter.kt   # Logique métier et formules de conversion
├── res/
│   ├── drawable/
│   │   └── ic_swap_vert.xml   # Icône vectorielle d'inversion des devises
│   ├── layout/
│   │   ├── activity_main.xml  # Disposition ConstraintLayout + TableLayout
│   │   └── spinner_item.xml   # Style personnalisé des éléments Spinner
│   └── values/
│       ├── colors.xml         # Définitions des couleurs hexadécimales
│       └── strings.xml        # Libellés et tableau R.array.devises
```

---

## 🚀 Exécution du Projet

1. Ouvrir le projet dans **Android Studio** (API Min 24, Kotlin).
2. Synchroniser les dépendances Gradle (`Gradle Sync`).
3. Lancer l'application sur un émulateur ou appareil connecté.
4. Lancer la suite de tests unitaires :
   ```bash
   ./gradlew testDebugUnitTest
   ```
