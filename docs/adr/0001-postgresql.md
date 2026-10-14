# ADR 001: PostgreSQL als database

- Status: Geaccepteerd
- Datum: 2026-10-15

## Context
frankys-api vervangt Knipklok voor een kapperszaak. De app bewaart vestigingen,
kappers, diensten, klanten en afspraken, en die verwijzen naar elkaar: een afspraak
hoort bij een klant, een kapper, een dienst en een vestiging.

Twee klanten mogen nooit dezelfde kapper op hetzelfde moment boeken, ook niet als
ze precies tegelijk op "boeken" drukken. Een controle in Java vangt dat niet altijd
af: twee verzoeken kunnen allebei een vrij tijdslot zien voordat een van beide is
opgeslagen. De database zelf moet een overlappende afspraak weigeren.

De app gaat later naar Azure. Daar wil ik een beheerde database met automatische
backups, omdat ik de enige ontwikkelaar ben en geen databaseserver wil onderhouden.

## Overwogen opties
- PostgreSQL
- MySQL
- MongoDB
- H2 in de tests, naast een andere database in productie

## Beslissing
Ik gebruik PostgreSQL 18: lokaal via Docker Compose, in tests via Testcontainers en
in productie als Azure Database for PostgreSQL. Het schema beheer ik met Flyway.

Beslissend is de exclusion constraint van PostgreSQL. Daarmee weigert de
database twee afspraken van dezelfde kapper waarvan de tijden overlappen. MySQL kent
geen exclusion constraints. MongoDB slaat documenten op, terwijl deze gegevens
relationeel zijn. H2 gedraagt zich anders dan PostgreSQL, waardoor een test kan
slagen die in productie faalt.

## Gevolgen
- Dubbele boekingen voorkom ik in de database, met een exclusion constraint op het
  tijdsbereik per kapper.
- Tijden sla ik op met tijdzone (`timestamptz`); voor een tijdsbereik heeft
  PostgreSQL het type `tstzrange`.
- Tests draaien tegen dezelfde database en versie als productie.
- Lokaal draaien en testen vraagt Docker, op mijn laptop en in GitHub Actions.
- Ik gebruik functies die alleen PostgreSQL heeft, dus overstappen naar een andere
  database wordt lastiger. Dat verwacht ik niet.
- Een beheerde database in Azure kost geld per maand. Dat zet ik later
  naast het Knipklok-abonnement.
