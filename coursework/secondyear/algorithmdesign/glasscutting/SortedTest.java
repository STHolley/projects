import java.util.Collections;
import java.util.List;

/**
 * @Purpose: The SortedTest class is used to compare the implemented algorithms
 *           in term of the number of sheets used WHEN the list of
 *           shapes is SORTED
 *
 *           You can add additional methods if you need to in this class
 * 
 * @author RYK
 * @since 30/10/2018
 * extended by @author
 */

public class SortedTest {
	public static void main(String[] args) {
		System.out.println("*********************************************");
		System.out.println("**************** Sorted Test ****************");
		System.out.println("*********************************************");
		System.out.println();

		/*
		 * Generate a random shape list and then check the number of sheets used
		 * when
		 ** this shape list is passed un-sorted
		 ** the list is sorted in ascending surface size
		 ** the list is sorted in descending surface size
		 * 
		 * run several tests for different sizes of the "list of shapes" 
		 */

		/*
		 * HINT: you might want to implements the comparTo method in the Shape
		 * class or implement the Comparator Interface to do the sorting
		 */

		/* An example output could be:
		 * System.out.println("NoOfShapes    NextFitSheets(unSorted)     NextFitSheets(AscOrder)  NextFitSheets(DesOrder)" 
		 * + "    FirstFitSheets(unSorted)     FirstFitSheets(AscOrder)  FirstFitSheets(DesOrder)");
		 */
		int noOfTests = 5;
		int increment = 10000;
		int size = 10000;
		for(int i = 0; i < noOfTests; i++){
			int testSize = (i * increment) + size;
			int noOfReps = 5;
			int NFUnAvg = 0;
			int FFUnAvg = 0;
			int NFAsAvg = 0;
			int FFAsAvg = 0;
			int NFDeAvg = 0;
			int FFDeAvg = 0;
			for(int j = 1; j <=noOfReps; j++){
				Generator gen = new Generator();
				Algorithms algo = new Algorithms();
				List<Shape> test = gen.generateShapeList(testSize);
				int NFUn = algo.nextFit(test).size();
				int FFUn = algo.firstFit(test).size();
				NFUnAvg += NFUn;
				FFUnAvg += FFUn;
				Collections.sort(test);
				int NFAs = algo.nextFit(test).size();
				int FFAs = algo.firstFit(test).size();
				NFAsAvg += NFAs;
				FFAsAvg += FFAs;
				Collections.reverse(test);
				int NFDe = algo.nextFit(test).size();
				int FFDe = algo.firstFit(test).size();
				NFDeAvg += NFDe;
				FFDeAvg += FFDe;
			}
			NFUnAvg /= noOfReps;
			FFUnAvg /= noOfReps;
			NFAsAvg /= noOfReps;
			FFAsAvg /= noOfReps;
			NFDeAvg /= noOfReps;
			FFDeAvg /= noOfReps;
			System.out.println("Number of Sheets for " + testSize + " shapes");
			System.out.println("Averages\tNext\tFirst");
			System.out.println("Unsorted\t" + NFUnAvg + "\t" + FFUnAvg);
			System.out.println("Ascending\t" + NFAsAvg + "\t" + FFAsAvg);
			System.out.println("Descending\t" + NFDeAvg + "\t" + FFDeAvg + "\n");
		}
	}

}
