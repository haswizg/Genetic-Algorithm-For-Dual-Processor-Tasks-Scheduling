package base;

import java.util.LinkedList;

/*
this class . ..........bla bla
*/

public class Processor {

    public LinkedList<TASK> tasksList=new LinkedList<>();
    private int processFinishTime; //initially value and get bigger letter by  accumulate tasks working time and all waiting time

    protected boolean timeCalc(){
        /*
            first, the finish time of processor is the fine time of his last task, since every task has work time and fine time
            ( work time: time that task need to complete within itself,
                fine time: time that when task complete when consider task order in the processor that it's Belongs to it, and consider task Requirements )
            second, when the fine time equal zero, we know it's not correct at all, since we assume that task can't complete in 0 unit of time
            so . we can build from this lemma , 1- when fine time of certain task equal zero, that task does not complete
                                                2- when fine time of certain task not equal zero, that task has been completed
            third, we know there in some situation task has Requirement task in other processor and that Requirement do not completed
            so. this task will stop until that task in other processor complete , from this my be call timeCalc() of one processor more than one time
            if called timeCalc() of one processor more than one time, each time will evaluate same task plus some new task to evaluate,
            such a wist to evaluate same task over and over !! , so we consider this and base on the above Assumptionsin following code

            note that is impassable to be requirement doesn't completed within same processor,
            by the way we assigned tasks to processors and that has proved
         */

        //for each task assigned to this processor
        for(TASK singleTast : this.tasksList){

            //if task has been evaluated , move on
            if(singleTast.getFineTime()!=0){
                continue;
            }

            //if task has requirement
            if(singleTast.getRequirement().size()!=0){
                for(TASK one:singleTast.getRequirement()){
                    //if the requirement does not complete then exit from this processor with false value that make allow to enter this processor again
                    if(one.getFineTime()==0){
                        return false;
                    }
                    //if the fine time of the requirement bigger than fine time of last completed task in this processor, then it will be lead the time instead
                    if(one.getFineTime()> this.processFinishTime){
                        this.processFinishTime =one.getFineTime();
                    }
                }
            }
            //if there are requirements and explore all of them or there are not, the finish time of this processor is the old finish time plus work time of current task
            this.processFinishTime = this.processFinishTime +singleTast.getWorkTime();
            //the fine time of current task is the finish time we end up whit , and it's mean this task complete with no error
            singleTast.setFineTime(this.processFinishTime);
        }
        //after explore all task in this processor with no error, so exiting returning true value that protect this processor to entering again
        return true;
    }

    protected void addSingelTaskToList(TASK input){
        // add tasks at last , and get from first
        this.tasksList.addLast(input);
    }

    protected int getProcessFinishTime(){
        // return the finish time
        return this.processFinishTime;
    }

    protected void printProcess(){
        //printProcess all task in the list from first to last
        for(TASK singleTast: this.tasksList){
            System.out.print(singleTast.getName()+" ");
        }
        System.out.println();
    }

    public void resetFinishTime() {
        //initial process finish time
        this.processFinishTime=0;
    }
}