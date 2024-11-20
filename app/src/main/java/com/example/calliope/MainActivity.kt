package com.example.calliope

import ProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.calliope.ui.theme.CalliopeTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Chat

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.calliope.screens.ChatScreen
import com.example.calliope.screens.ContactDetailScreen
import com.example.calliope.screens.HomeScreen
import com.example.calliope.screens.ProfileDetailScreen
import com.example.calliope.screens.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalliopeTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { NavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "chat",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("chat") { ChatScreen(navController) }  // 传入导航控制器
            composable("profile") { ProfileScreen(navController) }
            composable("settings") { SettingsScreen() }

            // 添加联系人详情页面路由
            composable(
                route = "contact_detail/{contactId}",
                arguments = listOf(navArgument("contactId") { type = NavType.StringType })
            ) { backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")
                ContactDetailScreen(
                    navController = navController,
                    name = when(contactId) {
                        "alice" -> "Alice"
                        "bob" -> "Bob"
                        "charlie" -> "Charlie"
                        else -> "Unknown"
                    },
                    title = when(contactId) {
                        "alice" -> "同事，市场部主管"
                        else -> "Unknown"
                    },
                    summary = when(contactId) {
                        "alice" -> "高效能沟通者，需要注意压力管理"
                        "bob" -> "创意丰富，需要帮助提高执行力"
                        "charlie" -> "创新思维者，注重细节的决策者"
                        else -> "Unknown"
                    }
                )
            }

            composable(
                route = "profile_detail/{profileId}",
                arguments = listOf(navArgument("profileId") { type = NavType.StringType })
            ) { backStackEntry ->
                val profileId = backStackEntry.arguments?.getString("profileId")
                ProfileDetailScreen(
                    navController = navController,
                    title = when(profileId) {
                        "elite" -> "职场精英"
                        "student" -> "学生"
                        "expert" -> "情感专家"
                        else -> "未知人设"
                    },
                    description = when(profileId) {
                        "elite" -> "自信、专业、注重效率"
                        "student" -> "求知欲强，充满活力"
                        "expert" -> "富有同理心，善解人意"
                        else -> "Unknown"
                    },
                    traits = when(profileId) {
                        "elite" -> listOf("善于沟通", "目标导向", "时间管理高手")
                        // 可以添加其他人设的特征
                        else -> emptyList()
                    }
                )
            }
        }
    }
}
@Composable
fun NavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 检查当前路由是否属于chat分组
    val isChatGroup = currentRoute == "chat" || currentRoute?.startsWith("contact_detail") == true

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary, // Purple for selected icon
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Chat, contentDescription = "Chat") },
            selected = isChatGroup,
            onClick = {
                if (!isChatGroup) {
                    navController.navigate("chat") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray
            )
        )
    }
}
