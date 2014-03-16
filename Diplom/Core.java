package Diplom;


import java.awt.*;
import java.util.ArrayList;

public class Core
{
    public static boolean isStopped = false;
    public static ArrayList<Robot> robots = new ArrayList<Robot>();
    public static TestClass repainting;
    public static int finish=0;
    public static void main(String []args) throws InterruptedException, NoWayException {
        repainting= new TestClass();  //подключаем модуль отрисовки формы и АОП
        //new oldtsp(); //модуль решения задачи коммивояжера
        LightRobot lightRobot1 = new LightRobot(1, Color.GREEN);
        CargoRobot cargoRobot1 = new CargoRobot(2, Color.YELLOW);
        LightRobot lightRobot3 = new LightRobot(3, Color.BLUE);
        Thread robot1 = new Thread(lightRobot1);
        Thread robot2 = new Thread(cargoRobot1);
        Thread robot3 = new Thread(lightRobot3);
        Thread generator = new Thread(Generator.getInstance());
        robots.add(lightRobot1);
        robots.add(cargoRobot1);
        robots.add(lightRobot3);
        robot1.start();
        robot2.start();
        robot3.start();
        generator.start();
        Thread.sleep(30000);   //общее время работы программы
        isStopped=true;
        //while(Core.finish!=3){} //не работает!!!!
        System.out.println("Остановка модели...");
        System.out.println(String.format("Выполнено заказов %s, потенциальных столкновений %s", Generator.countOfCompleteOrders, Generator.PotentialCollisions));
    }
    public static class LightRobot extends Robot{
        final static int MAX_CAPACITY = 50;
        public LightRobot(int ID, Color color)
        {
            super(ID, LightRobot.MAX_CAPACITY, "Легковой", color);
        }
    }
    public static class CargoRobot extends Robot{
        final static int MAX_CAPACITY=100;
        public CargoRobot(int ID, Color color)
        {
            super(ID, CargoRobot.MAX_CAPACITY, "Грузовой", color);
        }
    }

}
