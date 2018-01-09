package solr.client

import java.util.concurrent.CountDownLatch

fun runBulkLoad() {
    val config = createConfig()
    val client = BulkUpdateSyncClient(config)
    val producersCount = 10
    val allProducersLatch = CountDownLatch(producersCount)
    val mainLatch = CountDownLatch(1)
    for (i in 1..producersCount) {
        //start producers
        Thread(Runnable {
            try {
                mainLatch.await()
                // produce documents and push them with client
                // ...
            } finally {
                allProducersLatch.countDown()
            }
        }).start()
    }
    mainLatch.countDown()

    allProducersLatch.await()
    client.commit()
}

fun main(args : Array<String>) {
    runBulkLoad()
}


//read config from somewhere
fun createConfig(): Config {
    return Config("localhost:8121", "/", "products", 50000)
}
