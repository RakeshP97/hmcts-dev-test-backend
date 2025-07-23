# HMCTS Dev Test Backend
This will be the backend for the brand new HMCTS case management system. As a potential candidate we are leaving
this in your hands. Please refer to the brief for the complete list of tasks! Complete as much as you can and be
as creative as you want.

You should be able to run `./gradlew build` to start with to ensure it builds successfully. Then from that you
can run the service in IntelliJ (or your IDE of choice) or however you normally would.

There is an example endpoint provided to retrieve an example of a case. You are free to add/remove fields as you
wish.


## Running the application
To run the application, you can run the Application class `com.hmcts.devtestbackend.Application` in your IDE or use the command line:

swagger-ui: https://localhost:4000/swagger-ui/index.html
```

it internal run the H2 DB to storethe data.
Acess H2 console at https://localhost:4000/h2-console
use below credentials to connect to the H2 DB:
    Driver Class:	 org.h2.Driver
    JDBC URL:	jdbc:h2:mem:testdb
    User Name:	sa
    Password:	password
Click the "Connect" button to connect to the H2 database.
```

# API Endpoints Documentation

## Task Management API

### Base URL
`/api/task`

---

### Endpoints

#### 1. Create a New Task
**URL:** `/createNewTask`
**Method:** `POST`
**Description:** Creates a new task based on the request body.
**Request Body:**
```json
{
  "id": <Long>,
  "name": <String>,
  "description": <String>,
  "status": <String>
}
```
**Response:**
201 Created: Returns the created task object.
400 Bad Request: Invalid request body.

#### 2. Get All Tasks
**URL:** `/getAllTasks`
**Method:** `GET`
**Description:**  Retrieves all tasks available in the database.
**Response:**
200 OK: Returns a list of tasks.
204 No Content: No tasks found.


#### 3. Get Task by ID
**URL:** `/getTaskById`
**Method:** `GET`
**Description:** Retrieves the details of a task by its ID.
**Query Parameters:**
   id (Long): The ID of the task.
**Response:**
   200 OK: Returns the task object.


#### 4. Update Task Status
**URL:** `/UpdateTaskStatus`
**Method:** `POST`
**Description:** Updates the status of a task by its ID.
**Query Parameters:**
id (Long): The ID of the task.
status (String): The new status of the task.

**Response:**
   200 OK: Returns the updated task object.
   204 No Content: Task not found.


#### 5. Delete Task by ID
**URL:** `/deleteTask/{id}`
**Method:** `DELETE`
**Description:** Deletes a task by its ID.
**Path Parameters:**
   id (Long): The ID of the task.

**Response:**
   200 OK: Task deleted successfully.
