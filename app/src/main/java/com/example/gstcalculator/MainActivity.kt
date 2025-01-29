package com.example.gstcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gstcalculator.ui.theme.GSTCalculatorTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GSTCalculatorTheme {
                GSTCalcPreview()
            }
        }
    }
}

class Item(var name: String = "" , var price: Double = 0.0, var quantity: Int = 0){

}


@RequiresApi(35)
@Composable
fun GSTCalcLayout(modifier: Modifier = Modifier.fillMaxSize()
) {
    var listItems by remember {mutableStateOf(mutableListOf<Item>())}

    Column (modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .background(color = Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Text(text = listItems.size.toString())
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.06f)
        ){
            Text(
                text = "GST Calculator",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            IconButton(
                onClick = {
                    val newItem = Item()
                    listItems = (listItems + newItem).toMutableList()
                },
                content = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null,)
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)
                .background(Color.Black)

        ){

            items(listItems){product ->

                var itemName by remember {mutableStateOf(product.name)}
                var itemPrice by remember {mutableStateOf(product.price)}
                var itemQty by remember {mutableStateOf(product.quantity)}

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            modifier = Modifier.fillMaxWidth(0.65f)
                                .weight(1f)

                        )
                        Text(text = totalPrice(product.price, product.quantity).toString())
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = itemPrice.toString(),
                            onValueChange = { itemPrice = it.toDoubleOrNull()?:0.0
                            }

                        )
                        TextField(
                            value = itemQty.toString(),
                            onValueChange = { itemQty = it.toIntOrNull()?:0 }

                        )
                    }

                }
            }
        }

    }
}





fun totalPrice(price:Double = 0.0, quantity:Int = 0):Double{
    return price * quantity
}

@RequiresApi(35)
@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun GSTCalcPreview() {
    GSTCalculatorTheme {
        GSTCalcLayout()
    }
}