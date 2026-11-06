package com.vulnlab.ludo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Surface(Modifier.fillMaxSize(), color = Color(0xFFF7FAF8)) {
                    VulnLudoHome()
                }
            }
        }
    }
}

@Composable
private fun VulnLudoHome() {
    var selectedPlayer by remember { mutableStateOf("Leonard") }
    var serverMessage by remember { mutableStateOf("Pas encore connecté au serveur.") }
    var isLoading by remember { mutableStateOf(false) }

    val token = if (selectedPlayer == "Leonard") "leonard-token" else "patrick-token"

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
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
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
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("VULN-LUDO", color = Green, fontSize = 29.sp, fontWeight = FontWeight.Black)
            Text("Laboratoire Android de cybersécurité", color = Ink, fontSize = 14.sp)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PaleGreen)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("UTILISATEUR DE TEST", color = Green, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (selectedPlayer == "Leonard") {
                        Button(onClick = { selectedPlayer = "Leonard" }, modifier = Modifier.weight(1f)) {
                            Text("Leonard")
                        }
                    } else {
                        OutlinedButton(onClick = { selectedPlayer = "Leonard" }, modifier = Modifier.weight(1f)) {
                            Text("Leonard")
                        }
                    }
                    if (selectedPlayer == "Patrick") {
                        Button(onClick = { selectedPlayer = "Patrick" }, modifier = Modifier.weight(1f)) {
                            Text("Patrick")
                        }
                    } else {
                        OutlinedButton(onClick = { selectedPlayer = "Patrick" }, modifier = Modifier.weight(1f)) {
                            Text("Patrick")
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Compte sélectionné : $selectedPlayer", color = Ink)
                Text("Solde : fourni par le serveur", color = Green, fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
                // Test IDOR en laboratoire : demande le portefeuille fictif 102
                // même lorsque Leonard est authentifié.
                val walletId = "102"
                callApi("/wallet/$walletId")
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tester l'accès au portefeuille 102")
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
                    if (isLoading) "Requête en cours..." else serverMessage,
                    color = Ink,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Text(
            "Laboratoire uniquement : identités, jetons et soldes fictifs. " +
                "Ne jamais utiliser ces exemples avec de vrais comptes ou de l'argent.",
            color = Color(0xFF58665D),
            fontSize = 12.sp
        )
    }
}
