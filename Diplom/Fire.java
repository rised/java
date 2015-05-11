package Diplom;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fire {
    private ArrayList<Point> points;
    private static final List<Integer> VALUES = new ArrayList<Integer>();
    public ArrayList<Point> getPoints() {
        return points;
    }
    public Fire(ArrayList<Point> points) throws NoWayException {
        int number_of_nodes;
        points.add(0, Places.STARTPOINT);
        this.points = points;
        //number_of_nodes = points.size();
        number_of_nodes=points.size();
        int adjacency_matrix[][] = new int[number_of_nodes + 1][number_of_nodes + 1];
        for (int i = 1; i <= number_of_nodes; i++) {
            for (int j = 1; j <= number_of_nodes; j++) {
                if (i == j)
                    adjacency_matrix[i][j] = 0;
                else
                    adjacency_matrix[i][j] = DeicstraArea.getInstance().findWay(points.get(i - 1), points.get(j - 1)).size();
                //отображение матрицы взаимодействий
              //  System.out.print(String.format("%s (%s-%s) ", adjacency_matrix[i][j], Places.pointStringHashMap.get(points.get(i - 1)), Places.pointStringHashMap.get(points.get(j - 1))));
            }
            //для просмотра матрицы взаимодействий
           // System.out.println();System.out.println();
        }
       /*     тестовый прогон со случайными числами
        for (int i = 1; i <= number_of_nodes; i++) {
            for (int j = 1; j <= number_of_nodes; j++) {
                if (i == j)
                    adjacency_matrix[i][j] = 0;
                else
                    adjacency_matrix[i][j] = (int)(Math.random()*500);
                //отображение матрицы взаимодействий
                //  System.out.print(String.format("%s (%s-%s) ", adjacency_matrix[i][j], Places.pointStringHashMap.get(points.get(i - 1)), Places.pointStringHashMap.get(points.get(j - 1))));
            }
            //для просмотра матрицы взаимодействий
            // System.out.println();System.out.println();
        }
        */


        this.points = find(adjacency_matrix);
    }
    ArrayList<Point> find(int adjacencyMatrix[][]) {
        int T0=500;
        int decrement = 1;
        int TEND=50;
        int N = adjacencyMatrix[1].length - 1;       // adjна1 больше изза тупых индексов
        int F=0;
        int j=0;
        double T = T0;
        int Fnew=F;
        double P;
        int countOfReceivers = 5;
        F=calculateF(adjacencyMatrix);
        System.out.println(F);

        for (int k=2; k<=N;k++){
            VALUES.add(k);  //в пул случайных точек добавляем все, кроме стартовой
        }
        while (T > TEND)
        {
            int firstReceiver = (int) (Math.random()*VALUES.size()+2);
            int secondReceiver = (int) (Math.random()*VALUES.size()+2);
            while (firstReceiver==secondReceiver){     //чтобы не было одинаковых точек
                firstReceiver = (int) (Math.random()*VALUES.size()+2);
                secondReceiver = (int) (Math.random()*VALUES.size()+2);
            }
            System.out.println(firstReceiver);
            System.out.println(secondReceiver);
            invert(adjacencyMatrix,firstReceiver,secondReceiver);
            for (int i = 1; i <= N; i++) {
                for (int k = 1; k <= N; k++) {
                    System.out.print(String.format("%s (%s-%s) ", adjacencyMatrix[i][k], Places.pointStringHashMap.get(points.get(i - 1)), Places.pointStringHashMap.get(points.get(k - 1))));
                }
                //для просмотра матрицы взаимодействий
                System.out.println();System.out.println();
            }
            Fnew=calculateF(adjacencyMatrix);
            P=Math.exp(-Math.abs(F - Fnew)/T);
            System.out.println("Вероятность - " + P);
            if (Fnew<F){        //целевая функция улучшилась?
                F=Fnew;
            }
            else if (Math.random()<P){       //если не улучшилась сравнить вероятность перехода в новое состояние со случайным числом
                F=Fnew;
                System.out.println("Сработало! Перешли в хреновое состояние!");
            }

            T = T0 * Math.exp((-decrement) * Math.pow(j, (double)(decrement) / (2*(double)countOfReceivers)));
            ++j;
            System.out.println(F);
        }

        System.out.println("Итераций " + j);
        ArrayList<Point> multiPointPath = new ArrayList<Point>();
        for (int i=1; i<adjacencyMatrix[1].length;i++){
            if (i!=adjacencyMatrix[1].length-1){    }

        }
        // чтобы записать полученный путь, нужно перед начальным присвоением целевой функции, создать список типа START-A-B-C-D
        // и когда мы что либо изменяем в матрице, симметрично менять в этом списке.
        return  multiPointPath;
    }

    public static void main (String [] args) throws IOException, NoWayException {
        new TestClass();
        long before = System.currentTimeMillis();
        ArrayList<Point> testlist = new ArrayList<Point>();
        testlist.add(Places.POINTA);
        testlist.add(Places.POINTB);
        //testlist.add(Places.POINTC);
        testlist.add(Places.POINTD);
        new Fire(testlist);
        long after = System.currentTimeMillis();
        long diff = after - before;
        System.out.println("Время работы "+diff);
    }
    public static int [][] invert(int adjacencyMatrix[][], int firstReceiver, int secondReceiver){
        int N = adjacencyMatrix[1].length - 1;
        for (int i=1;i<=N;i++){
            int change1;
            for (int j = 1; j <= N; j++) {
                if (i == j)
                    adjacencyMatrix[i][j] = 0;
                else if (j == firstReceiver && i!=secondReceiver){
                    change1=adjacencyMatrix[i][j];
                    adjacencyMatrix[i][j]=adjacencyMatrix[i][secondReceiver];
                    adjacencyMatrix[i][secondReceiver]=change1;
                }
                if (i == j)
                    adjacencyMatrix[i][j] = 0;
                else if (i == firstReceiver && j!=secondReceiver){
                    change1=adjacencyMatrix[i][j];
                    adjacencyMatrix[i][j]=adjacencyMatrix[secondReceiver][j];
                    adjacencyMatrix[secondReceiver][j]=change1;
                }
            }
        }
        return adjacencyMatrix;
    }
    private int calculateF(int adjacencyMatrix[][]){
        int F=0;
        int N = adjacencyMatrix.length;
        for (int i=1; i<N;i++){
            if (i!=N-1)
                F+=adjacencyMatrix[i][i+1];
            else F+=adjacencyMatrix[1][N-1];     // из последней в старт
        }
        return F;
    }

}
