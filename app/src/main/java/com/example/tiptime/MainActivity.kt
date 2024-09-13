package com.example.tiptime

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0 // analyse en tant que double et renvoie, sinon null si c'est pas une représentation valide de nombre; operateur elvis ?: renvoie l'expression qui le precede si pas null et ce qui suit si null
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent)
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField( // pareil mais pour le choix du pourcentage
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.tip_amount, tip),// propriété tip pour afficher le montant du pourboire
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,//ref de ressource string.xml;
    keyboardOptions: KeyboardOptions, //transmettre le parametre en argument
    value: String,
    onValueChange: (String) -> Unit, // accepte valeur string et aucune valeur renvoyée; rappel lambda qui màj la valeur amountInput a partir de l'entrée utilisateur
    modifier: Modifier = Modifier
){
    /*var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull()  ?: 0.0// analyse en tant que double et renvoie, sinon null si c'est pas une représentation valide de nombre; operateur elvis ?: renvoie l'expression qui le precede si pas null et ce qui suit si null
    val tip = calculateTip(amount)*/
    // définies dans l'autre fonction pour récupérer l'état de EditNumberField
    TextField(
        value = value,
        onValueChange = onValueChange, // récupérer chiffre par chiffre, l'enregistre, l'attribue puis l'efface/recommence le process si un autre chiffre est entré
        label = { Text(stringResource(label))}, // accepte la valeur de la ressource bill_amount et affiche comme un placeholder
        singleLine = true,// une seule ligne à faire défiler
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */
private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
