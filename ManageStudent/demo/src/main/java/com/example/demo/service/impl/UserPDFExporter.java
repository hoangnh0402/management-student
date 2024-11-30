package com.example.demo.service.impl;

import com.example.demo.domain.dto.StudentPointInClassroomDTO;
import com.example.demo.repo.ClassroomRepo;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.StudentService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.persistence.Embedded;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import static javax.tools.JavaFileObject.Kind.HTML;

@Service
public class UserPDFExporter {
  private static final Logger log = LoggerFactory.getLogger(UserPDFExporter.class);
  @Autowired
  private CourseRepo courseRepo;
  @Autowired
  private ClassroomRepo classroomRepo;

  //export excel
  private void writeTableHeader(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.BLUE);
    cell.setPadding(5);

    FontFactoryImp factory = new FontFactoryImp();

    Font font = FontFactory.getFont("/font/vuArial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("Mã sinh viên", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Họ tên", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Khóa", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Lớp", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Điểm thường xuyên 1", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Điểm thường xuyên 2", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Điểm giữa kỳ", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("TB kiểm tra TX", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Điểm thi", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Điểm 4 tích lũy ", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Điểm 10 tích lũy ", font));
    table.addCell(cell);
  }

  private void writeTableData(PdfPTable table, List<StudentPointInClassroomDTO> listUser) {
    for (StudentPointInClassroomDTO student : listUser) {
      table.addCell(String.valueOf(student.getStudentCode()));
      table.addCell(new Phrase(student.getStudentName(), FontFactory.getFont("/font/vuArial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
      table.addCell(courseRepo.getCourseNameByStudentCode(student.getStudentCode()));
      table.addCell(classroomRepo.getNameClassByStudentCode(student.getStudentCode()));
      table.addCell(student.getRegularPointOne() != null ? String.valueOf(student.getRegularPointOne()) : "");
      table.addCell(student.getRegularPointTwo() != null ? String.valueOf(student.getRegularPointTwo()) : "");
      table.addCell(student.getMidtermPointOne() != null ? String.valueOf(student.getMidtermPointOne()) : "");
      table.addCell(student.getMediumPoint() != 0.0 ? String.valueOf(student.getMediumPoint()) : "");
      table.addCell(student.getTestPointOne() != null ? String.valueOf(student.getTestPointOne()) : "");
      table.addCell(student.getAccumulated_point() != 0.0 ? String.valueOf(student.getAccumulated_point()) : "");
      table.addCell(student.getPoint() != 0.0 ? String.valueOf(student.getPoint()) : "");
    }
  }
  public void export(HttpServletResponse response, List<StudentPointInClassroomDTO> listUser) throws DocumentException, IOException {
    Document document = new Document(PageSize.A2);
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();
    Font font = FontFactory.getFont("/font/vuArial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    font.setSize(18);
    font.setColor(Color.BLUE);

    Paragraph p = new Paragraph("Danh sách điểm : ", font);
    p.setAlignment(Paragraph.ALIGN_CENTER);

    document.add(p);

    PdfPTable table = new PdfPTable(11);
    table.setWidthPercentage(100f);
    table.setWidths(new float[] {10.0f, 10.0f, 5.0f, 5.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f});
    table.setSpacingBefore(10);

    writeTableHeader(table);
    writeTableData(table, listUser);

    document.add(table);

    document.close();

  }
}
