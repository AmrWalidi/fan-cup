package com.amrwalidi.android.fancup.repository

import com.amrwalidi.android.fancup.database.FanCupDatabase
import com.amrwalidi.android.fancup.domain.Match
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.service.Response
import com.amrwalidi.android.fancup.service.impl.MatchServiceImpl
import com.amrwalidi.android.fancup.service.model.asMatchDomain


class MatchRepository(database: FanCupDatabase) {
    private val matchService = MatchServiceImpl()
    private val userRepo = UserRepository(database)


    suspend fun enterLobby(id: String) {
        matchService.enterLobby(id)
    }

    suspend fun exitLobby(id: String) {
        matchService.exitLobby(id)
    }

    suspend fun getAllPlayersInLobby(): Int {
        return matchService.getAllPlayersInLobby()
    }

    suspend fun searchForPlayer(currentUserId: String): Pair<User?, String> {
        var opponent: User? = null
        var matchId = ""

        matchService.createMatch(currentUserId).collect { res ->
            if (res is Response.Success && res.data is List<*>) {
                val opponentId = res.data[0].toString()
                matchId = res.data[1].toString()
                opponent = userRepo.getUserById(opponentId)
            }
        }
        return Pair(opponent, matchId)

    }

    suspend fun playersReady(matchId: String): Boolean {
        return matchService.playersReady(matchId)
    }

    suspend fun getMatch(matchId: String): Match? {
        return matchService.getMatch(matchId).asMatchDomain()
    }

    suspend fun getMatchQuestion(matchId: String): String {
        return matchService.getMatchQuestion(matchId)
    }

    suspend fun getWinner(matchId: String): String {
        return matchService.getWinner(matchId)
    }

    suspend fun setWinner(winner: String, matchId: String) {
        matchService.setWinner(winner, matchId)
    }

    suspend fun endMatch(matchId: String) {
        matchService.endMatch(matchId)
    }

}