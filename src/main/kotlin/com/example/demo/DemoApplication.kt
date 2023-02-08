package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.util.*

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Table("messages")
data class Message(@Id var id: String?, val text: String)

interface MessageRepository : CrudRepository<Message, String>

@Service
@Transactional
class MessageService(val db: MessageRepository) {
    fun findMessages(): List<Message> = db.findAll().toList()

    fun findMessageById(id: String): List<Message> = db.findById(id).toList()

    fun somemethod(messages: List<Message>) {
        messages.map { db.save(it) }
    }

    fun common(messages: List<Message>) {
        somemethod(messages)
        somemethod(messages)
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun batch(messages: List<Message>) {
        common(messages)
        somemethod(messages)
    }

    fun save(message: Message) {
//        if (message.text == "hello") {
//            throw Exception("Restricted text")
//        }
        db.save(message)
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveList(messages: List<Message>) {
        messages.map {
            try {
                save(it)
            } catch (e: Exception) {
                println("Save error: ${e.message}")
            }
        }
    }

    fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}