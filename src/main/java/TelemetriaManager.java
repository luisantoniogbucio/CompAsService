import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.semconv.ResourceAttributes;

/**
 * Gestor centralizado de telemetría usando OpenTelemetry.
 * Implementa Singleton y proporciona traces, métricas y logs para el sistema.
 * 
 * @author Guzmán Bucio Luis Antonio
 * @author Espinosa Roque Rebeca  
 * @author Morales Martínez Edgar Jesús
 */
public class TelemetriaManager {
    
    private static TelemetriaManager instancia;
    
    private final OpenTelemetry openTelemetry;
    private final Tracer tracer;
    private final MetricasCompilacion metricas;

    /**
     * Constructor privado para patrón Singleton.
     * Configura OpenTelemetry con recursos, tracer provider y shutdown hook.
     */
    private TelemetriaManager() {
        Resource recurso = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                ResourceAttributes.SERVICE_NAME, "compilador-caas",
                ResourceAttributes.SERVICE_VERSION, "1.0"
            )));
        
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(
                LoggingSpanExporter.create()))
            .setResource(recurso)
            .build();
        
        this.openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .build();
        
        this.tracer = openTelemetry.getTracer("compiladores", "1.0");
        this.metricas = new MetricasCompilacion();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            tracerProvider.close();
        }));
    }

    /**
     * Obtiene la instancia única del TelemetriaManager (Singleton).
     * Crea la instancia si no existe, thread-safe.
     * 
     * @return instancia única de TelemetriaManager
     */
    public static synchronized TelemetriaManager getInstance() {
        if (instancia == null) {
            instancia = new TelemetriaManager();
        }
        return instancia;
    }

    /**
     * Inicia un nuevo span de rastreo para una operación.
     * 
     * @param operacion nombre de la operación a rastrear
     * @return span iniciado para la operación
     */
    public Span iniciarSpan(String operacion) {
        return tracer.spanBuilder(operacion).startSpan();
    }

    /**
     * Finaliza un span marcándolo como exitoso.
     * 
     * @param span span a finalizar con estado OK
     */
    public void finalizarSpanExitoso(Span span) {
        span.setStatus(StatusCode.OK);
        span.end();
    }

    /**
     * Finaliza un span marcándolo como error y registra la excepción.
     * 
     * @param span span a finalizar con estado ERROR
     * @param error excepción que causó el error
     */
    public void finalizarSpanConError(Span span, Exception error) {
        span.setStatus(StatusCode.ERROR, error.getMessage());
        span.recordException(error);
        span.end();
    }

    /**
     * Registra una compilación exitosa en las métricas.
     * 
     * @param lenguaje lenguaje de la expresión compilada
     * @param tokens cantidad de tokens procesados
     * @param latenciaMs tiempo de compilación en milisegundos
     */
    public void registrarCompilacionExitosa(String lenguaje, int tokens, long latenciaMs) {
        metricas.registrarCompilacionExitosa(lenguaje, tokens, latenciaMs);
    }

    /**
     * Registra una compilación fallida en las métricas.
     * 
     * @param lenguaje lenguaje donde ocurrió el error
     * @param latenciaMs tiempo hasta el fallo en milisegundos
     */
    public void registrarCompilacionFallida(String lenguaje, long latenciaMs) {
        metricas.registrarCompilacionFallida(lenguaje, latenciaMs);
    }

    /**
     * Obtiene el colector de métricas.
     * 
     * @return instancia de MetricasCompilacion
     */
    public MetricasCompilacion getMetricas() {
        return metricas;
    }

    /**
     * Registra un mensaje de log con timestamp y nivel.
     * 
     * @param nivel nivel del log (INFO, ERROR, WARN, etc.)
     * @param mensaje contenido del mensaje de log
     */
    public void log(String nivel, String mensaje) {
        String timestamp = java.time.Instant.now().toString();
        System.out.printf("[%s] %s: %s%n", timestamp, nivel, mensaje);
    }
}
