package configuration;

import domain.model.ClientData;
import domain.service.ClientDataService;
import infraestructure.client.excel.ExcelDataReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelDataReader excelDataReader;
    @Autowired
    private JobLauncher jobLauncher;  // Asegúrate de que esta línea esté presente y sea inyectada

    @Autowired
    private Job myJob;

    @Autowired
    private RepositoryItemWriter<ClientData> repositoryItemWriter;

    @Autowired
    private ClientDataService clienteService;


    @Bean
    public BatchConfigurer batchConfigurer(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        return new DefaultBatchConfigurer(dataSource) {
            @Override
            public void initialize() {
                // Puedes realizar configuraciones adicionales aquí según el tipo de base de datos
            }
        };
    }

    @Bean
    public ItemReader<ClientData> reader() {
        return excelDataReader;
    }

    @Bean
    public ItemWriter<ClientData> writer() {
        return items -> {
            List<ClientData> clientes = new ArrayList<>();
            for (Object item : items) {
                clientes.add((ClientData) item);
            }
            excelDataReader.saveClientes(clientes);
        };
    }

    @Bean
    public Step myStep(ItemReader<ClientData> reader, ItemWriter<ClientData> writer) {
        return stepBuilderFactory.get("myStep")
                .<ClientData, ClientData>chunk(500)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job myJob(Step myStep) {
        return jobBuilderFactory.get("myJob")
                .start(myStep)
                .build();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void perform() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(myJob, params);
    }
}