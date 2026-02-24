# 🏡 Smart Home Management System
## Final Course Project - MJT
___
### Project Overview
#### The Smart Home Management System is a multi-user platform designed to manage smart devices across various rooms.
#### It provides a centralized control for automation, power consumption tracking and data persistence.

#### The "User Home" Concept
To ensure data isolation and security, the system employs a sandbox folder architecture.
Each user is assigned a dedicated directory within the */home/root*.
This ensures that one user's configuration and device logs never interfere with another's.

#### 📁 Package Structure

* ```api``` — **The Core Controller**

  The *brain* of the house. This package contains the central logic that bridges the communication between the server layer and the data models.

**Responsibilities**: Validating user commands, managing the current state of the *Smart Home*, and ensuring that business rules (like "cannot turn on a device that doesn't exist") are enforced.


* ```exception```— **Error Handling Framework**

  A specialized package for custom checked and unchecked exceptions.

**Responsibilities**: Defining domain-specific errors like DeviceNotFoundException, UnauthorizedAccessException, or InvalidCommandException.
This ensures the server can provide meaningful feedback to the user rather than generic stack traces.


* ```io```— **The Persistence Layer** (The Librarian)

  Handles all interactions with the file system.

**Responsibilities**: Serializing objects to disk and deserializing them back into memory.
It manages the physical directory structure under */home/{user}* and ensures that SmartHomeLogger handles file locks safely during write operations.


* ```model```— **Data Blueprints**

  Contains the Plain Old Java Objects (POJOs) and record types that represent the entities in your system.

**Responsibilities**: Defining the hierarchy of devices (e.g., an abstract Device class with subclasses like SmartLight or Thermostat).
It holds the properties for Room and User objects, acting as the schema for your data.


* ```server```— **Multi-User Communication Hub**

  The entry point for network interactions.

**Responsibilities**: Managing the ServerSocket, accepting new client connections, and spawning threads for each user.
It translates raw string input from the network into commands the api can understand.

* ```tasks```— **Background Automation** (The Heartbeat)

  Contains classes that implement Runnable or Callable for scheduled execution.

**Responsibilities**: Running the *invisible* parts of the house.
This includes the PowerMonitoringTask, which samples energy usage every few seconds, and the AutoSaveTask, which prevents data loss by syncing memory to disk at regular intervals.

💡 **Why this structure?**
This architecture follows the Layered Architecture pattern.
By separating the server from the api, we ensure that if you ever wanted to switch from a Console-based interface to a Web-based interface, you would only need to replace the server package while the "brain" (api) remains untouched.

___
### Components
#### 📄 The Persistence Layer (IO)

* **SmartHomeLogger**: Manages all file-based operations with two distinct modes:


* **Overwrite**: Used for updating the current state of Rooms and Devices to ensure the configuration file remains a *single source of truth*.


* **Append**: Used for historical power logs, allowing for a continuous audit trail of energy consumption.


* **File Hierarchy**: Data is organized logically as */home/{user}/rooms/{room_name}.json*.


**This structure allows for easy scalability and manual inspection if necessary.**

#### 🤹🏻‍♀️ The Task System
* **PowerMonitoringTask**: A background thread that tracks energy consumption across all active devices over time, providing data for efficiency reports.


* **AutoSaveTask**: Acts as a *safety net*, periodically flushing the current system state to disk to prevent data loss in the event of an unexpected shutdown.
___
### 🚀 Design Patterns & Principles

#### Patterns Used
* **Command Pattern**: Implemented in executeCommand to decouple the requester (the user) from the provider (the device logic).


* **Factory Pattern**: The DeviceFactory is utilized during the initialization phase to instantiate specific subclasses (e.g., Light, Thermostat) based on configuration data.


* **Dependency Injection**: The UserManager injects the specific user's file path into the API, ensuring the logic remains agnostic of the underlying file structure.


* **Registry / Singleton**: The UserManager maintains a registry of active sessions to prevent duplicate logins and manage global user state.

#### SOLID Principles
* **Interface Segregation**: Interfaces are kept lean, ensuring that device models only implement the functionality they actually require.


* **Dependency Inversion**: High-level modules (API) do not depend on low-level modules (specific Device implementations); both depend on abstractions.
___
### 🧵 Multi-thread & Synchronization

**To support a multi-user environment, the system is designed with rigorous thread safety:**

* **Synchronized Logging**: Methods within the SmartHomeLogger are synchronized to prevent data corruption during simultaneous write operations.


* **Concurrent Collections**: ConcurrentHashMap is used within the UserManager to safely handle multiple simultaneous logins and session lookups without risking ConcurrentModificationException.
___
### 👩🏻‍💻 Setup & Usage Guide
1. **Start the Server**: Execute the HomeServer main class to begin listening for client connections.
2. **User Login:** Connect via a client and use the following format:
   ```login <username> <password>```
3. **Command Examples**:
- ```add-device --room living-room --type light```


- ```turn-on --device ceiling-lamp```


- ```report-power --period 24h```

⚠️
**[!IMPORTANT] Security Note**: *For simplicity in this project, passwords are stored in plain text.
In a production environment, industry-standard hashing like BCrypt or Argon2 would be implemented.*
___
### System requirements
- #### Java Development Kit (JDK): Version 17 or higher (recommended).

- #### Network: Localhost access for Server/Client communication.

- #### Storage: Write permissions for the project root to create the /home/ directory structure.
___
### ❌ Troubleshooting

1. **Port Already in Use**

*Issue*: The server fails to start with a BindException.

*Solution*: Ensure no other instance of HomeServer is running. You can change the port constant in the server package if needed.

2. **Data Persistence Issues**

*Issue*: Devices or rooms disappear after a restart.

*Solution*: Check if the AutoSaveTask is running. Verify that the /home/{user} directory has not been moved or deleted manually.

3. **Connection Refused**

*Issue*: The Client cannot connect to the Server.

*Solution*: Verify the Server is fully initialized before launching the Client. Check that both are using the same port and IP address (127.0.0.1).

4. **File Corruption**

*Issue*: Unexpected errors when loading the home state.

*Solution*: The system expects a specific JSON/Text format. If a file was edited manually and contains a syntax error, the SmartHomeLogger may fail to parse it. Reset the user folder to start fresh.