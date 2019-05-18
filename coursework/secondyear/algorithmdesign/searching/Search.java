
/************************************************/
/*** Purpose:                                 ***/
/***                                          ***/
/***                                          ***/
/*** Author:                                  ***/
/*** Date:                                    ***/
/***                                          ***/
/*** Note: Based on skeleton code provided by ***/
/*** Jason Steggles 16/10/2018                ***/
/************************************************/

import java.io.*;
import java.text.*;
import java.util.*;

public class Search {

	/** Global vars for counting comparisons **/
	public int compSeq=0;
	public int compBin=0;
	public int compNew=0;
	
	/** Array of values to be searched and size **/
	private int[] A;
	private int size;
	
	
	/** Constructor **/
	Search(int n)
	{
	    /** set size of array **/
	    size = n;
	
	    /** Create arrays **/
	    A = new int[size];
	    
	    /** Initialise Array **/
	    for (int i=0; i<size; i++)
	    {
	    	A[i] = 0;
	    }
	
	}
	
	
	/********************************************/
	/*** Read a file of numbers into an array ***/
	/********************************************/
	public void readFileIn(String file)
	{
	   try
	   {
	       /** Set up file for reading **/
	       FileReader reader = new FileReader(file);
	       Scanner in = new Scanner(reader);
	
	       /** Loop round reading in data **/
	       for (int i=0;i<size;i++)
	       {
	         A[i] = in.nextInt();
	       }
	    }
	    catch (IOException e)
	    {
	       System.out.println("Error processing file " + file);
	    }
	}
	
	
	/*****************************/
	/*** Display array of data ***/
	/*****************************/
	public void displayData(int line, String header)
	{
	    /** Integer Formatter **/
	    NumberFormat FI = NumberFormat.getInstance();
	    FI.setMinimumIntegerDigits(3);
	
	    /** Print header string **/
	    System.out.print("\n\n"+header);
	
	    /** Display array data **/
	    for (int i=0;i<size;i++)
	    {
	        if (i%line == 0) { System.out.println(); }
	        
	        System.out.print(FI.format(A[i])+" ");
	    }
	}
	
	/*************************************/
	/*** Running the sequential search ***/
	/*************************************/
	public int seqSearch(int key){
		for(int i = 0; i < size-1; i++){
			compSeq++; //Checking to see if the value checked is more than the key
			if(A[i] <= key){
				compSeq++; //Brute force comparison for each item
				if(A[i] == key){
					return i;
				}
			} else {
				return -1;
			}
		}
		return -1;
	}
	
	/*********************************/
	/*** Running the binary search ***/
	/*********************************/
	public int binSearch(int key, int l, int r){
		if(r < l){
			return -1;
		}
		int m = (r + l) / 2;
		compBin++;
		if(key == A[m]){return m;}
		compBin++;
		if(key > A[m]){
			return binSearch(key, m+1, r);
		} else {
			return binSearch(key, l, m-1);
		}
	}
	
	/****************************************/
	/*** Running the new search algorithm ***/
	/****************************************/
	public int newSearch(int key, int m){
		int i = m;
		int N = size;
		while((i < N) && (A[i] <= key)){
			compNew++; //For every successful comparison
			i += m;
		}
		compNew++; //As one comparison is missed when the while loop is unsuccessful
		int k = i - m;
		if(i > N){
			i = N;
		}
		while(k < i){
			compNew++; //For the If statement below
			if(key == A[k]){
				return k;
			}
			k++;
		}
		return -1;
	}

} /*** End of class Search ***/
