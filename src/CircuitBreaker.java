import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;



public class CircuitBreaker extends ElectricalComponent {

    private double breakingCapacity = 18000;

    public String contactWear = "contact on";  // is public because must be accessed from other components in order
    // to disconnect the load and prevent damages.
    private List<LocalDate> mantainanceHist = new ArrayList<>();
    private double contactResistance = 20000;
    public Transformer transformer;


    public CircuitBreaker(String componentId, ComponentType componentType, PowerComponentStatus status, LocalDate installationDate,
                          LocalDate lastMaintenanceDate,
                          Transformer transformer) {
        super(componentId, componentType, status, installationDate, lastMaintenanceDate);
        this.transformer = transformer;
    }

    public void openCircuit(){ // if there is a shortcircuit somewhere on the electricity provider or on grid,
        // or even power demand is to high the amount of current will jump hugely causing overload on generator,
        // this component will isolate the machine to prevent damages

        this.contactWear = "contact off";

        // gives feedback about circuit status to be 100% sure it's open
        if (checkCircuitStatus()){
            System.out.println("contact status : " + contactWear);
        }

    }

    public void closeCircuit(double current){
        if (contactWear.equals("contact off") && current < breakingCapacity){
            contactWear = "contact on";
            System.out.println("circuit closed");
        }
        else {
            System.out.println("contact not closed. Current to much high");
        }

    }

    public void testTripMech(double current){// new thing learned:
        // how to delay an operation
        if (current >= breakingCapacity){
            CountDownLatch latch = new CountDownLatch(1);
            delayedOpContaineer delayedOp = new delayedOpContaineer(current, this, latch);
            Thread delayThread = new Thread(delayedOp);
            delayThread.start();

            try {
                latch.await(); // Wait for completion
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (checkCircuitStatus()){
            System.out.println("contact status : " + contactWear);
        }
    }


    @Override
    protected boolean operatingParametersValidation() {
        if (transformer.getSecondaryVoltage() * transformer.getSecondaryCurrent() < transformer.Pin()){  // if current and voltage exceeds
            // the maximal values the component, no matter what is, will be isolated from the rest of the grid.
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
        double inputPower = transformer.Pout();
        double powerOutPut = inputPower * 0.97; // coefficient is due to resistance of the rust
        // on the switch// ;
        double eff = powerOutPut/inputPower;
        return eff;
    }

    @Override
    public void addDateToMantainanceHist(LocalDate lastMaintenanceDate) {
        mantainanceHist.add(lastMaintenanceDate);
    }

    public boolean checkCircuitStatus(){
        if (contactWear.equals("contact off")){
            return true;
        }
        return false;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }


}
