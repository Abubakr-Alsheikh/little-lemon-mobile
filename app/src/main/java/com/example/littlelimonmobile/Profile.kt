package com.example.littlelimonmobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.littlelimonmobile.ui.theme.LittleLemonCharcoal
import com.example.littlelimonmobile.ui.theme.LittleLemonYellow
import com.example.littlelimonmobile.ui.theme.LittleLimonMobileTheme

@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val user = remember { userPreferences.getUser() }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Text(
            text = "Personal information",
            color = LittleLemonCharcoal,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        ProfileField(label = "First name", value = user.firstName)
        ProfileField(label = "Last name", value = user.lastName)
        ProfileField(label = "Email", value = user.email)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                userPreferences.clear()
                navController.navigate(Onboarding.route) {
                    // Drop Home and Profile so Back can't return to them after logout.
                    popUpTo(Home.route) { inclusive = true }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LittleLemonYellow,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Log out", fontSize = 18.sp)
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = LittleLemonCharcoal,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = value, color = LittleLemonCharcoal, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    LittleLimonMobileTheme {
        Profile(navController = rememberNavController())
    }
}
