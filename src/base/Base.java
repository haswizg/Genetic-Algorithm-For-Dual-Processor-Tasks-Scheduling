package base;

import java.io.*;
import java.util.LinkedList;

/*
this class . ..........bla bla
*/

public class Base {

    //store all task unsorted and unorganized
    private static LinkedList<TASK> IncomeTask=new LinkedList<>();
    //List of List : the inner list represent the tasks that in the certain group & the outer list represent the levels groups
    protected static LinkedList<LinkedList<TASK>> taskesLeveled =new LinkedList<>();

    private static void taskSetup() {
        /*
            first read tasks from input file to add them incomeTask linkedList by calling ReadTask()
            and then organize them by grouping the tasks with same level together . by calling LevelTask()
         */
        ReadTask();
        if(IncomeTask.size()==0) {
            System.out.println("no tasks deducted !!");
        }else{
            LevelTask();
        }
    }

    private static void ReadTask(){
        try{
            File inputFile = new File("/home/has/IdeaProjects/Genetic-Algorithm-For-Dualprocessor-Scheduling/src/input3.txt");

            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String firstLine;

            if ((firstLine=bufferedReader.readLine())!= null){

                int index;
                int numberOfInputs = Integer.valueOf(firstLine);
                String[] allLines=new String[numberOfInputs];
                String[] Predecessors4Lines = new String[numberOfInputs];

                //store all lines in the array and then close the fileReader
                index =0;
                while (index<numberOfInputs){
                    allLines[index] = bufferedReader.readLine();
                    index++;
                }
                fileReader.close();

                //for each line in the array create task with the time and level that given in the input
                // and store the Predecessors in another array to assign them later , after create all task
                index=0;
                for(String singleLine : allLines){

                    String[] fourCategory = singleLine.split(" ");

                    int theTime = Integer.valueOf(fourCategory[2]);
                    int theHight = Integer.valueOf(fourCategory[3]);
                    IncomeTask.addLast(new TASK(theTime, theHight));

                    Predecessors4Lines[index]=fourCategory[0];

                    index++;
                }

                //now assign the Predecessors for each task
                index=0;
                for(TASK one: IncomeTask){

                    if(!Predecessors4Lines[index].contains("-")){
                        String[] Predecessors = Predecessors4Lines[index].split(",");
                        for(String singleOne: Predecessors){
                            int theTarget = Integer.valueOf(singleOne)-1;
                            one.addOneRequirement(IncomeTask.get(theTarget));
                        }
                    }
                    index++;
                }

            }else {
                fileReader.close();
                System.out.println("error: empty input file!! , check the file out");
                Thread.currentThread().interrupt();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void LevelTask() {
        /*
        first make sure that index with same task level value is already exist . if not do it
        note that : each index in the outer list represent the level value
            as: task with level zero go to index zero , level one tasks go to index [1] , and so on ...
         */
        for (TASK single_one:Base.IncomeTask){
            if( taskesLeveled.size() <= single_one.getLevel() ){
                while(taskesLeveled.size() <= single_one.getLevel()){
                    taskesLeveled.addLast(new LinkedList<>());
                }
            }
            taskesLeveled.get(single_one.getLevel()).addLast(single_one);
        }
    }
    protected static void taskFineTimeRefresh(){
        /*
        because the task assign to schedules by reference . and fine time will used every time in calculate schedules time
        then the fine time need reset before each schedule calculate time
        for each task FineTime = 0 ;
         */
        for(TASK one:IncomeTask){
            one.setFineTime(0);
        }
    }

    public static void main(String[] args) {
        //start initialling tasks
        taskSetup();
        if(IncomeTask.size()==0) {
            System.out.println("no tasks deducted !!");
        }else {
/*
            //print tasks that read from the text file
            for( TASK one : IncomeTask ){
                System.out.println("Task : "+one.getName());
                System.out.println("Time : "+one.getWorkTime());
                System.out.println("Level : "+one.getLevel());
                if(one.getRequirement().size()!=0){
                    System.out.println("Predecessors: ");
                    for(TASK two : one.getRequirement()){
                        System.out.println("  > "+two.getName()+" , Level: "+two.getLevel());
                    }
                }else {
                    System.out.println("has no Predecessors ");
                }
                System.out.println("-----------------------------");
            }
*/

            int popuSize = 100;
            int bestScheduleTimer = 10;
            //defined the population ( the number of schedules , the time of best schedule that when equals to zero interrupt the GA working)
            new Population(popuSize, bestScheduleTimer);

            int GAtime = 1000;
            final int crossoverProp = 90;
            final int mutationProp = 20;
            //start GA work , passing ( maximum generation number , crossover probability , mutation probability )
            //no need to pass the population since it is defined in Population class as protected static
            new NewGeneration(GAtime, crossoverProp, mutationProp);
        }
    }
}