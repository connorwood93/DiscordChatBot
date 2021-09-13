package com.github.connorwood93.discordchatbot

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageHistory
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.litote.kmongo.*
import java.io.File
import java.sql.Timestamp
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*
import java.util.stream.Collectors

data class Message(val author: String, val dateCreated: Instant, val dateEdited: Instant?,
                   val content: String)

fun main() {
    val token = File("bot.properties")
        .bufferedReader().use { it.readText() }.substring(6)

    val jdaBuilder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES).apply {
        setMemberCachePolicy(MemberCachePolicy.ONLINE)
        addEventListeners(SimpleEventListener())
        setActivity(Activity.playing("Grabbing History"))
    }

    val jda: JDA = jdaBuilder.build()
    val msgHandler: MessageDAO = MessageDAO("mongo_data")

    msgHandler.deleteHistory()
    println(msgHandler.countDocs())

    try {
        jda.awaitReady()

        if (msgHandler.countDocs() == 0L) {
            msgHandler.getHistory(jda)
            println(msgHandler.countDocs())
        }

        dontReturnUntilQuitInput()
    } finally {

        jda.shutdown()
    }
}

class SimpleEventListener: ListenerAdapter() {

    lateinit var jda: JDA

    override fun onReady(event: ReadyEvent) {
        jda = event.jda
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val msg = event.message.contentRaw.trim()
        if (msg.startsWith("echo ")) {
            event.channel.sendMessage(msg.substring(5)).queue()
        }
    }
}

fun dontReturnUntilQuitInput() {
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLine()) {
        val line = scanner.nextLine()
        if (line.equals("quit", ignoreCase = true)) {
            break
        }
    }
}

//fun getHistory(jda: JDA, col: MongoCollection<Message>) {
//        jda.textChannels
//        .forEach {
//        it.iterableHistory.cache(false).takeWhileAsync (1) { true }.thenAccept { m ->
//            m.forEach { me ->
//                // write toInstant for OffsetDateTime
//                println(me.author)
//                println(me.timeCreated)
//                println(me.timeEdited)
//                println(me.contentRaw)
//                col.insertOne(Message(me.author.toString(), me.timeCreated.toInstant(),
//                    me.timeEdited?.toInstant(), me.contentRaw))
//            }
//        }
//    }
//}