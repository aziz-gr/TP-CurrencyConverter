package com.example.myapplicationcurrencyconverter

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplicationcurrencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Taux de change fixes déclarés dans MainActivity comme demandé par l'énoncé
    @Suppress("Unused")
    private val taux get() = CurrencyConverter.taux

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialisation du View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestion des Insets pour le mode Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Souligner le montant et le résultat comme indiqué sur la maquette
        binding.tvMontant.paintFlags = binding.tvMontant.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvResultat.paintFlags = binding.tvResultat.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Configuration des Spinners avec les devises de strings.xml
        configurerSpinners()

        // Configuration des clics sur le clavier numérique
        configurerClavier()

        // Listener pour le bouton Convertir
        binding.btnConvertir.setOnClickListener {
            effectuerConversion()
        }

        // Listener pour le bouton Inverser
        binding.btnInverser.setOnClickListener {
            inverserDevises()
        }

        // Restauration de l'état si sauvegarde existante
        savedInstanceState?.let { bundle ->
            binding.tvMontant.text = bundle.getString("montant", "0")
            binding.tvResultat.text = bundle.getString("resultat", "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("montant", binding.tvMontant.text.toString())
        outState.putString("resultat", binding.tvResultat.text.toString())
    }

    /**
     * Remplissage des spinners source et cible avec l'adaptateur ArrayAdapter.
     * Par défaut, la source est USD et la cible EUR.
     */
    private fun configurerSpinners() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.devises,
            R.layout.spinner_item,
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spDeviseSource.adapter = adapter
        binding.spDeviseCible.adapter = adapter

        val listDevises = resources.getStringArray(R.array.devises)
        val usdIndex = listDevises.indexOf("USD").takeIf { it >= 0 } ?: 0
        val eurIndex = listDevises.indexOf("EUR").takeIf { it >= 0 } ?: 0

        binding.spDeviseSource.setSelection(usdIndex)
        binding.spDeviseCible.setSelection(eurIndex)

        // Écouteurs de changement de sélection pour mise à jour automatique (bonus)
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (binding.tvResultat.text.isNotEmpty()) {
                    calculerEtAfficher()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spDeviseSource.onItemSelectedListener = spinnerListener
        binding.spDeviseCible.onItemSelectedListener = spinnerListener
    }

    /**
     * Association des boutons du clavier numérique à la méthode ajouterCaractere.
     */
    private fun configurerClavier() {
        binding.btn0.setOnClickListener { ajouterCaractere("0") }
        binding.btn1.setOnClickListener { ajouterCaractere("1") }
        binding.btn2.setOnClickListener { ajouterCaractere("2") }
        binding.btn3.setOnClickListener { ajouterCaractere("3") }
        binding.btn4.setOnClickListener { ajouterCaractere("4") }
        binding.btn5.setOnClickListener { ajouterCaractere("5") }
        binding.btn6.setOnClickListener { ajouterCaractere("6") }
        binding.btn7.setOnClickListener { ajouterCaractere("7") }
        binding.btn8.setOnClickListener { ajouterCaractere("8") }
        binding.btn9.setOnClickListener { ajouterCaractere("9") }
        binding.btnDot.setOnClickListener { ajouterCaractere(".") }

        // Touche « C » remet le montant à « 0 » et vide le résultat
        binding.btnC.setOnClickListener {
            effacerMontant()
        }
    }

    /**
     * Ajoute un caractère au montant saisi en respectant les contraintes :
     * - Remplace "0" initial par le premier chiffre tapé.
     * - Limite la longueur maximale du montant à 10 caractères.
     * - Empêche la présence de plus d'un point décimal.
     */
    private fun ajouterCaractere(c: String) {
        val courant = binding.tvMontant.text.toString()

        // Contrainte : Limité à 10 caractères
        if (courant.length >= 10) return

        // Contrainte : Un seul point décimal autorisé
        if (c == "." && courant.contains(".")) return

        val nouveauMontant = if (courant == "0") {
            if (c == ".") "0." else c
        } else {
            courant + c
        }

        binding.tvMontant.text = nouveauMontant

        // Calcul automatique si un résultat était déjà présent
        if (binding.tvResultat.text.isNotEmpty()) {
            calculerEtAfficher()
        }
    }

    /**
     * Remet le montant à "0" et réinitialise l'affichage du résultat.
     */
    private fun effacerMontant() {
        binding.tvMontant.text = "0"
        binding.tvResultat.text = ""
    }

    /**
     * Inverse les sélections des deux spinners et relance la conversion si un résultat est affiché.
     */
    private fun inverserDevises() {
        val posSource = binding.spDeviseSource.selectedItemPosition
        val posCible = binding.spDeviseCible.selectedItemPosition

        binding.spDeviseSource.setSelection(posCible)
        binding.spDeviseCible.setSelection(posSource)

        if (binding.tvResultat.text.isNotEmpty()) {
            calculerEtAfficher()
        }
    }

    /**
     * Effectue la conversion au clic sur le bouton Convertir.
     * Affiche un Toast si le montant est égal à 0.
     */
    private fun effectuerConversion() {
        val montantTxt = binding.tvMontant.text.toString()
        val montant = montantTxt.toDoubleOrNull() ?: 0.0

        // Si le montant vaut 0, afficher un Toast
        if (montant == 0.0) {
            binding.tvResultat.text = ""
            Toast.makeText(this, getString(R.string.toast_saisir_montant), Toast.LENGTH_SHORT).show()
            return
        }

        calculerEtAfficher()
    }

    /**
     * Calcule le résultat et l'affiche dans tvResultat avec un arrondi à 2 décimales.
     */
    private fun calculerEtAfficher() {
        val montantTxt = binding.tvMontant.text.toString()
        val montant = montantTxt.toDoubleOrNull() ?: 0.0

        if (montant == 0.0) {
            binding.tvResultat.text = ""
            return
        }

        val source = binding.spDeviseSource.selectedItem.toString()
        val cible = binding.spDeviseCible.selectedItem.toString()

        binding.tvResultat.text = CurrencyConverter.convertirEtFormater(montant, source, cible)
    }

    /**
     * Fonction de conversion demandée par l'énoncé du TP.
     */
    fun convertir(montant: Double, source: String, cible: String): Double {
        return CurrencyConverter.convertir(montant, source, cible)
    }
}