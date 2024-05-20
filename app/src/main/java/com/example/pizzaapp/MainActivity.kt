package com.example.pizzaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaapp.ui.theme.PizzaAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzaAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val corutineScope = rememberCoroutineScope()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost (snackbarHostState) },
                    ) { innerPadding ->
                    OrdenPizzaPantalla(Modifier.padding(innerPadding),
                        snackbarHostState,
                        corutineScope
                    )
                }
            }
        }
    }

    @Composable
    fun OrdenPizzaPantalla(
        padding: Modifier = Modifier,
        snackbarHostState: SnackbarHostState,
        corutineScope: CoroutineScope
    ) {
        var SelectTamanio by remember { mutableStateOf("8-10 plgs")}
        var SelectIngredientes = remember { mutableStateListOf<String>() }
        var context = LocalContext.current

        var TamanioPrecios = mapOf(
            "8-10 plgs" to 5.00,
            "11 - 13 plgs" to 7.00,
            "18 plgs" to 9.99
        )

        val PrecioIngredientes = mapOf(
            "Piña" to 1.50,
            "Jamón" to 2.00,
            "Peperoni" to 2.50,
            "Hongos" to 1.70
        )

        val PrecioTotal = TamanioPrecios[SelectTamanio]!! + SelectIngredientes.sumOf { PrecioIngredientes[it] ?: 0.0 }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.pizza),
                contentDescription = "Pizza",
                modifier = Modifier.size(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text(text = "Tamaño", fontSize = 20.sp)
                    TamanioPrecios.keys.forEach { size ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (size == SelectTamanio),
                                    onClick = { SelectTamanio = size }
                                )
                            ){
                            RadioButton(
                                selected = (size == SelectTamanio),
                                onClick = {SelectTamanio = size}
                            )
                            Text(text= size)
                        }

                    }
                }
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Text(text = "Ingredientes", fontSize = 20.sp)
                    PrecioIngredientes.keys.forEach { Ingrediente ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = SelectIngredientes.contains(Ingrediente),
                                    onClick = {
                                        if (SelectIngredientes.contains(Ingrediente)) {
                                            SelectIngredientes.remove(Ingrediente)
                                        } else {
                                            SelectIngredientes.add(Ingrediente)
                                        }
                                    }
                                )
                        ){
                            Checkbox(
                                checked = SelectIngredientes.contains(Ingrediente),
                                onCheckedChange = {
                                    if (SelectIngredientes.contains(Ingrediente)){
                                        SelectIngredientes.remove(Ingrediente)
                                    }else{
                                        SelectIngredientes.add(Ingrediente)
                                    }
                                }
                            )
                            Text(text = Ingrediente)
                        }
                    }
                }
            }

            Text(text = "Total: $${"%.2f".format(PrecioTotal)}", fontSize = 24.sp)

            Button(onClick ={
                val ingredientes = if (SelectIngredientes.isEmpty()) "Sin ingredientes adicionales" else SelectIngredientes.joinToString(", ")
                val mensaje = "Pizza: $SelectTamanio, con $ingredientes. Total: $${"%.2f".format(PrecioTotal)}"
                corutineScope.launch {
                    snackbarHostState.showSnackbar(mensaje)
                }
            }) {
                Text(text = "Pagar")
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun OrdenPizzaPantallaPreview() {
        PizzaAppTheme {
            val snackbarHostState = remember { SnackbarHostState() }
            val corutineScope = rememberCoroutineScope()
            OrdenPizzaPantalla(
                snackbarHostState = snackbarHostState,
                corutineScope = corutineScope
            )
        }
    }
}
