package com.staf.uspto.test.retrofit;

import com.staf.uspto.api.UsptoApiService;
import com.staf.uspto.model.ApiInfo;
import com.staf.uspto.model.DataSetList;
import com.staf.uspto.test.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for USPTO Data Set API using Retrofit.
 * Tests the metadata endpoints for listing datasets and fields.
 */
@Slf4j
public class RetrofitDataSetTest extends BaseApiTest {
    
    private UsptoApiService apiService;
    
    @BeforeClass
    public void setUp() {
        log.info("Setting up Retrofit client for DataSet tests");
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        
        apiService = retrofit.create(UsptoApiService.class);
        log.info("Retrofit client configured successfully");
    }
    
    @Test(description = "Verify that the API returns a list of available datasets using Retrofit")
    public void testListDataSetsWithRetrofit() throws IOException {
        // Arrange
        logRequest("GET", BASE_URL + "/");
        
        // Act
        Response<DataSetList> response = apiService.listDataSets().execute();
        
        // Assert
        assertThat(response.isSuccessful())
                .as("Response is successful")
                .isTrue();
        
        assertThat(response.code())
                .as("HTTP Status Code")
                .isEqualTo(200);
        
        DataSetList dataSetList = response.body();
        assertThat(dataSetList)
                .as("Response body")
                .isNotNull();
        
        verificationService.verifyDataSetList(dataSetList);
        
        logResponse(response.code(), gson.toJson(dataSetList));
        log.info("Successfully retrieved {} datasets using Retrofit", dataSetList.getTotal());
    }
    
    @Test(description = "Verify that each dataset has valid API information using Retrofit")
    public void testDataSetApiInfoWithRetrofit() throws IOException {
        // Arrange
        logRequest("GET", BASE_URL + "/");
        
        // Act
        Response<DataSetList> response = apiService.listDataSets().execute();
        
        // Assert
        assertThat(response.isSuccessful()).isTrue();
        
        DataSetList dataSetList = response.body();
        assertThat(dataSetList).isNotNull();
        assertThat(dataSetList.getApis()).isNotEmpty();
        
        for (ApiInfo apiInfo : dataSetList.getApis()) {
            verificationService.verifyApiInfo(apiInfo);
            log.info("Verified API info for dataset: {} using Retrofit", apiInfo.getApiKey());
        }
    }
    
    @Test(description = "Verify that searchable fields can be retrieved using Retrofit")
    public void testGetSearchableFieldsWithRetrofit() throws IOException {
        // Arrange
        String url = buildFieldsUrl(DEFAULT_DATASET, DEFAULT_VERSION);
        logRequest("GET", url);
        
        // Act
        Response<Map<String, Object>> response = apiService.getFields(DEFAULT_DATASET, DEFAULT_VERSION).execute();
        
        // Assert
        assertThat(response.isSuccessful())
                .as("Response is successful")
                .isTrue();
        
        assertThat(response.code())
                .as("HTTP Status Code")
                .isEqualTo(200);
        
        Map<String, Object> fieldsResponse = response.body();
        assertThat(fieldsResponse)
                .as("Fields response")
                .isNotNull()
                .containsKey("fields");
        
        @SuppressWarnings("unchecked")
        List<String> fields = (List<String>) fieldsResponse.get("fields");
        verificationService.verifyFieldsList(fields);
        
        logResponse(response.code(), gson.toJson(fieldsResponse));
        log.info("Successfully retrieved {} searchable fields using Retrofit", fields.size());
    }
    
    @Test(description = "Verify that 404 is returned for non-existent dataset using Retrofit")
    public void testGetFieldsForNonExistentDatasetWithRetrofit() throws IOException {
        // Arrange
        String nonExistentDataset = "non_existent_dataset";
        String url = buildFieldsUrl(nonExistentDataset, DEFAULT_VERSION);
        logRequest("GET", url);
        
        // Act
        Response<Map<String, Object>> response = apiService.getFields(nonExistentDataset, DEFAULT_VERSION).execute();
        
        // Assert
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
        
        assertThat(response.code())
                .as("HTTP Status Code")
                .isEqualTo(404);
        
        log.info("Correctly received 404 for non-existent dataset using Retrofit");
    }
    
    @Test(description = "Verify response headers using Retrofit")
    public void testResponseHeadersWithRetrofit() throws IOException {
        // Arrange
        logRequest("GET", BASE_URL + "/");
        
        // Act
        Response<DataSetList> response = apiService.listDataSets().execute();
        
        // Assert
        assertThat(response.isSuccessful()).isTrue();
        
        String contentType = response.headers().get("Content-Type");
        assertThat(contentType)
                .as("Content-Type header")
                .isNotNull();
        
        verificationService.verifyContentTypeJson(contentType);
        log.info("Response headers verified using Retrofit. Content-Type: {}", contentType);
    }
}
