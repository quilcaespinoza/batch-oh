package infraestructure.client.excel;

import domain.model.ClientData;
import domain.service.ClientDataService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelDataReader implements ItemReader<ClientData> {
    @Autowired(required = true)
     ClientDataService clienteService;

    private Iterator<Row> rowIterator;

    public ExcelDataReader() throws IOException {
        // Cargar el archivo Excel desde el recurso de clase
        ClassPathResource resource = new ClassPathResource("data/cliente.xlsx");
        InputStream inputStream = new FileInputStream(resource.getFile());
        Workbook workbook = WorkbookFactory.create(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        this.rowIterator = sheet.iterator();
    }

    @Override
    public ClientData read() throws Exception {
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            ClientData clientData = new ClientData();
            clientData.setNombre(row.getCell(0).getStringCellValue());
            clientData.setApellidos(row.getCell(1).getStringCellValue());
            clientData.setDni(row.getCell(2).getStringCellValue());
            clientData.setTelefono(row.getCell(3).getStringCellValue());
            clientData.setEmail(row.getCell(4).getStringCellValue());

            return clientData;
        } else {
            return null;
        }
    }
    public void saveClientes(List<ClientData> clientes) {
        clienteService.saveAll(clientes);
    }
}
