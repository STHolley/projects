import java.util.List;

/**
 * @Purpose: The PerformanceTest class is used to compare the implemented
 *           algorithms in term of time and the number of sheets used
 *
 *           You can add additional methods if you need to in this class
 * 
 * @author RYK
 * @since 30/10/2018
 * extended by @author
 */

public class PerformanceTest {

	public static void main(String[] args) {

		System.out.println("***********************************************");
		System.out.println("*********** Performance analysis **************");
		System.out.println("**********************************************");

		System.out.println();
		/*
		 * You must complete the Generator class in order to generate a random
		 * test values. You must complete the Algorithms class in order to call
		 * the two algorithms.
		 * 
		 * You can use any additional method you created in this class
		 */

		/*
		 * 
		 * NOTE: you are using an unsorted list of shapes
		 */

		/**
		 * Remember: You need to calculate the time and number of sheets used
		 * for each run of each algorithm.
		 * 
		 * You are expected to run several tests (e.g. noOfTests=5) of your
		 * programs for, 10000, 20000, 30000, 40000, 50000 shapes
		 * (noOfShapes=10000, increment=10000) so that one can see how the
		 * algorithms perform for large datasets.
		 * 
		 * You are expected to run the same test a number of times to ensure
		 * accurate result (noOfRep=4). In this case, you need to calculate the
		 * average time and sheets needed for each run
		 **/

		// total number of tests - you need to CHANGE this value
		int noOfTests = 5;

		// number of repetitions for each test - you need to CHANGE this value
		int noOfRep = 8;

		// number of shapes needed for the first run - you need to CHANGE this
		// value
		int noOfShapes = 10000;

		// the increment in the number of shapes - you need to CHANGE this value
		int increment = 10000;
		System.out.println("---------------------------------------- Next Fit ----------------------------------------");
		System.out.print("Shapes");
		for(int i = 1; i <= noOfRep; i++){
			System.out.print("\t" + i + "(ms)");
		}
		System.out.print("\tavg(ms)\tavg sheets");
		for(int i = 0; i < noOfTests; i++){
			System.out.print("\n" + ((i * increment) + noOfShapes) + "\t");
			double avgTime = 0;
			double avgSheets = 0;
			for(int j = 0; j < noOfRep; j++){
				int testShapeSize = (i * increment) + noOfShapes;
				Generator g = new Generator();
				List<Shape> sl = g.generateShapeList(testShapeSize);
				double elapsedTime = System.nanoTime() / 1000000;
				Algorithms algo = new Algorithms();
				List<Sheet> test = algo.nextFit(sl);
				avgSheets += test.size();
				elapsedTime = (System.nanoTime() / 1000000) - elapsedTime;
				avgTime += elapsedTime;
				System.out.print(elapsedTime + "\t");
			}
			avgTime /= noOfRep;
			avgSheets /= noOfRep;
			System.out.print(avgTime + "\t" + (int)avgSheets);
		}
		System.out.println("\n------------------------------------------------------------------------------------------");
		
		
		System.out.println("---------------------------------------- First Fit ----------------------------------------");
		System.out.print("Shapes");
		for(int i = 1; i <= noOfRep; i++){
			System.out.print("\t" + i + "(s)");
		}
		System.out.print("\tavg(s)\tavg sheets");
		for(int i = 0; i < noOfTests; i++){
			System.out.print("\n" + ((i * increment) + noOfShapes) + "\t");
			double avgTime = 0;
			double avgSheets = 0;
			for(int j = 0; j < noOfRep; j++){
				int testShapeSize = (i * increment) + noOfShapes;
				Generator g = new Generator();
				List<Shape> sl = g.generateShapeList(testShapeSize);
				double elapsedTime = System.nanoTime() / 1000000;
				Algorithms algo = new Algorithms();
				List<Sheet> test = algo.firstFit(sl);
				avgSheets += test.size();
				elapsedTime = ((System.nanoTime() / 1000000) - elapsedTime)/1000;
				avgTime += elapsedTime;
				System.out.print(elapsedTime + "\t");
			}
			avgTime /= noOfRep;
			avgSheets /= noOfRep;
			System.out.print(((float)Math.round(avgTime*1000)/1000) + "\t" + (int)avgSheets);
		}
		System.out.println("\n------------------------------------------------------------------------------------------");
	}
}
