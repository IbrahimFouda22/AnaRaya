package com.anaraya.anaraya.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anaraya.anaraya.R

@Composable
fun CustomButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF006CB0),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Text(text = stringResource(id = R.string.submit), color = Color.White)
    }
}