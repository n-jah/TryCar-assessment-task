package com.example.trycar_assessment_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.trycar_assessment_task.presentation.navigation.NavGraph
import com.example.trycar_assessment_task.ui.theme.TryCarassessmenttaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TryCarassessmenttaskTheme {
                NavGraph()
            }
        }
    }
}
