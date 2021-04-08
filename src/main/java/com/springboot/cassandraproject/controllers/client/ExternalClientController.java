package com.springboot.cassandraproject.controllers.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.springboot.cassandraproject.dto.clientdto.Client;
import com.springboot.cassandraproject.dto.clientdto.ClientAmountBalance;
import com.springboot.cassandraproject.dto.clientdto.ClientBalance;
import com.springboot.cassandraproject.dto.clientdto.ClientOnlyNameBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Operations available with clients")
public class ExternalClientController {

    private static final String GET_ALL_CLIENTS_URL = "https://insurance-client-api.herokuapp.com/api/v2/clients";
    //private static final String CLIENT_ID_URL = "https://insurance-client-api.herokuapp.com/api/v2/clients/{id}";
    private static final String CLIENT_ID_URL = "https://si-client.herokuapp.com/api/v2/clients/{id}";
    private static final String CLIENT_FULL_INFO_URL = "https://insurance-client-api.herokuapp.com/api/v2/clients/{id}/info";
    //private static final String CLIENT_TOPUP_BALANCE_URL = "https://insurance-client-api.herokuapp.com/api/v2/clients/{id}/topup-balance";
    private static final String CLIENT_TOPUP_BALANCE_URL = "https://si-client.herokuapp.com/api/v2/clients/{id}/topup-balance";
    private static final String REQUEST_PARAM_FILTER = "name";
    private static final String MAPPING_PATH_ID = "/{id}";
    private static final String MAPPING_PATH_INFO = "/{id}/info";
    private static final String MAPPING_PATH_TOPUP_BALANCE = "/{id}/topup-balance";
    private static final String PATH_VARIABLE_ID = "id";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary="Gets all clients or filter by name.",
            description="View a list of clients.", tags = {"Clients"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Client[]> getAllClientsOrFilterByName(
            @Parameter(description = "Filter uses substrig as a parametr and look for any matches with client field \"name\".")
            @RequestParam(name = REQUEST_PARAM_FILTER, required = false) final String clientName) {

        if(StringUtils.isEmpty(clientName)) {
            return ResponseEntity.ok(restTemplate.getForObject(GET_ALL_CLIENTS_URL, Client[].class));
        }
        else {
            UriComponentsBuilder renewURIBuilder= UriComponentsBuilder.fromHttpUrl(GET_ALL_CLIENTS_URL).queryParam("name", clientName);
            UriComponents uriComponent=renewURIBuilder.build(true);
            URI uri=uriComponent.toUri();
            return ResponseEntity.ok(restTemplate.getForObject(uri, Client[].class));
        }
    }

    @DeleteMapping(path=MAPPING_PATH_ID)
    @Operation(summary = "Deletes a client.",
            description = "Deletes an existing client.", tags = {"Clients"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Void> deleteClientById(
            @Parameter(description = "ID value for the client you need to delete.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        restTemplate.delete(CLIENT_ID_URL, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a new client.",
            description = "Creates a new client.", tags = {"Clients"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Client> createNewClient(
            @Parameter(description = "Valid client with only name body.", required = true)
            @Valid @RequestBody final ClientOnlyNameBody client) throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Client newClient = new Client();
        newClient.setName(client.getName());

        HttpEntity<Client> entity = new HttpEntity<>(newClient, headers);
        ResponseEntity<Client> responseEntity = restTemplate.postForEntity(GET_ALL_CLIENTS_URL, entity, Client.class);

        UriComponentsBuilder renewURIBuilder= UriComponentsBuilder.fromHttpUrl(CLIENT_ID_URL);
        UriComponents uriComponent=renewURIBuilder.buildAndExpand(responseEntity.getBody().getId());
        URI uri=uriComponent.toUri();

        return ResponseEntity.ok(restTemplate.getForObject(uri, Client.class));
    }

    @GetMapping(path=MAPPING_PATH_INFO)
    @Operation(summary = "Gets all info about client.",
            description = "All info about Client", tags = {"Clients"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<JsonNode> getClientFullInfoById(
            @Parameter(description = "ID value for the client you need to find.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        UriComponentsBuilder renewURIBuilder= UriComponentsBuilder.fromHttpUrl(CLIENT_FULL_INFO_URL);
        UriComponents uriComponent=renewURIBuilder.buildAndExpand(id);
        URI uri=uriComponent.toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjY2FhYjBmNy0zYmQzLTQ0MTctOGE5Zi1hMjg0M2Y0MjI5MTciLCJyb2xlIjoiY2xpZW50IiwiaWF0IjoxNjE3ODcwMjk4LCJleHAiOjE2MTc4NzM4OTh9.segLReCfFj9Q-fVil4C6az0s1QaWsu5qtnOcbCW1ayI");
        HttpEntity<Client> entity = new HttpEntity<>(headers);

        JsonNode result = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping(path = MAPPING_PATH_TOPUP_BALANCE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a client balance.",
            description = "Updates a client balance.", tags = {"Clients"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<ClientBalance> updateClientBalance(
            @Parameter(description = "Balance to update.", required = true) @Valid @RequestBody final ClientAmountBalance amount,
            @Parameter(description = "ID value for the client you need to update.", required = true) @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        UriComponentsBuilder renewURIBuilder= UriComponentsBuilder.fromHttpUrl(CLIENT_ID_URL);
        UriComponents uriComponent=renewURIBuilder.buildAndExpand(id);
        URI uri=uriComponent.toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxYzg1NGJmOS1lZGRlLTQ3ZmMtYTNmMy1jYTZlNWU4NTA3NzAiLCJyb2xlIjoiY2xpZW50IiwiaWF0IjoxNjE3ODcxOTA5LCJleHAiOjE2MTc4NzU1MDl9.SjB3_sLvDwrCNU51IqKr9B-ku8gPsMKpY_qHG9qxVQs");
        HttpEntity<Client> entity = new HttpEntity<>(headers);

        Client client = restTemplate.exchange(uri, HttpMethod.GET, entity, Client.class).getBody();
        BigDecimal newBalance = client.getBalance().add(amount.getAmount());
        client.setBalance(newBalance);
        ClientAmountBalance clientAmountBalance = new ClientAmountBalance();
        clientAmountBalance.setAmount(newBalance);

        UriComponentsBuilder renewURIBuilderBalance= UriComponentsBuilder.fromHttpUrl(CLIENT_TOPUP_BALANCE_URL);
        UriComponents uriComponentBalance=renewURIBuilderBalance.buildAndExpand(id);
        URI uriBalance=uriComponentBalance.toUri();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjY2FhYjBmNy0zYmQzLTQ0MTctOGE5Zi1hMjg0M2Y0MjI5MTciLCJyb2xlIjoiY2xpZW50IiwiaWF0IjoxNjE3ODcwMjk4LCJleHAiOjE2MTc4NzM4OTh9.segLReCfFj9Q-fVil4C6az0s1QaWsu5qtnOcbCW1ayI");
//        HttpEntity<Client> entity = new HttpEntity<>(client);

//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(1000);
//        requestFactory.setReadTimeout(1000);
        HttpEntity<ClientAmountBalance> entity2 = new HttpEntity<>(clientAmountBalance, headers);

        CloseableHttpClient httpClient
                = HttpClients.custom()
                //.setDefaultHeaders(Collections.singletonList(MediaType.APPLICATION_JSON))
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate rest = new RestTemplate(requestFactory);
        ResponseEntity<ClientBalance> responseEntity = rest.exchange(uriBalance, HttpMethod.PATCH, entity2, ClientBalance.class);
        //Client responseEntity = rest.patchForObject(uriBalance, client, Client.class);

        return responseEntity;
    }


}
