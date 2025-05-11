package com.amrwalidi.android.fancup.service.impl

import com.amrwalidi.android.fancup.service.MatchService
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.model.MatchDoc
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MatchServiceImpl @Inject constructor() : MatchService {

    private val lobbyRef = FirebaseFirestore.getInstance().collection("lobby")
    private val matchRef = FirebaseFirestore.getInstance().collection("matches")
    private val questionService = QuestionServiceImpl()
    override suspend fun enterLobby(id: String) {
        lobbyRef.document(id).set(
            mapOf(
                "status" to "waiting",
                "timestamp" to FieldValue.serverTimestamp()
            )
        )
    }

    override suspend fun exitLobby(id: String) {
        lobbyRef.document(id).delete()
    }

    override suspend fun getAllPlayersInLobby(): Int {
        val waitingPlayers = lobbyRef.get().await()
        return waitingPlayers.documents.size
    }

    override suspend fun createMatch(id: String): Flow<Response> = callbackFlow {
        launch {
            val existingMatchSnapshot = try {
                matchRef.whereEqualTo("active", true).get().await()
            } catch (e: Exception) {
                trySend(Response.Failure(e))
                close()
                return@launch
            }

            val existingMatch = existingMatchSnapshot.documents.firstOrNull { doc ->
                val players = doc.get("players") as? List<Map<*, *>> ?: return@firstOrNull false
                players.any { it["id"] == id }
            }

            if (existingMatch != null) {
                val players = existingMatch.get("players") as? List<Map<*, *>>
                val updatedPlayers = players?.map {
                    if (it["id"] == id) {
                        mapOf("id" to it["id"], "ready" to true)
                    } else {
                        mapOf("id" to it["id"], "ready" to it["ready"])
                    }
                }

                val opponent = players?.firstOrNull { it["id"] != id }?.get("id") as? String ?: ""
                try {
                    matchRef.document(existingMatch.id).update("players", updatedPlayers).await()
                    trySend(Response.Success(listOf(opponent, existingMatch.id)))
                } catch (e: Exception) {
                    trySend(Response.Failure(e))
                }
                close()
            } else {
                val lobbySnapshot = try {
                    lobbyRef.whereEqualTo("status", "waiting").orderBy("timestamp").limit(10).get()
                        .await()
                } catch (e: Exception) {
                    trySend(Response.Failure(e))
                    close()
                    return@launch
                }

                val opponent = lobbySnapshot.documents.firstOrNull { it.id != id }

                if (opponent != null) {
                    val question = questionService.getRandomQuestion()
                    val match = mapOf(
                        "players" to listOf(
                            mapOf("id" to id, "ready" to true),
                            mapOf("id" to opponent.id, "ready" to false)
                        ),
                        "created_at" to FieldValue.serverTimestamp(),
                        "active" to true,
                        "question" to question,
                        "winner" to ""
                    )

                    try {
                        val matchDoc = matchRef.add(match).await()
                        lobbyRef.document(id).delete().await()
                        lobbyRef.document(opponent.id).delete().await()
                        trySend(Response.Success(listOf(opponent.id, matchDoc.id)))
                    } catch (e: Exception) {
                        trySend(Response.Failure(e))
                    }
                    close()
                } else {
                    trySend(Response.Failure(Exception("No opponent found.")))
                    close()
                }
            }
        }

        awaitClose()
    }

    override suspend fun getMatch(matchId: String): MatchDoc? {
        val doc = matchRef.document(matchId).get().await()
        return doc.toObject(MatchDoc::class.java)
    }


    override suspend fun playersReady(matchId: String): Boolean {
        val document = matchRef.document(matchId).get().await()
        val players = document.get("players") as? List<*>

        return players?.all {
            val playerMap = it as? Map<*, *>
            playerMap?.get("ready") == true
        } ?: false
    }

    override suspend fun getMatchQuestion(matchId: String): String {
        val match = matchRef.document(matchId).get().await()
        return match.getString("question") ?: ""
    }

    override suspend fun getWinner(matchId: String): String {
        val match = matchRef.document(matchId).get().await()
        return match.get("winner") as? String ?: ""
    }

    override suspend fun setWinner(winner: String, matchId: String) {
        matchRef.document(matchId).update("winner", winner).await()
    }

    override suspend fun endMatch(matchId: String) {
        matchRef.document(matchId).update("active", false).await()
    }
}