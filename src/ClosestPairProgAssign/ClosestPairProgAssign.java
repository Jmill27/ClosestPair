package ClosestPairProgAssign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ClosestPairProgAssign {

	static double closestDistance = Integer.MAX_VALUE;
    static Point point1 = null;
    static Point point2 = null;
    
    public static class Point {
        private int pointA;
        private int pointB;

        public Point(int a, int b) {
            this.pointA = a;
            this.pointB = b;
        }
    }
    
    public static double getDistance(Point a, Point b){ 
        int sPointA = ((a.pointA - b.pointA) * (a.pointA - b.pointA));
        int sPointB = ((a.pointB - b.pointB) * (a.pointB - b.pointB));
    	return Math.sqrt((sPointA) + (sPointB));
    }
    
    private static void mergePoints(Point[] a, Point[] aux, int low, int middle, int high) {
        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }
        int c = low;
        int m = middle + 1;
        
        for (int k = low; k <= high; k++) {
            if (c > middle) {
            	a[k] = aux[m];
            	m++;}
            else if (m > high) {
            	a[k] = aux[c];
            	c++;}
            else if (checkSmaller(aux[m], aux[c])) {
            	a[k] = aux[m];
            	m++;}
            else {
            	a[k] = aux[c];
            	c++;}
        }
    }
    
    private static boolean checkSmaller(Point smaller, Point small) {
		return (smaller.pointA < small.pointA);
    }
    
    public static void findClosestDistance(List<Point> allPoints) throws IllegalArgumentException{
        int s = allPoints.size();
        Point[] pX = new Point[s];
        for(int k = 0; k < s; k++)
            pX[k] = allPoints.get(k);
        
        Arrays.sort(pX, new Comparator<Point>() {
            public int compare(Point comPointA, Point comPointB) {
                if(comPointA.pointA != comPointB.pointA)
                    return comPointA.pointA - comPointB.pointA;
                else
                    return comPointA.pointB - comPointB.pointB;
        } } );
        for(int k = 0; k < s-1; k++){
            if(pX[k]==pX[k+1]){
                closestDistance = 0;
                point1 = pX[k];
                point2 = pX[k+1];
                break;
            }
        }
        Point[] pY = new Point[s];
        for (int i = 0; i < s; i++)
            pY[i] = pX[i];
        
        Point[] aux = new Point[s];
        closestPoints(pX, pY, aux, 0, s - 1);
    }
    
    private static double closestPoints(Point[] pX, Point[] pY, Point[] aux, int low, int high) {
        if (high <= low) 
        	return Double.POSITIVE_INFINITY;
        int middle = low + (high - low) / 2;
        Point median = pX[middle];

        double delta1 = closestPoints(pX, pY, aux, low, middle);
        double delta2 = closestPoints(pX, pY, aux, middle+1, high);
        double delta  = Math.min(delta1, delta2);

        mergePoints(pY, aux, low, middle, high);

        int x = 0;
        for (int k = low; k <= high; k++) {
            if (Math.abs(pY[k].pointA - median.pointA) < delta)
                aux[x++] = pY[k];
        }

        for (int f = 0; f < x; f++) {
            for (int j = f+1; (j < x) && (aux[j].pointB - aux[f].pointB < delta); j++) {
                double distance = getDistance(aux[f], aux[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < closestDistance) {
                        closestDistance = delta;
                        point1 = aux[f];
                        point2 = aux[j];
                    }
                }
            }
        }
        return delta;
    }
    
    public static void main(String[] args) throws NumberFormatException, IOException {
    	
    	Scanner myObj = new Scanner(System.in);
    	System.out.println("This will find the closest pair of points from either file below.");
    	System.out.println("Enter 100points.txt or 1000points.txt");
    	String file = myObj.nextLine();
    	
    	List<Point> points = new ArrayList<>();
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	String s;
    	while ((s = reader.readLine()) != null && s.trim().length() != 0) {
    		String[] split = s.split("\\s+");
    		points.add(new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
    		}
    	reader.close();
    	myObj.close();
    	findClosestDistance(points);
    	System.out.println("\n"+ file +" test file:\n");
    	System.out.println("The minimum distance is: \n"
    	+ closestDistance + ": ("+point1.pointA+","+point1.pointB+")<--->("+point2.pointA+","+point2.pointB+")\n");
    }
}