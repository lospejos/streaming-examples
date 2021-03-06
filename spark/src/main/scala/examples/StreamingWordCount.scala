package examples

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingWordCount {
   def main(args: Array[String]) {
       val conf = new SparkConf().setMaster("local[2]").setAppName("Test Application")
       val ssc = new StreamingContext(conf, Seconds(5))

       // Create a DStream that will connect to hostname:port
       val lines = ssc.socketTextStream("localhost", 1111, StorageLevel.MEMORY_AND_DISK_SER)

       // Split each line into words
       val words = lines.flatMap(_.split(" "))
       val pairs = words.map(word => (word, 1))

       val wordCounts = pairs.reduceByKey(_ + _)
       // Print the first ten elements of each RDD generated in this DStream to the console
       wordCounts.print()

       ssc.start()             // Start the computation
       ssc.awaitTermination()  // Wait for the computation to terminate
   }
 }
