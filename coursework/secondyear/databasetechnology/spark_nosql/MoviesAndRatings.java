package coursework;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class MoviesAndRatings {

	private static String PATH = "src/main/resources/"; 

	private static String MOVIES = "movies.csv";
	private static String RATINGS = "ratings.csv";
	private static String GENRES = "movieGenres.csv";
	
	public static void main(String[] args) throws IOException{

		LogManager.getLogger("org").setLevel(Level.ERROR);
		
		SparkConf conf = new SparkConf().setMaster("local").setAppName("coursework");
		SparkSession spark = SparkSession.builder().appName("coursework").config(conf).getOrCreate();
		
		//STEP 1
		System.out.println("Step 1:");
		Dataset<Row> movies = spark.read().option("inferSchema", true).option("header", true).option("multLine", true).option("mode", "DROPMALFORMED").csv(PATH + MOVIES);
		
		Dataset<Row> ratings = spark.read().option("inferSchema", true).option("header", true).option("multLine", true).option("mode", "DROPMALFORMED").csv(PATH + RATINGS);
		
		movies.printSchema();
		ratings.printSchema();
		System.out.println("Step 1 done...");
		
		//STEP 2
		System.out.println("\nStep 2:");
		
		Dataset<Row> movieGenres = movies.select(col("movieId"), explode(split(col("genres"), "\\|"))).withColumnRenamed("col", "genre");
		
		//movieGenres.write().csv(PATH + GENRES);
		
		FileWriter fwGen = new FileWriter(PATH + GENRES);
		List<Row> genresList = movieGenres.collectAsList();
		fwGen.write("movieId,genre\n");
		System.out.println("Writing files...");
		for(int i = 0; i < genresList.size(); i++){
			fwGen.write(genresList.get(i).getAs("movieId") + "," + genresList.get(i).getAs("genre") + "\n");
		}
		fwGen.close();
		System.out.println("Step 2 done...");
		
		//STEP 3
		System.out.println("\nStep 3:");
		Dataset<Row> genres = spark.read().option("inferSchema", true).option("header", true).option("multLine", true).option("mode", "DROPMALFORMED").csv(PATH + GENRES);
		
		genres.printSchema();
		
		genres.select("movieId", "genre").orderBy(col("movieId").desc()).show(50);
		System.out.println("Step 3 done...");
		
		//STEP 4
		System.out.println("\nStep4:");
		
		Dataset<Row> genrePopularity = genres.groupBy("genre").count().orderBy(col("count").desc()).limit(10);
		
		genrePopularity.show();
		System.out.println("Step 4 done...");
		
		//STEP 5
		System.out.println("\nStep 5:");
		
		Dataset<Row> genreToUser = genrePopularity.select("genre").join(genres.select("movieId", "genre"), "genre").join(ratings.select("userId", "movieId"), "movieId").groupBy("genre", "userId").count().orderBy(col("count").desc()).dropDuplicates("genre").orderBy(col("count").desc()).limit(10).drop("count");
		
		genreToUser.show();
		System.out.println("Step 5 done...");
		
		//STEP 6
		System.out.println("\nStep 6:");
		Dataset<Row> ratingsCount = ratings
				.groupBy("userId")
				.count()
				.orderBy(col("count").desc())
				.limit(10)
				.select("userId")
				.join(ratings, "userId")
				.join(genres, "movieId")
				.groupBy("userId", "genre")
				.count()
				.orderBy(col("count").desc())
				.dropDuplicates("userId")
				.orderBy(col("count").desc())
				.withColumnRenamed("count", "ratingsCount")
				.withColumnRenamed("genre", "mostCommonGenre");
		
		ratingsCount.show();
		System.out.println("Step 6 done...");
		
		//STEP 7
		System.out.println("\nStep 7:");
		Dataset<Row> avgMovRat = ratings.groupBy("movieId").agg(avg("rating") ,var_samp("rating")).orderBy(col("avg(rating)").desc()).limit(10);
				
		avgMovRat.show();
		System.out.println("Step 7 done...");
		
		spark.stop();
	}
	
}
