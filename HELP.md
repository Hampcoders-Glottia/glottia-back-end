--- 
## 1. Panorama rápido del Bounded Context

|Item|Resumen|
|:--|:--|
|**Nombre oficial**|`Venues & Promotions Management`|
|**Paquete Java**|`com.hampcoders.glottia.venues`|
|**Tipo DDD**|Core Domain|
|**Propósito**|Gestionar el ciclo de vida de locales (venues), sus espacios (tables) y promociones compartibles para encuentros presenciales.|
|**Regla crítica**|Un Partner puede tener **múltiples venues**, pero un Venue **solo pertenece a un Partner** (via PartnerVenueRegistry). Un Venue puede tener **múltiples promociones** y una Promotion (agregado) puede estar en **múltiples venues del mismo Partner**. Un Table solo existe en un Venue.|
|**Sub-flujos clave**|① Alta de venue + registro de mesas. ② Gestión de disponibilidad por mesa (AvailabilityCalendar). ③ Gestión de promociones compartidas entre venues (VenuePromotionRegistry).|

---

## 2. Artefactos del modelo (resumen visual)

|Agregados Raíz|Entidades|Value Objects|
|---|---|---|
|`PartnerVenueRegistry`|`VenueRegistration`|`PartnerId`|
|`Venue`|`VenueType`|`Address`|
|`TableRegistry`|`Table`|`VenueTypes` (enum)|
|`Promotion`|`AvailabilityCalendar`|`TableTypes` (enum)|
||`TableStatus`|`TableStatuses` (enum)|
||`TableType`|`VenueList` (record)|
||`VenuePromotion`|`TableList` (record)|
||`PromotionType`|`PromotionList`|
|||`PromotionTypes` (enum)|

> 🔌 Ver **sección 5** para explicación detallada de cada categoría.

---

## 3. Integraciones con otros Bounded Contexts

**Entrantes → Venues Management**

- `Profiles` → _ProfileCreated_ (crea partner)
- `Profiles` → _PartnerVenueRegistryCreated_ (habilita creación del Registro de Venues)
- `Encounters` → _TableReservationRequested_ (solicitud de reserva de mesa)
- `Encounters` → _TableReservationCancelled_ (liberación de mesa)

**Salientes ← Venues Management**

- `Encounters` → _TableReserved_ / _TableUnavailable_
- `Encounters` → _VenueDeactivated_
- `Analytics` → _VenueCreated_ / _TableRegistered_ / _VenueDeactivated_
- `Analytics` → _PromotionCreated_ / _VenuePromotionAdded_ / _PromotionActivated_ / _PromotionExpired_
- `Analytics` → _PromotionRedeemed_ / _PromotionUsageStats_

---

## 4. Relaciones internas (DDD puro)

|Relación|Símbolo|Significado|
|:--|:--|:--|
|**Composición**|`◆`|Parte **no sobrevive** sin el todo (vida ligada)|
|**Agregación**|`◇`|Parte **puede sobrevivir** sin el todo|
|**Asociación simple**|`→`|**Uso o navegación** sin control de ciclo de vida (ambos existen independientemente)|

```text
PartnerVenueRegistry ◆-- VenueList (VO)              → composición: VO muere sin su agregado
VenueList ◇-- VenueRegistration                      → agregación: Registration puede quedar histórico
VenueRegistration → Venue                            → asociación simple: ambos existen solos

Venue ◆-- TableRegistry                              → composición: registry desaparece sin venue
TableRegistry ◆-- TableList (VO)                     → composición: VO muere sin su agregado
TableList ◇-- Table                                  → agregación: tabla puede quedar huérfana
Table ◆-- AvailabilityCalendar                       → composición: slot muere sin mesa

Venue ◆-- PromotionList (VO)                         → composición: VO muere sin venue
PromotionList ◇-- VenuePromotion                     → agregación: VenuePromotion puede quedar histórico
VenuePromotion → Venue                               → asociación: referencia bidireccional
VenuePromotion → Promotion (Agregado)                → asociación: referencia a agregado compartido

Promotion → PromotionType                            → asociación simple: catálogo de tipos
Venue → VenueType                                    → asociación simple: catálogo de tipos
Table → TableType                                    → asociación simple: catálogo de tipos de mesa
Table → TableStatus                                  → asociación simple: catálogo de estados
```

> ◆ = composición (vida ligada)  
> ◇ = agregación (puede sobrevivir)  
> → = asociación simple (uso/navegación)

---

## 5. Explicación detallada de artefactos

### Agregados Raíz

|Agregado|Invariantes que protege|
|---|---|
|**PartnerVenueRegistry**|Un partner solo puede registrar venues únicos (sin duplicados lógicos). Actúa como "guardián" e inventario de venues por partner.|
|**Venue**|Datos generales del local: dirección única por venue, tipo de negocio, horarios base. Gestiona directamente sus promociones activas. Un venue pertenece a un solo partner (via PartnerVenueRegistry).|
|**TableRegistry**|Reglas de mesas: número máximo por venue, numeración única dentro del venue, generación de slots de disponibilidad. Un table solo existe en un venue (1:1 con Venue).|
|**Promotion**|Catálogo de promociones de un Partner. Protege la definición global de la promoción (nombre, descripción, tipo, valor). Puede ser reutilizada en múltiples venues del mismo partner.|

### Entidades

|Entidad|Responsabilidad|
|---|---|
|**VenueRegistration**|Hecho de vinculación entre Partner y Venue (fecha, estado).|
|**VenueType**|Catálogo de tipos de venue (CO_WORKING, RESTAURANT, CAFE, BAR). Se deja como entidad si el admin puede crearlos dinámicamente; de lo contrario, usar enum.|
|**Table**|Capacidad, tipo, estado actual; **contiene** sus calendarios. Un table pertenece a un solo venue (no puede existir el mismo table en múltiples venues). Similar a `ComponentStock` en el patrón TechnicianInventory.|
|**AvailabilityCalendar**|Slot diario (fecha + flag disponible); **cambia de estado** cuando se reserva.|
|**TableType**|Catálogo de tipos de mesa (ENCOUNTER_TABLE, GENERAL_TABLE). Se deja como entidad si es dinámico.|
|**TableStatus**|Catálogo de estados de mesa (AVAILABLE, RESERVED, UNAVAILABLE). Se deja como entidad si es dinámico.|
|**VenuePromotion**|Entidad intermedia que vincula un Venue con una Promotion (agregado). Contiene datos específicos de la asignación: fechas de vigencia locales, límite de redenciones por venue, contador de redenciones, estado activo. **Clave:** Tiene referencias a ambos agregados (Venue y Promotion).|
|**PromotionType**|Catálogo de tipos de promoción (DISCOUNT_PERCENT, FIXED_AMOUNT_DISCOUNT, etc.). Se deja como entidad si es dinámico.|

### Value Objects

|VO|Uso|
|---|---|
|**PartnerId**|Identificador cruzado de IAM/Profiles.|
|**Address**|Dirección completa del venue (calle, ciudad, estado, código postal, país).|
|**VenueList**|Devolución inmutable de lista de VenueRegistration de un partner (record embebido).|
|**TableList**|Devolución inmutable de lista de Table de un registry (record embebido).|
|**PromotionList**|Devolución inmutable de lista de VenuePromotion de un venue (VO embebido). Encapsula lógica de colección.|
|**VenueTypes**|Enum: tipos de venue (CO_WORKING, RESTAURANT, CAFE, BAR).|
|**TableTypes**|Enum: tipos de mesa (ENCOUNTER_TABLE, GENERAL_TABLE).|
|**TableStatuses**|Enum: estados de mesa (AVAILABLE, RESERVED, UNAVAILABLE).|
|**PromotionTypes**|Enum: tipo de promoción (DISCOUNT_PERCENT, FIXED_AMOUNT_DISCOUNT, FREE_ITEM, TWO_FOR_ONE, COMPLIMENTARY_DRINK).|

---

## 6. Diagrama UML (PlantUML) — Versión final

```plantuml
@startuml
!define ENTITY_COLOR #E1F5FE
!define VALUE_OBJECT_COLOR #FFF9C4
!define AGGREGATE_ROOT_COLOR #FFDBF6

skinparam class {
    BackgroundColor<<Entity>> ENTITY_COLOR
    BackgroundColor<<ValueObject>> VALUE_OBJECT_COLOR
    BackgroundColor<<Aggregate>> AGGREGATE_ROOT_COLOR
}

package "com.hampcoders.glottia.venues" {

' ==================== AGGREGATES ====================
class PartnerVenueRegistry <<Aggregate>> {
    - id: Long
    - partnerId: PartnerId
    - venueList: VenueList
    + registerVenue(Venue): VenueRegistration
    + getActiveVenues(): List<VenueRegistration>
}

class Venue <<Aggregate>> {
    - id: Long
    - name: String
    - address: Address
    - type: VenueType
    - isActive: Boolean
    - promotionList: PromotionList
    + addPromotion(Promotion, LocalDate, LocalDate, Integer): void
    + deactivatePromotion(Long, String): void
    + activatePromotion(Long): void
    + getActivePromotions(): List<VenuePromotion>
    + deactivate(): void
}

class TableRegistry <<Aggregate>> {
    - id: Long
    - venue: Venue
    - tableList: TableList
    + addTable(capacity, type): Table
    + getAvailableTables(date): List<Table>
    + reserveTable(tableId): void
    + releaseTable(tableId): void
}

class Promotion <<Aggregate>> {
    - id: Long
    - partnerId: PartnerId
    - name: String
    - description: String
    - promotionType: PromotionType
    - value: Integer
    - isActive: Boolean
    + updateDetails(String, String, Integer): void
    + deactivate(): void
    + activate(): void
}

' ==================== ENTITIES ====================
class VenueType <<Entity>> {
    - id: Long
    - name: VenueTypes
    + getStringName(): String
    + toVenueTypeFromName(): VenueType
}

class VenueRegistration <<Entity>> {
    - id: Long
    - partnerVenueRegistry: PartnerVenueRegistry
    - venue: Venue
    - registrationDate: LocalDateTime
}

class Table <<Entity>> {
    - id: Long
    - tableNumber: String
    - capacity: Integer
    - tableType: TableType
    - status: TableStatus
    + reserve(): void
    + release(): void
}

class AvailabilityCalendar <<Entity>> {
    - id: Long
    - table: Table
    - availabilityDate: LocalDate
    - isAvailable: Boolean
    + reserve(): void
    + release(): void
}

class TableStatus <<Entity>> {
    - id: Long
    - name: TableStatuses
    + getStringName(): String
    + toTableStatusFromName(): TableStatus
}

class TableType <<Entity>> {
    - id: Long
    - name: TableTypes
    + getStringName(): String
    + toTableTypeFromName(): TableType
}

class PromotionType <<Entity>> {
    - id: Long
    - name: PromotionTypes
    + getStringName(): String
    + toPromotionTypeFromName(): PromotionType
}

class VenuePromotion <<Entity>> {
    - id: Long
    - venue: Venue
    - promotion: Promotion
    - validFrom: LocalDate
    - validUntil: LocalDate
    - isActive: Boolean
    - maxRedemptions: Integer
    - currentRedemptions: Integer
    + canBeRedeemed(): Boolean
    + redeem(): void
    + deactivate(String): void
    + activate(): void
}

' ==================== VALUE OBJECTS ====================
record PartnerId <<ValueObject>> {
    partnerId: Long
}

record Address <<ValueObject>> {
    street: String
    city: String
    state: String
    zipCode: String
    country: String
}

class VenueList <<ValueObject>> {
    registrations: List<VenueRegistration>
}

class TableList <<ValueObject>> {
    tables: List<Table>
}

class PromotionList <<ValueObject>> {
    promotionItems: List<VenuePromotion>
    + addItem(Venue, Promotion, LocalDate, LocalDate, Integer): void
    + findById(Long): VenuePromotion
    + getActivePromotions(): List<VenuePromotion>
}

enum VenueTypes <<ValueObject>> {
    CO_WORKING
    RESTAURANT
    CAFE
    BAR
}

enum TableTypes <<ValueObject>> {
    ENCOUNTER_TABLE
    GENERAL_TABLE
}

enum TableStatuses <<ValueObject>> {
    AVAILABLE
    RESERVED
    UNAVAILABLE
}

enum PromotionTypes <<ValueObject>> {
    DISCOUNT_PERCENT
    FIXED_AMOUNT_DISCOUNT
    FREE_ITEM
    TWO_FOR_ONE
    COMPLIMENTARY_DRINK
}

}

' ==================== RELACIONES ====================
PartnerVenueRegistry *-- VenueList : <<embeds>>
VenueList "1" o-- "0..*" VenueRegistration
VenueRegistration --> "1" Venue : <<many-to-one>>
VenueRegistration --> "1" PartnerVenueRegistry : <<many-to-one>>

Venue "1" *-- "0..1" TableRegistry : <<composición>>
TableRegistry *-- TableList : <<embeds>>
TableList "1" o-- "0..*" Table
Table "1" *-- "0..*" AvailabilityCalendar : <<composición>>
Table --> "1" TableRegistry : <<many-to-one>>

PartnerVenueRegistry --> PartnerId
Venue --> Address
Venue --> "1" VenueType : <<type>>
Table --> "1" TableType : <<type>>
Table --> "1" TableStatus : <<status>>
VenueType --> VenueTypes
TableStatus --> TableStatuses
TableType --> TableTypes

' ==================== PROMOTION RELATIONSHIPS ====================
Promotion --> PartnerId : <<belongs to>>
Promotion --> "1" PromotionType : <<type>>
PromotionType --> PromotionTypes

Venue *-- PromotionList : <<embeds>>
PromotionList "1" o-- "0..*" VenuePromotion : <<aggregation>>
VenuePromotion --> "1" Venue : <<many-to-one>>
VenuePromotion --> "1" Promotion : <<many-to-one>>

@enduml
```

### ¿Qué muestra el diagrama? 

1. **Cuatro agregados raíz** con responsabilidades claras: `PartnerVenueRegistry`, `Venue`, `TableRegistry`, y **`Promotion`** . 
2. **Composición** solo sobre objetos sin vida propia (`VenueList`, `TableList`, `PromotionList`, `AvailabilityCalendar`). 
3. **Relación 1:1** entre `Venue` y `TableRegistry` (un venue tiene un solo registry de mesas). 
4. **Eliminado** `VenuePromotionRegistry` - ahora `Venue` gestiona directamente su `PromotionList`. 
5. **Relación Many-to-Many** entre `Venue` y `Promotion` a través de `VenuePromotion`: - `VenuePromotion` tiene referencias **@ManyToOne** tanto a `Venue` como a `Promotion` - Permite que una `Promotion` se use en múltiples venues del mismo Partner 
6. **`Promotion` como Agregado** con `PartnerId` - pertenece a un Partner específico. 
7. **`PromotionList`** como VO embebido en `Venue` - encapsula la colección de `VenuePromotion`. 

--- 

## 7. Patrón aplicado: Venue + PromotionList embebida

Similar a `TechnicianInventory` → `ComponentStock` → `Component`:

```
TableRegistry (1:1 con Venue)
    └── TableList (VO embebido)
            └── Table (Entidad)
                    └── AvailabilityCalendar (Entidad)

Venue (Agregado)
    └── PromotionList (VO embebido)
            └── VenuePromotion (Entidad intermedia - Many-to-Many)
                    └── Venue (referencia ManyToOne)
                    └── Promotion (Agregado - referencia ManyToOne)

Promotion (Agregado independiente)
    └── PartnerId (VO)
    └── PromotionType (Entidad catálogo)
```

**Ventajas:**

- ✅ Promociones realmente compartidas entre venues del mismo partner
- ✅ Cambios en `Promotion` se reflejan automáticamente en todos los venues que la usan
- ✅ Cada venue controla sus propias fechas y límites (via `VenuePromotion`)
- ✅ Historial de asignaciones (VenuePromotion no se elimina, se desactiva)
- ✅ Validación de Partner en la capa de aplicación (Command Service)
- ✅ Sigue patrón establecido (similar a TableRegistry)
- ✅ Queries eficientes con JOINs directos

**MVP:**

- ✅ CRUD de `Venue` y `PartnerVenueRegistry`
- ✅ CRUD de `Table` con disponibilidad
- ✅ CRUD de `Promotion` (agregado del Partner)
- ✅ Asignación de promociones a venues (VenuePromotion)
- ✅ Reserva/liberación de mesas vía eventos
	- ⏳ Redención de promociones (post-MVP)

---

## 8. Decisiones de diseño clave

|Decisión|Justificación|
|---|---|
|**Relación 1:1 Venue-TableRegistry**|Un venue tiene un solo inventario de mesas. Simplifica navegación.|
|**`Promotion` como Agregado (no VO)**|Tiene ciclo de vida propio, invariantes que proteger, y pertenece a un Partner. Puede ser actualizada independientemente.|
|**`VenuePromotion` como entidad intermedia**|Tabla Many-to-Many con datos adicionales (fechas, redenciones, estado). Necesita mutabilidad y auditoría.|
|**Venue gestiona directamente PromotionList**|Eliminado VenuePromotionRegistry (complejidad innecesaria). Venue maneja sus promociones directamente vía VO embebido.|
|**Lista embebida (`PromotionList`)**|Sigue patrón TechnicianInventory. Encapsula lógica de colección de VenuePromotion.|
|**Validación de Partner en Command Service**|Venue no conoce su Partner directamente (se relaciona via PartnerVenueRegistry). La validación se hace en capa de aplicación.|
|**`VenuePromotion` tiene referencias bidireccionales**|Necesario para JPA: `VenuePromotion` → `Venue` (ManyToOne) y `VenuePromotion` → `Promotion` (ManyToOne).|

---

## 9. Reglas de negocio críticas (invariantes)

|#|Regla|Implementación|
|---|---|---|
|1|Un venue no puede tener duplicados lógicos en PartnerVenueRegistry|`PartnerVenueRegistry.registerVenue()` valida unicidad|
|2|Un table solo puede existir en un venue|`TableRegistry` controla ciclo de vida|
|3|Numeración única de tables dentro del venue|`TableRegistry.addTable()` valida `tableNumber` único|
|4|Un table no puede ser reservado si ya está RESERVED|`Table.reserve()` valida status AVAILABLE|
|5|Un AvailabilityCalendar solo puede cambiar si la fecha no pasó|`AvailabilityCalendar.reserve()` valida fecha|
|6|Una promoción no puede tener `validFrom` > `validUntil`|Constructor de `VenuePromotion` valida|
|7|Una promoción no puede redimirse si está inactiva|`VenuePromotion.canBeRedeemed()` valida `isActive`|
|8|Una promoción no puede redimirse si excedió `maxRedemptions`|`VenuePromotion.redeem()` valida contador|
|9|**Una misma Promotion puede estar en múltiples venues del mismo Partner**|`Promotion` es agregado con PartnerId. Validación en Command Service.|
|10|**Una Promotion NO puede asignarse a venues de diferente Partner**|`VenueCommandService.handle(AddPromotionToVenueCommand)` valida PartnerId|
|11|Un venue desactivado cancela todos sus encounters|`Venue.deactivate()` emite `VenueDeactivatedEvent`|
|12|Una mesa reservada no puede eliminarse|`TableRegistry.removeTable()` valida status|
|13|Un venue debe tener dirección única|`Venue` valida unicidad de `Address` en creación|
|14|Una Promotion pertenece a un único Partner|Constructor de `Promotion` requiere PartnerId|

---

## 10. Commands, Queries y Events

### **Commands**

```java
// Partner & Venue
CreatePartnerVenueRegistryCommand(PartnerId)
RegisterVenueCommand(Long registryId, String name, Address, VenueType)
DeactivateVenueCommand(Long venueId)

// Table
CreateTableRegistryCommand(Long venueId)
AddTableCommand(Long registryId, String tableNumber, Integer capacity, TableType)
ReserveTableCommand(Long tableId, LocalDate date)
ReleaseTableCommand(Long tableId, LocalDate date)
RemoveTableCommand(Long tableId)

// Promotion 
CreatePromotionCommand(PartnerId, String name, String description, Long promotionTypeId, Integer value)
UpdatePromotionCommand(Long promotionId, String name, String description, Integer value)
DeactivatePromotionCommand(Long promotionId)
ActivatePromotionCommand(Long promotionId)

// VenuePromotion
AddPromotionToVenueCommand(Long venueId, Long promotionId, LocalDate validFrom, LocalDate validUntil, Integer maxRedemptions)
ActivateVenuePromotionCommand(Long venueId, Long venuePromotionId)
DeactivateVenuePromotionCommand(Long venueId, Long venuePromotionId, String reason)
UpdateVenuePromotionDatesCommand(Long venuePromotionId, LocalDate validFrom, LocalDate validUntil)
RedeemPromotionCommand(Long venuePromotionId, Long userId)
```

### **Queries**

```java
// Venue
GetVenueByIdQuery(Long venueId)
GetVenuesByPartnerQuery(PartnerId)
SearchVenuesQuery(String city, VenueType, Boolean isActive)
GetVenueDetailsQuery(Long venueId)

// Table
GetTablesByVenueQuery(Long venueId)
GetAvailableTablesQuery(Long venueId, LocalDate date, Integer minCapacity)
GetTableByIdQuery(Long tableId)
GetTableAvailabilityQuery(Long tableId, LocalDate fromDate, LocalDate toDate)

// Promotion (ACTUALIZADO)
GetPromotionsByPartnerQuery(PartnerId)
GetPromotionByIdQuery(Long promotionId)
GetActivePromotionsByPartnerQuery(PartnerId)

// VenuePromotion (ACTUALIZADO)
GetActivePromotionsByVenueQuery(Long venueId)
GetVenuePromotionByIdQuery(Long venuePromotionId)
GetVenuePromotionStatsQuery(Long venuePromotionId)
GetVenuesUsingPromotionQuery(Long promotionId)
```

### **Events**

```java
// Venue
VenueCreatedEvent(Long venueId, String name, Address)
VenueDeactivatedEvent(Long venueId, List<Long> affectedEncounterIds)
PartnerVenueRegistryCreatedEvent(Long registryId, PartnerId)

// Table
TableRegisteredEvent(Long tableId, Long venueId, Integer capacity)
TableReservedEvent(Long tableId, Long venueId, LocalDate date, Long encounterId)
TableReleasedEvent(Long tableId, Long venueId, LocalDate date)
TableUnavailableEvent(Long tableId, String reason)

// Promotion (ACTUALIZADO)
PromotionCreatedEvent(Long promotionId, PartnerId, String name)
PromotionUpdatedEvent(Long promotionId, String name, String description)
PromotionDeactivatedEvent(Long promotionId, PartnerId)

// VenuePromotion (ACTUALIZADO)
VenuePromotionAddedEvent(Long venuePromotionId, Long venueId, Long promotionId)
VenuePromotionActivatedEvent(Long venuePromotionId, Long venueId)
VenuePromotionDeactivatedEvent(Long venuePromotionId, Long venueId, String reason)
PromotionRedeemedEvent(Long venuePromotionId, Long userId, Integer discountValue)
PromotionUsageStatsEvent(Long venueId, Long promotionId, Integer totalRedemptions)
```

---

## 11. Endpoints REST

### **Partners & Venues**
```
POST   /api/v1/partners/registries          - Crear registry (PARTNER)
POST   /api/v1/venues                        - Registrar venue (PARTNER)
PUT    /api/v1/venues/{id}                   - Actualizar venue (PARTNER)
DELETE /api/v1/venues/{id}                   - Desactivar venue (PARTNER/ADMIN)
GET    /api/v1/venues/{id}                   - Obtener venue (PUBLIC)
GET    /api/v1/venues/search                 - Buscar venues (PUBLIC)
GET    /api/v1/partners/me/venues            - Mis venues (PARTNER)
```

### **Tables**
```
POST   /api/v1/venues/{id}/tables            - Agregar table (PARTNER)
PUT    /api/v1/tables/{id}                   - Actualizar table (PARTNER)
DELETE /api/v1/tables/{id}                   - Eliminar table (PARTNER)
GET    /api/v1/venues/{id}/tables            - Listar tables (PUBLIC)
GET    /api/v1/tables/{id}/availability      - Ver disponibilidad (PUBLIC)
POST   /api/v1/tables/{id}/reserve           - Reservar (SYSTEM - evento)
POST   /api/v1/tables/{id}/release           - Liberar (SYSTEM - evento)
```

### **Promotions (ACTUALIZADO)**
```
POST   /api/v1/promotions                    - Crear promoción (PARTNER)
GET    /api/v1/promotions                    - Listar mis promociones (PARTNER)
GET    /api/v1/promotions/{id}               - Obtener promoción (PARTNER)
PUT    /api/v1/promotions/{id}               - Actualizar promoción (PARTNER)
DELETE /api/v1/promotions/{id}               - Desactivar promoción (PARTNER)
POST   /api/v1/promotions/{id}/activate      - Activar promoción (PARTNER)

POST   /api/v1/venues/{id}/promotions        - Asignar promoción a venue (PARTNER)
GET    /api/v1/venues/{id}/promotions        - Listar promociones del venue (PUBLIC)
PUT    /api/v1/venues/{venueId}/promotions/{id} - Actualizar asignación (PARTNER)
DELETE /api/v1/venues/{venueId}/promotions/{id} - Desactivar asignación (PARTNER)
POST   /api/v1/venues/{venueId}/promotions/{id}/activate - Activar asignación (PARTNER)
POST   /api/v1/venue-promotions/{id}/redeem - Redimir promoción (LEARNER) [post-MVP]
```

---

## 12. Flujo de eventos entre BCs

### **Creación de Venue**
```
1. Partner → Venues: RegisterVenueCommand
2. Venues → DB: INSERT venue + table_registry
3. Venues → Analytics: VenueCreatedEvent
4. Venues → Profiles: PartnerVenueRegistryCreatedEvent (si es primer venue)
```

### **Creación y asignación de Promotion (NUEVO)**
```
1. Partner → Venues: CreatePromotionCommand
2. Venues → DB: INSERT promotion (con partnerId)
3. Venues → Analytics: PromotionCreatedEvent

4. Partner → Venues: AddPromotionToVenueCommand
5. Venues → VenueCommandService: Validar que Promotion.partnerId == Venue.partnerId (via PartnerVenueRegistry)
6. Venues → Venue: addPromotion(promotion, validFrom, validUntil, maxRedemptions)
7. Venues → DB: INSERT venue_promotion
8. Venues → Analytics: VenuePromotionAddedEvent
```

### **Reserva de Table (desde Encounter)**
```
1. Encounters → Venues: TableReservationRequested (evento)
2. Venues → TableRegistry: reserveTable(tableId, date)
3. alt table disponible
     Venues → AvailabilityCalendar: reserve()
     Venues → Encounters: TableReservedEvent(tableId, venueId, date, encounterId)
     Venues → Analytics: TableReservedEvent
   else table no disponible
     Venues → Encounters: TableUnavailableEvent(tableId, reason)
```

### **Liberación de Table (cancelación de Encounter)**
```
1. Encounters → Venues: TableReservationCancelled (evento)
2. Venues → TableRegistry: releaseTable(tableId, date)
3. Venues → AvailabilityCalendar: release()
4. Venues → Encounters: TableReleasedEvent
5. Venues → Analytics: TableReleasedEvent
```

### **Desactivación de Venue**
```
1. Partner/Admin → Venues: DeactivateVenueCommand
2. Venues → DB: UPDATE venues SET is_active = false
3. Venues → Encounters: VenueDeactivatedEvent(venueId, affectedEncounterIds)
4. Venues → Analytics: VenueDeactivatedEvent
5. Encounters escucha y cancela encounters futuros en ese venue