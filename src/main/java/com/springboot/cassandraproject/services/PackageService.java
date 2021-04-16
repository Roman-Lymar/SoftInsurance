package com.springboot.cassandraproject.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.cassandraproject.domains.PackageBase;
import com.springboot.cassandraproject.domains.PackageCustom;
import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.dto.PackageDto;
import com.springboot.cassandraproject.exceptions.EmptyPackageException;
import com.springboot.cassandraproject.exceptions.ErrorMessages;
import com.springboot.cassandraproject.exceptions.InvalidTokenException;
import com.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.springboot.cassandraproject.repositories.PackageCustomRepository;
import com.springboot.cassandraproject.security.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Service
public class PackageService {

    private static final Logger logger = LogManager.getLogger(PackageService.class.getSimpleName());

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtils jwtProvider;

    public PackageCustom createPackageCustom(PackageCustom packageCustom) {
        logger.info("Method createPackageCustom called");
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageCustom.getId())) {
            packageCustom.setId(uuid);
        }
        logger.warn("Package cannot be created. EMPTY_PACKAGE ");
        Set<UUID> productIds = packageCustom.getProductIds();
        if(productIds.isEmpty()){
            throw new EmptyPackageException(ErrorMessages.EMPTY_PACKAGE.getErrorMessage());
        }
        packageCustom.setPrice(productService.getProductsCostByIds(productIds));
        logger.info("Package {} created", packageCustom.getName());
        return packageCustomRepository.insert(packageCustom);
    }

    public PackageBase createPackageBase(PackageBase packageBase) {
        logger.info("Method createPackageBase called");
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageBase.getId())) {
            packageBase.setId(uuid);
        }
        Set<UUID> productIds = packageBase.getProductIds();
        packageBase.setPrice(productService.getProductsCostByIds(productIds));
        logger.info("Base package created");
        return packageBaseRepository.insert(packageBase);
    }

    public Iterable<PackageCustom> getAllCustomPackages() {
        logger.info("Method getAllCustomPackages called");
        logger.info("Custom Packages successfully returned");
        return packageCustomRepository.findAll();
    }

    public List<PackageCustom> getAllCustomPackagesBySearchString(String searchStr) {
        logger.info("Method getAllCustomPackagesByName called");
        List<PackageCustom> packageCustoms = packageCustomRepository.findAll();
        List<PackageCustom> resultList = new ArrayList<>();

        for (PackageCustom p : packageCustoms) {
            if (containsIgnoreCase(p.getName(), searchStr) || containsIgnoreCase(p.getDescription(), searchStr)) {
                resultList.add(p);
            }
        }

        if(resultList.isEmpty()){
            logger.warn("None results by filter: {} NO_RESOURCE_FOUND_BY_NAME", searchStr);
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage() + searchStr);
        }else {
            logger.info("Custom Packages successfully returned with filter: {}", searchStr);
        }
        return resultList;
    }


    public Optional<PackageCustom> getPackageCustomById(UUID id) {
        logger.info("Method getPackageCustomById called");
        Optional<PackageCustom> fetchedPack = Optional.ofNullable(packageCustomRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Custom Package successfully returned for: {}", id );
        return fetchedPack;
    }

    public Optional<PackageBase> getPackageBaseById(UUID id) {
        logger.info("Method getPackageBaseById called");
        Optional<PackageBase> fetchedPack = Optional.ofNullable(packageBaseRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Base package successfully returned for {}", id);
        return fetchedPack;
    }

    public PackageDto getInfoPackageById(UUID id, Map<String, String> headers) throws JsonProcessingException {
        logger.info("Method getInfoPackageById called");
        String authorization = headers.get("authorization");
        String userRole = jwtProvider.getUserRoleFromJwtToken(authorization.replace("Bearer ", ""));
        PackageCustom packageCustom = getPackageCustomById(id).get();
        if (!userRole.equals("admin")) {
            ResponseEntity<String> clientInfo = getClientInfo(headers);
            if(Objects.isNull(clientInfo.getBody())) {
                throw new InvalidTokenException();
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(clientInfo.getBody());
            String packageId = root.path("package").asText();
            String packageIdFromDb = packageCustom.getId().toString();
            if (StringUtils.isEmpty(packageId) || !packageId.equals(packageIdFromDb)) {
                throw new InvalidTokenException();
            }
        }


        List<Product> products = new ArrayList<>();
        for (UUID uuid : packageCustom.getProductIds()) {
            products.add(productService.getProductById(uuid).get());
        }

        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageCustom.getId());
        packageDto.setName(packageCustom.getName());
        packageDto.setDescription(packageCustom.getDescription());
        packageDto.setPrice(packageCustom.getPrice());
        packageDto.setCreatedTime(packageCustom.getCreatedTime());
        packageDto.setProductList(products);
        logger.info("Package INFO successfully returned for: {}", id );
        return packageDto;
    }



    public PackageDto getInfoBasePackageById(UUID id) {
        logger.info("Method getInfoBasePackageById called");

        PackageBase packageBase = getPackageBaseById(id).get();

        List<Product> products = new ArrayList<>();
        for (UUID uuid : packageBase.getProductIds()) {
            products.add(productService.getProductById(uuid).get());
        }

        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageBase.getId());
        packageDto.setName(packageBase.getName());
        packageDto.setDescription(packageBase.getDescription());
        packageDto.setPrice(packageBase.getPrice());
        packageDto.setCreatedTime(packageBase.getCreatedTime());
        packageDto.setProductList(products);
        logger.info("Base package INFO successfully returned for: {}", id );
        return packageDto;
    }

    public void deletePackage(UUID id) {
        logger.info("Method deletePackage called");
        if(getPackageCustomById(id).isPresent()){
            packageCustomRepository.deleteById(id);
            logger.info("Package {} was successfully deleted", id);
        }
    }

    public Iterable<PackageBase> getAllBasePackages(){
        logger.info("Method getAllBasePackages called");
        logger.info("All Base packages successfully returned");
        return packageBaseRepository.findAll();
    }

    public Boolean compareUserId(String header, UUID id){
        logger.info("Method compareId called");
        String token = header.substring(7);
        return jwtProvider.getUserIdFromJwtToken(token).equals(id.toString());
    }

    private ResponseEntity<String> getClientInfo(Map<String, String> headers) {
        String authorization = headers.get("authorization");
        String id = jwtProvider.getUserIdFromJwtToken(authorization.replace("Bearer ", ""));
        HttpHeaders headersForRequest = new HttpHeaders();
        headersForRequest.set("authorization", authorization);
        HttpEntity<Object> entity = new HttpEntity<>(headersForRequest);
        String uri = "https://insurance-client-api.herokuapp.com/api/v2/clients/" + id;
        RestTemplate clientInfoById = new RestTemplate();
        return clientInfoById.exchange(uri, HttpMethod.GET, entity, String.class);

    }
}
