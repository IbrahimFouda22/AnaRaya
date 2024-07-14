package com.anaraya.anaraya.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldTitle(
    title: String,
    hintText: String,
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var query by remember { mutableStateOf(value) }
    val focusRequester = remember { FocusRequester() }
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = query,
            onValueChange = {
                query = it
                onValueChange(it)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            readOnly = false,
            placeholder = {
                Text(
                    text = hintText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xffA0A4AB)
                )
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .focusable()
                .fillMaxWidth()
                .border(
                    1.dp, Color(0xffC8CBD0), RoundedCornerShape(8.dp)
                )
        )
    }
}