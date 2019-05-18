
/**
 * @Purpose: The Algorithms class contains the two algorithms you have to implement  
 * Do NOT modify the names and signatures of the two algorithm methods
 * 
 * @author  RYK
 * @since   30/10/2018
 * extended by @author 
 *
 **/

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

	/**
	 * This method is used to implement the next fit algorithm
	 * 
	 * @param shapes:a
	 *            list of shapes representing customer requests(shapes to be
	 *            cut/placed)
	 * @return a list of the sheets used to fulfil all customer requests (i.e.
	 *         place all the shapes). e.g. if you pass a "shapes" list that has
	 *         two shapes {(w=200,h=200),(w=50,h=100)}, then the returned list
	 *         of sheets should show us all the information needed (e.g. that
	 *         one sheet is used, this sheet has one shelf and this shelf has
	 *         two shapes in it). In the test program, you can use the returned
	 *         list to retrieve the total number of sheets used.
	 **/

	public List<Sheet> nextFit(List<Shape> shapes) {

		/*
		 * Start with an empty list of sheets (remember each sheet has a width
		 * of 300 and a height of 250 as specified in the Sheet class)
		 */
		List<Sheet> usedSheets = new ArrayList<Sheet>();

		/*
		 * Add in your own code so that the method will place all the shapes
		 * according to NextFit under ALL the assumptions mentioned in the
		 * specification
		 */

		// Create the initial sheet and shelf
		usedSheets.add(new Sheet());
		usedSheets.get(0).addShelf(new Shelf());

		// Loop through all shapes given
		for (Shape currentShape : shapes) {

			// get current sheet and shelf
			Sheet currentSheet = usedSheets.get(usedSheets.size() - 1);
			Shelf currentShelf = currentSheet.getShelves().get(currentSheet.getShelves().size() - 1);

			// Check if there is already 20 shapes on this sheet already
			int totalShapes = 0;
			for (Shelf x : currentSheet.getShelves()) {
				totalShapes += x.getShapes().size();
			}
			if (totalShapes == Sheet.SHAPE_LIMIT) {
				// if the number of shapes is at the limit make a new sheet
				usedSheets.add(new Sheet());
				usedSheets.get(usedSheets.size() - 1).addShelf(new Shelf());
				// reset the current shelf and sheet
				currentSheet = usedSheets.get(usedSheets.size() - 1);
				currentShelf = currentSheet.getShelves().get(currentSheet.getShelves().size() - 1);
			}

			// Check height and width fit
			// height check
			boolean heightCheck = (currentShape.getHeight() <= currentShelf.getHeight())
					|| (currentShelf.getHeight() == 0);

			// width check
			boolean widthCheck = Sheet.SHEET_WIDTH > (currentShape.getWidth() + currentShelf.getWidth());

			// Check if a new shelf can be made
			boolean newShelfCheck = Sheet.SHEET_HEIGHT > (currentShape.getHeight() + currentSheet.allShelvesHeight());

			if (heightCheck && widthCheck) {
				// The shape fits on the current shelf, add it
				currentShelf.place(currentShape);
			} else if (newShelfCheck) {
				// The shape does not fit on the current shelf but will fit on a
				// new one, add it that way
				currentSheet.addShelf(new Shelf());
				currentShelf = currentSheet.getShelves().get(currentSheet.getShelves().size() - 1);
				currentShelf.place(currentShape);
			} else {
				// There is no sheet space for this shape. Make a new sheet
				usedSheets.add(new Sheet());
				usedSheets.get(usedSheets.size() - 1).addShelf(new Shelf());
				// reset the current shelf and sheet
				currentSheet = usedSheets.get(usedSheets.size() - 1);
				currentShelf = currentSheet.getShelves().get(currentSheet.getShelves().size() - 1);
				// add the new shape
				currentShelf.place(currentShape);
			}

		}

		return usedSheets;
	}

	/**
	 * This method is used to implement the first fit algorithm
	 * 
	 * @param shapes:a
	 *            list of shapes representing customer requests (shapes to be
	 *            cut/placed)
	 * @return a list of the sheets used to fulfil all customer requests (i.e.
	 *         place all the shapes). e.g. if you pass a "shapes" list that has
	 *         two shapes {(w=200,h=200),(w=50,h=100)}, then the returned list
	 *         of sheets should show us all the information needed (e.g. that
	 *         one sheet is used, this sheet has one shelf and this shelf has
	 *         two shapes in it). In the test program, you can use the returned
	 *         list to retrieve the total number of sheets used
	 **/
	public List<Sheet> firstFit(List<Shape> shapes) {

		/*
		 * Start with an empty list of sheets (remember each sheet has a width
		 * of 300 and a height of 250 as specified in the Sheet class)
		 */
		List<Sheet> usedSheets = new ArrayList<Sheet>();

		/*
		 * Add in your own code so that the method will place all the shapes
		 * according to FirstFit under the assumptions mentioned in the spec
		 */

		// Create the initial sheet and shelf
		usedSheets.add(new Sheet());
		usedSheets.get(0).addShelf(new Shelf());

		// Loop through all shapes given
		for (Shape currentShape : shapes) {

			int j = 0;
			boolean placed = false;
			while (!placed) {
				int i = 0;
				Sheet currentSheet = usedSheets.get(j);
				j++;
				boolean newSheet = false;
				int total = 0;
				for(Shelf x: currentSheet.getShelves()){
					total += x.getShapes().size();
				}
				if(total == Sheet.SHAPE_LIMIT){
					newSheet = true;
				}
				while (!newSheet && !placed) {
					Shelf currentShelf = currentSheet.getShelves().get(i);
					i++;
					boolean heightCheck = (currentShape.getHeight() <= currentShelf.getHeight())
							|| (currentShelf.getHeight() == 0);

					// width check
					boolean widthCheck = Sheet.SHEET_WIDTH > (currentShape.getWidth() + currentShelf.getWidth());

					// Check if a new shelf can be made
					boolean newShelfCheck = Sheet.SHEET_HEIGHT > (currentShape.getHeight()
							+ currentSheet.allShelvesHeight());
					if (heightCheck && widthCheck) {
						// The shape fits on the current shelf, add it
						currentShelf.place(currentShape);
						placed = true;
						break;
					} else if (i == currentSheet.getShelves().size()) {
						// If looking at the last shelf and there is no space
						// Check if you can make a new shelf
						if (newShelfCheck) {
							currentSheet.addShelf(new Shelf());
							currentShelf = currentSheet.getShelves().get(currentSheet.getShelves().size() - 1);
							currentShelf.place(currentShape);
							placed = true;
							break;
						} else {
							newSheet = true;
							break;
						}
					}
				}
				if (newSheet && j == usedSheets.size()) {
					usedSheets.add(new Sheet());
					currentSheet = usedSheets.get(usedSheets.size() - 1);
					currentSheet.addShelf(new Shelf());
					Shelf currentShelf = currentSheet.getShelves().get(0);
					currentShelf.place(currentShape);
					placed = true;
				}
			}
		}
		return usedSheets;
	}

}
