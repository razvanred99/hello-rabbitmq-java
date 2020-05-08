package ro.razvan.hellorabbitmq

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback

object Consumer {
    private const val QUEUE_NAME = "hello"

    @JvmStatic
    fun main(args: Array<String>) {
        val factory = ConnectionFactory().apply {
            host = "localhost"
        }
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println(" [*} Waiting for messages. To exit press CTRL+C")

        val deliverCallback = DeliverCallback { _, delivery ->
            val message = String(delivery.body, Charsets.UTF_8)
            println(" [x] Received '$message'")
        }

        channel.basicConsume(QUEUE_NAME, deliverCallback, CancelCallback { })
    }
}