package Diplom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Coordinator implements Runnable {
    private static Coordinator ourInstance = new Coordinator();
    private volatile ArrayList<Order> orderQueue = new ArrayList<Order>();  // (new SortByPrio)
    private static int countOfCompleteOrders;

    public static Coordinator getInstance() {
        return ourInstance;
    }

    public static Coordinator getOurInstance() {
        return ourInstance;
    }

    public Coordinator() {
    }

    public ArrayList<Order> getOrderQueue() {
        return orderQueue;
    }

    @Override
    public void run() {
        while (!Core.isStopped) {
            try {  //генератор заказов
                Order order = new Order(Places.randomPlace(), (int) (Math.random() * 100), (int) (Math.random() * Priorities.countOfPriorities + 1));  //генерим заказы
                orderQueue.add(0, order);
                Collections.sort(orderQueue, new SortedByPriority());
                System.out.println(String.format("Поступил заказ номер %s, точка назначения: %s , вес %s, приоритет %s", order.getID(), order.getDestinationPoint(), order.getWeight(), order.getPriority()));
                Thread.sleep((int) (Math.random() * 1000));      //время через которое поступают заказы
            } catch (InterruptedException ignore)    /*NOP*/ {
            }
        }
    }

    public static class SortedByPriority implements Comparator<Order> { //сортировка очереди по приоритету (1,2,3)

        @Override
        public int compare(Order o1, Order o2) {
            int priority1 = o1.getPriority();
            int priority2 = o2.getPriority();

            if (priority1 > priority2) {
                return 1;
            } else if (priority1 < priority2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
