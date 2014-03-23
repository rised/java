package Diplom;

import java.awt.*;
import java.util.*;
import java.lang.*;

public class DeicstraArea {

    /**
     * Creates a new instance of DeicstraArea
     */
    private DeicstraArea() {
    }


    private static DeicstraArea instance = null;

    /**
     * Метод необходим для реализации шаблона Singletton
     */
    public static DeicstraArea getInstance() {
        if (instance == null) instance = new DeicstraArea();
        return instance;
    }

    /**
     * Метод создает внутренний образ карты переданной
     * в виде массива значений стоимости прохождения клети
     * без вызова этого метода работа с данным класом
     * не имеет смысла
     *
     * @param area передаеться в формате area[x][y]
     *               zonecost=2 расцениваються
     *               как непроходимые клетки
     */
    public void makeArea(int[][] area) {      //рисуем площадь weight X height
        if (area.length == 0) return;
        this.width = area.length;
        this.heigth = area[0].length;
        for (int y = 0; y < heigth; y++)
            for (int x = 0; x < width; x++) {
                CustomCell cell = new CustomCell(area[x][y], x, y);//в качестве первого аргумента значение стоимости (1) для тупиковых (2)
                this.area.add(cell);
            }
    }

    /**
     * Метод возвращает ссылку на клетку внутреннего
     * образа карты по переданным координатам x и y
     */
    public CustomCell getCell(int x, int y) {
        return (this.area.get(y * this.width + x));
    }

    /**
     * Метод возвращает ссылку на клетку Дейкстры
     * по переданным координатам и карте клеток Дейкстры
     *
     * @param x, y - координаты клетки
     *           area - ссылка на созданный вектор-образ
     *           карты состоящий из клеток класса DeicstraCell
     */
    private DeicstraCell getCell(int x, int y, ArrayList<DeicstraCell> area) {
        return (area.get(y * this.width + x));
    }

    /**
     * Метод возвращает список состоящий из клеток
     * на которые может шагнуть герой (по кретсу)
     * из переданной ссылке на клетку из
     * внутреннего образа карты
     *
     * @param cell - клетка в которой находиться герой
     */
    ArrayList getEnvironment(CustomCell cell) {
        return getEnvironment(cell, ENVIROMENT_MODEL_SNOWFLAKE);  //ЗДЕСЬ И НИЖЕ ЗАДАЕМ ПРЕСЛОВУТУЮ МОДЕЛЬ
    }

    /**
     * Метод возвращает список состоящий из клеток Дейкстры
     * на которые может шагнуть герой (по кретсу)
     * из переданной клетки Дейкстры
     *
     * @param cell - клетка в которой находиться герой
     *             area - ссылка на созданный вектор-образ
     *             карты состоящий из клеток класса DeicstraCell
     */
    private ArrayList getEnvironment(DeicstraCell cell, ArrayList<DeicstraCell> area) {
        return getEnvironment(cell, area, ENVIROMENT_MODEL_SNOWFLAKE);
    }

    /**
     * Метод возвращает список состоящий из клеток
     * на которые может шагнуть герой
     * в соответствии с переданной моделью (крест,снежинка)
     * из переданной клетки
     *
     * @param cell - клетка в которой находиться герой
     *             enviromentModel - одна из моделей:
     *             снежинка - ENVIROMENT_MODEL_SNOWFLAKE = 1;
     *             крест - ENVIROMENT_MODEL_CROSS = 2;
     */
    ArrayList getEnvironment(CustomCell cell, int enviromentModel) {
        int x = cell.getX();
        int y = cell.getY();
        if ((x >= this.width) || (y >= this.heigth)) return new ArrayList();
        ArrayList tempAL = new ArrayList();

//Up
        if (y != 0) tempAL.add(getCell(x, y - 1));
//Right
        if (x != this.width - 1) tempAL.add(getCell(x + 1, y));
//Down
        if (y != this.heigth - 1) tempAL.add(getCell(x, y + 1));
//Left
        if (x != 0) tempAL.add(getCell(x - 1, y));

        if (enviromentModel == ENVIROMENT_MODEL_SNOWFLAKE) {
//Left-Up
            if ((x != 0) && (y != 0)) tempAL.add(getCell(x - 1, y - 1));
//Down-Left
            if ((y != this.heigth - 1) && (x != 0)) tempAL.add(getCell(x - 1, y + 1));
//Right-Down
            if ((x != this.width - 1) && (y != this.heigth - 1)) tempAL.add(getCell(x + 1, y + 1));
//Up-Right
            if ((y != 0) && (x != this.width - 1)) tempAL.add(getCell(x + 1, y - 1));
        }
        return tempAL;
    }

    /**
     * Метод возвращает список состоящий из клеток Дейкстры
     * на которые может шагнуть герой
     * в соответствии с переданной моделью (крест,снежинка)
     * из переданной клетки Дейкстры
     *
     * @param Cell - клетка в которой находиться герой
     *             area - ссылка на созданный вектор-образ
     *             карты состоящий из клеток класса DeicstraCell
     *             enviromentModel - одна из моделей:
     *             снежинка - ENVIROMENT_MODEL_SNOWFLAKE = 1;
     *             крест - ENVIROMENT_MODEL_CROSS = 2;
     */
    private ArrayList getEnvironment(DeicstraCell Cell, ArrayList<DeicstraCell> area, int enviromentModel) {
        int x = Cell.getX();
        int y = Cell.getY();
        if ((x >= this.width) || (y >= this.heigth)) return new ArrayList();
        ArrayList tempAL = new ArrayList();

//Up
        if (y != 0) tempAL.add(getCell(x, y - 1, area));
//Right
        if (x != this.width - 1) tempAL.add(getCell(x + 1, y, area));
//Down
        if (y != this.heigth - 1) tempAL.add(getCell(x, y + 1, area));
//Left
        if (x != 0) tempAL.add(getCell(x - 1, y, area));

        if (enviromentModel == ENVIROMENT_MODEL_SNOWFLAKE) {
//Left-Up
            if ((x != 0) && (y != 0)) tempAL.add(getCell(x - 1, y - 1, area));
//Down-Left
            if ((y != this.heigth - 1) && (x != 0)) tempAL.add(getCell(x - 1, y + 1, area));
//Right-Down
            if ((x != this.width - 1) && (y != this.heigth - 1)) tempAL.add(getCell(x + 1, y + 1, area));
//Up-Right
            if ((y != 0) && (x != this.width - 1)) tempAL.add(getCell(x + 1, y - 1, area));
        }
        return tempAL;
    }
    /** Метод возвращает список состоящий из ПРОХОДИМЫХ клеток
     * на которые может шагнуть герой
     * в соответствии с переданной моделью (крест,снежинка)
     * из переданной клетки
     * cell - клетка в которой находиться герой
     */
    /**
     * Метод возвращает список состоящий из НЕПОСЕЩЕННЫХ клеток Дейкстры
     * на которые может шагнуть герой
     * в соответствии с переданной моделью (крест,снежинка)
     * из переданной клетки Дейкстры
     *
     * @param cell - клетка в которой находиться герой
     *             area - ссылка на созданный вектор-образ
     *             карты состоящий из клеток класса DeicstraCell
     */
    private ArrayList getCleanEnvironment(DeicstraCell cell, ArrayList area) {
        ArrayList tempAL = this.getEnvironment(cell, area);
        ArrayList outAL = new ArrayList();

        Iterator tempI = tempAL.iterator();
        DeicstraCell tempDC;
        while (tempI.hasNext()) {
            tempDC = (DeicstraCell) tempI.next();
            if ((tempDC.getStatus() == tempDC.STATUS_NOVISITED)
                    )
                outAL.add(tempDC);
        }
        return outAL;
    }

    /**
     * Метод реализует алгоритм поиска пути
     * и возвращает маршрут - список, состоящий
     * из координат точек, которые необходимо посетить,
     * что бы добраться от точки startCell
     * до точки finishCell
     *
     * @param startPoint - начальная точка пути
     *                   finishPoint - конечная точка пути
     * @return возвращает ArrayList состоящий из обьектов Point
     *         если путь не найден возвращает null
     */
    public ArrayList findWay(Point startPoint, Point finishPoint) throws NoWayException {
//Создаем временные области
        ArrayList<DeicstraCell> tempArea = new ArrayList<DeicstraCell>();
        ArrayList<DeicstraCell> BarrierList = new ArrayList<DeicstraCell>();
        DeicstraCell STARTCELL = null;
        DeicstraCell FINISHCELL = null;
//Получаем ссылки на начальную и конечные клетки
        CustomCell startCell = getCell(startPoint.x, startPoint.y);
        CustomCell finishCell = getCell(finishPoint.x, finishPoint.y);
//Производим создание вектора образа карты состоящий из обьектов
// класса DeicstraCell
        for (CustomCell TEMPCELL : area) {
            DeicstraCell tempDC = new DeicstraCell(TEMPCELL);
            if (TEMPCELL == startCell) STARTCELL = tempDC;
            if (TEMPCELL == finishCell) FINISHCELL = tempDC;
            tempArea.add(tempDC);
        }
//Обьявляем обект ссылку на который метод вернет в результате
        ArrayList retAL = new ArrayList();
//Для начальной точки делаем предшественника её саму
        STARTCELL.setPredecessor(STARTCELL);
//Пройденый путь начальной точки равен ее стоимости
        STARTCELL.setPassedWay(STARTCELL.getCoast());
//Начальная и конечная точки должны иметь статус непосещенных
        STARTCELL.setStatus(STARTCELL.STATUS_NOVISITED);
        FINISHCELL.setStatus(FINISHCELL.STATUS_NOVISITED);

//Помечаем начальную точку как граничную
        STARTCELL.setStatus(STARTCELL.STATUS_BARRIER);
//Очищаем список граничных точек
        BarrierList.clear();
//Помещаем в него начальную точку
        BarrierList.add(STARTCELL);
        DeicstraCell minCC;

        while (!BarrierList.isEmpty()) {
//Среди всех граничных точек находим Клетку1 - клетку с минимальной суммой оценки
            minCC = findOfBorderList(BarrierList);
//если таковая точка не найдена - пути быть не может
            if (minCC == null) {
                NoWayException e = new NoWayException("Way can not be finned!");
                throw e;
            }
//Для найденной клетки с минимальной суммой оценки рассматриваем соседей
            ArrayList<DeicstraCell> tempAL = getCleanEnvironment(minCC, tempArea);
//Если соседей нет
            if ((tempAL == null) || (tempAL.isEmpty())) {
//Пометить найденную клетку как отброшенную
                minCC.setStatus(minCC.STATUS_OFFCAST);
//и удалить ее из списка граничных точек
                BarrierList.remove(minCC);
//Перейти к новому поиску точки с минимальной суммой оценки
//из списка граничных точек
                continue;
            }
//Если сосед имеет статус непосещенного,
//а все клетки возвращенные методом getCleanEnvironment()
//таковыми и являються, то для каждого из них
            for (DeicstraCell dCell : tempAL) {
//то мы обозначаеми его (соседа) как граничную клетку,
                dCell.setStatus(dCell.STATUS_BARRIER);
//добавляем в список граничных точек
                BarrierList.add(dCell);
//и указываем Клетку1 как предыдущую для него.
                dCell.setPredecessor(minCC);
//Оцениваем соседей как сумму предшественника и стоимости самого соседа
                dCell.setPassedWay(minCC.getPassedWay() + dCell.getCoast());
//Находим оставшийся путь как длину вектора до финиша
                dCell.setResiduaryWay(Math.sqrt((double)
                        (dCell.getX() - finishCell.getX()) * (dCell.getX() - finishCell.getX())
                        + (dCell.getY() - finishCell.getY()) * (dCell.getY() - finishCell.getY())));
//Если сосед - это финиш, то путь найден
                if (dCell.equals(FINISHCELL)) {
//System.out.println("WayFinded");
//Трассировка пути
                    while (!minCC.getPredecessor().equals(STARTCELL)) {
                        retAL.add(minCC.getParentCell().getPosition());
                        minCC = minCC.getPredecessor();
                    }
                    retAL.add(minCC.getParentCell().getPosition());
                    return retAL;
                }
            }
//Рассматриваемую Клетку помечаем как отброшенную.
            minCC.setStatus(minCC.STATUS_OFFCAST);
//удаляем из списка граничных точек
            BarrierList.remove(minCC);
        }
//если граничных точек больше нет - то путь не найден
// if (BarrierList.isEmpty()) System.err.println("No Way!");
        NoWayException e = new NoWayException("Way can not be fined!");
        throw e;
    }

    /**
     * Метод находит клетку с минимальной суммой оценки
     * среди всех граничных точек из списка
     *
     * @param BarrierList - список граничных точек
     */
    private DeicstraCell findOfBorderList(ArrayList<DeicstraCell> BarrierList) {
        if (BarrierList.isEmpty()) return null;
        Iterator tempI = BarrierList.iterator();
        DeicstraCell tempCC, minCC = BarrierList.get(0);
        double minSO = minCC.getSummaryWay();
        double tempSO = 0.0;
        while (tempI.hasNext()) {
            tempCC = (DeicstraCell) tempI.next();
            tempSO = tempCC.getSummaryWay();
            if (tempSO < minSO) {
                minSO = tempSO;
                minCC = tempCC;
            }
        }
        return minCC;
    }

    /**
     * Метод немного оптимизирует путь найденый методом findWay
     * оптимизация исключает из пути углы где это возможно
     *
     * @param way - путь, последовательность точек класса Point
     */
    public ArrayList optimizeWay(ArrayList<Point> way) {
        if (way == null) return null;
        if (way.size() < 3) return way;
        Point p1, p2, p3;
        ArrayList<Point> retWay = new ArrayList<Point>();
        for (int i = 0; i < way.size() - 3; i++) {
            p1 = way.get(i);
            p2 = way.get(i + 1);
            p3 = way.get(i + 2);

// p2 p3
// p1
            if ((p1.x == p2.x) && (p2.y == p3.y) && (p1.y == p2.y + 1) && (p2.x == p3.x - 1)) {
                if (getCell(p3.x, p1.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p2 p1
// p3
            if ((p3.x == p2.x) && (p2.y == p1.y) && (p3.y == p2.y + 1) && (p2.x == p1.x - 1)) {
                if (getCell(p1.x, p3.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p1 p2
// p3
            if ((p1.y == p2.y) && (p2.x == p3.x) && (p1.y == p3.y - 1) && (p1.x == p2.x - 1)) {
                if (getCell(p1.x, p3.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p3 p2
// p1
            if ((p3.y == p2.y) && (p2.x == p1.x) && (p3.y == p1.y - 1) && (p3.x == p2.x - 1)) {
                if (getCell(p3.x, p1.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p1
// p3 p2
            if ((p1.x == p2.x) && (p2.y == p3.y) && (p1.y == p3.y - 1) && (p1.x == p3.x + 1)) {
                if (getCell(p3.x, p1.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p3
// p1 p2
            if ((p3.x == p2.x) && (p2.y == p1.y) && (p3.y == p1.y - 1) && (p3.x == p1.x + 1)) {
                if (getCell(p1.x, p3.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p3
// p2 p1
            if ((p1.y == p2.y) && (p2.x == p3.x) && (p1.y == p3.y + 1) && (p1.x == p2.x + 1)) {
                if (getCell(p1.x, p3.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
// p1
// p2 p3
            if ((p3.y == p2.y) && (p2.x == p1.x) && (p3.y == p1.y + 1) && (p3.x == p2.x + 1)) {
                if (getCell(p3.x, p1.y).isPassable()) {
                    way.remove(i + 1);
                    continue;
                }

            }
        }
        return way;
    }

    /**
     * Метод отображает все клетки на переданном
     * в качестве параметра контексте
     */
    public void paint(Graphics g) {
        for (CustomCell tempCC : this.area) {
            tempCC.paint(g);
        }
    }

    //Модели окружения
    private final int ENVIROMENT_MODEL_SNOWFLAKE = 1;
    private final int ENVIROMENT_MODEL_CROSS = 2;

    //Внутренний образ карты
    private final ArrayList<CustomCell> area = new ArrayList<CustomCell>();

    //Длинна и ширина образа
    private int width = 1;
    private int heigth = 1;
}

