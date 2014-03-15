package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Generator implements Runnable
{
    private static final Generator ourInstance = new Generator();
    private volatile ArrayList<Order> orderQueue = new ArrayList<Order>();
    public static int countOfCompleteOrders;
    public static int PotentialCollisions;

    private GeneratorType EXECtype = new GeneratorC();   // здесь задаем тип генератора, который будем использовать

    public static Generator getInstance()
    {
        return ourInstance;
    }
    public ArrayList<Order> getOrderQueue() {
        return orderQueue;
    }
    private Generator()
    {
    }

    //region Типы генераторов
    public interface GeneratorType {
        public void generateOrders();
    }
    public class GeneratorA implements GeneratorType{
        @Override
        public void generateOrders()
        {
            try
            {
                //large count of orders
                Point randompoint = Places.randomPlace();
                Order order = new Order(randompoint,Order.Sizes.randomSize(),(int)(Math.random()*Priorities.countOfPriorities+1));  //генерим заказы
                orderQueue.add(0,order);
                Collections.sort(orderQueue,new SortedByPriority());  //после каждого добавления очередь сортируется по приоритетам
                System.out.println(String.format("Поступил заказ номер %s, точка назначения: %s , вес %s, приоритет %s",order.getID(),Places.pointStringHashMap.get(randompoint),order.getWeight(),order.getPriority()));
                Thread.sleep((int)(Math.random()*1000));      //время через которое поступают заказы
            }
            catch (InterruptedException ignore)    /*NOP*/
            {
            }
        }
    }
    public class GeneratorB implements GeneratorType{
        @Override
        public void generateOrders()
        {
            try
            {
                //small count of orders
                Order order = new Order(Places.randomPlace(),Order.Sizes.randomSize(),(int)(Math.random()*Priorities.countOfPriorities+1));  //генерим заказы
                orderQueue.add(0,order);
                Collections.sort(orderQueue,new SortedByPriority());
                System.out.println(String.format("Поступил заказ номер %s, точка назначения: %s , вес %s, приоритет %s",order.getID(),order.getDestinationPoint(),order.getWeight(),order.getPriority()));
                Thread.sleep((int)(Math.random()*10000));      //время через которое поступают заказы
            }
            catch (InterruptedException ignore)    /*NOP*/
            {
            }
        }
    }
    public class GeneratorC implements GeneratorType{
        @Override
        //заказы по 10 весом
        public void generateOrders()
        {
            try
            {
                //large count of orders
                Point randompoint = Places.randomPlace();
                Order order = new Order(randompoint,10,(int)(Math.random()*Priorities.countOfPriorities+1));  //генерим заказы
                orderQueue.add(0,order);
                Collections.sort(orderQueue,new SortedByPriority());  //после каждого добавления очередь сортируется по приоритетам
                System.out.println(String.format("Поступил заказ номер %s, точка назначения: %s , вес %s, приоритет %s",order.getID(),Places.pointStringHashMap.get(randompoint),order.getWeight(),order.getPriority()));
                Thread.sleep((int)(Math.random()*1000));      //время через которое поступают заказы
            }
            catch (InterruptedException ignore)    /*NOP*/
            {
            }
        }
    }
    //endregion

    @Override
    public void run()
    {
            while (!Core.isStopped)
            {
            EXECtype.generateOrders();
            }
    }
    private static class SortedByPriority implements Comparator<Order>{ //сортировка очереди по приоритету (1,2,3)
        @Override
        public int compare(Order o1, Order o2)
        {
            int priority1 = o1.getPriority();
            int priority2 = o2.getPriority();

            if(priority1 > priority2) {
                return 1;
            }
            else if(priority1 < priority2) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
