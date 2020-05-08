package ro.razvan.hellorabbitmq

import com.rabbitmq.client.ConnectionFactory

object Producer {
    private const val QUEUE_NAME = "hello"
    private const val MESSAGE = "how are you?"

    @JvmStatic
    fun main(args: Array<String>) {
        val factory = ConnectionFactory().apply {
            host = "localhost"
        }
        factory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                channel.basicPublish("", QUEUE_NAME, null, MESSAGE.toByteArray())
            }
        }
        println(" [x] Sent '$MESSAGE'")
    }
}