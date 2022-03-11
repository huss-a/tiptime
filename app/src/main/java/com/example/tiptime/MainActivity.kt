package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.tiptime.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var costInput: EditText
    private lateinit var roundupToggle: SwitchCompat
    private lateinit var tipAmountText: TextView

    private var tipAmount = 0.0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            costInput = costOfServiceInput
            roundupToggle = roundupSwitch
            tipAmountText = tipAmount

            calcButton.setOnClickListener {
                onCalcButtonClicked(it)
            }
        }


        roundupToggle.setOnCheckedChangeListener { _, isChecked: Boolean ->
            onRoundSwitchChanged(
                isChecked
            )
        }
    }

    private fun onCalcButtonClicked(v: View) {
        if (costInput.text.toString().trim().isEmpty()) {
            showSnackbar(v, "Please enter the cost of Service")
            return
        }

        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(v.windowToken, 0)

        val tipPercentage = when (binding.radiogroupService.checkedRadioButtonId) {
            R.id.radio_amazing -> 20
            R.id.radio_good -> 18
            else -> 15
        }

        val serviceCost = costInput.text.toString().toDouble()

        // Format to 2 decimal places
        val df = DecimalFormat("#.##")

        // Calculate the tip amount
        tipAmount = df.format((tipPercentage.toDouble() / 100) * serviceCost).toDouble()

        tipAmountText.text =
            ((if (roundupToggle.isChecked) round(tipAmount) else tipAmount)).toString()
        tipAmountText.visibility = View.VISIBLE
    }

    private fun onRoundSwitchChanged(isChecked: Boolean) {
        if (tipAmountText.text.toString().isNotEmpty()) {
            // Get the text from tipAmountText, convert it to a double, round it,
            // convert it to a String and set the text.

            tipAmountText.text =
                (if (isChecked) round(
                    tipAmountText.text.toString().toDouble()
                ) else tipAmount).toString()
        }
    }

    private fun showSnackbar(v: View, msg: String) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
    }
}