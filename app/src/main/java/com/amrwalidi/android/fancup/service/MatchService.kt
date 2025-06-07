package com.amrwalidi.android.fancup.service

import com.amrwalidi.android.fancup.service.model.MatchDoc
import kotlinx.coroutines.flow.Flow

interface MatchService {
    suspend fun enterLobby(id: String)
    suspend fun exitLobby(id: String)
    suspend fun getAllPlayersInLobby(): Int
    suspend fun createMatch(id: String): Flow<Response>
    suspend fun createInvitedMatch(id: String, opponentId: String): Flow<Response>
    suspend fun getMatch(matchId: String): MatchDoc?
    suspend fun playersReady(matchId: String): Boolean
    suspend fun getMatchQuestion(matchId: String): String
    suspend fun getWinner(matchId: String): String
    suspend fun setWinner(winner: String, matchId: String)
    suspend fun endMatch(matchId: String)
}