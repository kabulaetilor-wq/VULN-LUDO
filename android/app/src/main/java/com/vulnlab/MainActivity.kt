package com.vulnlab.ludo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF168447)
private val PaleGreen = Color(0xFFEAF6EE)
private val Ink = Color(0xFF17231B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF7FAF8)
                ) {
                    VulnLudoHome()
                }
            }
        }
    }
}

@Composable
private fun VulnLudoHome() {
    var selectedPlayer by remember { mutableStateOf("Leonard") }
    val balance = if (selectedPlayer == "Leonard") "10 000" else "5 000"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column {
            Text(
                text = "VULN-LUDO",
                color = Green,
                fontSize = 29.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Laboratoire Android de cybersécurité",
                color = Ink,
                fontSize = 14.sp
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PaleGreen)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("JOUEUR DE TEST", color = Green, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlayerButton(
                        label = "Leonard",
                        selected = selectedPlayer == "Leonard",
                        onClick = { selectedPlayer = "Leonard" },
                        modifier = Modifier.weight(1f)
                    )
                    PlayerButton(
                        label = "Patrick",
                        selected = selectedPlayer == "Patrick",
                        onClick = { selectedPlayer = "Patrick" },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text("Portefeuille fictif", color = Ink)
                Text(
                    text = "$balance pièces",
                    color = Green,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Compte actuellement affiché : $selectedPlayer",
                    color = Ink,
                    fontSize = 12.sp
                )
            }
        }

        Text("ESPACE DE TEST", color = Green, fontWeight = FontWeight.Bold)
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Jouer une partie (bientôt disponible)")
        }
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Historique (bientôt disponible)")
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Mode laboratoire : toutes les pièces et identités affichées sont fictives. " +
                "Cette première version fonctionne localement et n'est pas encore connectée au serveur.",
            color = Color(0xFF58665D),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PlayerButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) {
            Text(label)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(label)
        }
    }
}
