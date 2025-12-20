**4413 BES Term Project** 

**1\. LOCAL VERSION \- Github Link: [https://github.com/pyne1/BES\_TERM\_PROJECT\_2](https://github.com/pyne1/BES_TERM_PROJECT_2)**

Using the localized version. You will need to clone the repo and start your tomcat 9 server manually. 

Then execute [http://localhost:8080/4413\_BES\_Term\_Project/](http://localhost:8080/4413_BES_Term_Project/) in a browser: 

**2\. Dockerized \- Github Link:** [https://github.com/pyne1/4413\_BES\_Term\_Project\_Updated.git](https://github.com/pyne1/4413_BES_Term_Project_Updated.git)

This project is a Java-based e-commerce web application developed using Servlets and JSP, following the MVC architecture and DAO design pattern. The application uses MySQL for data persistence and Docker to ensure the system runs consistently across different machines without manual setup.

**Prerequisites**

Before running the project, the following need to installed on your machine:

• Git  
• Docker Desktop (includes Docker Compose)

You can verify Docker installation by running:

*docker \--version*  
*docker compose version*

**Step 1: Clone the Repository**

Open a terminal and run the following commands:

git clone [https://github.com/pyne1/4413\_BES\_Term\_Project\_Updated.git](https://github.com/pyne1/4413_BES_Term_Project_Updated.git)  
cd 4413\_BES\_Term\_Project\_Updated

**Step 2: Build and Run the Application Using Docker**

From the root directory of the project (where docker-compose.yml is located), run:

*docker compose up \-d \--build*

Note: You may need to run all the commands with superuser access (sudo)

*sudo docker compose up \-d \--build*

This command will:

* Build the Tomcat application container  
* Start the MySQL database container  
* Initialize the database schema and seed data  
* Connect the application and database using Docker networking

**Step 3: Access the Application in the Browser**

Once Docker finishes starting, open a web browser and navigate to:

[http://localhost:8081](http://localhost:8081)

From here you will be able to maneuver through the application and test all the functionalities. 

**Step 4: Verify Containers Are Running (Optional)**

To confirm that the containers are running, open a new terminal window and run:

*sudo docker ps*

You can also view container logs using:

*sudo docker logs bes\_app*  
*sudo docker logs bes\_db*

**Step 5: Verify Database Initialization (Optional)**

To access the MySQL database inside the container, run:

*sudo docker exec \-it bes\_db mysql \-uappuser \-papppass thriftstore\_products*

Then execute the following SQL commands:  
*SHOW TABLES;*  
*SELECT COUNT(\*) FROM products;*

**Step 6: Stopping the Application**

To stop the running containers, press Ctrl \+ C in the terminal or run:

*docker compose down*

To stop the containers and remove all database data, run:

*docker compose down \-v*

