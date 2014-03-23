package Diplom;


import java.util.ArrayList;

public class Core {
    public static boolean isStopped = false;
    public static ArrayList<Robot> robots = new ArrayList<Robot>();
    public static TestClass repainting;
    public static int finish = 0;
    private static final int numberOfRobots = 5;

    public static void main(String[] args) throws InterruptedException, NoWayException {
        repainting = new TestClass();  //подключаем модуль отрисовки формы и АОП
        /*for (int i = 0;i<numberOfRobots;i++){

        } */
        LightRobot lightRobot1 = new LightRobot(1);
        CargoRobot cargoRobot2 = new CargoRobot(2);
        LightRobot lightRobot3 = new LightRobot(3);
        CargoRobot cargoRobot4 = new CargoRobot(4);
        Thread robot1 = new Thread(lightRobot1);
        Thread robot2 = new Thread(cargoRobot2);
        Thread robot3 = new Thread(lightRobot3);
        Thread robot4 = new Thread(cargoRobot4);
        Thread generator = new Thread(Generator.getInstance());
        robots.add(lightRobot1);
        robots.add(cargoRobot2);
        robots.add(lightRobot3);
        //robots.add(cargoRobot4);
        robot1.start();
        robot2.start();
        robot3.start();
        //robot4.start();
        generator.start();
        Thread.sleep(30000);   //общее время работы программы
        isStopped = true;
        //while(Core.finish!=3){} //не работает!!!!
        System.out.println("Остановка модели...");
        System.out.println(String.format("Выполнено заказов %s, потенциальных столкновений %s", Generator.countOfCompleteOrders, Generator.PotentialCollisions));
    }

    public static class LightRobot extends Robot {
        final static int MAX_CAPACITY = 50;
        final static int MAX_ENERGY = 10000;
        final static int energyPerMove = 1;

        public LightRobot(int ID) {
            super(ID, LightRobot.MAX_CAPACITY, "Легковой", energyPerMove, MAX_ENERGY);
        }
    }

    public static class CargoRobot extends Robot {
        final static int MAX_CAPACITY = 100;
        final static int MAX_ENERGY = 15000;
        final static int energyPerMove = 2;

        public CargoRobot(int ID) {
            super(ID, CargoRobot.MAX_CAPACITY, "Грузовой", energyPerMove, MAX_ENERGY);
        }
    }

}
