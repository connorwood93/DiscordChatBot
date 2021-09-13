package com.github.connorwood93.discordchatbot

import com.mongodb.client.MongoCollection
import net.dv8tion.jda.api.JDA

class HistoryRetrieval(jda: JDA, mDAO: MessageDAO) {

    private val jdaObj: JDA = jda
    private val mongoHandler: MessageDAO = mDAO

    fun getHistory() {
        jdaObj.textChannels
            .forEach {
                it.iterableHistory.cache(false).takeWhileAsync (1) { true }.thenAccept { m ->
                    m.forEach { me ->
                        // write toInstant for OffsetDateTime
                        println(me.author)
                        println(me.timeCreated)
                        println(me.timeEdited)
                        println(me.contentRaw)
                        mongoHandler.insert(Message(me.author.toString(), me.timeCreated.toInstant(),
                            me.timeEdited?.toInstant(), me.contentRaw))
                    }
                }
            }
    }

}