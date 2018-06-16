package base;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/*
this class . ..........bla bla
*/

class Schedule {

    public Processor firstProcess =new Processor();
    public Processor secondProcess =new Processor();
    private double finishTimeValue;

    Schedule(){
        //empty constorctor
    }

    Schedule(LinkedList<LinkedList<TASK>> TL){
        /*
        take each task level list (form lower height to the higher)
            1- make the list sorted randomly for more randomness
            2- for each task in the list either assign to processor no.1 or no.2 by chosen 1 or 0 randomly ( 1 for processor no.1 , 2 for no.2 )
        when finish all task level list , calculate the fitness value for this schedule by finishTimeCalc() and after all printSchedule the schedule
         */

        for(LinkedList<TASK> singleLevel : TL){
            Collections.shuffle(singleLevel);
            for(TASK singleTask : singleLevel){
                Random rand = new Random();
                if( (rand.nextInt(2)) == 1 ){
                    this.firstProcess.addSingelTaskToList(singleTask);
                }else{
                    //if the task doesn't assign to first processor , then it will be in second processor
                    this.secondProcess.addSingelTaskToList(singleTask);
                }
            }
        }
        this.finishTimeCalc();
        //this.printSchedule();
        //System.out.println("---------------------------");
    }

    protected void finishTimeCalc(){
        /*
            since the two processor are synchronized , then we should use thread or semi-thread, we used semi-thread
            at first we defined two boolean variables on for each processor, which tell us whither the processor finish or not
            then , while at lest one of the processors don't finish call his time calculator , of course if one of them finished we stop call his time calculator
            because we calculate the time for processors, there will be some side effects on task data
                    ( specifically the fineTime variable at task class and therefor consideration for it clarify in task class)
                    the thing we call Base.taskFineTimeRefrech() here to erase that
            after that Compare the finish time of the two processors , to take the higher (higher value will consider, why? we will not gonna tell you)
         */
        boolean firstDone=false;
        boolean secondDone=false;

        //initial process finish time which equal zero
        this.firstProcess.resetFinishTime();
        this.secondProcess.resetFinishTime();

        while (!firstDone||!secondDone){
            if(!firstDone){
                firstDone= this.firstProcess.timeCalc();
            }
            if(!secondDone){
                secondDone= this.secondProcess.timeCalc();
            }
        }
        Base.taskFineTimeRefresh();
        if(this.firstProcess.getProcessFinishTime()>= this.secondProcess.getProcessFinishTime()){
            this.finishTimeValue = this.firstProcess.getProcessFinishTime();
        }else {
            this.finishTimeValue = this.secondProcess.getProcessFinishTime();
        }
    }

    protected void printSchedule(){
        //printProcess function that printProcess first, second processor and the fitness value of the schedule
        System.out.print("p1: ");
        this.firstProcess.printProcess();
        System.out.print("p2 :");
        this.secondProcess.printProcess();
        System.out.println("with fitness value equal "+this.finishTimeValue);
    }

    protected double getFinishTimeValue(){
        //return this fitness value
        return this.finishTimeValue;
    }

}