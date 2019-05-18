import java.util.ArrayList;
import java.util.List;

/**
 * @Purpose: The CorrectnessTest class is used to validate the correctness of
 *           the implemented algorithms You can add additional methods if you
 *           need
 * 
 * @author RYK
 * @since 30/10/2018 extended by @author
 */

public class CorrectnessTest {
	public static void main(String[] args) {
		System.out.println("*********************************************");
		System.out.println("*********** Correctness testing *************");
		System.out.println("*********************************************");
		System.out.println();

		/*
		 * Here you will need to validate that your algorithms (nextFit() and
		 * firstFit()) behave as expected on small data sets. Think about normal
		 * cases and border cases. You can use any additional method you created
		 * in this class
		 */
		Algorithms algor = new Algorithms();

		/*
		 * Shapes List n for normal, b for boundary Test 1
		 */
		Shape b11 = new Shape(20, 50);
		Shape n12 = new Shape(80, 40);
		Shape b13 = new Shape(300, 60);
		Shape n14 = new Shape(50, 70);
		Shape n15 = new Shape(15, 65);
		Shape n16 = new Shape(70, 62);
		Shape b17 = new Shape(40, 250);
		List<Shape> list1 = new ArrayList<Shape>();
		list1.add(b11);
		list1.add(n12);
		list1.add(b13);
		list1.add(n14);
		list1.add(n15);
		list1.add(n16);
		list1.add(b17);

		Sheet NFtest11 = new Sheet();
		Sheet NFtest12 = new Sheet();
		Shelf NFshelf111 = new Shelf();
		Shelf NFshelf112 = new Shelf();
		Shelf NFshelf113 = new Shelf();
		Shelf NFshelf121 = new Shelf();

		NFshelf111.place(b11);
		NFshelf111.place(n12);
		NFshelf112.place(b13);
		NFshelf113.place(n14);
		NFshelf113.place(n15);
		NFshelf113.place(n16);
		NFshelf121.place(b17);

		NFtest11.addShelf(NFshelf111);
		NFtest11.addShelf(NFshelf112);
		NFtest11.addShelf(NFshelf113);
		NFtest12.addShelf(NFshelf121);

		List<Sheet> sheetList1 = new ArrayList<Sheet>();
		sheetList1.add(NFtest11);
		sheetList1.add(NFtest12);

		System.out.println("*********************************************");
		System.out.println("****************** Next Fit *****************");
		System.out.println("*********************************************");
		List<Sheet> testNext = algor.nextFit(list1);
		System.out.println("\n****************** Created ******************");
		System.out.println(testNext.toString());
		System.out.println("\n****************** Expected ******************");
		System.out.println(sheetList1.toString());
		System.out.println("Are they equal?: " + testNext.toString().equals(sheetList1.toString()));

		Shape n21 = new Shape(20, 30);
		Shape n22 = new Shape(80, 25);
		Shape n23 = new Shape(80, 40);
		Shape b24 = new Shape(25, 1);
		Shape b25 = new Shape(300, 50);
		Shape n26 = new Shape(70, 35);
		Shape b27 = new Shape(100, 31);
		List<Shape> list2 = new ArrayList<Shape>();
		list2.add(n21);
		list2.add(n22);
		list2.add(n23);
		list2.add(b24);
		list2.add(b25);
		list2.add(n26);
		list2.add(b27);

		Sheet FFtest21 = new Sheet();
		Shelf FFshelf211 = new Shelf();
		Shelf FFshelf212 = new Shelf();
		Shelf FFshelf213 = new Shelf();

		FFshelf211.place(n21);
		FFshelf211.place(n22);
		FFshelf211.place(b24);
		FFshelf212.place(n23);
		FFshelf212.place(n26);
		FFshelf212.place(b27);
		FFshelf213.place(b25);

		FFtest21.addShelf(FFshelf211);
		FFtest21.addShelf(FFshelf212);
		FFtest21.addShelf(FFshelf213);

		List<Sheet> sheetList2 = new ArrayList<Sheet>();
		sheetList2.add(FFtest21);

		System.out.println("*********************************************");
		System.out.println("****************** First Fit ****************");
		System.out.println("*********************************************");
		List<Sheet> testFirst = algor.firstFit(list2);
		System.out.println("\n****************** Created ******************");
		System.out.println(testFirst.toString());
		System.out.println("\n****************** Expected ******************");
		System.out.println(sheetList2.toString());
		System.out.println("Are they equal?: " + testFirst.toString().equals(sheetList2.toString()));
	}
}
