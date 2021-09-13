package com.github.connorwood93.discordchatbot

import com.mongodb.client.MongoCollection
import net.dv8tion.jda.api.JDA
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection


class MessageDAO(dbName: String): MongoDAO {
    private val client = KMongo.createClient()
    private val database = client.getDatabase(dbName)
    private val col = database.getCollection<Message>()

    override fun insert(msg: Message) {
        col.insertOne(msg)
    }

    override fun deleteHistory() {
        col.drop()
    }

    override fun countDocs(): Long {
        return col.countDocuments()
    }

    override fun getHistory(jda: JDA) {
        val hist: HistoryRetrieval = HistoryRetrieval(jda, this)
    }

}