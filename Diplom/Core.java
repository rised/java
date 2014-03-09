package Diplom;


import java.util.ArrayList;

public class Core
{
    public static boolean isStopped = false;
    public static ArrayList<Robot> robots = new ArrayList<Robot>();
    public static TestClass repainting;
    public static int finish=0;
    public static void main(String []args) throws InterruptedException
    {
        repainting= new TestClass();  //подключаем модуль отрисовки формы и АОП
        LightRobot lightRobot1 = new LightRobot(1);
        CargoRobot cargoRobot1 = new CargoRobot(2);
        LightRobot lightRobot3 = new LightRobot(3);
        Thread robot1 = new Thread(lightRobot1);
        Thread robot2 = new Thread(cargoRobot1);
        Thread robot3 = new Thread(lightRobot3);
        Thread generator = new Thread(Generator.getInstance());
        robot1.start();
        robot2.start();
        robot3.start();
        generator.start();
        robots.add(lightRobot1);
        robots.add(cargoRobot1);
        robots.add(lightRobot3);

        Thread.sleep(20000);   //общее время работы программы
        isStopped=true;
        while(Core.finish!=3){} //какого то хуя не работает!!!!
        System.out.println("Остановка модели...");
        System.out.println(String.format("Выполнено заказов %s, потенциальных столкновений %s", Generator.countOfCompleteOrders, Generator.PotentialCollisions));
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
