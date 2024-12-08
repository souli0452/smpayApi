package com.switchmaker.smpay.log;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.switchmaker.smpay.constant.urls.GlobalConstantUrls.CROSS_ORIGIN;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN;

@CrossOrigin(CROSS_ORIGIN)
@RestController
@RequestMapping(ROOT_API)
public class LogController {
	private static final String LOG_FILE_PATH = "src/main/resources/application.log";

	@RolesAllowed(ADMIN)
    @GetMapping("/logs")
    public Resource getLogs() throws IOException {
        Path logFilePath = Paths.get(LOG_FILE_PATH);
        Resource resource = new UrlResource(logFilePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read log file.");
        }
    }

	@RolesAllowed(ADMIN)
    @DeleteMapping("/logs/clear")
    public String clearLogs() {
        try {
            FileWriter fileWriter = new FileWriter(LOG_FILE_PATH);
            fileWriter.write(""); // Efface le contenu du fichier
            fileWriter.close();
            return "Ok";
        } catch (IOException e) {
            return "Erreur: " + e.getMessage();
        }
    }
}

