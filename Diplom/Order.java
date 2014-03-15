package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Order
{
    //justify sex
    private int weight;
    private int priority;
    private static int totalOrderCount;
    private int ID;
    private double completetime;
    private Point DestinationPoint;
    public Order (Point destinationPoint,int weight,int priority){
        this.DestinationPoint=destinationPoint;
        this.weight=weight;
        this.priority=priority;
        totalOrderCount++;
        this.ID=totalOrderCount;
    }
    //region Сеттеры
    public void setWeight(int weight)
    {
        this.weight = weight;
    }


    //endregion
    //region Геттеры
    public Point getDestinationPoint()
    {
        return DestinationPoint;
    }

    public int getWeight()
    {
        return weight;
    }

    public int getPriority()
    {
        return priority;
    }

    public static int getTotalOrderCount()
    {
        return totalOrderCount;
    }

    public int getID()
    {
        return ID;
    }
    enum Sizes{
        ;
        private static ArrayList<Integer> VALUES = new ArrayList<Integer>();
        static {

            for (int i=10; i<101;){
             VALUES.add(i);
            i+=10;
                }
        }
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Integer randomSize()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }
    //endregion
}
