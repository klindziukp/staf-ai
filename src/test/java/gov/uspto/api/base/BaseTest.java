package gov.uspto.api.base;

import gov.uspto.api.config.Configuration;
import gov.uspto.api.services.DataSetService;
import gov.uspto.api.services.FieldsService;
import gov.uspto.api.services.RecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

/**
 * Base test class with common setup and utilities
 */
public class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected Configuration config;
    protected DataSetService dataSetService;
    protected FieldsService fieldsService;
    protected RecordsService recordsService;

    @BeforeClass
    public void setUp() {
        log.info("Setting up base test configuration");
        config = Configuration.getInstance();
        dataSetService = new DataSetService();
        fieldsService = new FieldsService();
        recordsService = new RecordsService();
        log.info("Base test setup completed");
    }
}
