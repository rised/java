package Diplom;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
    public class RobotExecutionС implements RobotExecutionType
    {
        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
            /* запускать с GeneratorA
            *Оптимизация-2 Роботы берут из очереди несколько заказов для разных получателей   (+развозить должны по задаче о комивояжере)
            *робот берет из очереди несколько заказов, Максимально загружаясь, втупую пытается взять подряд топ очереди
            */
        {
            for (int i=0;i<queue.size();i++){
                if (getCAPACITY()>=queue.get(i).getWeight()){
                    pickOrderFromTopQueue(queue.get(i));
                    i--;
                }
            }
        }
    }
    //endregion


    /**
    *
    * @param path - кратчайший путь из всех точек, которые необходимо дойти из одной точки в другую
    * @param multipath - список точек, которые нужно посетить роботу в случае если он взял больше одного заказа.
    *
     * */
    private final RobotExecutionType EXECtype = new RobotExecutionС(); // здесь задаем тип исполнения
    private final int ID;
    private final String type;
    private final Point StoragePoint = DeicstraArea.getInstance().getCell((int)Places.STARTPOINT.getX(),(int)Places.STARTPOINT.getY()).getPosition();
    private int capacity;
    private int energyRatePerMove;
    private int energy;
    private boolean free=true;
    private static  ArrayList<Order> queue = Generator.getInstance().getOrderQueue();
    private Point EndPoint;
    private Point CurrentPoint=StoragePoint;
    private ArrayList<Order> orders = new ArrayList<Order>();
    private ArrayList<Point> path;
    private ArrayList<Point> multipath;

    public Robot(int ID, int CAPACITY, String type, int energyPerMove, int energy){
        this.ID=ID;
        this.capacity = CAPACITY;
        this.type=type;
        this.energy=energy;
        this.energyRatePerMove =energyPerMove;
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

    public int getEnergy() {
        return energy;
    }
    private Point getCurrentPoint()
    {
        return CurrentPoint;
    }
    private Point getStoragePoint()
    {
        return StoragePoint;
    }
    private ArrayList<Order> getOrders()
    {
        return orders;
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
            //ArrayList<Order> queue = Generator.getInstance().getOrderQueue();
            //region Если робот свободен и на находится на складе, то взять заказ
            if(isFree())
            { // одновременно доступ к очереди имеет только один робот
                synchronized (queue)
                {
                if (!queue.isEmpty() && getCurrentPoint().equals(getStoragePoint()))
                {
                        try
                        {
                            Thread.sleep(1000); // задержка на взятие заказа
                            EXECtype.pickOrderByQueue(queue);
                        }
                        catch (Exception e){
                            System.out.println("pickorder" + e.getMessage());
                        }

                    //region Блок коммиовяжера(можно отключить )
                    ArrayList<Point> TSPpoints = buildUniqueListByOrders();  // получаем точки которые нужно посетить
                    try {
                        TSPNN TSP = new TSPNN(TSPpoints);
                        multipath=TSP.getPoints();
                        //подключение модуля комивояжера
                    } catch (Exception e) {
                        System.out.println("TSPNN:" + e.getMessage());
                    }
                    //endregion
                    }
                }
                //endregion

            }
            //region Едет с заказом
            try
            {
                //moveToDestinationWithOrder(EndPoint);
                moveToDestinationWithOrder(multipath);
            }
            catch (Exception e)
            {
                System.out.println("moveToDest"+e.getMessage());
            }
            //endregion
            //region Едет пустой домой
            try
            {   if (!getCurrentPoint().equals(getStoragePoint()))
                moveToStorage();
            }
            catch (Exception e)
            {
                System.out.println("simplemove:" + e.getMessage());
            }
            //endregion
        }
        Core.finish++;
    }
    private void moveToDestinationWithOrder(Point endpoint) throws NoWayException, InterruptedException, IOException {
        //единственная точка назначения
        if (endpoint ==null )   return;
        System.out.println(String.format("%s Робот %s c заказом %s поехал в точку назначения %s", getType(), getID(), getOrders().get(0).getID(), Places.pointStringHashMap.get(getOrders().get(0).getDestinationPoint())));
        path = DeicstraArea.getInstance().findWay(endpoint,StoragePoint);// тут на самом деле переменные наоброт
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        setCurrentPoint(endpoint);
        setCapacity(getCAPACITY() + getOrders().get(0).getWeight());
        Generator.countOfCompleteOrders++;
        System.out.println(String.format("%s Робот %s c заказом %s приехал в точку назначения %s, стало ресурсов %s", getType(), getID(), getOrders().get(0).getID(), Places.pointStringHashMap.get(getOrders().get(0).getDestinationPoint()), getCAPACITY()));
        getOrders().remove(0);
        setFree(true);
        setEndPoint(getStoragePoint());
    }
    private void moveToDestinationWithOrder(ArrayList<Point> multipath) throws NoWayException, InterruptedException {
       if(multipath ==null) return;
       for (int i=0; i<multipath.size();i++)
       {
           EndPoint=multipath.get(i);
           System.out.println(String.format("%s Робот %s c заказами поехал в точку назначения %s", getType(), getID(), Places.pointStringHashMap.get(EndPoint)));
           path = DeicstraArea.getInstance().findWay(EndPoint,CurrentPoint);
           DeicstraArea.getInstance().optimizeWay(path);
           paintPath(path);
           setCurrentPoint(EndPoint);
           System.out.println(String.format("%s Робот %s c заказоми приехал в точку назначения %s", getType(), getID(), Places.pointStringHashMap.get(EndPoint)));
           for (int j=0;j< orders.size();j++)
           {   //смотрим какие заказы надо отдать
               if(CurrentPoint.equals(orders.get(j).getDestinationPoint()))
               {
                   setCapacity(getCAPACITY() + getOrders().get(j).getWeight());
                   System.out.println(String.format("Отдал заказ %s, стало ресурсов %s", orders.get(j).getID(),getCAPACITY()));
                   orders.remove(j);
                   j--;
                   Generator.countOfCompleteOrders++;
               }
           }
           if (i!=(multipath.size()-1))
           System.out.println(String.format("%s Робот %s едет в следующую точку", getType(), getID()));

       }
       setFree(true);
       setEndPoint(getStoragePoint());


    }
    private void moveToStorage() throws NoWayException, InterruptedException
    {
        System.out.println(String.format("%s Робот %s едет обратно на склад", getType(), getID()));
        path = DeicstraArea.getInstance().findWay(StoragePoint,CurrentPoint);  //точки стоят наоборот чтобы правильно рисовало
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        System.out.println(String.format("%s Робот %s на складе, осталось энергиии %S", getType(), getID(),getEnergy()));
        setCurrentPoint(getStoragePoint());
    }
    private void pickOrderFromTopQueue(Order order){
        setEndPoint(order.getDestinationPoint());
        setCapacity(getCAPACITY() - order.getWeight());
        System.out.println(String.format("%s Робот %s взял заказ %s, приоритета %s, осталось ресурсов %s", getType(), getID(), order.getID(), order.getPriority(), getCAPACITY()));
        Generator.getInstance().getOrderQueue().remove(0);
        getOrders().add(order);
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
    public void paintPath(ArrayList<Point> path) throws InterruptedException, NoWayException {
        Color bodyColor = Colors.randomColor();
        Color headColor = Colors.randomColor();   //начало змейки
         for (Point point : path){
                //Столкновения
            if (Core.repainting.getArea()[point.x][point.y]==2){
                DeicstraArea.getInstance().findWay(point,EndPoint);
                System.out.println(String.format("Потенциальное столкновение. Робот %s ждет",getID()));
                //while(Core.repainting.getArea()[point.x][point.y]==2){}
                Thread.sleep(40);
                System.out.println(String.format("Робот %s продолжает движение",getID()));
                Generator.PotentialCollisions++;
                }
            setCurrentPoint(point);
            DeicstraArea.getInstance().getCell(point.x, point.y).setColor(bodyColor);
            Core.repainting.repaint();
            Core.repainting.getArea()[point.x][point.y]=2;    //клетка стала занятой
            Thread.sleep(30);
            energy=getEnergy()- energyRatePerMove;
            Core.repainting.getArea()[point.x][point.y]=1;   // освободилась
            DeicstraArea.getInstance().getCell(point.x, point.y).setColor(headColor);
        }
        /*try{
        for (int i =0;i<path.size();i++)
        {
            setCurrentPoint(path.get(i));
            if (Core.repainting.getArea()[path.get(i+1).x][path.get(i+1).y]==2)
            {
                 paintPath(DeicstraArea.getInstance().findWay(CurrentPoint,EndPoint));
                 break;
            }
            DeicstraArea.getInstance().getCell(path.get(i).x, path.get(i).y).setColor(bodyColor);
            Core.repainting.repaint();
            Core.repainting.getArea()[path.get(i).x][path.get(i).y]=2;    //клетка стала занятой
            Thread.sleep(30);
            Core.repainting.getArea()[path.get(i).x][path.get(i).y]=1;   // освободилась
        }      }
        catch (Exception e){
            System.out.println("Paintpath косяк" + e.getMessage());
        }   */ //тут сквозь стены не ездит, но ошибка блеать в алгортме поиска пути!!!
    }
    public ArrayList<Point> buildUniqueListByOrders(){
        //метод находит в списке заказов робота уникальные точки, которые необходимо посетить для задачи комммивояжера
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < orders.size(); i++) {
            points.add(orders.get(i).getDestinationPoint());
        }
        HashSet<Point> hashSet = new HashSet<Point>();
        for (int i=0;i<points.size();i++)
        {
        hashSet.add(points.get(i));
        }
        ArrayList<Point> TSPpoints = new ArrayList<Point>();
        TSPpoints.addAll(hashSet);
        // System.out.println(set.size());   //сколько уникальных точек в списке заказов
        return TSPpoints;
    }

}
