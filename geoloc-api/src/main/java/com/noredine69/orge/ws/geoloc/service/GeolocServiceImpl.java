package com.noredine69.orge.ws.geoloc.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.noredine69.orge.ws.geoloc.model.IpLocation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.MalformedURLException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

@Service("geolocServiceImpl")
@Slf4j
public class GeolocServiceImpl implements GeolocService {

    private final String ACCESS_KEY = "access_key";
    private final String FIELDS_KEY = "fields";
    private final String FIELDS_VALUE = "ip,country_code";
    private final String OUTPUT_KEY = "output";
    private final String OUTPUT_VALUE = "json";
    // ?: http://api.ipstack.com/
    @Value("${geoloc.provider.url}")
    private String providerUrl;
    // ?: 69f738f29e62fcec458543adcfad142a
    @Value("${geoloc.provider.access_key}")
    private String providerAccessKey;
    private WebTarget webTarget;
    private Client client;
    private JacksonJsonProvider jacksonJsonProvider;

    @PostConstruct
    public void init() {
        if(checkIpStackConnectionParams()) {
            this.jacksonJsonProvider = new JacksonJaxbJsonProvider();
            this.jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final ClientConfig clientConfig = new ClientConfig(this.jacksonJsonProvider);
            this.client = this.getClientBuilder().withConfig(clientConfig).build();
            this.webTarget = this.client.target(this.providerUrl);
        }
    }

    private boolean checkIpStackConnectionParams() {
        return StringUtils.isNotEmpty(providerUrl) && StringUtils.isNotEmpty(providerAccessKey) && isValidURL(providerUrl);
    }

    private boolean isValidURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }

    public ClientBuilder getClientBuilder() {
        return ClientBuilder.newBuilder();
    }

    public IpLocation geolocFromIp(final String ip) {
        IpLocation ipLocation = null;
        //@formatter:off
        final Response response = this.webTarget.path(ip).queryParam(this.ACCESS_KEY, this.providerAccessKey)
                .queryParam(this.FIELDS_KEY, this.FIELDS_VALUE)
                .queryParam(this.OUTPUT_KEY, this.OUTPUT_VALUE)
                .request(MediaType.APPLICATION_JSON).get();
        //@formatter:on
        if (response.getStatus() == HTTP_OK) {
            ipLocation = response.readEntity(IpLocation.class);
            log.debug("Receive json object from provider {}", ipLocation);
        }
        try {
            response.close();
        } catch (final Exception e) {
            log.error("Exception unserializing object from json response {}", e);
        }

        return ipLocation;
    }


}
