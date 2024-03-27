import org.apache.spark.sql.SparkSession
import  org.apache.spark.ml.evaluation.RegressionEvaluator
import  org.apache.spark.ml.recommendation.ALS
object MovieLensALS {
  case  class  Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long);
  def  parseRating(str: String): Rating = {
    val  fields = str.split("::")
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
  }

  def main(args: Array[String]): Unit = {
    val spark=SparkSession.builder().appName("sparkmllibtest").master("local[2]").getOrCreate()
    import spark.implicits._
    val  ratings = spark.sparkContext.textFile("file:///usr/local/spark/data/mllib/als/sample_movielens_ratings.txt").
      map(parseRating).toDF()
    ratings.show()
    val  Array(training,test) = ratings.randomSplit(Array(0.8,0.2))
    val  alsExplicit = new ALS().setMaxIter(5).setRegParam(0.01).setUserCol("userId").setItemCol("movieId").setRatingCol("rating")
    val  alsImplicit = new ALS().setMaxIter(5).setRegParam(0.01).setImplicitPrefs(true).setUserCol("userId").setItemCol("movieId").setRatingCol("rating")
    val  modelExplicit = alsExplicit.fit(training)
    val  modelImplicit = alsImplicit.fit(training)
    val  predictionsExplicit= modelExplicit.transform(test).na.drop()
    val  predictionsImplicit= modelImplicit.transform(test).na.drop()
    predictionsExplicit.show()
    predictionsImplicit.show()
    val  evaluator = new RegressionEvaluator().setMetricName("rmse").setLabelCol("rating").setPredictionCol("prediction")

    val  rmseExplicit = evaluator.evaluate(predictionsExplicit)
    val  rmseImplicit = evaluator.evaluate(predictionsImplicit)
    println(s"Explicit:Root-mean-square error = $rmseExplicit")
    println(s"Implicit:Root-mean-square error = $rmseImplicit")

  }
}