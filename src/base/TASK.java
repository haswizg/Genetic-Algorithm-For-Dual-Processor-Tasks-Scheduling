package base;

import java.util.LinkedList;

/*
this class . ..........bla bla
*/

public class TASK {

    private static int generateName =1;
    private int name;
    private int WorkTime;
    private int fineTime;
    private int level;
    private LinkedList<TASK> Requirement =new LinkedList<>();

    TASK(int T, int L){
        this.name= generateName++;
        this.WorkTime =T;
        this.level=L;
        this.fineTime=0;
    }

    protected int getName(){
        return this.name;
    }

    protected int getWorkTime(){
        return this.WorkTime;
    }

    protected int getFineTime(){
        return this.fineTime;
    }

    protected int getLevel(){
        return this.level;
    }

    protected LinkedList<TASK> getRequirement(){
        return this.Requirement;
    }

    protected void setFineTime(int time){
        this.fineTime=time;
    }

    protected void addOneRequirement(TASK incomeTask){
        this.Requirement.add(incomeTask);
    }

}