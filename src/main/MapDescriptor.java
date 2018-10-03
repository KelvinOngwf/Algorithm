package main;

import arena.Arena;

import java.io.*;

/**
*
* @author Chris
*/

public class MapDescriptor {
	
	public static void loadMapFromDisk(Arena arena, String filename) {
		try {

			InputStream inputStream = new FileInputStream("Maps/" + filename);

			BufferedReader buffer = new BufferedReader (new InputStreamReader (inputStream));
			
			String line = buffer.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line);
				line = buffer.readLine();
			}
		
		
			String bin = sb.toString();
			int binPtr = 0;
			for (int x = Arena.arenaX - 1; x >= 0; x--) {
				for (int y = 0; y < Arena.arenaY; y++) {
					if (bin.charAt(binPtr) == '1') arena.placeObstacle(x ,y, true);
					binPtr++;
				}
			}
			
			//Arena.setAllExplored();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String binToHex(String bin) {
		int dec = Integer.parseInt(bin, 2);
		
		return Integer.toHexString(dec);
	}
	
	public static String[] generateMapDescriptor(Arena arena) {
		String[] retStr = new String[2];
		
		StringBuilder Part1 = new StringBuilder();
		StringBuilder Part1_bin = new StringBuilder();
		Part1_bin.append("11");
		for (int a = 0; a <Arena.arenaX; a++ ){
			for(int b = 0; b < Arena.arenaY; b++) {
				if(arena.getCell(a, b).getIsVisited())
					Part1_bin.append("1");
				else
					Part1_bin.append("0");
				
				if(Part1_bin.length() == 4) {
					Part1.append(binToHex(Part1_bin.toString()));
					Part1_bin.setLength(0);
				}							
			}
		}
		Part1_bin.append("11");
		Part1.append(binToHex(Part1_bin.toString()));
		System.out.println("P1: " + Part1.toString());
		retStr[0] = Part1.toString();
		
		StringBuilder Part2 = new StringBuilder();
		StringBuilder Part2_bin = new StringBuilder();
		for(int a = 0; a <Arena.arenaX; a++) {
			for(int b = 0; b < Arena.arenaY; b++) {
				if(arena.getCell(a, b).getIsVisited()) {
					if(arena.getCell(a,b).getIsObstacle())
						Part2_bin.append("1");
					else
						Part2_bin.append("0");
					
					if (Part2_bin.length() == 4) {
						Part2.append(binToHex(Part2_bin.toString()));
						Part2_bin.setLength(0);
					}
				}
			}
		}
		
		if (Part2_bin.length() > 0) Part2.append(binToHex(Part2_bin.toString()));
        System.out.println("P2: " + Part2.toString());
        retStr[1] = Part2.toString();
		
		return retStr;
	}
	
	public static String[] generate2DArray(int[][] map2D_explored, int[][] map2D_obstacle) {
		String[] retStr = new String[2];
		
		StringBuilder Part1 = new StringBuilder();
		StringBuilder Part1_bin = new StringBuilder();
		
		Part1_bin.append("11");
		for (int a = map2D_explored.length-1; a >= 0 ; a--) {
			for(int b = 0; b < map2D_explored[a].length; b++) {
				if(map2D_explored[a][b] == 1)
					Part1_bin.append("1");
				else
					Part1_bin.append("0");
				
				if(Part1_bin.length() == 4) {
					Part1.append(binToHex(Part1_bin.toString()));
					Part1_bin.setLength(0);
				}							
			}
		}
		Part1_bin.append("11");
		Part1.append(binToHex(Part1_bin.toString()));
		System.out.println("P1: " + Part1.toString());
		retStr[0] = Part1.toString();
		
		StringBuilder Part2 = new StringBuilder();
		StringBuilder Part2_bin = new StringBuilder();
		for(int a = map2D_obstacle.length-1; a >= 0; a--) {
			for(int b = 0; b < map2D_obstacle[a].length; b++) {
				if(map2D_explored[a][b] == 1) {
					if(map2D_obstacle[a][b] == 1)
						Part2_bin.append("1");
					else
						Part2_bin.append("0");
					
					if (Part2_bin.length() == 4) {
						Part2.append(binToHex(Part2_bin.toString()));
						Part2_bin.setLength(0);
					}
				}
			}
		}
		
		if (Part2_bin.length() > 0) Part2.append(binToHex(Part2_bin.toString()));
        System.out.println("P2: " + Part2.toString());
        retStr[1] = Part2.toString();
		
		return retStr;
		
		
	}
	
	
	
	
}
