package com.anaraya.anaraya.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anaraya.anaraya.screens.more.surveys.details.SurveyChoicesUiState

@Composable
fun ItemMultipleChoice(
    title: String,
    list: List<SurveyChoicesUiState>,
    selected: Int,
    onClickItem: (String,Int) -> Unit,
) {
    var selectedPosition by remember { mutableIntStateOf(selected) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        list.forEachIndexed { index, item ->
            Item(
                text = item.choice,
                isSelected = selectedPosition == index,
                onClickItem = {
                    selectedPosition = index
                    onClickItem(item.choiceId,selectedPosition)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Item(text: String, isSelected: Boolean, onClickItem: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xffC8CBD0), RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xffF4F4F4) else Color.White)
            .clickable {
                onClickItem()
            }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            color = if (isSelected) Color(0xff006CB0) else Color.Black
        )
    }
}