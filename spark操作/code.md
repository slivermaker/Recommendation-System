```shell
bin/run-example SparkPi

./bin/spark-shell --master local[4]


val textFile = sc.textFile("file:///usr/local/spark/README.md")

```