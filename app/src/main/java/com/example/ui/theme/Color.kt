package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Sleek Interface Indigo & Slate Theme
val Indigo900 = Color(0xFF312E81)
val Indigo800 = Color(0xFF3730A3)
val Indigo700 = Color(0xFF4338CA)
val Indigo600 = Color(0xFF4F46E5)
val Indigo500 = Color(0xFF6366F1)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo50 = Color(0xFFEEF2FF)

// Slate Neutrals
val Slate950 = Color(0xFF0B0F19)
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

// Vibrant Accent Colors
val Purple600 = Color(0xFF9333EA)
val Purple500 = Color(0xFFA855F7)
val Rose600 = Color(0xFFE11D48)
val Rose500 = Color(0xFFF43F5E)
val Pink500 = Color(0xFFEC4899)
val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Orange500 = Color(0xFFF97316)
val Emerald600 = Color(0xFF059669)
val Emerald500 = Color(0xFF10B981)
val Emerald50 = Color(0xFFECFDF5)
val Cyan500 = Color(0xFF06B6D4)

// Gradient Brushes
val SleekIndigoPurpleGradient = Brush.linearGradient(
    listOf(Indigo500, Purple600)
)

val SleekPinkRoseGradient = Brush.linearGradient(
    listOf(Pink500, Rose600)
)

val SleekAmberOrangeGradient = Brush.linearGradient(
    listOf(Amber400, Orange500)
)

val SleekEmeraldTealGradient = Brush.linearGradient(
    listOf(Emerald500, Color(0xFF0D9488))
)

val SleekHeaderGradient = Brush.verticalGradient(
    listOf(Indigo700, Indigo800)
)
