import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Transformer extends ElectricalComponent {
    private PowerGenerator powerGenerator;
    private double primaryVoltage;
    private double transformationRatio = 0.01;
    private double secondaryVoltage;
    private String coolingType = "oil";
    private List<LocalDate> mantainanceHist = new ArrayList<>();
    private double primaryCurrent;
    private PowerGridController controller;


    public Transformer(PowerGenerator powerGenerator, String componentId, ComponentType componentType,
                       PowerComponentStatus status, LocalDate installationDate, LocalDate lastMaintenanceDate,
                        PowerGridController controller) {
        super(componentId, componentType, status, installationDate, lastMaintenanceDate);
        this.powerGenerator = powerGenerator;
        this.primaryVoltage = powerGenerator.voltage;
        this.secondaryVoltage = primaryVoltage/transformationRatio;
        this.primaryCurrent = powerGenerator.current;
        this.controller = controller;
    }


    public double getPrimaryVoltage() {
        return this.primaryVoltage;
    }
    public double getPrimaryCurrent(){
        return primaryCurrent;
    }

    public double getSecondaryVoltage(){
        return secondaryVoltage;
    }

    public double getSecondaryCurrent(){
        return getPrimaryCurrent() * transformationRatio;
    }

    public void monitorTemp(double temperature){
        if (temperature > 100){
            setCoolingSystem();
        }
    }


    @Override
    protected boolean operatingParametersValidation() {
        if (primaryVoltage <= 25000 && primaryCurrent <= 18000){  // if current and voltage exceeds the maximal values
            // the component, no matter what is, will be isolated from the rest of the grid.
            return true;
        }
        return false;
    }


    @Override
    public void performMantainance(TaskType taskType) {
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;
        this.status = PowerComponentStatus.OFFLINE;
    }



    @Override
    public double calculateEfficiency() {
        return Pout()/Pin();
    }

    @Override
    public void addDateToMantainanceHist(LocalDate lastMaintenanceDate) {
        mantainanceHist.add(lastMaintenanceDate);
    }

    public void setCoolingSystem(){
        System.out.println("cooling system engaged");
    }

    public double Pin(){  // same as generator
        return powerGenerator.calculateGeneratedElectricPower(powerGenerator.activePower, powerGenerator.reactivePower, powerGenerator.powerFactor);

    }

    public double Pout(){
        double result = Pin() * 0.9;  // coefficient keeps the losses from induced eddy currents and istheresis effect
        return result;
    }



    public void setPowerGenerator(PowerGenerator powerGenerator) {
        this.powerGenerator = powerGenerator;
    }

    public void setController(PowerGridController controller) {
        this.controller = controller;
    }
}
