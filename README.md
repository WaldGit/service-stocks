com.example.project
├── application       // Core application logic (use cases)
├── domain            // Business entities and domain logic
├── adapters          // Implementation of ports
│   ├── inbound       // Controllers, event listeners, etc.
│   ├── outbound      // Database, external services, etc.
└── ports             // Interface definitions
    ├── inbound       // Use case interfaces //
    ├── outbound      // Repository interfaces

Application: Business logic for the application.
- implementation of the inbound ports

Inbound Ports: Interfaces for use cases that the application exposes.
- Services interfaces
Outbound Ports: Interfaces for external services that the application exposes.
- Repositories interfaces

Inbound Adapters: Expose use cases via REST endpoints or other mechanisms.
- Controller, implementations of the inbound ports
- Handles HTTP requests from the frontend, maps them to service calls, and returns responses.

Outbound Adapters: Implement repository interfaces or APIs to interact with external systems.
- Database, implementations of the outbound ports



Responsibilities
Responsibilities
application
Purpose: Contains the core application logic, implementing the workflows (use cases) of the system.

Details:

Implements the inbound ports (use case interfaces) to execute the business workflows.
Orchestrates interactions between the domain and outbound ports (interfaces for external dependencies).
Does not contain direct references to frameworks (e.g., Spring) or external systems.

domain
Purpose: Encapsulates the business entities and domain logic.

Details:

Contains pure business logic that is independent of application workflows or external systems.
Contains entities, value objects, and domain services.
Avoids any references to external systems, libraries, or frameworks.

ports
Purpose: Defines the interfaces (contracts) for communication between the application and adapters.

Details:

Inbound Ports: Define the use cases (what the application offers). Typically implemented by services in the application layer.
Example: OrderService interface.
Outbound Ports: Define the external dependencies (what the application requires). Implemented by adapters for databases, APIs, etc.
Example: OrderRepository interface.

adapters
Purpose: Contains the implementations of the ports, connecting the application to external systems.
Subcategories:
Inbound Adapters: Handle incoming requests (e.g., HTTP requests, messages from a queue, CLI commands) and call the application layer (via inbound ports).
Examples: REST controllers, event listeners.
Outbound Adapters: Handle communication with external systems (e.g., databases, APIs) and implement outbound ports.
Examples: Repository implementations, API clients.

Detailed Roles of Adapters
Inbound Adapters
Responsibilities:

Expose application use cases through mechanisms like REST, GraphQL, message queues, etc.
Validate and translate incoming requests into domain-level objects.
Call the application layer (via inbound ports) to execute use cases.

Outbound Adapters
Responsibilities:

Implement outbound ports to handle database access, API calls, or messaging.
Translate application-level objects into formats required by external systems.
Abstract external details from the application layer.






