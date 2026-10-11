package com.example.littlelimonmobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.littlelimonmobile.ui.theme.LittleLemonCharcoal
import com.example.littlelimonmobile.ui.theme.LittleLemonGreen
import com.example.littlelimonmobile.ui.theme.LittleLemonYellow
import com.example.littlelimonmobile.ui.theme.LittleLimonMobileTheme
import java.util.Locale

@Composable
fun Home(navController: NavHostController) {
    val context = LocalContext.current
    // remember the LiveData so recomposition doesn't start a new query each time
    val menuItemsLiveData = remember {
        AppDatabase.getInstance(context).menuItemDao().getAll()
    }
    val menuItems by menuItemsLiveData.observeAsState(emptyList())

    HomeContent(
        menuItems = menuItems,
        onProfileClick = { navController.navigate(Profile.route) }
    )
}

@Composable
private fun HomeContent(menuItems: List<MenuItemRoom>, onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Header(onProfileClick = onProfileClick)
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            HeroSection()
            Text(
                text = "ORDER FOR DELIVERY!",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
            )
            MenuItems(menuItems = menuItems)
        }
    }
}

// Header: logo in the center, profile picture on the right
@Composable
private fun Header(onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .clickable(onClick = onProfileClick)
        )
    }
}

@Composable
private fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LittleLemonGreen)
            .padding(16.dp)
    ) {
        Text(
            text = "Little Lemon",
            color = LittleLemonYellow,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Chicago",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "We are a family-owned Mediterranean restaurant, focused on " +
                        "traditional recipes served with a modern twist.",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.hero),
                contentDescription = "A tray of Little Lemon appetizers",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 130.dp, height = 140.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun MenuItems(menuItems: List<MenuItemRoom>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (menuItem in menuItems) {
            HorizontalDivider(color = Color.LightGray)
            MenuItem(menuItem = menuItem)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(menuItem: MenuItemRoom) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = menuItem.title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = menuItem.description,
                color = LittleLemonCharcoal,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatPrice(menuItem.price),
                color = LittleLemonCharcoal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        GlideImage(
            model = menuItem.image,
            contentDescription = menuItem.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

// The API sends prices as strings such as "10"; show them as "$10.00".
private fun formatPrice(price: String): String =
    price.toDoubleOrNull()?.let { String.format(Locale.US, "$%.2f", it) } ?: "$$price"

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    LittleLimonMobileTheme {
        HomeContent(
            menuItems = listOf(
                MenuItemRoom(
                    id = 1,
                    title = "Greek Salad",
                    description = "The famous greek salad of crispy lettuce, peppers, olives, our Chicago.",
                    price = "10",
                    image = "",
                    category = "starters"
                )
            ),
            onProfileClick = {}
        )
    }
}
