package apgraphicslib;

import java.awt.Color;
import java.awt.Font;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Physics_engine_toolbox {

	public static String[] colorNames = {"black","blue","cyan","gray","green","magenta","orange","pink","red","white","yellow"};
	public static Color[] colors = {Color.black,Color.blue,Color.cyan,Color.GRAY,Color.green,Color.MAGENTA,Color.orange,Color.pink,Color.red,Color.white,Color.YELLOW};
	

	public static Font bigFont = new Font("TimesRoman", Font.BOLD, (int) (Math.sqrt(Math.pow(Settings.width, 2) + Math.sqrt(Math.pow(Settings.height, 2))) / 20  ));
	
	public static Font littleFont = new Font("TimesRoman", Font.PLAIN, 12);
	
	
	public static double getDoubleFromUser(JFrame messageAnchor) {
		
		boolean error = false;
		double num = 0;
		
		try {
			String numStr = JOptionPane.showInputDialog(messageAnchor,"what number?");
			num = Double.parseDouble(numStr);
		}catch(NumberFormatException n) {
			error = true;
			while (error) {
				try {
					String numStr = JOptionPane.showInputDialog(messageAnchor,"Invalid Number");
					num = Double.parseDouble(numStr);
					error = false;
				}catch(NumberFormatException t) {
					error = true;
				}
			}
		}
		
		
		return num;
	}
	
	public static double getDoubleFromUser(JFrame messageAnchor, String message) {
		
		boolean error = false;
		double num = 0;
		
		try {
			String numStr = JOptionPane.showInputDialog(messageAnchor,message);
			num = Double.parseDouble(numStr);
		}catch(NumberFormatException n) {
			error = true;
			while (error) {
				try {
					String numStr = JOptionPane.showInputDialog(messageAnchor,"Invalid Number\n" + message);
					num = Double.parseDouble(numStr);
					error = false;
				}catch(NumberFormatException t) {
					error = true;
				}
			}
		}

		return num;
	}
	
	
	
	public static int getIntegerFromUser(JFrame messageAnchor) {
		
		boolean error = false;
		int num = 0;
		
		try {
			String numStr = JOptionPane.showInputDialog(messageAnchor,"what number?");
			num = Integer.parseInt(numStr);
		}catch(NumberFormatException n) {
			error = true;
			while (error) {
				try {
					String numStr = JOptionPane.showInputDialog(messageAnchor,"Invalid Number\n" + "what number?");
					num = Integer.parseInt(numStr);
					error = false;
				}catch(NumberFormatException t) {
					error = true;
				}
			}
		}

		return num;
	}
	


	public static int getIntegerFromUser(JFrame messageAnchor, String message) {
		
		boolean error = false;
		int num = 0;
		
		try {
			String numStr = JOptionPane.showInputDialog(messageAnchor,message);
			num = Integer.parseInt(numStr);
		}catch(NumberFormatException n) {
			error = true;
			while (error) {
				try {
					String numStr = JOptionPane.showInputDialog(messageAnchor,"Invalid Number\n" + message);
					num = Integer.parseInt(numStr);
					error = false;
				}catch(NumberFormatException t) {
					error = true;
				}
			}
		}

		return num;
	}
	
	
	public static Color getColorFromUser(Physics_frame frame) {
		Color color;
		String colorStr = (String) JOptionPane.showInputDialog(frame, "What Color?", "Color Select", 3, null, colorNames, null);
		
		switch(colorStr) {
			case("black"):
				color = Color.black;
				break;
				
			case("blue"):
				color = Color.blue;
				break;
				
			case("cyan"):
				color = Color.cyan;
				break;
				
			case("gray"):
				color = Color.gray;
				break;
				
			case("green"):
				color = Color.green;
				break;
				
			case("magenta"):
				color = Color.magenta;
				break;
				
			case("orange"):
				color = Color.orange;
				break;
				
			case("pink"):
				color = Color.pink;
				break;
				
			case("red"):
				color = Color.red;
				break;
				
			case("white"):
				color = Color.white;
				break;
				
			case("yellow"):
				color = Color.yellow;
				break;
				
			default:
				color = Color.WHITE;
				Exception colorEx = new Exception("ERROR: Not a valid color: " + colorStr);
				colorEx.printStackTrace();
				break;
		}
		
		return color;
	}
	
	/**
	 * @param system a double[3][2] representing each equation in the form ax + by = c
	 * @return a double[2] representing the {x,y} solutions
	 * {@summary solves a system of 2 linear equations with 2 variables each}
	 */
	public static double[] solveLinearSystem(double[][] system) {
		return solveLinearSystem(system[0][0],system[0][1],system[0][2],system[1][0],system[1][1],system[1][2]);
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @return a double[2] representing the {x,y} solutions
	 * {@summary solves a system of 2 linear equations with 2 variables each of the form ax + by = c , dx + ey = e}
	 */
	public static double[] solveLinearSystem(double a, double b, double c, double d, double e, double f) {
		double y = (f-c*d/a) / (e - b*d/a);
		double x = (c - b*y)/a;
		return new double[] {x,y};
	}
	
	public static void printMatrix(PrintStream printer, double[][] mat) {
		try {
			
			
			for (int r = 0; r < mat.length; r++) {
				printer.print("{");
				for (int c = 0; c < mat[0].length-1; c++) {
					printer.print(mat[r][c] + ",");
				}
				printer.println(mat[r][mat[0].length-1] + "}");
				
			}
		}catch(ArrayIndexOutOfBoundsException a) {
			printer.println("Matrix cannot be printed.");
			printer.println(a);
			a.printStackTrace();
		}
	}
	
	private static double[] getMatrixColumn(double[][] matrix, int column) {
		double[] matColmn = new double[matrix.length];
		
		for (int r = 0; r < matrix.length; r++) {
			matColmn[r] = matrix[r][column];
		}
		
		return matColmn;
	}
	
	private static double matrixVectorDot(double[] u, double[] v ) {
		if (u.length != v.length) {
			Exception e = new Exception("Matrix of size: " + u.length + " cannot be multiplied by a matrix of size: " + v.length);
			e.printStackTrace();
			return 0;
		}
		
		double ans = 0;
		for (int i = 0; i < u.length; i++) {
			ans += u[i]*v[i];
		}
		return ans;
	}
	
	public static double[][] matrixMultiply(double[][] u, double[][] v) {
		return matrixMultiply(u,v,false);
	}
	public static double[][] matrixMultiply(double[][] u, double[][] v, boolean print) {
		if ( (u[0].length != v.length) ) {
			Exception e = new Exception("Matrix of size: " + u.length + "," + u[0].length + " cannot be multiplied by a matrix of size: " + v.length + "," + v[0].length);
			e.printStackTrace();
			return null;
		}
		
		if (print) {
			System.out.println("multiplying");
			printMatrix(System.out,u);
			System.out.println("*");
			printMatrix(System.out,v);
		}
	
		double[][] answer = new double[u.length][v[0].length];

		double rows = answer.length;
		double columns = answer[0].length;
		
		if (print) {
			System.out.println("ansBefore:");
			printMatrix(System.out, answer);
		}
		
		for (int c = 0; c < columns; c++) {
			for (int r = 0; r < rows; r++) {
				answer[r][c] = matrixVectorDot(u[r], getMatrixColumn(v,c));
			}
		}
		
		if (print) {
			System.out.println("ansAfter:");
			printMatrix(System.out, answer);
		}
		
		
		return answer;
	}
	
	public static double distance2D(Coordinate2D point1, Coordinate2D point2) {
		return Math.sqrt( Math.pow(( point2.getX() - point1.getX() ), 2) + Math.pow(( point2.getY() - point1.getY() ), 2));
	}
	
	public static double distance2D(double x1, double y1, double x2, double y2) {
		return Math.sqrt( Math.pow(( x2 - x1 ), 2) + Math.pow(( y2 - y1 ), 2) );
	}
	
	public static double distance3D(Coordinate3D point1, Coordinate3D point2) {
		return Math.sqrt( Math.pow(( point2.getX() - point1.getX() ), 2) + Math.pow(( point2.getY() - point1.getY() ), 2) + Math.pow(( point2.getZ() - point1.getZ() ), 2) );
	}
	
	public static double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt( Math.pow(( x2 - x1 ), 2) + Math.pow(( y2 - y1 ), 2) + Math.pow(( z2 - z1 ), 2) );
	}
	
	/**
	 * {@summary calculates the distance between two points in objects. (Adds the obX, obY, and obZ to the point coords)}
	 * @param ob1X the x position of the first object
	 * @param ob1Y the y position of the first object
	 * @param ob1Z the z position of the first object
	 * @param point1 the point to calculate distance that is in the first object
	 * @param ob2X the x position of the second object
	 * @param ob2Y the y position of the second object
	 * @param ob2Z the z position of the second object
	 * @param point2 the point to calculate distance that is in the second object
	 * @return the distance between the two points
	 */
	public static double objectRelativePointDistance3D(double ob1X, double ob1Y, double ob1Z, Coordinate3D point1, double ob2X, double ob2Y, double ob2Z, Coordinate3D point2) {
		return Math.sqrt( Math.pow(( (point2.getX() + ob2X) - (point1.getX() + ob1X) ), 2) + Math.pow(( (point2.getY() + ob2Y) - (point1.getY() + ob1Y) ), 2) + Math.pow(( (point2.getZ() + ob2Z) - (point1.getZ() + ob1Z) ), 2) );
	}
	
	public static Physics_object loadObjectFromFile(String fileName) {
		
		try {
			System.out.println("Loading object from file...");
			
			
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream(fileName));
			
			
			Physics_object readOb = (Physics_object) loader.readObject();
			
			loader.close();
			
			System.out.println("load successful");
			
			return readOb;
			
		}catch(InvalidClassException e) {
			System.out.println("Corrupted Save_file : " + fileName); 
		}catch(EOFException e) {
			System.out.println("Corrupted Save_file : " + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(fileName + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("load failed");
		
		return null;
	}

	public static double[] resizeArray(double[] array, int size) {
		double[] prevArray = array;
		array = new double[size];
		
		double refillSize;
		if (size > prevArray.length) {
			refillSize = prevArray.length;
		}else {
			refillSize = size;
		}
		
		for (int i = 0; i < refillSize; i++) {
			array[i] = prevArray[i];
		}
		
		return array;
	}
	
	/**
	 * @deprecated
	 * {@code WARNING not tested; test before use, only works for rectangular arrays}
	 * @param array the array to resize
	 * @param x  the size in accordance to [x][y]
	 * @param y  the size in accordance to [x][y]
	 * @return the resized array
	 */
	public static double[][] resizeArray(double[][] array, int x, int y) {
		double[][] prevArray = array;
		array = new double[x][y];
		
		double xSize,ySize;
		if (x > prevArray.length) {
			xSize = prevArray.length;
		}else {
			xSize = x;
		}
		
		if (y > prevArray[0].length) {
			ySize = prevArray[0].length;
		}else {
			ySize = y;
		}
		
		for (int xIndx = 0; xIndx < xSize; xIndx++) {
			for (int yIndx = 0; yIndx < ySize; yIndx++) {
				array[xIndx][yIndx] = prevArray[xIndx][yIndx];
			}
		}
		
		return array;
	}



}

