package ru.byksar.analclicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    var unselectedIcon: ImageVector
)



@Composable
fun TabView(
    tabItems: List<TabBarItem>,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination



    NavigationBar(
    ) {
        tabItems.forEach {TabBarItem ->
            val itemSelected = currentDestination?.hierarchy?.any { ti ->
                ti.route == TabBarItem.title
            } == true
            NavigationBarItem(
                selected = itemSelected,
                onClick = {
                    if (navController.currentDestination?.route != TabBarItem.title) {
                        navController.navigate(TabBarItem.title) {
                            launchSingleTop = true
                            restoreState = true

                        }
                    }
                },
                icon = { if (itemSelected) {TabBarIconViewSelected(TabBarItem)} else {TabBarIconViewUnselected(TabBarItem)} },
                label = { TextLabel(itemSelected, TabBarItem.title) }
            )

        }


    }
}

@Composable
fun TextLabel(isSelected: Boolean, title: String) {
    when(isSelected) {
        true -> Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
        false -> Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = FontFamily.SansSerif)
    }
}

@Composable
fun TabBarIconViewSelected(
    item: TabBarItem
) {
    Icon(
        imageVector = item.selectedIcon,
        contentDescription = item.title
    )
}

@Composable
fun TabBarIconViewUnselected(
    item: TabBarItem
) {
    Icon(
        imageVector = item.unselectedIcon,
        contentDescription = item.title
    )
}