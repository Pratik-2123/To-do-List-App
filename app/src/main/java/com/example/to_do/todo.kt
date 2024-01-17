package com.example.to_do

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Todo_class(
    var id : Int,
    var name : String,
    var isEditing : Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Todo_App() {
    var sItem by remember { mutableStateOf(listOf<Todo_class>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }


    Column {
        Row {
            Text(
                text = "TODO LIST",
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                textAlign = TextAlign.Center
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            items(sItem) {
                    item ->
                if(item.isEditing) {
                    Todo_editor(
                        item = item
                    ) { editedName ->
                        sItem = sItem.map { it.copy(isEditing = false) }
                        val editedItem = sItem.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                        }
                    }
                } else {
                    Todo_Item(item = item, onEditClick = {
                        // finding out which item we are editing and changing its value and the name
                        sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                    },
                        onDeleteClick = {
                            sItem = sItem - item
                        }
                    )
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp, end = 30.dp), // Assuming you want it within a full-screen layout
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .size(60.dp) // Adjust size as needed
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(16.dp) // Optional padding for visual spacing
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add item",
                tint = Color.White // Optional tint for contrast
            )
        }
    }


    if(showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        showDialog = false
                        itemName = ""
                    }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        if(itemName.isNotBlank()) {
                            val newItem = Todo_class(
                                id = sItem.size+1,
                                name = itemName
                            )
                            sItem = sItem + newItem
                            showDialog = false
                            itemName = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            },
            title = {
                Text(text = "List the Work to be done !!", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(10.dp))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Todo_editor(item: Todo_class, onEditComplete: (String) -> Unit) {
    var editedItemname by remember { mutableStateOf(item.name) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ){
        Column {
            OutlinedTextField(
                value = editedItemname,
                onValueChange = {editedItemname = it},
                modifier = Modifier
                    .padding(10.dp)
            )
            Button(onClick = {
                isEditing = false
                onEditComplete(editedItemname)
            },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Save")
            }
        }
    }
}



@Composable
fun Todo_Item(
    item : Todo_class,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit
) {
    val checkedState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                BorderStroke(
                    width = 2.dp,
                    MaterialTheme.colorScheme.primary,
                ), shape = RoundedCornerShape(20)
            )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
            Text(text = item.name, modifier = Modifier.padding(10.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                IconButton(onClick = onEditClick ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }
                IconButton(onClick = onDeleteClick ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    }
}