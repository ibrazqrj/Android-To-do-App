# 📱 Android To-Do App

A simple **To-Do List application** that uses a RESTful API to display, add, and delete tasks. The app is written in **Java** and utilizes **Retrofit** for API communication as well as **RecyclerView** for displaying tasks.

---

## 🚀 Current Status

- **Android frontend** with a fully functional to-do list, including a **Recycle Bin feature** to manage completed tasks.
- **Backend is already implemented** and handles CRUD operations for tasks via a RESTful API.
- **Different views for active and completed tasks**:
  - **TaskActivity**: Displays active tasks.
  - **TaskBinActivity**: Displays completed tasks, which can be restored or permanently deleted.
- **Infinite Scroll Pagination**: Dynamically loads **10 new tasks** when scrolling.
- **Microsoft Authentication Library (MSAL)** is used for authentication.
- **Transition to Blazor**: The Android frontend is being replaced with a **Blazor WebAssembly application** for better web integration.

---

## ✅ Features

- **View tasks** (retrieved from the API)
- **Add tasks** (with title, description, and due date input)
- **Delete tasks** (either from the list view or from the recycle bin)
- **Restore tasks** (move completed tasks back to the to-do list from the recycle bin)
- **Display total task count** using API headers `X-Total-Active-Count` and `X-Total-Bin-Count`
- **Pagination**: Loads 10 tasks per page dynamically
- **Pull-to-Refresh** for manual task list updates
- **Microsoft Login (MSAL)** for authentication with Azure AD

---

## 🔧 Tech Stack

### 📱 Frontend
- **Java (Android)**
- **Android SDK**
- **RecyclerView** for list rendering
- **SwipeRefreshLayout** for pull-to-refresh
- **AlertDialog** for user inputs
- **SharedPreferences** for local user data storage

### 🌍 Backend
- **REST API** (existing backend handling tasks)
- **Spring Boot** with PostgreSQL database

### 🔗 Networking
- **Retrofit** for API communication
- **GsonConverter** for JSON data processing
- **Microsoft Authentication Library (MSAL)** for OAuth 2.0 login

---

## 🌐 API Endpoints

| Method | Endpoint                  | Description                              |
|--------|---------------------------|------------------------------------------|
| GET    | `/api/todo`                | Retrieve all tasks                      |
| GET    | `/api/todo?completed=true` | Retrieve only completed tasks (Recycle Bin) |
| POST   | `/api/todo`                | Add a new task                          |
| PATCH  | `/api/todo/{id}`           | Update a task                           |
| DELETE | `/api/todo/{id}`           | Delete a task                           |

---

## 📸 Screenshots

### 📋 **Start**
![Startscreen](https://i.imgur.com/CvlQQSi.png)

### 🔑 **MSAL Login**
![Startscreen](https://i.imgur.com/JoRbpta.png)

### 🔄 **Loading Screen**
![Task List](https://i.imgur.com/QGYwH8m.png)

### 🏡 **Home**
![Task List](https://i.imgur.com/U0hCm6H.png)

### ➕ **Add Task**
![Add Task](https://i.imgur.com/N0aT7du.png)

### 🗑 **Recycle Bin**
![Recycle Bin](https://i.imgur.com/AOHOFU8.png)

---

## 🔜 Planned Improvements

### 🎯 **Transition to Blazor**
- The current Android frontend will be migrated to **Blazor WebAssembly**.
- Benefits:
  - **Cross-platform support** (web app instead of Android-only)
  - **Better maintainability** with C# and .NET
  - **Easier integration with the existing backend**
- Planned technologies for the new frontend:
  - **Blazor WebAssembly**
  - **Microsoft Authentication Library for OAuth**
  - **HttpClient for API communication**
  - **Bootstrap or MudBlazor for UI design**

---

## 📌 Installation & Usage (Current Android Version)

1. **Clone the repository**
   ```sh
   git clone https://github.com/your-repository/todoapp.git
   cd todoapp
   ```

2. Open Android Studio
   - Open the project in Android Studio.
   - Adjust the `local.properties` file and set up the API backend.
   - Start the app.

3. Select a device/emulator.
   - Build & Run the app in Android Studio.

---

## 💡 Contributors

- **Ibrahim Zeqiraj** - Development

