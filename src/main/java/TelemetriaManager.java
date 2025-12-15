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
 * Gestor de telemetria usando OpenTelemetry.
 * Implementa los siguientes: Traces, Metrics, Logs.
 */
public class TelemetriaManager {
    
    private static TelemetriaManager instancia;
    
    private final OpenTelemetry openTelemetry;
    private final Tracer tracer;
    private final MetricasCompilacion metricas;
    
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
    
    public static synchronized TelemetriaManager getInstance() {
        if (instancia == null) {
            instancia = new TelemetriaManager();
        }
        return instancia;
    }
    
    public Span iniciarSpan(String operacion) {
        return tracer.spanBuilder(operacion).startSpan();
    }
    
    public void finalizarSpanExitoso(Span span) {
        span.setStatus(StatusCode.OK);
        span.end();
    }
    
    public void finalizarSpanConError(Span span, Exception error) {
        span.setStatus(StatusCode.ERROR, error.getMessage());
        span.recordException(error);
        span.end();
    }
    
    public void registrarCompilacionExitosa(String lenguaje, int tokens, long latenciaMs) {
        metricas.registrarCompilacionExitosa(lenguaje, tokens, latenciaMs);
    }
    
    public void registrarCompilacionFallida(String lenguaje, long latenciaMs) {
        metricas.registrarCompilacionFallida(lenguaje, latenciaMs);
    }
    
    public MetricasCompilacion getMetricas() {
        return metricas;
    }
    
    public void log(String nivel, String mensaje) {
        String timestamp = java.time.Instant.now().toString();
        System.out.printf("[%s] %s: %s%n", timestamp, nivel, mensaje);
    }
}
