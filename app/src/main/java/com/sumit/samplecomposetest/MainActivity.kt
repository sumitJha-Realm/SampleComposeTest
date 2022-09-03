package com.sumit.samplecomposetest

import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.widget.TextViewCompat
import com.sumit.samplecomposetest.ui.theme.SampleComposeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            SampleComposeTestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MessageCard(Message(author = "Sumit", body = "Composable UI"))

                }
            }
        }
    }
}

@Composable
fun Conversation( messages : List<Message>){
    LazyColumn{
        items(messages) { message ->
              MessageCard(message)
        }
    }

}

data class Message (val author:String, val body:String )

@Composable
fun MessageCard(msg:Message){
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(painter = painterResource(id = R.drawable.profile_picture), contentDescription = "dummy icon"
            , modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2

            )
            Spacer(modifier = Modifier.width(3.dp))


            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)

            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }

        }

    }

}


//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
@Composable
fun AnotherGreeting(greeting: String){
    Text(text = "Another $greeting")
}
//@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Preview(showBackground = true)
@Composable
fun PreviewMassage() {
    SampleComposeTestTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            //MessageCard(Message(author = "Sumit", body = "Composable UI"))
Conversation(SampleData.conversationSample)
        }
    }
}