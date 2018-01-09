Code snippet to demonstrate an idea to answer on a question:
I've got a multithreaded Solr's documents producer on client side, how should I push it into SolrCloud cluster?
 
Why it's not out-off-the-box? 
* CloudSolrClient is thread-safe, but how to avoid sending last unfinished batch from each thread? 
* ConcurrentUpdateSolrClient knows nothing about clouds

 Let's write thread-safe bulk friendly wrapper around CloudSolrClient (on Kotlin by no reasons):
 * BulkUpdateSyncClient takes an implementation idea from Elasticsearch's Java Bulk API, check 
 org.elasticsearch.action.bulk.BulkProcessor to make sure. There is a sync on every write operation, but 
write operations are (relatively) fast in this type of loads. Also it is a good idea (from processors
 cache perspective) to consume batches IN THE SAME thread where we just added docs to it, don't have to copy 
 batches anywhere else I suppose  
 * BulkUpdateLockWriteOptimizedClient tries to replace "sync on write" by "lock free" pattern
 * BlockingQueue would come as an another option I suppose   
 