package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
//https://sites.google.com/site/indy256/algo/dynamic_tsp
public class TSP {
    public TSP() throws NoWayException {
        ArrayList<Point> pathAB = DeicstraArea.getInstance().findWay(Places.POINTA,Places.POINTB);
        ArrayList<Point> pathAC = DeicstraArea.getInstance().findWay(Places.POINTA,Places.POINTC);
        ArrayList<Point> pathBC = DeicstraArea.getInstance().findWay(Places.POINTB,Places.POINTC);
        ArrayList<Point> pathBA = DeicstraArea.getInstance().findWay(Places.POINTB,Places.POINTA);
        ArrayList<Point> pathCA = DeicstraArea.getInstance().findWay(Places.POINTC,Places.POINTA);
        ArrayList<Point> pathCB = DeicstraArea.getInstance().findWay(Places.POINTC,Places.POINTB);
        ArrayList<Point> pathASTART = DeicstraArea.getInstance().findWay(Places.POINTA,Places.STARTPOINT);
        ArrayList<Point> pathBSTART = DeicstraArea.getInstance().findWay(Places.POINTB,Places.STARTPOINT);
        ArrayList<Point> pathCSTART = DeicstraArea.getInstance().findWay(Places.POINTC,Places.STARTPOINT);
        System.out.println(String.format("AB %s, AC %s, BC %s, BA %s, CA %s, CB %s, ASTART %s, BSTART %s, CSTART %s",pathAB.size(),pathAC.size(),pathBC.size(),pathBA.size(),pathCA.size(),pathCB.size(),pathASTART.size(),pathBSTART.size(),pathCSTART.size()));
        int[][] dist = { { 0, pathAB.size(), pathAC.size(), pathASTART.size()}, { pathAB.size(), 0, pathBC.size(), pathBSTART.size()}, { pathAC.size(), pathBC.size(), 0, pathCSTART.size()}, { pathASTART.size(), pathBSTART.size(), pathCSTART.size(),0}};
        System.out.println(getShortestTSPPath(dist));


    }

        public static int getShortestTSPPath(int[][] dist) {
            int n = dist.length;
            int[][] dp = new int[1 << n][n];    //destination point?
            for (int[] d : dp)
                Arrays.fill(d, Integer.MAX_VALUE / 2);
            dp[1][0] = 0;
            for (int mask = 1; mask < 1 << n; mask += 2) {
                for (int i = 1; i < n; i++) {
                    if ((mask & 1 << i) != 0) {
                        for (int j = 0; j < n; j++) {
                            if ((mask & 1 << j) != 0) {
                                dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j] + dist[j][i]);
                            }
                        }
                    }
                }
            }
            int res = Integer.MAX_VALUE;
            for (int i = 1; i < n; i++) {
                res = Math.min(res, dp[(1 << n) - 1][i] + dist[i][0]);
            }

            // reconstruct path
            int cur = (1 << n) - 1;
            int[] order = new int[n];
            int last = 0;
            for (int i = n - 1; i >= 1; i--) {
                int bj = -1;
                for (int j = 1; j < n; j++) {
                    if ((cur & 1 << j) != 0 && (bj == -1 || dp[cur][bj] + dist[bj][last] > dp[cur][j] + dist[j][last])) {
                        bj = j;
                    }
                }
                order[i] = bj;
                cur ^= 1 << bj;
                last = bj;
            }
            System.out.println(Arrays.toString(order));
            return res;
        }
    }

