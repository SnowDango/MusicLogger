package com.snowdango.musiclogger.view.common


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.snowdango.musiclogger.R

@Composable
fun CustomAppBar(title: String? = null, backButton: Boolean = false, icon: ImageVector, onClick: () -> Unit) {
    val controller = rememberNavController()
    TopAppBar(
        title = {
            Text(
                text = title ?: ""
            )
        },
        backgroundColor = colorResource(R.color.backGround),
        contentColor = colorResource(R.color.menuColor),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth(),
        navigationIcon = {
            if (backButton) {
                IconButton(onClick = {
                    controller.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            }
        },
        actions = {
            IconButton(onClick = onClick) {
                Icon(icon, null)
            }
        }
    )
}