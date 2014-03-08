package Diplom;


import java.util.ArrayList;

public class Core
{
    public static boolean isStopped = false;
    private static ArrayList<Thread> robots = new ArrayList<Thread>();
    public static void main(String []args) throws InterruptedException
    {
        new TestClass();  //подключаем модуль отрисовки формы и АОП
        Thread robot1 = new Thread(new LightRobot(1));
        Thread robot2 = new Thread(new CargoRobot(2));
        Thread robot3 = new Thread(new LightRobot(3));
        Thread coordinator = new Thread(Generator.getInstance());
        robot1.start();
        robot2.start();
        robot3.start();
        coordinator.start();
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);

        Thread.sleep(10000);   //общее время работы программы
        isStopped=true;
        System.out.println("Остановка модели...");
        System.out.println(String.format("Выполнено заказов %s", Generator.countOfCompleteOrders));

    }
    public static class LightRobot extends Robot{
        final static int MAX_CAPACITY = 50;
        public LightRobot(int ID)
        {
            super(ID, LightRobot.MAX_CAPACITY, "Легковой");
        }
    }
    public static class CargoRobot extends Robot{
        final static int MAX_CAPACITY=100;
        public CargoRobot(int ID)
        {
            super(ID, CargoRobot.MAX_CAPACITY, "Грузовой");
        }
    }

}
