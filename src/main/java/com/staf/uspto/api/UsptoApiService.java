package com.staf.uspto.api;

import com.staf.uspto.model.DataSetList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

/**
 * Retrofit service interface for USPTO Data Set API.
 * Defines all API endpoints and their request/response types.
 */
public interface UsptoApiService {
    
    /**
     * Lists all available datasets.
     *
     * @return Call with DataSetList response
     */
    @GET("/")
    Call<DataSetList> listDataSets();
    
    /**
     * Gets searchable fields for a specific dataset.
     *
     * @param dataset the dataset name
     * @param version the version
     * @return Call with fields response
     */
    @GET("/{dataset}/{version}/fields")
    Call<Map<String, Object>> getFields(
            @Path("dataset") String dataset,
            @Path("version") String version
    );
    
    /**
     * Searches records in a dataset with given criteria.
     *
     * @param dataset the dataset name
     * @param version the version
     * @param criteria the search criteria
     * @param start the starting record number
     * @param rows the number of rows to return
     * @return Call with search response
     */
    @FormUrlEncoded
    @POST("/{dataset}/{version}/records")
    Call<Map<String, Object>> searchRecords(
            @Path("dataset") String dataset,
            @Path("version") String version,
            @Field("criteria") String criteria,
            @Field("start") Integer start,
            @Field("rows") Integer rows
    );
}
