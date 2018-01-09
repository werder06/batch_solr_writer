package solr.client


data class Config(val zkHost: String, val zkChroot: String,
                  val collection: String, val bulkSize: Int)