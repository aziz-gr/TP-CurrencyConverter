package com.example.myapplicationcurrencyconverter

import java.util.Locale

/**
 * Utilitaire de conversion de devises avec taux fixes par rapport à l'Euro (EUR).
 */
object CurrencyConverter {

    // Taux de change fixes exprimés pour 1 EUR
    val taux = mapOf(
        "EUR" to 1.0,
        "USD" to 1.17,
        "TND" to 3.40,
        "GBP" to 0.87,
        "JPY" to 172.50,
        "CAD" to 1.62,
    )

    /**
     * Effectue la conversion de la devise source vers la devise cible en passant par l'Euro.
     */
    fun convertir(montant: Double, source: String, cible: String): Double {
        if (source == cible) return montant
        val tauxSource = taux[source] ?: 1.0
        val tauxCible = taux[cible] ?: 1.0

        val montantEUR = montant / tauxSource
        return montantEUR * tauxCible
    }

    /**
     * Effectue la conversion et retourne la chaîne formatée avec 2 décimales.
     */
    fun convertirEtFormater(montant: Double, source: String, cible: String): String {
        val resultat = convertir(montant, source, cible)
        return String.format(Locale.US, "%.2f", resultat)
    }
}