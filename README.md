# Power Grid Management System
Overview:
This project simulates a comprehensive electrical power grid management system implemented in Java. It models the core components of an electrical grid including power generators, transformers, and circuit breakers, along with their monitoring, maintenance, and control operations.

## Core Components
### ElectricalComponent (Abstract Base Class)

Base class for all grid components
Provides common functionality: status management, maintenance history, efficiency calculations.
Enforces implementation of component-specific validation and maintenance procedures.

### PowerGenerator

Simulates electrical power generation with realistic electrical parameters.
Handles power factor correction and load balancing.
Manages generator startup/shutdown procedures.
Calculates active and reactive power output.

### Transformer

Models voltage transformation between generator and distribution levels.
Implements primary/secondary voltage and current relationships.
Includes temperature monitoring and cooling system management.
Calculates transformation losses and efficiency.

### CircuitBreaker

Provides overcurrent protection for the grid.
Implements delayed trip mechanisms for fault conditions.
Manages circuit opening/closing operations.
Protects against overload conditions.

### PowerGridController

Central management system for all grid components.
Handles component registration and monitoring.
Processes maintenance task queues with priority scheduling.
Implements emergency shutdown and recovery procedures.
Monitors grid integrity and identifies faulty components.

## Supporting Systems
### Maintenance Management

MantainanceTask: Represents scheduled maintenance operations.
Priority-based task scheduling (Emergency → Corrective → Preventive → Routine).
Automatic scheduling based on maintenance type and component history.
Task execution with proper component isolation.

### Status Management

Component status tracking (OPERATIONAL, MAINTENANCE, FAULT, OFFLINE).
Grid-wide status monitoring and fault detection.
Automated response to component failures.

## Key Features
### 1. Realistic Electrical Modeling

Three-phase power calculations.
Power factor correction mechanisms.
Voltage transformation ratios.
Current and voltage validation limits.
Efficiency calculations based on electrical losses.

### 2. Fault Detection and Protection

Overcurrent protection via circuit breakers.
Temperature monitoring for transformers.
Excitation field monitoring for generators.
Automatic component isolation during faults.

### 3. Maintenance Management

Priority-based maintenance scheduling.
Maintenance history tracking.
Component-specific maintenance procedures.
Queue processing with proper component isolation.

### 4. Grid Control Operations

Emergency shutdown procedures.
Grid recovery and restart operations.
Load balancing capabilities.
Component connectivity management.

### 5. Monitoring and Diagnostics

Real-time grid status monitoring.
Component health validation.
Faulty component identification.
Operational parameter verification.

## System Flow

### Initialization: Components are created and registered with the grid controller.
### Normal Operation: Generator produces power, transformer steps voltage, circuit breaker manages protection.
### Monitoring: Continuous monitoring of all component parameters and grid status.
### Maintenance: Scheduled maintenance tasks are processed based on priority.
### Fault Response: Automatic protection mechanisms activate during fault conditions.
### Recovery: Controlled restart and recovery procedures restore grid operation.

## Component Relationships
PowerGenerator → Transformer → CircuitBreaker → Load
     ↑              ↑              ↑
     └──────────────┴──────────────┴─→ PowerGridController

Generator supplies power to the transformer
Transformer steps voltage for distribution
CircuitBreaker provides protection and load isolation
Controller manages all components and coordinates operations

## Running the System
The main class (Main.java) includes comprehensive test scenarios:

### Component creation and registration
### Individual component functionality testing
### Grid monitoring and status management
### Maintenance operations
### Component failure scenarios
### Emergency shutdown procedures
### Grid recovery operations

## Test Scenarios

### Component Functionality Test: Validates individual component operations
### Grid Monitoring Test: Checks system-wide status monitoring
### Maintenance Operations Test: Tests maintenance scheduling and execution
### Failure Scenarios Test: Simulates component failures and protection responses
### Emergency Shutdown Test: Tests emergency procedures
### Recovery Operations Test: Validates grid restart capabilities

## Technical Specifications
### Electrical Parameters

#### Generator: 22kV, 10kA, 450MW capacity
#### Transformer: 100:1 transformation ratio, oil cooling
#### Circuit Breaker: 18kA breaking capacity

## Safety Features

### Overcurrent protection (>18kA)
### Overvoltage protection (>25kV)
### Temperature monitoring (>100°C for transformers)
### Power factor correction (target: 0.95)

## Future Enhancements

### Multiple generator support for load balancing
### Advanced protection coordination
### SCADA system integration
### Real-time data acquisition
### Distributed control architecture
### Enhanced fault analysis capabilities

# Note: This system is designed for educational and simulation purposes, demonstrating key concepts in power system management and object-oriented programming.
