Application server can be configured to use HTTP2 protocol.

### Set application server to use HTTP2

```java

@Configuration
public class TomcatHttp2Config {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return (factory) -> factory.addConnectorCustomizers(connector -> {
            // Enable SSL
            connector.setSecure(true);
            connector.setScheme("https");

            // Add HTTP/2 Protocol
            Http2Protocol http2Protocol = new Http2Protocol();
            connector.addUpgradeProtocol(http2Protocol);

            // Optional: Customize other connector settings (e.g., port, threads)
            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?> protocol) {
                protocol.setMaxThreads(200);
                protocol.setPort(8443);
            }
        });
    }
}
```