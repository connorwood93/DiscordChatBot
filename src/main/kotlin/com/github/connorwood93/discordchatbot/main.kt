package com.github.connorwood93.discordchatbot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.util.*

fun main() {
    val token = "ODExNDM3NjY3NTk0MTQxNzM2.YCyMNw.Oo7GeiwARiOoNrNNxraajiOUGK8"

    val jdaBuilder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES).apply {
        setMemberCachePolicy(MemberCachePolicy.ONLINE)
        addEventListeners(SimpleEventListener())
        setActivity(Activity.playing("echo"))
    }

    val jda: JDA = jdaBuilder.build()
    try {
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

    fun checkMessage() {

    }

    fun storeMessage(msg: String) {

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