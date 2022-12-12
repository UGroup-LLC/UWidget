package com.kikoproject.uwidget.dialogs

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

enum class DialogPosition {
    BOTTOM, TOP
}

fun Modifier.dialogPos(pos: DialogPosition) = layout { measurable, constraints ->

    val placeable = measurable.measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight){
        when(pos) {
            DialogPosition.BOTTOM -> {
                placeable.place(0, constraints.maxHeight - placeable.height, 10f)
            }
            DialogPosition.TOP -> {
                placeable.place(0,0,10f)
            }
        }
    }
}