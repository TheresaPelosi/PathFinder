/** Theresa Pelosi
 *  Period 9
 *  1/4/17
 *  
 *  Find the shortest path through New Jersey that is the easiest to climb (elevation based)
 */

import java.util.*;
import java.io.*;
import java.awt.*;

public class MountainPaths 
{ 
  public static void main(String[] args) throws Exception{
      
       //construct DrawingPanel, and get its Graphics context
        DrawingPanel panel = new DrawingPanel(840, 480);
        Graphics g = panel.getGraphics();
        
        //Test Step 1 - construct mountain map data
        Scanner S = new Scanner(new File("Colorado_844x480.dat"));
        int[][] grid = read(S, 480, 840);
        
        //Test Step 2 - min, max
        int min = findMinValue(grid);
        System.out.println("Min value in map: "+ min);
        
        int max = findMaxValue(grid);
        System.out.println("Max value in map: "+ max);
        
        
        //Test Step 3 - draw the map
        drawMap(g, grid);
        
        //Test Step 4 - draw a greedy path
        
        // 4.1 implement indexOfMinInCol
        int minRow = indexOfMinInCol(grid, 0); // find the smallest value in col 0
        System.out.println("Row with lowest val in col 0: "+ minRow);
        
        // 4.2 use minRow as starting point to draw path
        g.setColor(Color.RED); //can set the color of the 'brush' before drawing, then method doesn't need to worry about it
        int totalChange = drawLowestElevPath(g, grid, minRow); //
        System.out.println("Lowest-Elevation-Change Path starting at row "+minRow+" gives total change of: "+totalChange);
        
        //Test Step 5 - draw the best path
        g.setColor(Color.RED);
        int bestRow = indexOfLowestElevPath(g, grid);
        
        //drawMap(g, grid); //use this to get rid of all red lines
        //g.setColor(Color.GREEN); //set brush to green for drawing best path
        //totalChange = drawLowestElevPath(g, grid, bestRow);
        System.out.println("The Lowest-Elevation-Change Path starts at row: "+bestRow+" and gives a total change\nof: "+totalChange);
    }
  
  /**
   * @param S a Scanner instantiated and pointing at a file
   * @param numRows the number of rows represented in the file
   * @param numCols the number of cols represented in the file
   * @return a 2D array (rows x cols) of the data from the file read
   */
  public static int[][] read(Scanner S, int numRows, int numCols){
       int[][] grid = new int[numRows][numCols];
       
       for(int i = 0; i < 480; i++)
       {
           for(int j = 0; j < 840; j++)
           {
               if(S.hasNextInt() == true)
                grid[i][j] = S.nextInt();
           }
       }
       
       return grid;
  }
  
  
  /**
   * @param grid a 2D array from which you want to find the smallest value
   * @return the smallest value in the given 2D array
   */
  public static int findMinValue(int[][] grid){
      int min = 9999;
      
      for(int i = 0; i < 480; i++)
       {
           for(int j = 0; j < 840; j++)
           {
               if(grid[i][j] < min)
                  min = grid[i][j]; 
          }
       }
      return min;
  }
  
  /**
   * @param grid a 2D array from which you want to find the largest value
   * @return the largest value in the given 2D array
   */
  public static int findMaxValue(int[][] grid){
     int max = -1;
      
     for(int i = 0; i < 480; i++)
       {
           for(int j = 0; j < 840; j++)
           {
               if(grid[i][j] > max)
                  max = grid[i][j];
           }
      }
 
     return max;
  }
  
  /**
   * Given a 2D array of elevation data create a image of size rows x cols, 
   * drawing a 1x1 rectangle for each value in the array whose color is set
   * to a a scaled gray value (0-255).  Note: to scale the values in the array 
   * to 0-255 you must find the min and max values in the original data first.
   * @param g a Graphics context to use
   * @param grid a 2D array of the data
   */
  public static void drawMap(Graphics g, int[][] grid){
      for(int i = 0; i < 480; i++)
      {
          for(int j = 0; j < 840; j++)
          {
              Color gray = new Color(grid[i][j] / 17, grid[i][j] / 17, grid[i][j] / 17);
              g.setColor(gray);
              
              g.drawRect(j, i, 1, 1);
          }
      }
  }
  
  /**
   * Scan a single column of a 2D array and return the index of the
   * row that contains the smallest value
   * @param grid a 2D array
   * @col the column in the 2D array to process
   * @return the index of smallest value from grid at the given col
   */
  public static int indexOfMinInCol(int[][] grid, int col){
      int minRow = -1;
      int min = 9999;
      
      for(int i = 0; i < 480; i++)
       {
               if(grid[i][col] < min)
               {
                  min = grid[i][col];
                  minRow = i;
               }
       }
       
      return minRow;
  }
  
  
  
  /**
   * Find the minimum elevation-change route from West-to-East in the given grid, from the
   * given starting row, and draw it using the given graphics context
   * @param g - the graphics context to use
   * @param grid - the 2D array of elevation values
   * @param row - the starting row for traversing to find the min path
   * @return total elevation of the route
   */
  public static int drawLowestElevPath(Graphics g, int[][] grid, int row){
     int i = row;
     int elevation = 0;
      
    for(int j = 0; j < 839; j++)
    {               //Straight                      //Up                            //Straight                         //Down    
         if(i - 1 < 0)
           i = i + 1;
        else if(i + 1 > 479)
            i--;
        
        int straight = (grid[i][j+1] - grid[i][j]);
        int up = (grid[i - 1][j+1] - grid[i][j]);
        int down = (grid[i + 1][j+1] - grid[i][j]);
        
       

        if((straight < up) && (straight < down))
        {      //straight is least   
            g.drawRect(j + 1, i, 1, 1);
            elevation += Math.abs((grid[i][j+1] - grid[i][j]));
        }               
        else if((up < down) && (up < straight))
        {       //up is least
            g.drawRect(j + 1, i - 1, 1, 1);
            if(i - 1 > 0)
            {
                elevation += Math.abs((grid[i - 1][j+1] - grid[i][j]));
                i--;
            }
        }     
        else if((down < up) && (down < straight))
        {     //down is least
            g.drawRect(j + 1, i + 1, 1, 1);
            if(i + 1 < 480)
            {
                elevation += Math.abs((grid[i + 1][j+1] - grid[i][j]));
                i++;
            }
        }       
        else if(up == down)
        {
            double rand = Math.random();
            int path = (int)(rand * 2);
            
            if(path == 0)
            {
                g.drawRect(j + 1, i + 1, 1, 1);
                if(i + 1 < 480)
                {
                    elevation += Math.abs((grid[i + 1][j+1] - grid[i][j]));
                    i++;
                }
            }
            else if(path == 1)
            {
                g.drawRect(j + 1, i - 1, 1, 1);
                if(i - 1 > 0)
                {
                    elevation += Math.abs((grid[i - 1][j+1] - grid[i][j]));
                    i--;
                }
            }
        }                  //Straight                          //Up                             //Straight                           //Down                   //Straight                        //Down                                 //Straight                //Up
        else if(((straight == up) && (straight < down)) || ((straight == down) && (straight < up)))
        {
           g.drawRect(j + 1, i, 1, 1);
           elevation += Math.abs((grid[i][j+1] - grid[i][j]));
        }
    }

    return elevation;
    }
  
  
  /**
   * Generate all west-to-east paths, find the one with the lowest total elevation change,
   * and return the index of the row that path starts on.
   * @param g - the graphics context to use
   * @param grid - the 2D array of elevation values
   * @return the index of the row where the lowest elevation-change path starts.
   */
  public static int indexOfLowestElevPath(Graphics g, int[][] grid){
      int lowest = 9999999;
      int row = -1;
      
      for(int i = 1; i < 480; i++)
      {
          if(drawLowestElevPath(g, grid, i) < lowest)
          {
            lowest = drawLowestElevPath(g, grid, i);
            row = i;
          }
      }
        
      return row;
  }
     
}