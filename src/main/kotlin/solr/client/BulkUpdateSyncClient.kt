package solr.client

import org.apache.solr.common.SolrInputDocument


class BulkUpdateSyncClient(config: Config) : AbstractBulkUpdateClient(config) {
    var docs = ArrayList<SolrInputDocument>()

    override fun addDoc(doc: SolrInputDocument) {
        synchronized(this) {
            docs.add(doc)
            if (docs.size >= config.bulkSize) {
                sendBatch(docs)
                docs = ArrayList<SolrInputDocument>()
            }
        }
    }

    override fun commit() {
        synchronized(this) {
            if (docs.size > 0) {
                sendBatch(docs)
                docs = ArrayList<SolrInputDocument>()
            }
            commitInternal()
        }
    }
}