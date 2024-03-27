import java.util.Properties
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
object SparkOperateMySQL
{
  def main(args: Array[String]): Unit = {
    val spark= SparkSession.builder().appName("sqltest").master("local[2]").getOrCreate()
    val jdbcDF = spark.
      read.format("jdbc").
      option("url", "jdbc:mysql://localhost:3306/spark").
      option("driver","com.mysql.jdbc.Driver").
      option("dbtable", "student").
      option("user", "root").
      option("password", "123456").load()
    jdbcDF.show()
    val studentRDD = spark.sparkContext.parallelize(Array("5 Chenglu F 22","6 Linzhe M 23")).map(_.split(" "))
    val rowRDD = studentRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))
    rowRDD.foreach(println)
    val schema = StructType(List(
      StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("gender", StringType, true),
      StructField("age", IntegerType, true)))
    val studentDF=spark.createDataFrame(rowRDD,schema)
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    prop.put("driver","com.mysql.jdbc.Driver")
    studentDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "spark.student", prop)
    jdbcDF.show()
  }
}