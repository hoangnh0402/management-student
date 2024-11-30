package com.example.demo.job;

import com.example.demo.domain.model.ProcessFileImport;
import com.example.demo.repo.ProcessFileImportRepo;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportFile {
  private final FileService fileService;
  private final ProcessFileImportRepo processFileImportRepo;

  @SneakyThrows
  @Scheduled(fixedDelay = 60*1000)
  private void importFile(){
    List<ProcessFileImport> processFileImports = processFileImportRepo.getProcessFileImportsByStatus(1);
    for (ProcessFileImport process: processFileImports){
      log.info("Begin import file {}", process.getId());
      process.setStatus(-1);
      processFileImportRepo.save(process);
      fileService.importFile(process);
    }

  }
}
