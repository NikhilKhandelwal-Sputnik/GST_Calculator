package com.example.gstcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gstcalculator.ui.theme.GSTCalculatorTheme
import java.util.Locale

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

class Item(var name: String = "" , var price: String = "", var quantity: String = ""){

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
        ,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.06f)
                .background(color = Color(0xFF2196F3))
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
                .weight(1f)
//                .background(Color.White)

        ){

            items(listItems){product ->

                var itemName by remember {mutableStateOf(product.name)}
                var itemPrice by remember {mutableStateOf(product.price)}
                var itemQty by remember {mutableStateOf(product.quantity)}
                val itemTotal = itemTotalPrice(itemPrice, itemQty)

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
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .weight(1f)

                        )
                        Text(
                            text = itemTotal,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(0.3f))
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
                            onValueChange = { itemPrice = it
                                product.price = itemPrice
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                                )

                        )
                        TextField(
                            value = itemQty.toString(),
                            onValueChange = { itemQty = it
                                            product.quantity = itemQty
                                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            )
                        )
                    }

                }
            }
        }

        Column {
            totalRow(listParam = listItems, rowType = "items", datField = "${listItems.size}")
            totalRow(listParam = listItems, rowType = "price", datField = "${listTotalPrice(listItems)}")

            totalRow(listParam = listItems, rowType = "GST", datField = "${totalGST()}", fSize = 32)
        }

    }
}


@Composable
fun totalRow(listParam: MutableList<Item>,  rowType: String, datField:String, fSize: Int = 24){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp, 8.dp)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Text(
            text = "Total ${rowType.replaceFirstChar{it.titlecase(Locale.getDefault())}}",
            fontSize = fSize.sp,
            modifier = Modifier.border(width = 1.dp, color = Color.Black)
                .fillMaxWidth(0.6f)
                .padding(horizontal = 4.dp)
        )

        Text(
            text = datField,
            fontSize = fSize.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.border(width = 1.dp, color = Color.Black)
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
    }
}


fun itemTotalPrice(price:String, quantity:String ):String{
    return ((price.toDoubleOrNull()?:0.0) * (quantity.toIntOrNull()?:0)).toString()
}

fun listTotalPrice(productList : MutableList<Item>) : Double{
    var sum = 0.0
    for(i in productList){
        sum += i.price.toDoubleOrNull()?:0.0
    }
    return sum
}

fun totalGST(): Double{
    return 0.0
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