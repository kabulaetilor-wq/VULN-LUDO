package com.vulnlab.ludo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val API_BASE_URL = "https://vuln-ludo-api.onrender.com"

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
    var serverMessage by remember {
        mutableStateOf("Pas encore connecté au serveur.")
    }
    var isLoading by remember { mutableStateOf(false) }

    val token = if (selectedPlayer == "Leonard") {
        "leonard-token"
    } else {
        "patrick-token"
    }

    fun callApi(path: String) {
        isLoading = true
        serverMessage = "Connexion au serveur..."

        Thread {
            var connection: HttpURLConnection? = null
            val result = try {
                connection = (URL("$API_BASE_URL$path").openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 15000
                    readTimeout = 15000
                    setRequestProperty("Authorization", token)
                    setRequestProperty("Accept", "application/json")
                }

                val status = connection.responseCode
                val stream = if (status in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }
                val body = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                "HTTP $status\n$body"
            } catch (e: Exception) {
                "Erreur de connexion : ${e.message ?: "erreur inconnue"}"
            } finally {
                connection?.disconnect()
            }

            Handler(Looper.getMainLooper()).post {
                serverMessage = result
                isLoading = false
            }
        }.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                Text("UTILISATEUR DE TEST", color = Green, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlayerButton(
                        label = "Leonard",
                        selected = selectedPlayer == "Leonard",
                        onClick = {
                            selectedPlayer = "Leonard"
                            serverMessage = "Utilisateur Leonard sélectionné. Appuie sur « Charger mon profil »."
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PlayerButton(
                        label = "Patrick",
                        selected = selectedPlayer == "Patrick",
                        onClick = {
                            selectedPlayer = "Patrick"
                            serverMessage = "Utilisateur Patrick sélectionné. Appuie sur « Charger mon profil »."
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(14.dp))
                Text("Compte sélectionné : $selectedPlayer", color = Ink)
                Text(
                    text = "Solde : fourni par le serveur",
                    color = Green,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text("COMMUNICATION AVEC LE BACKEND", color = Green, fontWeight = FontWeight.Bold)

        Button(
            onClick = { callApi("/me") },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Charger mon profil")
        }

        OutlinedButton(
            onClick = {
                val walletId = if (selectedPlayer == "Leonard") "101" else "102"
                callApi("/wallet/$walletId")
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consulter mon portefeuille")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("RÉPONSE DU SERVEUR", color = Green, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (isLoading) "Requête en cours..." else serverMessage,
                    color = Ink,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Laboratoire uniquement : identités, jetons et soldes fictifs. " +
                "Ne jamais utiliser ces exemples avec de vrais comptes ou de l'argent.",
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
