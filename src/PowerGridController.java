
import java.util.*;

public class PowerGridController {
    private Map<String,ElectricalComponent> componentRegistry = new LinkedHashMap<>(); // has been chosen
    // to keep insertion order
   private Queue<MantainanceTask> maintainanceQue = new PriorityQueue<>(new comparatorByPriority());
   private List<PowerGenerator> availableGenerators = new ArrayList<>();
   private Set<String> faultyComponents = new LinkedHashSet<>(); // because allows null elements, keeps
    // insertion order
    //protected MantainanceTask scheduledDate;
    protected PowerGenerator genRef;
    protected CircuitBreaker CBRef;
    protected Transformer transfRef;

    public PowerGridController(PowerGenerator genRef, Transformer transfRef, CircuitBreaker CBRef) {
        this.transfRef = transfRef;
        this.CBRef = CBRef;
        this.genRef = genRef;
    }

    public void registerComponent(String componentID, ElectricalComponent ec){
        componentRegistry.putIfAbsent(componentID, ec);
    }

    public void removeComponent(String componentID){
        componentRegistry.remove(componentID);
    }



    public void processMantainanceQue(){
        Iterator<MantainanceTask> it = maintainanceQue.iterator();
        while (it.hasNext()){
            MantainanceTask currentTask = maintainanceQue.peek();
            String currentComponent = currentTask.getComponentId();
            ElectricalComponent actualComp = componentRegistry.get(currentComponent);
            actualComp.setStatus(PowerComponentStatus.MAINTENANCE);
            currentTask.executeTask(actualComp);
            currentTask.updatePriority(4);
            actualComp.setStatus(PowerComponentStatus.OPERATIONAL);
            faultyComponents.remove(actualComp);
            maintainanceQue.remove(currentTask);
        }
    }

    public void monitorGridStatus(){
        boolean genBroken = false;
        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            var component = entry.getValue();
            if (component instanceof PowerGenerator){
                int excitationField = ((PowerGenerator) component).getExcitationField();
                    if (excitationField < 10){
                        // java is not quite clever to change type from electrical Component to Power Generator,
                        // so this procedure requires a manual action (casting). Same as circuitBreaker
                        component.setStatus(PowerComponentStatus.FAULT);
                        genBroken = true;
                        faultyComponents.add(entry.getKey());
                        //executeTaskRef.executeTask(entry.getValue());
                    }
            }
            if (component instanceof Transformer){
                if (genBroken){
                    component.setStatus(PowerComponentStatus.OFFLINE);
                }
            }
            if (component instanceof CircuitBreaker){
                if (genBroken){
                    CircuitBreaker cb = (CircuitBreaker) component;
                    cb.openCircuit();
                }
            }

        }
    }

// unuseful, only 1 generator exists for now

//    public void balanceLoad(){
//        double currentFromGen = 0;
//        double maxCapacity = 0;
//        double totMaxCapacity = 5*maxCapacity;
//        if (electricalComponentRef.status.equals(PowerComponentStatus.OPERATIONAL)){
//            currentFromGen = electricalComponentRef.current;
//            maxCapacity = pGenRef.getMaxCapacity();
//            double genShare = maxCapacity/totMaxCapacity * currentFromGen;
//        }
//    }

    public void handleEmergencyShutDown(){
        for (PowerGenerator gen : availableGenerators){
            gen.stopGenerator("off");
            gen.updateStatus(PowerComponentStatus.OFFLINE);
        }

        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            if (entry.getValue() instanceof CircuitBreaker){
                CBRef.openCircuit();
            }
        }

        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            entry.getValue().setStatus(PowerComponentStatus.OFFLINE);
        }
        // now every componet is offline, this does not mean if turned on can be operational. If was broken
        // at the moment of emergency, remains broken.
        //faultyComponents.clear();
    }

    private boolean validateGridIntegrity(){
        boolean integrityStatus = true;
        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            ElectricalComponent ec = entry.getValue();
            if (!ec.operatingParametersValidation()){
                integrityStatus = false;
                return integrityStatus;
            }
            PowerComponentStatus componentStatus = ec.getStatus();
            if (componentStatus.equals(PowerComponentStatus.FAULT) ||
                    componentStatus.equals(PowerComponentStatus.OFFLINE)){
                integrityStatus = false;
            }
        }

        // despite generator is only one the loop is made with prospective of adding more of them.
        int operationalGeneratorsCount = 0;
        for (PowerGenerator gen : availableGenerators){
            PowerComponentStatus genStatus = gen.getStatus();
            if (genStatus.equals(PowerComponentStatus.OPERATIONAL)){
                operationalGeneratorsCount++;
            }
        }
        if (operationalGeneratorsCount == 0){
            integrityStatus = false;
        }

        boolean hasOperationalTransformer = false;
        boolean hasOperationalCircuitBreaker = false;

        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            ElectricalComponent transformer = null;
            ElectricalComponent ec = entry.getValue();
            if (ec instanceof Transformer){
                transformer = ec;
                PowerComponentStatus ecStatus = ec.getStatus();
                if (ecStatus.equals(PowerComponentStatus.OPERATIONAL)){
                    hasOperationalTransformer = true;
                }
            }
        }
        for (Map.Entry<String, ElectricalComponent> entry : componentRegistry.entrySet()){
            ElectricalComponent circuitBreaker = null;
            ElectricalComponent cb = entry.getValue();
            if (cb instanceof CircuitBreaker){
                circuitBreaker = cb;
                PowerComponentStatus cbStatus = cb.getStatus();
                if (cbStatus.equals(PowerComponentStatus.OPERATIONAL)){
                    hasOperationalCircuitBreaker = true;
                }
            }
        }
        if (hasOperationalTransformer == false || hasOperationalCircuitBreaker == false){
            integrityStatus = false;
        }
        return integrityStatus;
    }

    public Map<String, ElectricalComponent> getComponentRegistry() {
        return componentRegistry;
    }

    public Queue<MantainanceTask> getMaintainanceQue() {
        return maintainanceQue;
    }

    // controller methоd to check if generator is under load or not
    public boolean isConnectedToTheLoad(String command){
        if (command.equals("disconnect")){
            return false;
        }
        else if (command.equals("connect")){
            return true;
        }
        return false;
    }

    public Set<String> getFaultyComponents() {
        return faultyComponents;
    }


}
