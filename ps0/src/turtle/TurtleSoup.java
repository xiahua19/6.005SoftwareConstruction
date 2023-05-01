/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	/** some parameters:
    	 *  degree: the turtle turn degree to draw a square
    	 *  edge_num: the square's edge number
    	 */
    	final double degree = 90.0;
    	final int edgeNum = 4;
    	
    	// draw each edge of the square
    	for (int i = 0; i < edgeNum; ++i) {
            turtle.forward(sideLength);
            turtle.turn(degree);	
    	}
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	/**
    	 * some angle parameters.
    	 */
    	final double exteriorAngles = 360.0;
    	final double flatAngle = 180.0;
    	
    	// compute the angle of a regular polygon
        final double insideAngle = flatAngle - (exteriorAngles / sides);
        
        return insideAngle;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
    	/**
    	 * some angle parameters.
    	 */
    	final double exteriorAngles = 360.0;
    	final double flatAngle = 180.0;
    	
    	// compute the number of sides of a regular polygon
    	final int sidesNum = (int) Math.round(exteriorAngles / (flatAngle - angle));
    	
    	return sidesNum;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
    	/**
    	 * compute the innerAngel and sidesNum of this regular polygon for draw
    	 */
    	final double flatAngle = 180.0;
        final double innerAngle = calculateRegularPolygonAngle(sides);
        final int sidesNum =  calculatePolygonSidesFromAngle(innerAngle);
        
        // draw the regular polygon
        for (int i = 0; i < sidesNum; ++i) {
        	turtle.forward(sideLength);
        	turtle.turn(flatAngle - innerAngle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
    	// preprocess to change origin point to current position.
    	targetX -= currentX;
    	targetY -= currentY;
    	currentX = 0;
    	currentY = 1;
    	
    	// some angle constant
    	final double flatAngle = 180.0;
    	final double fullAngle = 360.0;
    	
    	// use dot product formula to compute the target heading
    	final int dotProduct = currentX * targetX + currentY * targetY;
        final double lenCurrent = Math.sqrt(Math.pow(currentX, 2) + Math.pow(currentY, 2));
        final double lenTarget = Math.sqrt(Math.pow(targetX, 2) + Math.pow(targetY, 2));
        
        final double targetHeading = flatAngle * Math.acos(dotProduct / (lenCurrent * lenTarget)) / Math.PI;
        
        if (targetHeading < currentHeading) {
        	final double adgustHeading = targetHeading - currentHeading + fullAngle;
        	return adgustHeading;
        } else {
        	final double adgustHeading = targetHeading - currentHeading;
        	return adgustHeading;
        }  
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
    	assert (xCoords.size() == yCoords.size());
    	
        ArrayList<Double> adgustHeadings = new ArrayList<Double>();
        
        double currentHeading = 0;
        for (int i = 0; i < xCoords.size()-1; ++i) {
        	final double adgustHeading = calculateHeadingToPoint(currentHeading, 
        			xCoords.get(i).intValue(), 
        			yCoords.get(i).intValue(), 
        			xCoords.get(i+1).intValue(), 
        			yCoords.get(i+1).intValue());
        	adgustHeadings.add(adgustHeading);
        	currentHeading += adgustHeading;
        }
        
        return adgustHeadings;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        throw new RuntimeException("implement me!");
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawRegularPolygon(turtle, 5, 50);

        // draw the window
        turtle.draw();
    }

}
