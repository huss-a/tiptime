package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.tiptime.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import kotlin.math.round

const val KEY_TIP_AMOUNT = "tip_amount"

class MainActivity : AppCompatActivity() {

    private lateinit var costInput: TextInputEditText
    private lateinit var roundupToggle: SwitchCompat
    private lateinit var tipAmountText: TextView

    private var tipAmount = 0.0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {

            costInput = costOfServiceInput
            roundupToggle = roundupSwitch
            tipAmountText = tipAmount

            calcButton.setOnClickListener {
                onCalcButtonClicked(it)
            }
        }

        setContentView(binding.root)

        if (savedInstanceState != null) {
            tipAmount = savedInstanceState.getDouble(KEY_TIP_AMOUNT)
            tipAmountText.text = DecimalFormat("#.##").format(tipAmount)
            tipAmountText.visibility = View.VISIBLE
        }


        roundupToggle.setOnCheckedChangeListener { _, isChecked: Boolean ->
            onRoundSwitchChanged(
                isChecked
            )
        }

        costInput.setOnKeyListener { v, keycode, _ -> handleKeyEvent(v, keycode) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble(KEY_TIP_AMOUNT, tipAmount)
        super.onSaveInstanceState(outState)
    }

    private fun onCalcButtonClicked(v: View) {
        if (costInput.text.toString().trim().isEmpty()) {
            showSnackbar(v, "Please enter the cost of Service")
            return
        }

        hideKeyboard(v)

        val tipPercentage = when (binding.radiogroupService.checkedRadioButtonId) {
            R.id.radio_amazing -> 20
            R.id.radio_good -> 18
            else -> 15
        }

        val serviceCost = costInput.text.toString().toDouble()

        setTipAmount(tipPercentage, serviceCost)
    }

    private fun setTipAmount(tipPercentage: Int, serviceCost: Double) {
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

    private fun handleKeyEvent(v: View, keycode: Int): Boolean {
        if (keycode == KeyEvent.KEYCODE_ENTER) {
            hideKeyboard(v)
            return true
        }
        return false
    }

    private fun hideKeyboard(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun showSnackbar(v: View, msg: String) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
    }
}