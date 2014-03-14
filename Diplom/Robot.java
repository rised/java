package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Robot implements Runnable
{
    //region Типы исполнения

    public interface RobotExecutionType
    {
        public void pickOrderByQueue(ArrayList<Order> queue);
    }
    public class RobotExecutionA implements RobotExecutionType
    {    /* Робот просматривает всю очередь, и выбирает себе наиболее подходящий по весу - грузовой берет более тяжелый, легковой берет более легкий.
          Если тяжелых не будет - то грузовой не возьмет ничего.
          запускать с GeneratorA
    */
        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
        {
            for (Order order : queue)
            {
                if (getCAPACITY()>order.getWeight() && order.getWeight()> Core.LightRobot.MAX_CAPACITY && getType().equals("Грузовой"))
                {
                    pickOrderFromTopQueue(order);
                    break;
                }
                else if (getCAPACITY()>order.getWeight() && order.getWeight()<=Core.LightRobot.MAX_CAPACITY && getType().equals("Легковой"))
                {
                    pickOrderFromTopQueue(order);
                    break;
                }
            }

        }
    }
    public class RobotExecutionB implements RobotExecutionType
    {
        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
        {   //Оптимизация-1 Роботы берут из очереди несколько заказов для одного получателя
            for (Order order : queue) {

            }
        }
    }
    public class RobotExecutionС implements RobotExecutionType  //робот берет из очереди несколько заказов, Максимально загружаясь, втупую пытается взять подряд топ очереди
    {
        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
            // запускать с GeneratorA
        {   //Оптимизация-2 Роботы берут из очереди несколько заказов для разных получателей   (+развозить должны по задаче о комивояжере)
            for (Order order : queue) {
                if (getCAPACITY()>order.getWeight() && order.getWeight()> Core.LightRobot.MAX_CAPACITY && getType().equals("Грузовой"))
                {
                    pickOrderFromTopQueue(order);

                }
                else if (getCAPACITY()>order.getWeight() && order.getWeight()<=Core.LightRobot.MAX_CAPACITY && getType().equals("Легковой"))
                {
                    pickOrderFromTopQueue(order);

                }
            }
        }
    }
    //endregion

    private final RobotExecutionType EXECtype = new RobotExecutionA(); // здесь задаем тип исполнения
    private final int ID;
    private final String type;
    private final Point StoragePoint = DeicstraArea.getInstance().getCell((int)Places.STARTPOINT.getX(),(int)Places.STARTPOINT.getY()).getPosition();
    private int capacity;
    private boolean free=true;
    private Point EndPoint;
    private Point CurrentPoint=StoragePoint;
    private ArrayList<Order> myOrders = new ArrayList<Order>();
    private ArrayList<Point> path;

    public Robot(int ID, int CAPACITY, String type){   //сделать ENUM для типов
        this.ID=ID;
        this.capacity = CAPACITY;
        this.type=type;
    }
    //region Сеттеры
    private void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }
    private void setFree(boolean free)
    {
        this.free = free;
    }

    private void setEndPoint(Point endPoint)
    {
        EndPoint = endPoint;
    }

    private void setCurrentPoint(Point currentPoint)
    {
        CurrentPoint = currentPoint;
    }
    //endregion
    //region Геттеры
    private Point getCurrentPoint()
    {
        return CurrentPoint;
    }
    private Point getStoragePoint()
    {
        return StoragePoint;
    }
    private ArrayList<Order> getMyOrders()
    {
        return myOrders;
    }
    private String getType(){
        return type;
    }

    private int getID()
    {
        return ID;
    }

    private boolean isFree()
    {
        return free;
    }

    private int getCAPACITY()

    {
        return capacity;
    }

    //endregion

    @Override
    public void run()
    {
        while(!Core.isStopped)
        {
            ArrayList<Order> queue = Generator.getInstance().getOrderQueue();
            //region Робот свободен, взятие заказа
            if(isFree())
            { // одновременно доступ к очереди имеет только один робот
                synchronized (queue)
                {
                if (!queue.isEmpty() && getCurrentPoint().equals(getStoragePoint()))
                {
                        try
                        {
                            Thread.sleep(1000); // задержка на взятие заказа
                        }
                        catch (InterruptedException ignore)
                        {}
                            try
                            {
                               EXECtype.pickOrderByQueue(queue);
                            }
                         catch (NullPointerException ignore){}
                    }
                }
            }
            //endregion
            //region Едет с заказом
            try
            {
                moveToDestinationWithOrder(EndPoint);
            }
            catch (NoWayException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //endregion
            //region Едет пустой домой
            try
            {   if (!getCurrentPoint().equals(getStoragePoint()))
                simpleMove(getStoragePoint());
            }
            catch (Exception e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //endregion
        }
        Core.finish++;
    }
    private void moveToDestinationWithOrder(Point endpoint) throws NoWayException, InterruptedException
    {
        if (endpoint ==null )   return;
        System.out.println(String.format("%s Робот %s c заказом %s поехал в точку назначения %s", getType(), getID(), getMyOrders().get(0).getID(), Places.pointStringHashMap.get(getMyOrders().get(0).getDestinationPoint())));
        path = DeicstraArea.getInstance().findWay(endpoint,StoragePoint);// тут на самом деле переменные наоброт
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        setCurrentPoint(endpoint);
        setCapacity(getCAPACITY() + getMyOrders().get(0).getWeight());
        Generator.countOfCompleteOrders++;
        System.out.println(String.format("%s Робот %s c заказом %s приехал в точку назначения %s, стало ресурсов %s", getType(), getID(), getMyOrders().get(0).getID(), Places.pointStringHashMap.get(getMyOrders().get(0).getDestinationPoint()), getCAPACITY()));
        getMyOrders().remove(0);
        setFree(true);
        setEndPoint(getStoragePoint());
    }
    private void simpleMove(Point endpoint) throws NoWayException, InterruptedException
    {
        if (endpoint ==null )   return;
        System.out.println(String.format("%s Робот %s едет обратно на склад", getType(), getID()));
        path = DeicstraArea.getInstance().findWay(endpoint,CurrentPoint);  //точки стоят наоборот чтобы правильно рисовало
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        System.out.println(String.format("%s Робот %s на складе", getType(), getID()));
        setCurrentPoint(getStoragePoint());
    }
    private void pickOrderFromTopQueue(Order order){
        setEndPoint(order.getDestinationPoint()); //добавить установка дестинейшна (EndPoint List) если берем несколько заказов
        setCapacity(getCAPACITY() - order.getWeight());
        System.out.println(String.format("%s Робот %s взял заказ %s, приоритета %s, осталось ресурсов %s",getType(),getID(),order.getID(),order.getPriority(),getCAPACITY()));
        Generator.getInstance().getOrderQueue().remove(0);  //  забираем топ очереди
        getMyOrders().add(order);    //суем в массив своих заказов
        setFree(false);
    }

    //region Colors
    enum Colors{
        ;
        private static ArrayList<Color> VALUES = new ArrayList<Color>();
        static {
            VALUES.add(Color.red);
            VALUES.add(Color.blue);
            VALUES.add(Color.CYAN);
            VALUES.add(Color.ORANGE);
            VALUES.add(Color.YELLOW);
            VALUES.add(Color.MAGENTA);
            VALUES.add(Color.PINK);

        }
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Color randomColor()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    //endregion
    public void paintPath(ArrayList<Point> path) throws InterruptedException {
        Color one = Colors.randomColor();
        Color two = Colors.randomColor();
        for (Point point : path){
            if (Core.repainting.getArea()[point.x][point.y]==2){
                System.out.println(String.format("Потенциальное столкновение. Робот %s ждет",getID()));
                //while(Core.repainting.getArea()[point.x][point.y]==2){}
                Thread.sleep(30);
                System.out.println(String.format("Робот %s продолжает движение",getID()));
                Generator.PotentialCollisions++;
                }
            DeicstraArea.getInstance().getCell(point.x, point.y).setColor(one);
            Core.repainting.repaint();
            Core.repainting.getArea()[point.x][point.y]=2;
            Thread.sleep(30);
            Core.repainting.getArea()[point.x][point.y]=1;
            DeicstraArea.getInstance().getCell(point.x, point.y).setColor(two);
        }
    }
}
