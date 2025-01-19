package com.example.budgetmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetTrackerApp()
        }
    }
}

@Composable
fun BudgetTrackerApp() {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var totalAmount by remember { mutableStateOf(0.0) }
    val expenseList = remember { mutableStateListOf<Triple<String, Double, Boolean>>() }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Budget Tracker",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = expenseName,
                    onValueChange = { expenseName = it },
                    label = { Text("Nazwa transakcji") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = expenseAmount,
                    onValueChange = { expenseAmount = it },
                    label = { Text("Kwota transakcji") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Przychód", style = MaterialTheme.typography.bodyLarge)
                    Switch(checked = isIncome, onCheckedChange = { isIncome = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val amount = expenseAmount.toDoubleOrNull()
                        if (!expenseName.isBlank() && amount != null) {
                            expenseList.add(Triple(expenseName, amount, isIncome))
                            totalAmount += if (isIncome) amount else -amount
                            expenseName = ""
                            expenseAmount = ""
                            isIncome = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dodaj transakcje")
                }

                Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Suma: ${"%.2f".format(totalAmount)} ZŁ",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)

                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(expenseList.size) { index ->
                        val expense = expenseList[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = expense.first, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = if (expense.third) "Przychód: ${"%.2f".format(expense.second)} ZŁ"
                                    else "Wydatek: ${"%.2f".format(expense.second)} ZŁ",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Button(onClick = {
                                totalAmount -= if (expense.third) expense.second else -expense.second
                                expenseList.removeAt(index)
                            }) {
                                Text("Usuń")
                            }
                        }
                    }
                }
            }
        }
    }
}
}
