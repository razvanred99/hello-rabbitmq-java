# RabbitMQ Introduction

RabbitMQ is a message broker: it accepts and forwards messages.

RabbitMQ, and messaging in general, uses some jargon:

* **Producing** means nothing more than sending. A program that sends messages is a _producer_.
* A **queue** is the name for a post box which lives inside RabbitMQ. Although messages flow through RabbitMQ and your
applications, they can only be stored inside a _queue_. A queue is only bound by the host's memory & disk limits.
It's essentially a large message buffer. Many producers can send messages that go to one queue, and many consumers can
try to receive data from one queue.
* **Consuming** has a similar meaning to receiving. A consumer is a program that mostly waits to receive messages.

Note that producer, consumer and broker do not have to reside on the same host; indeed in most applications they don't.
An application can be both a producer and a consumer, too.

## Hello World

We'll write two programs in Java; a producer that sends a single message, and a consumer that receives messages and
prints them out.

### Sending

Set up the class and name the queue:

```kotlin
object Publisher {
    private const val QUEUE_NAME = "hello"    

    @JvmStatic
    fun main(args: Array<String>) { }
}
```

then we can create a connection to the server:

```kotlin
val factory = ConnectionFactory().apply {
    host = "localhost"
}
factory.newConnection().use { connection ->
    connection.createChannel().use { channel ->
        
    }
}
```

The connection abstracts the socket connection, and takes care of protocol version negotiation and authentication and so
on for us. Here we connect to a broker on the local machine - hence the ```localhost```.

Next we create a channel, which is where most of the API for getting things done resides. Note we can use a
try-with-resources statement because ```Connection``` and ```Channel``` implement ```java.io.Closeable```.

To send, we must declare a queue for us to send to; then we can publish a message to the queue, all of this in the
try-with-resources statement.

```kotlin
channel.queueDeclare(QUEUE_NAME, false, false, false, null)
val message = "Hello world!"
channel.basicPublish("", QUEUE_NAME, null, message.getBytes())
println(" [x] Sent '$message'")
```

Declaring a queue is idempotent - it will only be created if it doesn't exist already. The message content is a
byte array, so you can encode whatever you like there.

### Receiving

Our consumer listening for messages from RabbitMQ, so unlike the publisher which publishes a single message, we'll keep
it running to listen for messages and print them out.

