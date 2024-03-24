
测试hadoop
```shell

#./bin/hdfs dfs -mkdir -p /user/hadoop
#./bin/hdfs dfs -mkdir input
./bin/hdfs dfs -mkdir -p /user/silversaya/input
#在hdfs中创建本用户对应的input目录
#以下是复制到hdfs
./bin/hdfs dfs -put ./etc/hadoop/*.xml input



./bin/hadoop jar ./share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar grep input output 'dfs[a-z.]+'

./bin/hdfs dfs -cat output/*

```