# ToDoApp

Eine einfache **ToDo-Listen-App**, die eine RESTful API verwendet, um Aufgaben anzuzeigen, hinzuzufügen und zu löschen. Die App ist in **Java** geschrieben und verwendet **Retrofit** für die API-Kommunikation sowie **RecyclerView** für die Anzeige der Aufgaben.

## Features

- Aufgaben anzeigen (von der API abgerufen)
- Aufgaben hinzufügen (mit Eingabe von Titel, Beschreibung und Datum)
- Aufgaben löschen (mittels Schaltfläche in der Listenansicht)
- Anzeige der Gesamtanzahl der Aufgaben
- Saubere und moderne Benutzeroberfläche

---

## Tech-Stack

### Frontend
- **Java**
- **Android SDK**
- **RecyclerView**
- **AlertDialog** für Benutzereingaben

### Backend
- **REST API** (Anpassbar für jede API, die Aufgaben unterstützt)

### Netzwerk
- **Retrofit** für die API-Kommunikation
- **GsonConverter** für die JSON-Datenverarbeitung

---

## API-Endpunkte

| Methode | Endpunkt          | Beschreibung                           |
|---------|--------------------|---------------------------------------|
| GET     | `/api/todo`       | Alle Aufgaben abrufen                 |
| POST    | `/api/todo`       | Neue Aufgabe hinzufügen               |
| DELETE  | `/api/todo/{id}`  | Aufgabe anhand der ID löschen         |

### Loadingscreen
![Task List](https://i.imgur.com/cxRlf7g.png)

### Aufgaben hinzufügen
![Add Task](https://i.imgur.com/AmKrB5d.png)

### Aufgaben löschen
![Delete Task](https://i.imgur.com/0Plox7c.png)

## Mögliche Verbesserungen

- **Filter- und Suchfunktion:**
  - Benutzer können nach Aufgaben suchen oder filtern (z. B. nach Status).
- **Aufgaben bearbeiten:**
  - Hinzufügen einer Bearbeitungsfunktion für bestehende Aufgaben.
- **Lokale Speicherung:**
  - Offline-Modus mit SQLite oder Room für lokale Datenbankunterstützung (Wurde verwendet im Verlauf der Projekterarbeitung. Aktuell arbeitet das Backend mit der API.)

## Mitwirkende

- **Ibrahim Zeqiraj** - Entwicklung



