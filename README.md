<div align="center">
  <h1>🏦 OEM Portal</h1>
  <p><strong>OEM Recommendation Management System for Banking Sector</strong></p>

  <p>
    <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="License: MIT"></a>
    <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 21">
    <img src="https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot 3.5">
    <img src="https://img.shields.io/badge/React_18-61DAFB?style=for-the-badge&logo=react&logoColor=black" alt="React 18">
    <img src="https://img.shields.io/badge/MySQL_8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL 8.0">
  </p>
  <p>
    <img src="https://img.shields.io/badge/Status-Active-success.svg?style=for-the-badge" alt="Status">
    <img src="https://img.shields.io/badge/PRs-Welcome-brightgreen.svg?style=for-the-badge" alt="PRs Welcome">
    <img src="https://img.shields.io/badge/Version-1.0.0-blue.svg?style=for-the-badge" alt="Version 1.0.0">
    <img src="https://img.shields.io/badge/Made%20with-%E2%9D%A4%EF%B8%8F-red.svg?style=for-the-badge" alt="Made with Love">
  </p>

  <p>A comprehensive full-stack platform for managing OEM recommendations, security patches, and system updates in banking institutions with role-based access control and complete audit trails.</p>

  <p>
    <a href="#live-demo">View Demo</a> •
    <a href="#11-installation--setup">Quick Start</a> •
    <a href="#13-api-documentation">API Docs</a> •
    <a href="#16-testing">Testing</a>
  </p>
</div>

---

## 2. TABLE OF CONTENTS

1. [Project Title](#1-project-title)
2. [Table of Contents](#2-table-of-contents)
3. [About The Project](#3-about-the-project)
4. [Key Features](#4-key-features)
5. [Technology Stack](#5-technology-stack)
6. [System Architecture](#6-system-architecture)
7. [User Roles & Permissions](#7-user-roles--permissions)
8. [Recommendation Workflow](#8-recommendation-workflow)
9. [Project Structure](#9-project-structure)
10. [Prerequisites](#10-prerequisites)
11. [Installation & Setup](#11-installation--setup)
12. [Running The Application](#12-running-the-application)
13. [API Documentation](#13-api-documentation)
14. [Database Schema](#14-database-schema)
15. [Screenshots](#15-screenshots)
16. [Testing](#16-testing)
17. [Security Features](#17-security-features)
18. [Key Learnings & Challenges](#18-key-learnings--challenges)
19. [Future Enhancements](#19-future-enhancements)
20. [Contributing](#20-contributing)
21. [License](#21-license)
22. [Author](#22-author)
23. [Acknowledgments](#23-acknowledgments)
24. [Support](#24-support)

---

## 3. ABOUT THE PROJECT

**OEM Portal** is a mission-critical web application engineered to bridge the gap between Original Equipment Manufacturers (OEMs) and banking IT departments. Managing system updates, security patches, and hardware recommendations at scale requires immense oversight to ensure zero downtime and strict regulatory compliance.

This project was built to replace fragmented, email-based tracking systems with a unified, transparent platform. By enforcing a strict 8-step lifecycle workflow and meticulously logging every action, the OEM Portal reduces compliance risk and streamlines the deployment of vital infrastructure updates.

### Real-World Banking Use Case
In a banking environment, an unpatched server or outdated hardware component can lead to disastrous security vulnerabilities. When a vendor (e.g., Cisco, Dell) releases a critical patch recommendation, it must traverse a strict hierarchy: Admin intake ➡️ Department triage ➡️ Banker implementation ➡️ Cross-verification. This system digitizes and enforces that exact chain of custody, complete with real-time dashboards and comprehensive audit trails.

---

## 4. KEY FEATURES

### 🔐 Security & Access Control
- ✅ **Role-Based Access Control (RBAC)**: Strict segregation of duties across 4 distinct user tiers.
- ✅ **JWT Authentication**: Stateless, tamper-proof session management using JWT.
- ✅ **Secure Password Management**: BCrypt hashing and auto-generated system passwords for vendors.
- ✅ **Complete Audit Trail**: Immutable logging of every system change for compliance auditing.

### 🔄 Workflow Management
- ✅ **8-Stage Lifecycle**: Strict state-machine progression from vendor upload to final admin verification.
- ✅ **Department Isolation**: Dynamic routing of tasks to appropriate departments (toggleable states).
- ✅ **Approval Gates**: Bankers must be vetted and approved by admins before platform access.

### 🎨 UI/UX Enhancements
- ✅ **Responsive Design**: Mobile-first architecture scaling beautifully to enterprise desktop monitors.
- ✅ **Dark/Light Mode**: Seamless theme switching managed via React Context API.
- ✅ **Real-Time Dashboards**: Customized statistical views based on the logged-in role.
- ✅ **Modal-Driven CRUD**: Clean, uninterrupted user flows without unnecessary page reloads.

### ⚙️ Technical Excellence
- ✅ **DTO Pattern**: Optimized data transfer shielding internal database entities from the presentation layer.
- ✅ **Global Exception Handling**: Standardized API error responses for predictable frontend parsing.
- ✅ **CORS Management**: Strictly configured cross-origin resource sharing for banking security standards.
- ✅ **Interface-Driven Services**: Decoupled backend business logic promoting testability.

---

## 5. TECHNOLOGY STACK

| Category | Technologies / Frameworks | Badges |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5.x, Spring Security 6.x | <img src="https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white"> |
| **Frontend** | React 18, Vite, Tailwind CSS 3.x, React Router 6.x | <img src="https://img.shields.io/badge/React_18-20232A?style=flat&logo=react&logoColor=61DAFB"> <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=flat&logo=tailwind-css&logoColor=white"> |
| **Database** | MySQL 8.0, Spring Data JPA, Hibernate | <img src="https://img.shields.io/badge/MySQL_8.0-4479A1?style=flat&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white"> |
| **Security** | JSON Web Tokens (jjwt), BCrypt | <img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON%20web%20tokens&logoColor=white"> <img src="https://img.shields.io/badge/Security-Spring-green?style=flat"> |
| **Tools** | Maven, Lombok, Postman, Git | <img src="https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white"> |

---

## 6. SYSTEM ARCHITECTURE

The application utilizes a classic 3-Tier Enterprise Architecture, strictly separating concerns between the client presentation, business logic processing, and data persistence.

```text
+-----------------------------------------------------------------------------------+
|                              CLIENT TIER (Frontend)                               |
|   +----------------+   +-----------------+   +----------------+                   |
|   | React Router   |   |   React Icons   |   | Tailwind CSS   |                   |
|   | Context API    |   |     (UI)        |   | (Styling)      |                   |
|   +-------+--------+   +--------+--------+   +--------+-------+                   |
|           |                     |                     |                           |
|           +---------------------+---------------------+                           |
|                                 | Axios / Fetch                                   |
+---------------------------------|-------------------------------------------------+
                                  v REST / JSON (JWT in Authorization Header)
+---------------------------------|-------------------------------------------------+
|                            APPLICATION TIER (Backend)                             |
|   +-----------------------------------------------------------+                   |
|   |                   Spring Security (Filters)               |                   |
|   +-----------------------------------------------------------+                   |
|   +----------------+   +-----------------+   +----------------+                   |
|   | Controllers    |-->| Service Impl    |-->| Data JPA Repos |                   |
|   | (REST APIs)    |<--| (Business Logic)|<--| (Hibernate)    |                   |
|   +----------------+   +-----------------+   +----------------+                   |
|           |                     |                     |                           |
|           +---------------------+---------------------+                           |
|                                 | DTO Conversions                                 |
+---------------------------------|-------------------------------------------------+
                                  v JDBC / TCP Driver
+---------------------------------|-------------------------------------------------+
|                             DATA TIER (Database)                                  |
|   +-----------------------------------------------------------+                   |
|   |                                                           |                   |
|   |                    MySQL 8.0 Relational DB                |                   |
|   |    (Entities: Admin, Banker, Recommendation, AuditLog)    |                   |
|   |                                                           |                   |
|   +-----------------------------------------------------------+                   |
+-----------------------------------------------------------------------------------+