package main;


import arena.*;

public class test{
	public static void main(String[] args) {
		
		int[][] map2D_explored = new int[20][15];
		int[][] map2D_obstacle = new int[20][15];
		int[][] map2D_explored2 = new int[20][15];
		int[][] map2D_obstacle2 = new int[20][15];
		
		for(int i = 0; i<20; i++) {
			for(int j =0;j<15;j++) {
				map2D_explored[i][j] = 1;
				map2D_obstacle[i][j] = 0;
			}
		}
		
		for(int i = 0; i<20; i++) {
			for(int j =0;j<15;j++) {
				map2D_explored2[i][j] = 1;
				map2D_obstacle2[i][j] = 0;
			}
		}
		
		// sample arena 1
		map2D_obstacle[2][7] = 1;
		map2D_obstacle[5][13] = 1;
		map2D_obstacle[5][12] = 1;
		map2D_obstacle[5][11] = 1;
		map2D_obstacle[6][0] = 1;
		map2D_obstacle[6][1] = 1;
		map2D_obstacle[6][2] = 1;
		map2D_obstacle[6][3] = 1;
		map2D_obstacle[6][4] = 1;
		map2D_obstacle[6][5] = 1;
		map2D_obstacle[9][10] = 1;
		map2D_obstacle[11][5] = 1;
		map2D_obstacle[11][6] = 1;
		map2D_obstacle[11][7] = 1;
		map2D_obstacle[14][1] = 1;
		map2D_obstacle[15][11] = 1;
		map2D_obstacle[15][12] = 1;
		map2D_obstacle[15][13] = 1;
		map2D_obstacle[17][7] = 1;
		
		// sample arena 2
		map2D_obstacle2[0][8] = 1;
		map2D_obstacle2[3][4] = 1;
		map2D_obstacle2[4][4] = 1;
		map2D_obstacle2[5][4] = 1;
		map2D_obstacle2[5][9] = 1;
		map2D_obstacle2[5][10] = 1;
		map2D_obstacle2[5][11] = 1;
		map2D_obstacle2[5][12] = 1;
		map2D_obstacle2[5][13] = 1;
		map2D_obstacle2[5][14] = 1;
		map2D_obstacle2[9][1] = 1;
		map2D_obstacle2[9][6] = 1;
		map2D_obstacle2[9][7] = 1;
		map2D_obstacle2[14][3] = 1;
		map2D_obstacle2[14][4] = 1;
		map2D_obstacle2[14][5] = 1;
		map2D_obstacle2[14][10] = 1;
		map2D_obstacle2[15][5] = 1;
		map2D_obstacle2[15][10] = 1;
		map2D_obstacle2[16][10] = 1;
		map2D_obstacle2[17][10] = 1;
		
		//MapDescriptor.generate2DArray(map2D_explored,map2D_obstacle);
		MapDescriptor.generate2DArray(map2D_explored2,map2D_obstacle2);
		
		
		
	}
}

