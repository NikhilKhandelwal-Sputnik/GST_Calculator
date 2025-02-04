package com.example.gstcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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

class Item(var name: String = "" , var price: String = "", var quantity: String = "", var totalPrice: String = "")


@RequiresApi(35)
@Composable
fun GSTCalcLayout() {
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
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .weight(1f)
//                .verticalScroll(state = ScrollState(0) ,enabled = true)
//                .background(Color.White)

        ){

            items(listItems){product ->

                ProductDisplay(product)

                Spacer(Modifier.height(2.dp))
            }
        }

        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                IconButton(
                    onClick = {
                        val tempItem = Item()
                        listItems = (listItems+ tempItem).toMutableList()
                        val i = listItems.lastIndex
                        listItems.removeAt(i)
                    },
                    modifier= Modifier.border(width = 1.dp, color = Color.Black)
                        .padding(horizontal = 4.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "OK",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp)
                    )
                }

            }
            TotalRow(listParam = listItems, rowType = "items", datField = "${listItems.size}")
            TotalRow(listParam = listItems, rowType = "price", datField = "${listTotalPrice(listItems)}")

            TotalRow(listParam = listItems, rowType = "GST", datField = "${totalGST()}", fSize = 32)
        }

    }
}


@Composable
fun TotalRow(listParam: MutableList<Item>,  rowType: String, datField:String, fSize: Int = 24){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Text(
            text = "Total ${rowType.replaceFirstChar{it.titlecase(Locale.getDefault())}}",
            fontSize = fSize.sp,
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black)
                .fillMaxWidth(0.6f)
                .padding(horizontal = 4.dp)
        )

        Text(
            text = datField,
            fontSize = fSize.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black)
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
    }
}

@Composable
fun ProductDisplay(product: Item){
    var itemName by remember {mutableStateOf(product.name)}
    var itemPrice by remember {mutableStateOf(product.price)}
    var itemQty by remember {mutableStateOf(product.quantity)}
    val itemTotal = itemTotalPrice(itemPrice, itemQty)
    product.totalPrice = itemTotal
    product.name = itemName
    product.price = itemPrice
    product.quantity = itemQty

    val txtFieldColor = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)


    Column (modifier = Modifier
        .background(color = Color.LightGray)
        .padding(8.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                colors = txtFieldColor,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .weight(1f)

            )

            Spacer(modifier = Modifier.fillMaxWidth(0.1f))

            Text(
                text = itemTotal,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(0.3f)
                    .background(color = Color.White)
                    .height(48.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = itemPrice,
                onValueChange = { itemPrice = it
                    product.price = itemPrice
                },
                colors = txtFieldColor,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
//                            modifier = Modifier.fillMaxWidth(0.4f)
//
            )

            Spacer(modifier = Modifier.fillMaxWidth(0.1f))

            TextField(
                value = itemQty,
                onValueChange = { itemQty = it
                    product.quantity = itemQty
                },
                colors = txtFieldColor,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
//                            modifier = Modifier.fillMaxWidth(0.4f)
            )
        }

    }
}


fun itemTotalPrice(price:String, quantity:String ):String{
    return ((price.toDoubleOrNull()?:0.0) * (quantity.toIntOrNull()?:0)).toString()
}

fun listTotalPrice(productList : MutableList<Item>) : Double{
    var sum = 0.0
    for(i in productList){
        sum += (i.totalPrice.toDoubleOrNull() ?: 0.0)
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