# NASA APOD Viewer

A simple web application to display NASA's Astronomy Picture of the Day (APOD).
It uses a Java (SparkJava) backend to fetch data from the NASA API and an HTML/CSS/JavaScript frontend to display it.

## Prerequisites

*   Java Development Kit (JDK) 11 or higher
*   Apache Maven
*   A NASA API Key (get one for free from [https://api.nasa.gov/](https://api.nasa.gov/))

## Setup

1.  **Clone the repository or create the files:**
    Make sure all the files (`pom.xml`, `App.java`, `index.html`, `style.css`, `script.js`) are in the correct directory structure as outlined in the main response.

2.  **Update NASA API Key:**
    Open `src/main/java/com/example/apod/App.java` and replace `"DEMO_KEY"` with your actual NASA API key:
    ```java
    private static final String NASA_API_KEY = "YOUR_ACTUAL_NASA_API_KEY";
    ```

## How to Run

1.  **Open a terminal** in the root directory of the project (`nasa-apod-viewer/`).

2.  **Build the project using Maven:**
    ```bash
    mvn clean package
    ```
    This will compile the Java code and create an executable JAR file in the `target/` directory (e.g., `nasa-apod-viewer-1.0-SNAPSHOT.jar`).

3.  **Run the application:**
    ```bash
    java -jar target/nasa-apod-viewer-1.0-SNAPSHOT.jar
    ```
    (Adjust the JAR file name if it's different).

4.  **Open your web browser** and go to:
    http://localhost:8080

You should see the webpage with a button. Click the button to fetch and display the Astronomy Picture of the Day.

## Project Structure

