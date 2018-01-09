package solr.client

import org.apache.solr.client.solrj.impl.CloudSolrClient
import org.apache.solr.common.SolrInputDocument


abstract class AbstractBulkUpdateClient(val config: Config) {
    val client = client(config)

    abstract fun addDoc(doc: SolrInputDocument)
    abstract fun commit()

    protected fun sendBatch(docs: Collection<SolrInputDocument>) {
        client.add(config.collection, docs)
    }

    protected fun commitInternal() {
        client.commit()
    }

    private fun client(config : Config): CloudSolrClient {
        return CloudSolrClient.Builder()
                .withZkHost(config.zkHost)
                .withZkChroot(config.zkChroot)
                .build()
    }
}