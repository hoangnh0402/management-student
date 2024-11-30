package com.example.demo.repo;

import com.example.demo.domain.model.ProcessFileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessFileImportRepo extends JpaRepository<ProcessFileImport, Long> {
  List<ProcessFileImport> getProcessFileImportsByStatus(Integer status);
  List<ProcessFileImport> findAllByStatusInOrderByCreateDatetimeDesc(List<Integer> statusList);
  @Query(value = "select * from process_file_import where id = :idFile", nativeQuery = true)
  ProcessFileImport getProcessFileById(Long idFile);

  @Query(value = "select * from process_file_import pfi where id_class_sbject = :classroomId and type = 3", nativeQuery = true)
  List<ProcessFileImport> getListFileByClassroomId(Long classroomId);
}
