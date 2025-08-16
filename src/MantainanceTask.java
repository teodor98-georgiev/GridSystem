import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class MantainanceTask {
    private  String taskId;
    private String componentId;
    private TaskType taskType;
    private int priority;
    private LocalDate scheduledDate;
    private TaskStatus taskStatus;
    private PowerGridController pgc;


    public MantainanceTask(String taskId, String componentId, TaskType taskType, PowerGridController pgc){

        this.taskId = taskId;
        this.componentId = componentId;
        this.taskType = taskType;
        this.pgc = pgc;

    }



    public void updatePriority(int priority){
        this.priority = priority;
    }

    public TaskStatus executeTask(ElectricalComponent ec){

        if (ec instanceof PowerGenerator){
            ec.performMantainance(taskType);
        }

        if (ec instanceof CircuitBreaker){
            pgc.CBRef.openCircuit();
            ec.performMantainance(taskType);
        }

        if (ec instanceof Transformer){
            pgc.CBRef.openCircuit();
            ec.performMantainance(taskType);
        }
        return this.taskStatus = (TaskStatus.COMPLETED);


    }

    public String getTaskDetails(){
        return taskType +" for" + scheduledDate;
    }

    public String logTask(){
        return "MT created for component " + this.componentId + " on " + this.scheduledDate;
    }


    public void scheduleMantainanceTask(String componentId, TaskType taskType, ElectricalComponent ec){
        if (componentId.equals(null)){
            System.out.println("error: provide a valid id");
        }

        else if (taskType == TaskType.EMERGENCY){
            this.priority = 1;
            scheduledDate = LocalDate.now();
            ec.setLastMaintenanceDate(scheduledDate);
            ec.addDateToMantainanceHist(scheduledDate);
        }
        else if (taskType == TaskType.CORRECTIVE){
            this.priority = 2;
            scheduledDate = (ec.getLastMaintenanceDate().plusDays(3));
            ec.setLastMaintenanceDate(scheduledDate);
            ec.addDateToMantainanceHist(scheduledDate);
        }
        else if (taskType == TaskType.PREVENTIVE){
            this.priority = 3;
            scheduledDate = (ec.getLastMaintenanceDate().plusDays(15));
            ec.setLastMaintenanceDate(scheduledDate);
            ec.addDateToMantainanceHist(scheduledDate);
        }
        else if (taskType == TaskType.ROUTINE){
            this.priority = 4;
            scheduledDate = (ec.getLastMaintenanceDate().plusDays(30));
            ec.setLastMaintenanceDate(scheduledDate);
            ec.addDateToMantainanceHist(scheduledDate);

        }
    }

    public String getTaskId() {
        return taskId;
    }


    public TaskType getTaskType() {
        return taskType;
    }

    public int getPriority() {
        return priority;
    }



    public String getComponentId() {
        return componentId;
    }
}
