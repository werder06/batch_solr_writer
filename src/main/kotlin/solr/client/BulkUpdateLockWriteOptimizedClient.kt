package solr.client

import org.apache.solr.common.SolrInputDocument
import java.util.concurrent.ConcurrentLinkedQueue


class BulkUpdateLockWriteOptimizedClient(config: Config) : AbstractBulkUpdateClient(config) {
    @Volatile var docs = ConcurrentLinkedQueue<SolrInputDocument>()

    override fun addDoc(doc: SolrInputDocument) {
        if (docs.size >= config.bulkSize) {
            synchronized(this) {
                if (docs.size >= config.bulkSize) {
                    val buffer = docs
                    docs = ConcurrentLinkedQueue()
                    sendBatch(buffer)
                }
            }
        }
        docs.add(doc)
    }

    override fun commit() {
        synchronized(this) { // do not need this sync if contract is to call it
            // in main thread only after all producers threads are finished,
            // but it's just one commit per load, and, for example,
            // will save if one will call commit() from several threads
            if (docs.size > 0) {
                sendBatch(docs)
                docs = ConcurrentLinkedQueue<SolrInputDocument>()
            }
            commitInternal()
        }
    }
}