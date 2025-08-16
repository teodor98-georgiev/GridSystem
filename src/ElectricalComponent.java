import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ElectricalComponent {
    protected String componentId;
    protected ComponentType componentType;
    protected PowerComponentStatus status;


    private LocalDate installationDate;
    private LocalDate lastMaintenanceDate;


    public ElectricalComponent(String componentId, ComponentType componentType, PowerComponentStatus status,
                               LocalDate installationDate, LocalDate lastMaintenanceDate) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.status = status;

        //this.powerRating = powerRating;
        this.installationDate = installationDate;
        this.lastMaintenanceDate = lastMaintenanceDate;
    }


    protected void updateStatus(PowerComponentStatus status){
        setStatus(status);
    }
    // has been overrided because every component (generator, transf, circuitBraker has their own operational parameters
    protected abstract boolean operatingParametersValidation();

    public String getComponentInfo(){
        return toString();
    }

    public String toString(){
        return "component id: " + this.componentId + "---component type: " + this.componentType;
    }


    public abstract void performMantainance(TaskType taskType);
    public abstract double calculateEfficiency();
    public abstract void addDateToMantainanceHist(LocalDate lastMaintenanceDate);

//    public double getVoltage() {
//        return voltage;
//    }

//    public double getCurrent() {
//        return current;
//    }

    public LocalDate getInstallationDate(){
        return this.installationDate;
    }

    public PowerComponentStatus getStatus() {
        return this.status;
    }

    public LocalDate getLastMaintenanceDate() {
        return this.lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public void setStatus(PowerComponentStatus status) {
        this.status = status;
    }



//    public void changeComponentStatus(){
//        if (validateOperatingParameters()){
//
//        }
//    }
}
