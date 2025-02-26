# Android To do App

Eine einfache **ToDo-Listen-App**, die eine RESTful API verwendet, um Aufgaben anzuzeigen, hinzuzufügen und zu löschen. Die App ist in **Java** geschrieben und verwendet **Retrofit** für die API-Kommunikation sowie **RecyclerView** für die Anzeige der Aufgaben.

## 🚀 Aktueller Stand

- **Android-Frontend** mit einer funktionierenden To-Do-Liste, einschließlich einer **Papierkorb-Funktion** (Recycling Bin), um abgeschlossene Aufgaben zu verwalten.
- **Backend ist bereits vorhanden** und verarbeitet CRUD-Operationen für Aufgaben über eine RESTful API.
- **Unterschiedliche Ansichten für aktive und erledigte Aufgaben**:
  - **TaskActivity**: Zeigt aktive Aufgaben an.
  - **TaskBinActivity**: Zeigt abgeschlossene Aufgaben, die wiederhergestellt oder endgültig gelöscht werden können.
- **Paginierung mit Infinite Scroll**: Beim Scrollen werden immer **10 neue Aufgaben** geladen.
- **Microsoft Authentication Library (MSAL)** wird für die Authentifizierung genutzt.
- **Übergang zu Blazor**: Das Android-Frontend wird durch eine **Blazor WebAssembly-Anwendung** ersetzt, um eine bessere Web-Integration zu ermöglichen.

---

## ✅ Features

- **Aufgaben anzeigen** (von der API abgerufen)
- **Aufgaben hinzufügen** (mit Eingabe von Titel, Beschreibung und Datum)
- **Aufgaben löschen** (in der Listenansicht oder aus dem Papierkorb)
- **Aufgaben wiederherstellen** (abgeschlossene Aufgaben aus dem Papierkorb zurück in die To-Do-Liste verschieben)
- **Anzeige der Gesamtanzahl der Aufgaben** mit API-Headern `X-Total-Active-Count` und `X-Total-Bin-Count`
- **Paginierung**: Dynamisches Laden von 10 Aufgaben pro Seite
- **Pull-to-Refresh** für manuelle Aktualisierung der Aufgabenliste
- **Microsoft Login (MSAL)** für Authentifizierung mit Azure AD

---

## 🔧 Tech-Stack

### 📱 Frontend
- **Java (Android)**
- **Android SDK**
- **RecyclerView** für die Listendarstellung
- **SwipeRefreshLayout** für Pull-to-Refresh
- **AlertDialog** für Benutzereingaben
- **SharedPreferences** für lokale Speicherung von Nutzerdaten

### 🌍 Backend
- **REST API** (bestehendes Backend, verarbeitet Aufgaben)
- **Spring Boot** mit PostgreSQL-Datenbank

### 🔗 Netzwerk
- **Retrofit** für API-Kommunikation
- **GsonConverter** für JSON-Datenverarbeitung
- **Microsoft Authentication Library (MSAL)** für OAuth 2.0 Login

---

## 🌐 API-Endpunkte

| Methode | Endpunkt             | Beschreibung                               |
|---------|----------------------|-------------------------------------------|
| GET     | `/api/todo`          | Alle Aufgaben abrufen                     |
| GET     | `/api/todo?completed=true` | Nur erledigte Aufgaben abrufen (Papierkorb) |
| POST    | `/api/todo`          | Neue Aufgabe hinzufügen                   |
| PATCH   | `/api/todo/{id}`     | Aufgabe aktualisieren                     |
| DELETE  | `/api/todo/{id}`     | Aufgabe löschen                           |

---


## 📸 Screenshots


### 📋**Start**
![Startscreen](https://i.imgur.com/CvlQQSi.png)

### 🔑 **MSAL Login**
![Startscreen](https://i.imgur.com/JoRbpta.png)

### 🔄 **Ladescreen**
![Task List](https://i.imgur.com/QGYwH8m.png)





### 🏡 **Home**
![Task List](https://i.imgur.com/U0hCm6H.png)

### ➕ **Aufgaben hinzufügen**
![Add Task](https://i.imgur.com/N0aT7du.png)

### 🗑 **Papierkorb**
![Recycle Bin](https://i.imgur.com/AOHOFU8.png)

---

## 🔜 Geplante Verbesserungen

### 🎯 **Übergang zu Blazor**
- Das aktuelle Android-Frontend wird auf **Blazor WebAssembly** migriert.
- Vorteile:
  - **Plattformunabhängig** (Web-App statt nur Android)
  - **Bessere Wartbarkeit** durch C# und .NET
  - **Einfache Integration in bestehendes Backend**
- Geplante Technologien für das neue Frontend:
  - **Blazor WebAssembly**
  - **Microsoft Authentication Library für OAuth**
  - **HttpClient für API-Kommunikation**
  - **Bootstrap oder MudBlazor für das UI-Design**

---

## 📌 Installation & Nutzung (aktuelle Android-Version)

1. **Projekt klonen**
   ```sh
   git clone https://github.com/dein-repository/todoapp.git
   cd todoapp
   ```
   
2. Android Studio öffnen
  - Das Projekt mit Android Studio öffnen.
  - Die local.properties Datei anpassen und das API-Backend eintragen.
  - App starten

3. Gerät/Emulator auswählen.
  - Build & Run in Android Studio ausführen.

---

## 💡 Mitwirkende

- **Ibrahim Zeqiraj** - Entwicklung
