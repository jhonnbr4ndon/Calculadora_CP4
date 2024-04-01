package br.com.fiap.calculadora_cp4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.calculadora_cp4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentExpression = ""
    private var lastOperator = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val buttons = listOf(
            binding.zero, binding.um, binding.dois, binding.tres, binding.quatro,
            binding.cinco, binding.seis, binding.sete, binding.oito, binding.nove
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                addToExpression(button.text.toString())
            }
        }

        binding.soma.setOnClickListener { handleOperator("+") }
        binding.subtracao.setOnClickListener { handleOperator("-") }
        binding.multiplicacao.setOnClickListener { handleOperator("*") }
        binding.divisao.setOnClickListener { handleOperator("/") }
        binding.porcentagem.setOnClickListener { handleOperator("%") }
        binding.inverter.setOnClickListener { invertSignal() }
        binding.apagar.setOnClickListener { clearExpression() }
        binding.igual.setOnClickListener { calculate() }
    }

    private fun addToExpression(value: String) {
        currentExpression += value
        updateCalculationText()
    }

    private fun handleOperator(operator: String) {
        if (currentExpression.isNotEmpty()) {
            lastOperator = operator
            currentExpression += operator
            updateCalculationText()
        }
    }

    private fun invertSignal() {
        if (currentExpression.isNotEmpty() && !currentExpression.contains('+') && !currentExpression.contains('-')) {
            currentExpression = "-$currentExpression"
            updateCalculationText()
        }
    }

    private fun clearExpression() {
        currentExpression = ""
        lastOperator = ""
        updateCalculationText()
    }

    private fun calculate() {
        if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
            val result = evaluateExpression()
            binding.resultado.text = result.toString()
        }
    }

    private fun evaluateExpression(): Double {
        val tokens = mutableListOf<String>()
        var numBuffer = StringBuilder()

        for (char in currentExpression) {
            if (char.isDigit() || char == '.') {
                numBuffer.append(char)
            } else {
                if (numBuffer.isNotEmpty()) {
                    tokens.add(numBuffer.toString())
                    numBuffer.clear()
                }
                tokens.add(char.toString())
            }
        }
        if (numBuffer.isNotEmpty()) {
            tokens.add(numBuffer.toString())
        }

        val stack = mutableListOf<Double>()
        var currentOperator = ""
        for (token in tokens) {
            if (token == "+" || token == "-" || token == "*" || token == "/" || token == "%") {
                currentOperator = token
            } else {
                val number = token.toDouble()
                when (currentOperator) {
                    "+" -> stack.add(number)
                    "-" -> stack.add(-number)
                    "*" -> stack[stack.lastIndex] = stack.last() * number
                    "/" -> stack[stack.lastIndex] = stack.last() / number
                    "%" -> stack[stack.lastIndex] = stack.last() % number
                    "" -> stack.add(number)
                }
            }
        }

        return stack.sum()
    }

    private fun updateCalculationText() {
        binding.calculo.text = currentExpression
    }
}
