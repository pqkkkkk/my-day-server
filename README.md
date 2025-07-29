# My Day - Backend Server (Ongoing Project)
> A backend server for the **My Day** application, a personal task management tool.

[![Spring](https://img.shields.io/badge/Spring-green?style=for-the-badge&logo=spring)](https://spring.io/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)


## 🚀 Overview
**My Day** is a personal task management application that helps users
organize their tasks, track their progress, track deadlines, and manage their time effectively. This repository contains backend implementation by using Spring Boot and PostgreSQL for database management.

**🔗 Frontend Repository:** [my-day-client](https://github.com/pqkkkkk/my-day-client)


## 📁 Project Structure
```
my-day-server/
    ├── src/
    │    ├── main/
    │    │    ├── java/org/pqkkkkk/my_day_server/
    │    │    │    ├── common/
    │    │    │    ├── config/
    │    │    │    ├── task/
    │    │    │    ├── user/
    │    │    │    ├── MyDayServerApplication.java
    |    ├── test/
    ├── [README.md]
    ├── .gitignore
```

## 🛠️ Technologies Used
- **Java**: Programming language used for backend development.
- **Spring Boot**: Framework for building the backend server.
- **PostgreSQL**: Relational database management system for data storage.
- **Spring Data JPA**: For database interactions and ORM.
- **Spring Security**: For securing the application and managing user authentication.
- **OpenAPI**: For API documentation.
- Set up CI workflow using **GitHub Actions** for continuous integration.


## Features
- **User Management**: Register, login, and manage user profiles.
- **List Management**: Create, update, delete, and view lists.
    - **Search and filter lists**.
    - **Task Lists**: Organize tasks into lists for better categorization.
    - **Statistics**: View basic statistics for each list, including task counts and completion rates.
- **Task Management**: Create, update, delete, and view tasks.
    - Categorize tasks by priority, status, and due date.
    - Tracking task status (e.g., to do, in progress, completed).
    - **Task Steps**: Break down tasks into many steps for better organization and tracking.
    - **Search and filter tasks**.
- **Intend features**:
    - **Notifications**: Set up reminders and notifications for tasks, upcoming deadlines.
    - **Task suggestion**: Use AI to suggest task lists for the day based on deadlines, priorities, and user preferences.
    - **AI Assistant**: Assist users in managing their tasks and suggesting instructions to complete tasks.

