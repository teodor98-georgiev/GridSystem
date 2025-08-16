import java.time.LocalDate;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        //The current architecture supports multiple generators through the availableGenerators list and component registry design patterns.
        // However, I chose to implement a single-unit system to concentrate on the more complex aspects: circular dependency resolution, proper encapsulation and
        // component interaction patterns. The multi-generator implementation would be largely repetitive instantiation and mathematical load distribution.

        System.out.println("=== ELECTRICAL POWER GRID MANAGEMENT SYSTEM TEST ===\n");

        // Initialize the power grid controller


        // the part of creation of objects and their registration was in test creation
        // and registration section but because I need references of objects in other tests (otherwise they are
        // confinated in the scope of test), I extracted them being absolutly sure that during instantiation won't be
        // any exceptins
        PowerGenerator generator = new PowerGenerator(
                "GEN-001", ComponentType.GENERATOR, PowerComponentStatus.OPERATIONAL,
                LocalDate.of(2020, 1, 15), LocalDate.of(2024, 6, 1),
                null, null
        );
        generator.addDateToMantainanceHist(LocalDate.of(2024, 6, 1));


        // Create a Transformer
        Transformer transformer = new Transformer(
                generator, "TRANS-001", ComponentType.TRANSFORMER, PowerComponentStatus.OPERATIONAL,
                LocalDate.of(2019, 5, 10), LocalDate.of(2024, 3, 15),null
        );

        transformer.addDateToMantainanceHist(LocalDate.of(2024, 3, 15));



        // Create a CircuitBreaker
        CircuitBreaker circuitBreaker = new CircuitBreaker(
                "CB-001", ComponentType.CIRCUIT_BREAKER, PowerComponentStatus.OPERATIONAL,
                LocalDate.of(2021, 8, 20), LocalDate.of(2024, 7, 10),
                transformer
        );
        circuitBreaker.addDateToMantainanceHist(LocalDate.of(2024, 7, 10));



        PowerGridController gridController = new PowerGridController(generator,transformer, circuitBreaker);

        // because of circular dependencies (on generator, transformer and circuitBreaker) has been used setters to
        // add them in a 2nd moment. The problem was I needed a reference in PowerGenerator of CircuitBreaker type,
        // so instance of CircuitBreaker must be created for 1st, BUT, it contains a Transformer reference so
        // I thought to instanciate 1st transformer but this one holds reference of PowerGenerator, so, that
        // means I cannot change the order of instantiating objects. To solve this problem setters came on help.
        generator.setCb(circuitBreaker);
        generator.setController(gridController);
        transformer.setPowerGenerator(generator);
        transformer.setController(gridController);
        circuitBreaker.setTransformer(transformer);




        // Register components with the grid controller
        gridController.registerComponent("GEN-001", generator);
        gridController.registerComponent("TRANS-001", transformer);
        gridController.registerComponent("CB-001", circuitBreaker);

        System.out.println(" Successfully created and registered components:");
        System.out.println("   Power Generator: " + generator.getComponentInfo());
        System.out.println("  - Transformer: " + transformer.getComponentInfo());
        System.out.println("  - Circuit Breaker: " + circuitBreaker.getComponentInfo());
        System.out.println(" Component registry size: " + gridController.getComponentRegistry().size());




        // Test 1: Create and register components
        //testComponentCreationAndRegistration(gridController);

        // Test 2: Test individual component functionality
        //testComponentFunctionality(gridController);

        // Test 3: Test grid monitoring and status management
        //testGridMonitoring(gridController);

        // Test 4: Test maintenance operations
        //testMaintenanceOperations(gridController);

        // Test 5: Simulate component failures
        //testComponentFailures(gridController);

        // Test 6: Test emergency shutdown
        testEmergencyShutdown(gridController);

        // Test 7: Test grid recovery operations
        testGridRecovery(gridController);

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }

    private static void testComponentCreationAndRegistration(PowerGridController controller) {
//        System.out.println("1. TESTING COMPONENT CREATION AND REGISTRATION");
//        System.out.println("===============================================");

        try {
            // creation and registration of grid elements

        }
        catch (Exception e) {
            System.out.println(" Error in component creation: " + e.getMessage());
        }
        System.out.println();
    }
    // --- importat note--- during testing previous tests from actual test must be commented to ensure each component is tested with its working status.
    // This rule is not applied for tests 6 and 7 which work together

    private static void testComponentFunctionality(PowerGridController controller) {
        System.out.println("2. TESTING INDIVIDUAL COMPONENT FUNCTIONALITY");
        System.out.println("==============================================");

        // Get components from registry
        PowerGenerator generator = (PowerGenerator) controller.getComponentRegistry().get("GEN-001");
        Transformer transformer = (Transformer) controller.getComponentRegistry().get("TRANS-001");
        CircuitBreaker circuitBreaker = (CircuitBreaker) controller.getComponentRegistry().get("CB-001");

        // Test PowerGenerator methods
        System.out.println("Testing PowerGenerator:");
        try {
            System.out.println("  - Generated electric power: " + generator.calculateGeneratedElectricPower(generator.activePower, generator.reactivePower, generator.powerFactor) + " W");
            System.out.println("  - Efficiency: " + (generator.calculateEfficiency() * 100) + "%");
            System.out.println("  - Max capacity: " + generator.getMaxCapacity() + " MW");
            System.out.println("  - Operating parameters valid: " + generator.operatingParametersValidation());

            // Test generator start/stop
            generator.startGenerator("on");
            System.out.println("   Generator started, status: " + generator.getStatus());

            // Test output adjustment
            generator.adjustOutput(230000000);
            System.out.println("   Output adjusted to meet grid demand");

        } catch (Exception e) {
            System.out.println("   Generator test error: " + e.getMessage());
        }

        // Test Transformer methods
        System.out.println("\nTesting Transformer:");
        try {
            System.out.println("  - Primary voltage: " + transformer.getPrimaryVoltage() + " V");
            System.out.println("  - Primary current: " + transformer.getPrimaryCurrent() + " A");
            System.out.println("  - Secondary voltage: " + transformer.getSecondaryVoltage() + " V");
            System.out.println("  - Secondary current: " + transformer.getSecondaryCurrent() + " A");
            System.out.println("  - Input power: " + transformer.Pin() + " W");
            System.out.println("  - Output power: " + transformer.Pout() + " W");
            System.out.println("  - Efficiency: " + (transformer.calculateEfficiency() * 100) + "%");

            // Test temperature monitoring
            System.out.println("if temperature is normal:");
            System.out.println("---");
            transformer.monitorTemp(85.0);

            System.out.println("if temperature is too high:");
            transformer.monitorTemp(105.0);

        } catch (Exception e) {
            System.out.println("   Transformer test error: " + e.getMessage());
        }


        System.out.println("\nTesting CircuitBreaker:");
        try {
            System.out.println("  - Initial circuit status: " + (circuitBreaker.checkCircuitStatus() ? "Open" : "Closed"));
            System.out.println("  - Efficiency: " + (circuitBreaker.calculateEfficiency() * 100) + "%");
            System.out.println("  - Operating parameters valid: " + circuitBreaker.operatingParametersValidation());

            // Test circuit operations
            circuitBreaker.openCircuit();
            System.out.println("   Circuit opened, status: " + (circuitBreaker.checkCircuitStatus() ? "Open" : "Closed"));
            System.out.println("test if current exceeds max value");
            circuitBreaker.closeCircuit(4242352436.0);


            // Test trip mechanism
            //circuitBreaker.testTripMech(180080);//
            //System.out.println("   Trip mechanism tested with overcurrent condition");

        } catch (Exception e) {
            System.out.println("  Circuit breaker test error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testGridMonitoring(PowerGridController controller) {
        System.out.println("3. TESTING GRID MONITORING AND STATUS MANAGEMENT");
        System.out.println("=================================================");

        try {
            System.out.println("Initial grid status monitoring:");
            controller.monitorGridStatus();

            // Check component statuses
            for (String componentId : controller.getComponentRegistry().keySet()) {
                ElectricalComponent component = controller.getComponentRegistry().get(componentId);
                System.out.println("  - " + componentId + ": " + component.getStatus());
            }

            System.out.println(" Grid monitoring completed");
            System.out.println(" Faulty components detected: " + controller.getFaultyComponents().size());

        } catch (Exception e) {
            System.out.println(" Grid monitoring error: " + e.getMessage());
        }
        System.out.println();
    }


    private static void testMaintenanceOperations(PowerGridController controller) {
        System.out.println("4. TESTING MAINTENANCE OPERATIONS");
        System.out.println("==================================");

        try {
             //Create maintenance tasks for different components
            MantainanceTask emergencyTask = new MantainanceTask(
                    "TASK-001", "GEN-001", TaskType.EMERGENCY, controller);


            MantainanceTask preventiveTask = new MantainanceTask(
                    "TASK-002", "TRANS-001", TaskType.PREVENTIVE, controller);

            MantainanceTask routineTask = new MantainanceTask(
                    "TASK-003", "CB-001", TaskType.ROUTINE, controller);

            // Add tasks to maintenance queue
            controller.getMaintainanceQue().add(emergencyTask);
            controller.getMaintainanceQue().add(preventiveTask);
            controller.getMaintainanceQue().add(routineTask);


            System.out.println(" Created and queued maintenance tasks:");
            System.out.println("  - Emergency task for GEN-001: " + emergencyTask.getTaskDetails());
            System.out.println("  - Preventive task for TRANS-001: " + preventiveTask.getTaskDetails());
            System.out.println("  - Routine task for CB-001: " + routineTask.getTaskDetails());

            // Process maintenance queue
            System.out.println("\nProcessing maintenance queue:");
            controller.processMantainanceQue();  // to test the delay function
            System.out.println(" All maintenance tasks processed");

            // Test individual component maintenance
            PowerGenerator generator = (PowerGenerator) controller.getComponentRegistry().get("GEN-001");
            Transformer transformer = (Transformer) controller.getComponentRegistry().get("TRANS-001");
            CircuitBreaker circuitBreaker = (CircuitBreaker) controller.getComponentRegistry().get("CB-001");

            System.out.println("\nTesting individual component maintenance:");
            try {
                generator.performMantainance(emergencyTask.getTaskType());
                emergencyTask.scheduleMantainanceTask(generator.componentId,
                        emergencyTask.getTaskType(), generator);

                transformer.performMantainance(preventiveTask.getTaskType());
                preventiveTask.scheduleMantainanceTask(transformer.componentId, preventiveTask.getTaskType(), transformer);

                circuitBreaker.performMantainance(routineTask.getTaskType());
                routineTask.scheduleMantainanceTask(circuitBreaker.componentId, routineTask.getTaskType(), circuitBreaker);
                System.out.println(" Component maintenance checks completed");
            } catch (Exception e) {
                System.out.println("  Maintenance scheduling validation working: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(" Maintenance operations error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testComponentFailures(PowerGridController controller) {
        System.out.println("5. TESTING COMPONENT FAILURE SCENARIOS");
        System.out.println("=======================================");

        try {
            // Simulate generator overload
            System.out.println("Simulating generator overload condition:");
            PowerGenerator generator = (PowerGenerator) controller.getComponentRegistry().get("GEN-001");
            generator.connectGenToTheLoad("connect");

            // Force high current to trigger validation failure
            //System.out.println("  Adjusting generator output beyond safe limits...");
            //generator.adjustOutput(1500000000); // Beyond max capacity has been commented to ensure default contactWear of circuit breaker (contact on) for the next test
           // System.out.println("  Generator status after overload: " + generator.getStatus());

            // Simulate transformer overheating
            System.out.println("\nSimulating transformer overheating:");
            Transformer transformer = (Transformer) controller.getComponentRegistry().get("TRANS-001");
            transformer.monitorTemp(120.0); // Critical temperature

            // Simulate circuit breaker fault
            System.out.println("\nSimulating circuit breaker overcurrent protection:");
            CircuitBreaker circuitBreaker = (CircuitBreaker) controller.getComponentRegistry().get("CB-001");
            // to test tripMech from on position to off in case of a failure, tests from 2 to 4 inclusively, lines from 97 to 271, and lines from 283 to 286
            // inclusively must be commented to ensure contact wear of circuit breaker is on, instead of off
            circuitBreaker.testTripMech(200000.0); // High current
            System.out.println(" Circuit breaker tripped due to overcurrent");

            // Monitor grid after failures
            System.out.println("\nGrid status after component failures:");
            controller.monitorGridStatus();

            // Show faulty components
            if (!controller.getFaultyComponents().isEmpty()) {
                System.out.println("Components requiring attention:");
                for (String faultyId : controller.getFaultyComponents()) {
                    ElectricalComponent component = controller.getComponentRegistry().get(faultyId);
                    System.out.println("    - " + faultyId + ": " + component.getStatus());
                }
            }

        } catch (Exception e) {
            System.out.println(" Component failure test error: " + e.getMessage());
        }
        System.out.println("because of circuit breaker insertion in overcurrent condition, there are no faulty components");
        System.out.println();
    }


    private static void testEmergencyShutdown(PowerGridController controller) {
        System.out.println("6. TESTING EMERGENCY SHUTDOWN PROCEDURES");
        System.out.println("=========================================");

        try {
            System.out.println("Initiating emergency shutdown sequence...");
            controller.handleEmergencyShutDown();

            System.out.println(" Emergency shutdown completed");
            System.out.println("Grid status after emergency shutdown:");

            // Check all component statuses
            for (String componentId : controller.getComponentRegistry().keySet()) {
                ElectricalComponent component = controller.getComponentRegistry().get(componentId);
                System.out.println(" " + componentId + ": " + component.getStatus());
            }

        } catch (Exception e) {
            System.out.println(" Emergency shutdown error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testGridRecovery(PowerGridController controller) {
        System.out.println("7. TESTING GRID RECOVERY OPERATIONS");
        System.out.println("====================================");

        try {
            // Attempt to restart components
            System.out.println("Attempting grid recovery...");

            PowerGenerator generator = (PowerGenerator) controller.getComponentRegistry().get("GEN-001");
            Transformer transformer = (Transformer) controller.getComponentRegistry().get("TRANS-001");
            CircuitBreaker circuitBreaker = (CircuitBreaker) controller.getComponentRegistry().get("CB-001");

            // Reset component statuses
            generator.updateStatus(PowerComponentStatus.OPERATIONAL);
            transformer.updateStatus(PowerComponentStatus.OPERATIONAL);
            circuitBreaker.updateStatus(PowerComponentStatus.OPERATIONAL);

            System.out.println(" Component statuses reset to operational");

            // Test connectivity
            System.out.println("Testing load connectivity:");
            boolean connected = controller.isConnectedToTheLoad("connect");
            System.out.println("  - Load connection status: " + (connected ? "Connected" : "Disconnected"));

            // Restart generator
            generator.startGenerator( "on");
            System.out.println(" Generator restarted successfully");

            // Connect generator to the load
            generator.connectGenToTheLoad("connect");
            System.out.println(" Circuit breaker closed, power flow restored");

            // Final grid monitoring
            System.out.println("\nFinal grid status check:");
            controller.monitorGridStatus();

            // Display final component information
            System.out.println("\nFinal component status:");
            for (String componentId : controller.getComponentRegistry().keySet()) {
                ElectricalComponent component = controller.getComponentRegistry().get(componentId);
                System.out.println("  - " + componentId + ": " + component.getStatus());
                System.out.println("    Efficiency: " + (component.calculateEfficiency() * 100) + "%");
            }

        } catch (Exception e) {
            System.out.println(" Grid recovery error: " + e.getMessage());
        }
        System.out.println();
    }
}