package base;

import base.Base;
import base.Schedule;

import java.util.Comparator;
import java.util.LinkedList;

/*
this class . ..........bla bla
*/

class Population {

    private static LinkedList<Schedule> popuList = new LinkedList<>();
    private static Schedule bestSchedule =null;

    private static int bestTime;
    private static int popuSize;

    private static int GenerationNo =0;
    private static double avgFinishTimeValue;
    private static int bestScheduleTimer;

    Population(int size,int time) {
        popuSize=size;
        bestTime=time;
        bestScheduleTimer=bestTime;

        //create Schedule (size) times , store them in (popList) and the order doesn't matter
        for (int i = 0; i < popuSize; i++) {
            popuList.add(new Schedule(Base.taskesLeveled));
        }
    }

    protected static void popuStatistic(){
        System.out.println("~ ~ ~ ~ ~ statistic start ~ ~ ~ ~ ~");

        popuList.sort(new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return Double.compare(o1.getFinishTimeValue(), o2.getFinishTimeValue());
            }
        });

        double fitnessValuesCounter=0;
        for(Schedule singleSchedlue : popuList) {
            System.out.print(" = " + singleSchedlue.getFinishTimeValue());
            fitnessValuesCounter = fitnessValuesCounter + singleSchedlue.getFinishTimeValue();
        }
        avgFinishTimeValue =fitnessValuesCounter/popuSize;

        System.out.println(" , in the GenerationNo: "+GenerationNo);
        System.out.println("the avrege finish time value is:"+ avgFinishTimeValue);

        if(bestSchedule==null || bestSchedule.getFinishTimeValue()!=popuList.getFirst().getFinishTimeValue() ){
            bestSchedule=popuList.getFirst();
            bestScheduleTimer=bestTime;
        }else {
            bestScheduleTimer--;
        }

        System.out.println("the first best schedlue is that with");
        bestSchedule.printSchedule();

        System.out.println("the worst schedlue is that with");
        popuList.getLast().printSchedule();

        System.out.println("~ ~ ~ ~ ~ statistic end ~ ~ ~ ~ ~");
    }

    protected static LinkedList<Schedule> getPopu(){
        return popuList;
    }


    protected static int getBestScheduleTimer() {
        return bestScheduleTimer;
    }


    protected static void getNewPopu(LinkedList<Schedule> v ){
        popuList=v;
        GenerationNo++;
    }

}
