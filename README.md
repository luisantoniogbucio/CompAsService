# Compilador como Servicio con Telemetría

## Proyecto 02 - Compiladores
**Equipo**:
- Guzmán Bucio Luis Antonio
- Espinosa Roque Rebeca
- Morales Martínez Edgar Jesús

---

## Inicio 

### Compilar
```bash
mvn clean package
```

### Ejecutar
```bash
java -jar target/compilador-telemetria-1.0.jar 8080
```

### Pruebas
```bash
curl -X POST http://localhost:8080/api/compilar \
  -H "Content-Type: application/json" \
  -d '{"expresion": "2 + 3 * 4", "lenguaje": "ARITMETICA"}'
```

---

## Estructura General

Todos los archivos están en `src/main/java/`:

```
src/main/java/
├── Main.java                      # Punto de entrada
├── CompiladorExpresiones.java     # Compilador de expresiones
├── Token.java                     # Tokens léxicos
├── SolicitudCompilacion.java      # DTO request
├── RespuestaCompilacion.java      # DTO response
├── ResultadoMetricas.java         # DTO métricas
├── MetricasCompilacion.java       # Recolector de métricas
├── TelemetriaManager.java         # Gestor OpenTelemetry
└── ServicioCompilador.java        # API REST
```

---

## Endpoints

- **POST /api/compilar** - Compila expresión
- **GET /api/metricas** - Retorna métricas
- **GET /api/salud** - Health check

---

## Ejemplos de Uso

**Compilación exitosa**:
```bash
curl -X POST http://localhost:8080/api/compilar \
  -H "Content-Type: application/json" \
  -d '{"expresion": "(2 + 3) * 4", "lenguaje": "ARITMETICA"}'
```

**Ver métricas**:
```bash
curl http://localhost:8080/api/metricas
```

---

## Requisitos

- Java 17+
- Maven 3.6+
