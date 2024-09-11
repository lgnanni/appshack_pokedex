package com.lgnanni.appshack.pokedex.ui.screens.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lgnanni.appshack.pokedex.R
import kotlinx.coroutines.delay

@Composable
fun SearchField(onChange: (String) -> Unit = {}) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        //Wait for a little while that the user stops writing
        delay(1000)
        // Notify parent when the text changes
        onChange(text)
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        singleLine = true,
        placeholder = { Text(stringResource(id = R.string.search)) },
        onValueChange = {
            text = it
        }
    )
}
