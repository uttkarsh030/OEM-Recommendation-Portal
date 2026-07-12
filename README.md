# 🛡️ OEM Recommendation Portal

> A full-stack enterprise recommendation management system built using Spring Boot, React, JWT Authentication, and MySQL.

---

## 📖 Overview

The OEM Recommendation Portal is a role-based web application designed to streamline the process of managing OEM recommendations across departments.

The system allows administrators to manage departments and users, bankers to review assigned recommendations, vendors to submit OEM recommendations, and department heads to oversee departmental activities.

The application implements secure JWT authentication and role-based authorization to ensure that every user only has access to their permitted resources.

---

## ✨ Features

- 🔐 JWT Authentication
- 👥 Role-Based Access Control
- 🏢 Department Management
- 👨‍💼 Banker Management
- 🏭 Vendor Registration
- 📄 Recommendation Submission
- ✅ Recommendation Approval Workflow
- 📊 Dashboard for every role
- 🗂 Audit Logging
- ⚡ RESTful APIs
- 💾 MySQL Database Integration

---

## 👥 User Roles

### 👑 Admin

- Manage Departments
- Approve Bankers
- Assign Department Heads
- Monitor Recommendations
- Manage Vendors
- View Audit Logs

---

### 🏦 Banker

- View Assigned Recommendations
- Review Vendor Requests
- Update Recommendation Status

---

### 🏭 Vendor

- Register
- Submit Recommendations
- Track Recommendation Status

---

### 👨‍💼 Department Head

- View Department Recommendations
- Monitor Assigned Bankers
- Review Department Activity

---

## 🛠 Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Maven

### Frontend

- React
- Vite
- JavaScript
- Axios
- Tailwind CSS *(Remove if not used)*

### Database

- MySQL

### Tools

- Git
- GitHub
- Postman
- VS Code
- IntelliJ IDEA

---

## 📂 Project Structure

```
OEM-Recommendation-Portal
│
├── README.md
│
├── oem-portal
│   ├── controller
│   ├── service
│   ├── repository
│   ├── security
│   ├── config
│   ├── model
│   └── dto
│
└── oem-portal-frontend
    ├── src
    ├── components
    ├── pages
    ├── services
    └── assets
```

---

## 🏗 System Architecture

*(Architecture diagram will be added here.)*

---

## 📸 Screenshots

### Login Page

*(Coming Soon)*

### Admin Dashboard

*(Coming Soon)*

### Banker Dashboard

*(Coming Soon)*

### Vendor Dashboard

*(Coming Soon)*

### Department Head Dashboard

*(Coming Soon)*

---

## 🚀 Getting Started

### Backend

```bash
cd oem-portal
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd oem-portal-frontend
npm install
npm run dev
```

---

## 🔑 Environment Variables

Configure the following before running the project.

```
Database URL
Database Username
Database Password
JWT Secret
```

---

## 📡 API Overview

### Authentication

```
POST /api/auth/login
```

### Admin

```
POST /api/admin/createDepartment
POST /api/admin/assignBanker
```

### Banker

```
GET /api/banker/dashboard
```

### Vendor

```
POST /api/vendor/register
POST /api/vendor/recommendation
```

---

## 📈 Future Improvements

- Docker Deployment
- Kubernetes Support
- Email Notifications
- Analytics Dashboard
- AI-Based Recommendation Ranking
- Real-Time Notifications
- Report Generation

---

## 👨‍💻 Author

**Uttkarsh Kumar**

GitHub: https://github.com/uttkarsh030

---

⭐ If you found this project useful, consider giving it a star.