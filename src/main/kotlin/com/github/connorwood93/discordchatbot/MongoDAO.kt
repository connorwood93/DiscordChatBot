package com.github.connorwood93.discordchatbot

import com.mongodb.client.MongoCollection
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction

interface MongoDAO {


    fun insert(msg: Message)

    fun deleteHistory()

    fun countDocs(): Long

    fun getHistory(jda: JDA)

}