package base;

import java.util.LinkedList;
import java.util.Random;

/*
this class . ..........bla bla
*/

final class NewGeneration {
    //since random object will Frequently used in this class , defined it as class member
    private static Random rand = new Random();
    private static int crossover_prop;
    private static int mutation_prop;
    private static int GA_timer;

    NewGeneration( int timer, int cros, int mut){
        GA_timer =timer;
        mutation_prop=mut;
        crossover_prop=cros;

        System.out.println("--------------------------------------------------------------------------");
        //until number GA_timer or best schedule time equals zero
        while (GA_timer !=0 && Population.getBestScheduleTimer() !=0){

            /*
            call the statistic , statistic two things :
                                    1- sort the schedules based on their finishing time
                                    2- print some info about current population
            note that : sorting part important in selection
             */
            Population.popuStatistic();
            /*
                pass the returning values from function to another instead of store the result of each function in variables
                selection -> crossover -> mutation -> round
             */
            round(mutation(crossover(seletion(Population.getPopu()))));
            //it's obvious why , right ?
            GA_timer--;

            System.out.println("--------------------------------------------------------------------------");

        }
        //for last population
        Population.popuStatistic();
    }

    private static LinkedList<Schedule> seletion(LinkedList<Schedule> currentPopulation){
        //the current population already sorted when the statistic of the population called in The NewGeneration function

        //to edit to the values only within the function , no other effect in the population
        double[] indexing =new double[currentPopulation.size()];
        int index;
        double sumOfAllFitnessValue=0;

        //converge from finish time value to fitness value , and store them in the array
        index=0;
        for(Schedule singleSchedule : currentPopulation) {
            indexing[index] = (singleSchedule.getFinishTimeValue() - (currentPopulation.getLast().getFinishTimeValue() + 1)) * (-1);
            index++;
        }

        //accumulate values of the array elements
        for (int i = 0; i < indexing.length ; i++) {
            indexing[i] = indexing[i]+sumOfAllFitnessValue;
            sumOfAllFitnessValue=indexing[i];
        }

        //normalize values of the array elements
        for (int i = 0; i < indexing.length; i++) {
            indexing[i]=sumOfAllFitnessValue-indexing[i];
        }

        //selection
        LinkedList<Schedule> newPopulation=new LinkedList<>();
        newPopulation.add(currentPopulation.getFirst());

        //roulette wheel selection
        for (int i = 0; i < currentPopulation.size()-1; i++) {
            double get = sumOfAllFitnessValue*rand.nextDouble();
            for (int j =0 ; j<currentPopulation.size() ; j++) {
                if(indexing[j]<=get){
                    newPopulation.addLast(currentPopulation.get(j));
                    break;
                }
            }
        }

        return newPopulation;
    }

    private static LinkedList<Schedule> crossover(LinkedList<Schedule> newPopulation){
        /*
        crossover work as follow:
            1-defined tmpList which contents the schedules that is result of crossover
            2- if there is schedule in newPopulation apply step #3 , if there is not go to step #12
            3- pick one schedule randomly from newPopulation
            4- store the picked schedule in tmp_variable_no_1
            5- delete the picked schedule from newPopulation
            6- a) if there still schedules in newPopulation ,
                        pick another schedule and store it in tmp_variable_no_2
                        after that delete it from newPopulation
               b) if there are no schedules remained in newPopulation ,
                        just pass the tmp_variable_no_1 to tmpList , and jump to step #12
            7- select randomly one of levels value ( 0 , 1 , ,higher level )
            8- linearly go in first process and second process of tmp_variable_no_1 and tmp_variable_no_2 ,
                        to store for each of them where find a task that exactly first task that has level value bigger than the selected level
                        call it "cutPoint"
            9- now apply the crossover on them . as follow
                    a) for first new schedule "tmp_variable_no_3" :
                                first process = first process of tmp_variable_no_1 [from 0 to the cutPoint-1]
                                                    + first process of tmp_variable_no_2 [from cutPoint to the end]
                                second process = second process of tmp_variable_no_1 [from 0 to the cutPoint-1]
                                                    + second process of tmp_variable_no_2 [from cutPoint to the end]
                    b) for second new schedule "tmp_variable_no_4" :
                                first process = first process of tmp_variable_no_2 [from 0 to the cutPoint-1]
                                                    + first process of tmp_variable_no_1 [from cutPoint to the end]
                                second process = second process of tmp_variable_no_2 [from 0 to the cutPoint-1]
                                                    + second process of tmp_variable_no_1 [from cutPoint to the end]
            10- pass tmp_variable_no_3 and tmp_variable_no_4 to tmpList
            11- repeat step #2 to step #11 , while step #2 may jump to step #12
            12- now #of schedules in tmpList should equals to #of schedules in newPopulation when it's come in first pleas
                    or by other word #of schedules in newPopulation must equal to zero
                so , now this function return the tmpList

            note that each schedule has crossover , should calculate his finishing time again since it's changed
         */
        LinkedList <Schedule> tempList = new LinkedList<>();
        Schedule s1;
        Schedule s2;
        int getRand;
        int cutPoint;

        while(newPopulation.size()!=0){

            getRand = rand.nextInt(newPopulation.size());
            s1 = newPopulation.get(getRand);
            newPopulation.remove(getRand);

            if(newPopulation.size()!=0){
                getRand = rand.nextInt(newPopulation.size());
                s2 = newPopulation.get(getRand);
                newPopulation.remove(getRand);
            }
            else{
                tempList.add(s1);
                return tempList;
            }

            if(rand.nextInt(100)<=crossover_prop){

                cutPoint = rand.nextInt(Base.taskesLeveled.size()-1);

                int s1P1 = selectPointToCut( s1.firstProcess , cutPoint );
                int s1P2 = selectPointToCut( s1.secondProcess , cutPoint );
                int s2P1 = selectPointToCut( s2.firstProcess , cutPoint );
                int s2P2 = selectPointToCut( s2.secondProcess , cutPoint );

                Schedule s3 = new Schedule();
                s3.firstProcess.tasksList.addAll(s1.firstProcess.tasksList.subList(0,(s1P1)));
                s3.firstProcess.tasksList.addAll(s2.firstProcess.tasksList.subList(s2P1,s2.firstProcess.tasksList.size()));
                s3.secondProcess.tasksList.addAll(s1.secondProcess.tasksList.subList(0,(s1P2)));
                s3.secondProcess.tasksList.addAll(s2.secondProcess.tasksList.subList(s2P2,s2.secondProcess.tasksList.size()));
                s3.finishTimeCalc();

                Schedule s4 = new Schedule();
                s4.firstProcess.tasksList.addAll(s2.firstProcess.tasksList.subList(0,(s2P1)));
                s4.firstProcess.tasksList.addAll(s1.firstProcess.tasksList.subList(s1P1,s1.firstProcess.tasksList.size()));
                s4.secondProcess.tasksList.addAll(s2.secondProcess.tasksList.subList(0,(s2P2)));
                s4.secondProcess.tasksList.addAll(s1.secondProcess.tasksList.subList(s1P2,s1.secondProcess.tasksList.size()));
                s4.finishTimeCalc();
/*

            //    this hiding part of code return the two best of (
            //            the old two schedule , the new two schedule
            //            )
            //    because crossover may generate schedules that worst then old schedules
            //            and may eliminate optimal schedules

                Schedule min_1=s3;
                Schedule min_2=s4;

                if(min_1.getFinishTimeValue() > min_2.getFinishTimeValue()){
                    if(s1.getFinishTimeValue() < min_1.getFinishTimeValue()){
                        min_1=s1;
                    }
                }else {
                    if(s1.getFinishTimeValue() < min_2.getFinishTimeValue()){
                        min_2=s1;
                    }
                }

                if(min_1.getFinishTimeValue() > min_2.getFinishTimeValue()){
                    if(s2.getFinishTimeValue() < min_1.getFinishTimeValue()){
                        min_1=s2;
                    }
                }else {
                    if(s2.getFinishTimeValue() < min_2.getFinishTimeValue()){
                        min_2=s2;
                    }
                }

                tempList.add(min_1);
                tempList.add(min_2);
*/
                tempList.add(s3);
                tempList.add(s4);
            }else{
                tempList.add(s1);
                tempList.add(s2);
            }
        }
        return tempList;
    }

    private static int selectPointToCut(Processor tempProcess, int cuttingPoint) {
        /*
        implementation of cutPoint linearly search
        mote that : when the point not found , make the point after last task
         */

        for(int i = 0; i < tempProcess.tasksList.size();i++) {
            if (tempProcess.tasksList.get(i).getLevel() > cuttingPoint) {
                return i;
            }
            if(i+1 == tempProcess.tasksList.size()){
                return i+1;
            }
        }
        return 0;
    }

    private static LinkedList<Schedule> mutation(LinkedList<Schedule> temp){
        /*
        for each schedule in temp do the following:
            1- check if the probability apply , if not countun to another schedule
            2- pick level has at lest two different task
            3- pick two tasks that have the picked level
            4- switch between those two picked task
         then return the temp after modified by the mutation operation

         note that each schedule has mutation , should calculate his finishing time again since it's changed
         */
        for(Schedule singleSchedule : temp){
            if(rand.nextInt(100)<=mutation_prop){
                int levelSlected;
                while (true){
                    levelSlected = rand.nextInt(Base.taskesLeveled.size());
                    if(Base.taskesLeveled.get(levelSlected).size()>1){
                        break;
                    }
                    //what id every level has only one task
                }

                int item1 = rand.nextInt(Base.taskesLeveled.get(levelSlected).size());
                int item2 ;
                while (true){
                    item2 = rand.nextInt(Base.taskesLeveled.get(levelSlected).size());
                    if(item1 != item2){
                        break;
                    }
                }

                TASK choose_1 = Base.taskesLeveled.get(levelSlected).get(item1);
                TASK choose_2 = Base.taskesLeveled.get(levelSlected).get(item2);

                int index ;
                if(singleSchedule.firstProcess.tasksList.contains(choose_1)){
                    if (singleSchedule.firstProcess.tasksList.contains(choose_2)) {
                        index = singleSchedule.firstProcess.tasksList.indexOf(choose_1);
                        int index2 = singleSchedule.firstProcess.tasksList.indexOf(choose_2);
                        singleSchedule.firstProcess.tasksList.set(index,choose_2);
                        singleSchedule.firstProcess.tasksList.set(index2,choose_1);
                    }else {
                        index = singleSchedule.firstProcess.tasksList.indexOf(choose_1);
                        singleSchedule.firstProcess.tasksList.set(index,choose_2);
                        index = singleSchedule.secondProcess.tasksList.indexOf(choose_2);
                        singleSchedule.secondProcess.tasksList.set(index,choose_1);
                    }
                }else {
                    if (singleSchedule.firstProcess.tasksList.contains(choose_2)) {
                        index = singleSchedule.secondProcess.tasksList.indexOf(choose_1);
                        singleSchedule.secondProcess.tasksList.set(index,choose_2);
                        index = singleSchedule.firstProcess.tasksList.indexOf(choose_2);
                        singleSchedule.firstProcess.tasksList.set(index, choose_1);
                    }else {
                        index = singleSchedule.secondProcess.tasksList.indexOf(choose_1);
                        int index2 = singleSchedule.secondProcess.tasksList.indexOf(choose_2);
                        singleSchedule.secondProcess.tasksList.set(index,choose_2);
                        singleSchedule.secondProcess.tasksList.set(index2,choose_1);

                    }
                }

                singleSchedule.finishTimeCalc();
            }
        }
        return temp;
    }

    private static void round(LinkedList<Schedule> temp){
        /*
        every prev operations , they change population within them self not the global static population
        in round() update the global static population by calling getNewPopu()
        after this function , complete GA circle done
         */

        Population.getNewPopu(temp);
    }

}