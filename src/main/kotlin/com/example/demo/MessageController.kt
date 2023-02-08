package com.example.demo

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(val service: MessageService) {
    @GetMapping("/")
    fun index() : List<Message> = service.findMessages()

    @GetMapping("/{id}")
    fun index(@PathVariable id: String): List<Message> =
        service.findMessageById(id)

    @PostMapping("/")
    fun post(@RequestBody message: Message) {
        service.save(message)
    }

    @PostMapping("/batch")
//    @Transactional
    fun post(@RequestBody messages: List<Message>) {
        service.common(messages)
        service.batch(messages)
        service.common(messages)
    }


    @PostMapping("/save_list")
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun save(@RequestBody messages: List<Message>) {
        service.saveList(messages)
    }
}