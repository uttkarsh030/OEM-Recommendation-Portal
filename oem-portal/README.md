
<div align="center">

# 🏦 OEM Portal

### OEM Recommendation Management System for Banking Sector

**Streamline OEM recommendations across banking organizations**

![License](https://img.shields.io/badge/License-MIT-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![React](https://img.shields.io/badge/React-18-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Tailwind](https://img.shields.io/badge/Tailwind-3.x-cyan)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen)
![Made in India](https://img.shields.io/badge/Made%20with%20%E2%9D%A4-in%20India-orange)

OEM Portal is a centralized, role-based platform that helps banking institutions manage OEM recommendations, security patches, and system updates from submission through implementation and final verification — with a complete audit trail built for regulatory compliance.

<img src="/oem-portal/assets/login-page.png" alt="OEM Portal Login Screen" width="800"/>
<p><em>Secure, role-based login for Admins, Department Heads, Bankers, and Vendors</em></p>

</div>

---

## 📑 Table of Contents

- [✨ About the Project](#-about-the-project)
- [🎯 Key Features](#-key-features)
- [🚀 Tech Stack](#-tech-stack)
- [📊 System Architecture](#-system-architecture)
- [👥 User Roles & Permissions](#-user-roles--permissions)
- [🔄 Recommendation Workflow](#-recommendation-workflow)
- [📸 Screenshots](#-screenshots)
- [📁 Project Structure](#-project-structure)
- [⚙️ Prerequisites](#️-prerequisites)
- [🚀 Getting Started](#-getting-started)
- [🔑 Default Credentials](#-default-credentials)
- [📡 API Documentation](#-api-documentation)
- [🗄️ Database Schema](#️-database-schema)
- [🧪 Testing](#-testing)
- [🔒 Security Implementation](#-security-implementation)
- [📝 Key Learnings & Challenges](#-key-learnings--challenges)
- [🗺️ Roadmap](#️-roadmap)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [👨‍💻 Author](#-author)
- [🙏 Acknowledgments](#-acknowledgments)
- [⭐ Show Your Support](#-show-your-support)

---

## ✨ About the Project

Banking institutions work with dozens of OEM (Original Equipment Manufacturer) vendors — Oracle, Microsoft, IBM, and others — who continuously push security patches, product updates, and system recommendations. Without a centralized system, these recommendations get lost across emails, spreadsheets, and disconnected tickets, making it nearly impossible to track what's been reviewed, assigned, implemented, or verified. For a regulated industry like banking, that's a compliance risk as much as an operational one.

**OEM Portal** solves this by giving every stakeholder — vendors, department heads, bankers, and admins — a single source of truth. Every recommendation flows through a defined 8-stage lifecycle, every action is logged to an audit trail, and every role only sees what's relevant to them thanks to strict role-based access control.

<div align="center">
  <img src="/oem-portal/assets/admin-dashboard.png" alt="Admin Dashboard" width="800"/>
  <p><em>Admin dashboard — real-time visibility into departments, bankers, vendors, and the recommendation pipeline</em></p>
</div>

**Why it matters:**
- 🎯 Centralized management of all OEM recommendations in one place
- 🔐 Role-Based Access Control across 4 distinct user types
- 🔄 An automated, auditable 8-stage workflow
- 🏛️ Built with banking-grade compliance and traceability in mind
- 🏢 Department-wise segregation so teams only see their own work

---

## 🎯 Key Features

### 🔐 Security Features
- ✅ JWT-based Authentication & Authorization
- ✅ Role-Based Access Control (RBAC)
- ✅ BCrypt password hashing
- ✅ Minimal token storage — only the JWT lives in `localStorage`, never role or email directly
- ✅ Role extracted from the JWT itself (tamper-proof, not trusted from client state)
- ✅ Public routes automatically redirect already-authenticated users
- ✅ Protected routes guarded by role
- ✅ Properly configured CORS policy

### 🔄 Workflow Management
- ✅ Full 8-stage recommendation lifecycle
- ✅ Automated status transitions as work progresses
- ✅ Admin-gated banker approval system
- ✅ Auto-generated vendor passwords on self-registration
- ✅ Complete, queryable audit trail for every recommendation
- ✅ Department activation/deactivation toggle

### 🎨 UI/UX Features
- ✅ Modern, responsive design across all screen sizes
- ✅ Dedicated dashboards tailored to each role
- ✅ At-a-glance statistics cards with icons
- ✅ Modal-based forms for quick actions
- ✅ Loading states for all async operations
- ✅ Friendly empty-state messaging
- ✅ Clean, professional color palette
- ✅ Built entirely with Tailwind CSS utility classes

### 🛡️ Technical Features
- ✅ Interface + Implementation service pattern on the backend
- ✅ DTO pattern for clean data transfer
- ✅ Global exception handling
- ✅ Standardized `ApiResponse` wrapper for all API responses
- ✅ Repository pattern via Spring Data JPA
- ✅ Optimistic UI updates on the frontend
- ✅ Auto-seeder that provisions the default admin on first boot

---

## 🚀 Tech Stack

### Backend

| Category | Technology | Version |
|---|---|---|
| Language | ![Java](https://img.shields.io/badge/Java-21-orange) | 21 |
| Framework | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green) | 3.5.x |
| Security | ![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-green) | 6.x |
| ORM | Spring Data JPA / Hibernate | 6.6 |
| Database Driver | MySQL Connector | — |
| Build Tool | Maven | 3.9+ |
| Boilerplate Reduction | Lombok | — |
| Auth Tokens | jjwt | 0.12.6 |
| Password Hashing | BCrypt | — |

### Frontend

| Category | Technology | Version |
|---|---|---|
| Library | ![React](https://img.shields.io/badge/React-18-blue) | 18 |
| Build Tool | Vite | — |
| Styling | ![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.x-cyan) | 3.x |
| Routing | React Router DOM | 6.x |
| HTTP Client | Axios | — |
| Icons | React Icons (Feather set) | — |
| State Management | Context API | — |

### Database

| Category | Technology | Version |
|---|---|---|
| RDBMS | ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) | 8.0 |

### Development Tools

| Tool | Purpose |
|---|---|
| Postman | API testing |
| DBeaver / MySQL Workbench | Database inspection & management |
| VS Code / IntelliJ IDEA | Development environments |
| Git / GitHub | Version control |

---

## 📊 System Architecture

OEM Portal follows a classic 3-tier architecture, cleanly separating presentation, business logic, and data persistence:

```
┌─────────────────────┐        HTTPS/JSON        ┌──────────────────────┐        JPA/Hibernate        ┌─────────────────┐
│                      │  ─────────────────────▶  │                      │  ─────────────────────────▶ │                 │
│   Client (React)     │                            │  REST API            │                              │   Database      │
│   Vite + Tailwind     │  ◀─────────────────────  │  (Spring Boot)        │  ◀───────────────────────── │   (MySQL 8.0)   │
│   Port 5173           │        JWT Auth           │  Port 8080            │                              │                 │
└─────────────────────┘                            └──────────────────────┘                              └─────────────────┘
        │                                                     │
        │                                                     │
   Context API                                       Spring Security +
   (Auth state)                                       JWT Filter Chain
```

- **Presentation Layer (React):** Role-aware dashboards render conditionally based on the JWT's decoded role, with Axios handling all API communication and attaching the bearer token to every request.
- **Application Layer (Spring Boot):** Controllers route requests to services that enforce business rules (workflow transitions, role permissions), backed by DTOs so internal entities never leak directly to the client.
- **Data Layer (MySQL):** Ten relational entities capture the full domain — from departments and bankers to recommendations and audit logs — with foreign key constraints enforcing referential integrity.

---

## 👥 User Roles & Permissions

| Feature | Admin | Dept Head | Banker | Vendor |
|---|:---:|:---:|:---:|:---:|
| Manage departments | ✅ | ❌ | ❌ | ❌ |
| Assign department heads | ✅ | ❌ | ❌ | ❌ |
| Approve/reject bankers | ✅ | ❌ | ❌ | ❌ |
| Assign bankers to departments | ✅ | ❌ | ❌ | ❌ |
| View all vendors | ✅ | ❌ | ❌ | ❌ |
| Assign recommendations to departments | ✅ | ❌ | ❌ | ❌ |
| Final verification of recommendations | ✅ | ❌ | ❌ | ❌ |
| View full audit logs | ✅ | ❌ | ❌ | ❌ |
| Review assigned recommendations | ❌ | ✅ | ❌ | ❌ |
| Assign recommendations to bankers | ❌ | ✅ | ❌ | ❌ |
| Review completed implementations | ❌ | ✅ | ❌ | ❌ |
| Forward work for admin verification | ❌ | ✅ | ❌ | ❌ |
| View bankers in own department | ❌ | ✅ | ❌ | ❌ |
| Update recommendation status | ❌ | ❌ | ✅ | ❌ |
| View own assigned recommendations | ❌ | ❌ | ✅ | ❌ |
| Upload new recommendations | ❌ | ❌ | ❌ | ✅ |
| Edit/delete own recommendations* | ❌ | ❌ | ❌ | ✅ |
| Track submission status | ❌ | ❌ | ❌ | ✅ |

\* Vendors can only edit or delete recommendations that are still in the `UPLOADED` status.

**Registration paths differ by role:**
- **Admin** — Seeded automatically on first application startup
- **Department Head** — Created by an Admin
- **Banker** — Self-registers, then waits for Admin approval (`PENDING → ACTIVE / INACTIVE`)
- **Vendor** — Self-registers with an auto-generated password and gets immediate access

---

## 🔄 Recommendation Workflow

Every recommendation moves through eight well-defined stages, giving full traceability from submission to final sign-off:

```
 1. UPLOADED
      │  Vendor submits a new recommendation
      ▼
 2. DEPARTMENT_ASSIGNED
      │  Admin routes it to the right department
      ▼
 3. UNDER_REVIEW
      │  Department Head begins reviewing
      ▼
 4. ASSIGNED
      │  Department Head assigns it to a specific banker
      ▼
 5. IN_PROGRESS
      │  Banker starts implementation work
      ▼
 6. IMPLEMENTED
      │  Banker marks the work complete
      ▼
 7. REVIEWED
      │  Department Head approves the completed work
      ▼
 8. VERIFIED ✅
       Admin gives final sign-off — recommendation closed
```

<div align="center">
  <img src="/oem-portal/assets/recommendations-page.png" alt="Recommendations Management Page" width="800"/>
  <p><em>Recommendations page — every submission's status is tracked and actionable at every stage</em></p>
</div>

> 💡 **Note:** Every transition between stages is written to the `AuditLog` table, so there is always a complete, immutable record of who did what and when — essential for banking compliance reviews.

---

## 📸 Screenshots

<div align="center">

<img src="/oem-portal/assets/login-page.png" alt="Login Page" width="800"/>
<p><em>Login Page — role-aware sign-in with self-registration options for Bankers and Vendors</em></p>

<br/>

<img src="/oem-portal/assets/admin-dashboard.png" alt="Admin Dashboard" width="800"/>
<p><em>Admin Dashboard — pipeline overview, department stats, and quick summary widgets</em></p>

<br/>

<img src="/oem-portal/assets/recommendations-page.png" alt="Recommendations Page" width="800"/>
<p><em>Recommendations Page — role-specific views into recommendation status and next actions</em></p>

<br/>

<img src="/oem-portal/assets/user-panel.png" alt="Vendor/Banker/Dept Head Panel" width="800"/>
<p><em>Vendor Panel — vendors can track every submission and add new recommendations</em></p>

</div>

---

## 📁 Project Structure

<details>
<summary><strong>Backend structure (click to expand)</strong></summary>

```
oem-portal/
├── src/main/java/com/oem/oem_portal/
│   ├── config/
│   │   ├── SecurityConfig.java        # Spring Security configuration
│   │   ├── JwtConfig.java             # JWT secret & expiry config
│   │   ├── CorsConfig.java            # CORS policy
│   │   └── AdminSeeder.java           # Seeds default admin on startup
│   ├── security/
│   │   ├── JwtTokenProvider.java      # Token generation & validation
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtAuthenticationEntryPoint.java
│   │   └── CustomUserDetailsService.java
│   ├── enums/
│   │   ├── Role.java
│   │   ├── BankerStatus.java
│   │   └── RecommendationStatus.java
│   ├── model/                         # 10 JPA entity classes
│   ├── repository/                    # 10 Spring Data repositories
│   ├── dto/
│   │   ├── request/                   # 10 request DTOs
│   │   └── response/                  # 10 response DTOs
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── AdminService.java
│   │   ├── VendorService.java
│   │   ├── DepartmentHeadService.java
│   │   ├── BankerService.java
│   │   ├── AuditLogService.java
│   │   └── impl/                      # Service implementations
│   ├── controller/                    # 5 REST controllers
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ResourceNotFoundException.java
│       └── DuplicateResourceException.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

</details>

<details>
<summary><strong>Frontend structure (click to expand)</strong></summary>

```
oem-portal-frontend/
├── src/
│   ├── api/
│   │   └── axios.js                   # Axios instance with auth interceptor
│   ├── context/
│   │   └── AuthContext.jsx            # Global auth state
│   ├── components/
│   │   ├── common/
│   │   │   ├── Navbar.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── StatCard.jsx
│   │   │   ├── Badge.jsx
│   │   │   ├── Modal.jsx
│   │   │   ├── Table.jsx
│   │   │   └── LoadingSpinner.jsx
│   │   └── layouts/
│   │       └── DashboardLayout.jsx
│   ├── pages/
│   │   ├── auth/
│   │   │   ├── Login.jsx
│   │   │   ├── BankerRegister.jsx
│   │   │   └── VendorRegister.jsx
│   │   ├── admin/
│   │   │   ├── AdminDashboard.jsx
│   │   │   ├── Departments.jsx
│   │   │   ├── Bankers.jsx
│   │   │   ├── Vendors.jsx
│   │   │   ├── Recommendations.jsx
│   │   │   └── AuditLogs.jsx
│   │   ├── vendor/
│   │   │   ├── VendorDashboard.jsx
│   │   │   └── VendorRecommendations.jsx
│   │   ├── departmenthead/
│   │   │   ├── DHDashboard.jsx
│   │   │   ├── DHRecommendations.jsx
│   │   │   └── DHBankers.jsx
│   │   └── banker/
│   │       ├── BankerDashboard.jsx
│   │       └── BankerRecommendations.jsx
│   ├── utils/
│   │   ├── helpers.js
│   │   └── jwt.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── public/
├── index.html
├── vite.config.js
├── tailwind.config.js
├── postcss.config.js
└── package.json
```

</details>

---

## ⚙️ Prerequisites

Make sure you have the following installed before setting up the project:

- ☕ **Java 21+** — [Download](https://www.oracle.com/java/technologies/downloads/)
- 📦 **Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- 🗄️ **MySQL 8.0+** — [Download](https://dev.mysql.com/downloads/mysql/)
- 🟢 **Node.js 18+** — [Download](https://nodejs.org/)
- 📥 **npm** (bundled with Node.js) or **yarn**
- 🌐 A modern browser (Chrome, Firefox, Edge)

---

## 🚀 Getting Started

### Step 1: Clone the Repository
```bash
git clone https://github.com/uttkarsh030/oem-portal.git
cd oem-portal
```

### Step 2: Set Up the Database
```sql
CREATE DATABASE oem_portal;
```

### Step 3: Configure the Backend
Create or update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oem_portal
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.jwt.secret=357638792F423F4428472B4B6250655368566D597133743677397A2443264629
app.jwt.expiration=86400000

server.port=8080
```
> ⚠️ Replace the JWT secret and database password with your own values before deploying anywhere beyond local development.

### Step 4: Run the Backend
```bash
mvn clean install
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

### Step 5: Configure the Frontend
```bash
cd oem-portal-frontend
npm install
```

### Step 6: Run the Frontend
```bash
npm run dev
# Frontend runs on http://localhost:5173
```

### Step 7: Access the Application
Open `http://localhost:5173` in your browser and sign in with one of the [default credentials](#-default-credentials) below.

---

## 🔑 Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | `admin@oem.com` | `admin123` |

> 💡 **Bankers** and **Vendors** don't have pre-seeded accounts — register them through the **Register as Banker** / **Register as Vendor** links on the login page. Banker accounts require Admin approval before they can sign in; Vendor accounts get immediate access.

---

## 📡 API Documentation

### Authentication (`/api/auth`) — Public

| Method | Endpoint | Description |
|---|---|---|
| POST | `/login` | Authenticate and receive a JWT |
| POST | `/register/banker` | Self-register as a banker (pending approval) |
| POST | `/register/vendor` | Self-register as a vendor (instant access) |

<details>
<summary><strong>Sample request/response — POST /api/auth/login</strong></summary>

```json
// Request
{
  "email": "admin@oem.com",
  "password": "admin123"
}

// Response
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "role": "ADMIN"
  }
}
```
</details>

### Admin (`/api/admin`) — 20 endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/dashboard` | Get dashboard statistics |
| POST | `/departments` | Create a department |
| PUT | `/departments/{id}` | Update a department |
| PUT | `/departments/{id}/toggle-status` | Activate/deactivate a department |
| GET | `/departments` | List all departments |
| GET | `/departments/{id}` | Get a single department |
| POST | `/departments/assign-head` | Assign a head to a department |
| GET | `/bankers` | List all bankers |
| GET | `/bankers/pending` | List bankers awaiting approval |
| PUT | `/bankers/{id}/approve` | Approve a pending banker |
| PUT | `/bankers/{id}/reject` | Reject a pending banker |
| POST | `/bankers/assign-department` | Assign a banker to a department |
| GET | `/vendors` | List all vendors |
| GET | `/recommendations` | List all recommendations |
| GET | `/recommendations/uploaded` | List newly uploaded recommendations |
| GET | `/recommendations/reviewed` | List recommendations pending final verification |
| POST | `/recommendations/assign` | Assign a recommendation to a department |
| PUT | `/recommendations/{id}/verify` | Final verification (closes the workflow) |
| GET | `/audit-logs` | Full audit log |
| GET | `/audit-logs/recommendation/{id}` | Audit log for a specific recommendation |

### Vendor (`/api/vendor`) — 6 endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/dashboard` | Vendor dashboard statistics |
| GET | `/recommendations` | List own recommendations |
| GET | `/recommendations/{id}` | Get a specific recommendation |
| POST | `/recommendations` | Submit a new recommendation |
| PUT | `/recommendations/{id}` | Edit a recommendation (only if `UPLOADED`) |
| DELETE | `/recommendations/{id}` | Delete a recommendation (only if `UPLOADED`) |

### Department Head (`/api/department-head`) — 7 endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/dashboard` | Department Head dashboard statistics |
| GET | `/recommendations/assigned` | Recommendations assigned to the department |
| GET | `/recommendations/implemented` | Recommendations awaiting review |
| PUT | `/recommendations/{id}/review` | Begin/complete review |
| POST | `/recommendations/assign-banker` | Assign a recommendation to a banker |
| PUT | `/recommendations/{id}/review-implementation` | Approve a banker's completed work |
| GET | `/bankers` | List bankers in the department |

### Banker (`/api/banker`) — 4 endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/dashboard` | Banker dashboard statistics |
| GET | `/recommendations` | List assigned recommendations |
| GET | `/recommendations/{id}` | Get a specific recommendation |
| PUT | `/recommendations/{id}/status` | Update work status |

---

## 🗄️ Database Schema

### Entities (10 total)

| # | Entity | Description |
|---|---|---|
| 1 | `Admin` | System administrators |
| 2 | `Banker` | Banking employees (`PENDING` / `ACTIVE` / `INACTIVE`) |
| 3 | `Vendor` | OEM providers |
| 4 | `DepartmentHead` | Department managers |
| 5 | `Department` | Banking departments, with an active/inactive toggle |
| 6 | `OEM` | Original Equipment Manufacturers |
| 7 | `Product` | Products offered under an OEM |
| 8 | `Application` | Applications running under a product |
| 9 | `Recommendation` | The core entity — carries all 8 workflow statuses |
| 10 | `AuditLog` | Immutable log of every action taken |

### Relationships

| From | To | Cardinality |
|---|---|---|
| Admin | Departments | 1 : Many |
| Department | DepartmentHead | 1 : 1 |
| Department | Bankers | 1 : Many |
| OEM | Products | 1 : Many |
| Product | Applications | 1 : Many |
| Vendor | Recommendations | 1 : Many |
| Recommendation | Department | Many : 1 |
| Recommendation | Banker | 1 : 1 |

---

## 🧪 Testing

### Testing with Postman
1. Import the `/api/auth/login` request and authenticate as `admin@oem.com`.
2. Copy the returned JWT and set it as a Bearer token in Postman's Authorization tab (or as a collection-level variable).
3. Exercise the Admin endpoints to create a department, assign a head, and approve a test banker.

### Sample End-to-End Workflow
1. **Vendor** registers and submits a new recommendation → status `UPLOADED`
2. **Admin** assigns it to a department → status `DEPARTMENT_ASSIGNED`
3. **Department Head** starts reviewing → status `UNDER_REVIEW`
4. **Department Head** assigns it to a banker → status `ASSIGNED`
5. **Banker** starts work → status `IN_PROGRESS`
6. **Banker** marks it done → status `IMPLEMENTED`
7. **Department Head** reviews and approves → status `REVIEWED`
8. **Admin** gives final verification → status `VERIFIED` ✅

### What to Test Per Role
- **Admin:** department CRUD, banker approval/rejection, recommendation assignment, final verification, audit log accuracy
- **Department Head:** review flow, banker assignment, implementation review
- **Banker:** status updates, restricted visibility to only assigned work
- **Vendor:** submission, edit/delete restrictions (only while `UPLOADED`), status tracking

---

## 🔒 Security Implementation

- **JWT Authentication Flow:** On login, the backend issues a signed JWT containing the user's role and identity claims. Every subsequent request carries this token in the `Authorization: Bearer` header, validated by a custom `JwtAuthenticationFilter` before the request reaches any controller.
- **Password Hashing:** All passwords are hashed with BCrypt before storage — plaintext passwords are never persisted.
- **Token Storage Strategy:** Only the JWT itself is stored in `localStorage`. Role and identity are *not* separately cached client-side; they are decoded from the token at runtime, which prevents a user from tampering with local storage to escalate privileges.
- **Role Verification from JWT:** Every protected route re-derives the caller's role from the validated JWT server-side, rather than trusting any role value the client might send.
- **CORS Configuration:** A dedicated `CorsConfig` restricts allowed origins, methods, and headers to only what the frontend needs.
- **Protected Routes:** Both frontend (route guards) and backend (method-level security) enforce role checks, so even a direct API call from an unauthorized role is rejected server-side.

---

## 📝 Key Learnings & Challenges

Building OEM Portal surfaced a number of real engineering challenges:

- **JWT token validation** — Getting expiry handling and signature validation right, especially around edge cases like clock skew and malformed tokens, took careful testing.
- **CORS configuration** — Local development across two different ports (5173 and 8080) required a properly scoped CORS policy rather than a blanket allow-all, to avoid security gaps.
- **Role security (localStorage vs. JWT)** — An early version cached the user's role directly in `localStorage`, which is trivially editable in DevTools. Moving to deriving role strictly from the validated JWT closed that gap.
- **State management** — Coordinating auth state across route guards, the navbar, and role-specific dashboards pushed the Context API setup to be more deliberate than initially planned.
- **Workflow status transitions** — Enforcing that a recommendation can only move forward through valid stage transitions (and not skip or reverse illegally) required centralizing that logic in the service layer rather than trusting the frontend.
- **Foreign key constraints** — Getting the entity relationships right (especially the 1:1 Department ↔ DepartmentHead constraint) required a few schema iterations before settling on the final design.

---

## 🗺️ Roadmap

- [ ] Email notifications for status changes
- [ ] SLA tracking and deadline alerts
- [ ] Advanced analytics and reporting dashboard
- [ ] Mobile app
- [ ] PDF/Excel export for recommendations and audit logs
- [ ] Real-time notifications (WebSocket-based)
- [ ] Two-factor authentication
- [ ] File upload support for supporting documents

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. **Fork** this repository
2. **Create a branch** for your feature: `git checkout -b feature/your-feature-name`
3. **Commit** your changes: `git commit -m "Add: your feature description"`
4. **Push** to your branch: `git push origin feature/your-feature-name`
5. **Open a Pull Request** describing your changes

Please keep PRs focused and include a clear description of what changed and why.

---

## 📄 License

![License](https://img.shields.io/badge/License-MIT-yellow)

This project is licensed under the MIT License — see the `LICENSE` file for details.

---

## 👨‍💻 Author

**Uttkarsh Kumar**

- GitHub: [github.com/yourusername](https://github.com/)
- LinkedIn: [linkedin.com/in/yourprofile](https://linkedin.com/)
- Email: your.email@example.com
- Portfolio: [yourportfolio.com](https://example.com)

---

## 🙏 Acknowledgments

- The **Spring Boot** community for excellent documentation and support
- The **React** community for a robust frontend ecosystem
- The **Tailwind CSS** team for making rapid, consistent UI development possible
- The banking domain itself, for the real-world workflow inspiration behind this project

---

## ⭐ Show Your Support

If you found this project useful or interesting, consider giving it a **star** ⭐ on GitHub — and feel free to share it with others who might find it helpful!

</div>
