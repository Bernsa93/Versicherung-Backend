Versicherungsprämienrechner - Backend

Anwendung dient zur Berechnung von Versicherungsprämien auf Basis von Benutzereingaben:
- Geschätzte jährliche Kilometerleistung
- Fahrzeugtyp
- Postleitzahl der Zulassungsstelle

Zudem stellt die Anwendung eine HTTP-API für Drittanbieter bereit und speichert sämtliche Anfragen
in einer Datenbank (Microsoft SQL Server).

Technologie-Stack
- Java (Amazon Corretto) 
- Spring Boot
- Gradle
- Microsoft SQL Server
- Lombok

Architektur
Kommunitkation zwischen Frontend und Backend mittels REST-Api

Backend-Services:
PremiumCalculationService - Stellt die Berechnungslogik bereit
KilometersService - Liefert den Kilometer-Factor anhand der geschätzen jährelichen Kilometerleistung
RegionService - Liest den Region-Factor aus der Datenbank anhand einer Postleitzahl
VehicleFactorService - Liefert den Fahrzeugtyp-Factor anhand eines übermittelen Typs
PremiumController - REST-Endpunkt /api/premium/calculate zur Prämienberechnung

Datenbank:
Tabelle applicant - PK(generierte Sequenznummer) - Speichert Nutzeranfragen (Kilometer, Fahrzeugtyp, Postleitzahl und berechnete Prämie)
Tabelle locations - PK(generierte Sequenznummer) - Enthält alle Daten aus postcodes.csv
Tabelle region - PK(Postleitzahl) - Hilfstabelle Zuordnung jeder Postleitzahl zu region_Factor (Postleitzahl, Bundesland, Stadt, Land, Region-Factor)

