# Versicherungsprämienrechner - Backend

## Übersicht

Anwendung zur Berechnung von Versicherungsprämien auf Basis von Benutzereingaben:
- Geschätzte jährliche Kilometerleistung
- Fahrzeugtyp (KI-gestützte Klassifizierung)
- Postleitzahl der Zulassungsstelle

Zudem stellt die Anwendung eine HTTP-API für Drittanbieter bereit und speichert sämtliche Anfragen in einer Datenbank (Microsoft SQL Server).

## Setup

1. Microsoft SQL Server einrichten (Datenbanksicherung InsuranceDB.bak in /src/main/resources/data wiederherstellen)
   - InsuranceDB muss User SA - Passwort123! haben
2. Backend starten:
   ```bash
   ./gradlew bootRun
   ```
   Oder IntelliJ Projekt laden und ausführen.
3. Bei neuer Datenbank wird die CSV in die Tabelle `locations` geladen und die `region` Tabelle erstellt. Dies kann etwas dauern.

## Technologie-Stack

- **Java** (Amazon Corretto)
- **Spring Boot**
- **Gradle**
- **Microsoft SQL Server**
- **Lombok** (Annotations für automatische Generierung von Code)
- **OpenAI API** Integration für KI-gestützte Fahrzeugklassenermittlung

## Architektur

Kommunikation zwischen Frontend und Backend mittels REST-API.

### Backend-Services

- **PremiumCalculationService**: Stellt die Berechnungslogik bereit
- **KilometersService**: Liefert den Kilometer-Faktor anhand der geschätzten jährlichen Kilometerleistung
- **RegionService**: Liest den Region-Faktor aus der Datenbank anhand einer Postleitzahl
- **VehicleFactorService**: Liefert den Fahrzeugtyp-Faktor anhand eines übermittelten Typs
- **VehicleClassificationService**: KI-gestützte Fahrzeugklassenermittlung
- **OpenAiService**: OpenAI API Integration für KI-Funktionen

## API-Endpunkte

### 1. Prämienberechnung

**POST** `/api/premium/calculate`

Request Body (JSON):
```json
{
  "estimatedKilometers": 12000,
  "vehicleType": "suv",
  "postcode": "10115"
}
```

Response Body (JSON):
```json
{
  "calculatedPremium": 45.50,
  "kmFactor": 1.5,
  "vehicleFactor": 1.3,
  "regionFactor": 1.1
}
```

### 2. KI-gestützte Fahrzeugklassenermittlung (NEU!)

**POST** `/api/premium/ai-classify`

Request Body (JSON):
```json
{
  "description": "groß und robust"
}
```

Response Body (JSON):
```json
{
  "suggestedVehicleType": "suv",
  "description": "groß und robust",
  "reason": "Die Beschreibung enthält Merkmale, die auf SUV hindeuten.",
  "confidence": 0.95
}
```

## Fahrzeugtypen

| Fahrzeugtyp   | Faktor | Beschreibung              |
|----------------|--------|---------------------------|
| kleinwagen     | 1.0    | Kleinwagen                |
| suv            | 1.4    | Sport Utility Vehicle     |
| sportwagen     | 1.8    | Hochperformant            |

**KI-generierte Fahrzeugklassen**:
- Die KI schlägt basierend auf der Fahrzeugbeschreibung einen Fahrzeugtyp vor
- Gültige Typen: `kleinwagen`, `suv`, `sportwagen`
- Bei fehlender OpenAI-API-Konfiguration wird eine Fallback-Logik verwendet

## Berechnungslogik

Versicherungsprämie = Kilometer-Faktor × Fahrzeugtyp-Faktor × Region-Factor

### Kilometer-Faktor
| Kilometer       | Faktor |
|-----------------|--------|
| 0 - 5.000 km    | 0.5    |
| 5.001 - 10.000 km | 1.0  |
| 10.001 - 20.000 km | 1.5  |
| ab 20.000 km   | 2.0    |

### Fahrzeugtyp-Faktor
| Fahrzeugtyp  | Faktor |
|--------------|--------|
| Kleinwagen   | 1.0    |
| SUV          | 1.5    |
| Sportwagen   | 2.0    |

### Region-Faktor
| Bundesland      | Faktor |
|-----------------|--------|
| Bayern          | 1.0    |
| Baden-Württemberg | 1.01  |
| Berlin          | 1.02  |
| Hamburg         | 1.03  |
| Hessen          | 1.04  |
| Nordrhein-Westfalen | 1.05  |
| Sachsen         | 1.06  |
| Thüringen       | 1.07  |
| Brandenburg     | 1.08  |
| Mecklenburg-Vorpommern | 1.09  |
| Sachsen-Anhalt | 1.10  |
| Niedersachsen   | 1.11  |
| Bremen          | 1.12  |
| Rheinland-Pfalz | 1.13  |
| Saarland        | 1.14  |
| Schleswig-Holstein | 1.15  |

## Datenbank

Tabelle `applicant` - PK(generierte Sequenznummer) - Speichert Nutzeranfragen (Kilometer, Fahrzeugtyp, Postleitzahl und berechnete Prämie)
Tabelle `locations` - PK(generierte Sequenznummer) - Enthält alle Daten aus postcodes.csv
Tabelle `region` - PK(Postleitzahl) - Hilfstabelle Zuordnung jeder Postleitzahl zu region_Factor (Postleitzahl, Bundesland, Stadt, Land, Region-Factor)
