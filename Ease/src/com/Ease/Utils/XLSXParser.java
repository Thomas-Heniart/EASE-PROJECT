package com.Ease.Utils; 
 
import com.Ease.Utils.Crypto.CodeGenerator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
 
public class XLSXParser { 
 
  protected String infra_id; 
  protected String filePath; 
 
  public XLSXParser(String infra_id, String filePath) throws HttpServletException { 
    this.infra_id = infra_id; 
    this.filePath = filePath; 
  } 
 
  public void parseFile(DataBaseConnection db) throws HttpServletException { 
    File myFile = new File(filePath); 
    FileInputStream fis = null; 
    XSSFWorkbook myWorkBook = null; 
    XSSFSheet sheet; 
    try { 
      fis = new FileInputStream(myFile); 
      myWorkBook = new XSSFWorkbook(fis); 
      sheet = myWorkBook.getSheetAt(0); 
    } catch (Exception e) { 
      throw new HttpServletException(HttpStatus.InternError, e); 
    } 
    Iterator<Row> rowIterator = sheet.iterator(); 
    // Traversing over each row of XLSX file 
    int i = 0; 
    while (rowIterator.hasNext()) { 
      Row row = rowIterator.next(); 
      // For each row, iterate through each columns 
      Iterator<Cell> cellIterator = row.cellIterator(); 
      String name = ""; 
      String email = ""; 
      String groupName = ""; 
      String groupId = ""; 
      while (cellIterator.hasNext()) { 
        Cell cell = cellIterator.next(); 
        switch (cell.getCellType()) { 
        case Cell.CELL_TYPE_STRING: 
          if (i % 4 == 0 && i > 3) { 
            groupName = cell.getStringCellValue(); 
            switch (cell.getStringCellValue()) { 
            case "Administratif": 
              groupName = "Administratif"; 
              break; 
            case "Vacataire": 
              groupName = "Administratif"; 
              break; 
            default: 
              groupName = "Etudiant"; 
              break; 
            } 
            DatabaseRequest request = db.prepareRequest("SELECT id FROM groups WHERE infrastructure_id = ? AND name = ?;");
            request.setInt(this.infra_id);
            request.setString(name);
            DatabaseResult groupRs = request.get(); 
            groupRs.next(); 
            groupId = groupRs.getString(1); 
          } else if (i % 4 == 2 && i > 3) { 
            name = cell.getStringCellValue(); 
          } 
          else if (i % 4 == 3 && i > 3) { 
            email = cell.getStringCellValue(); 
            String linkCode = CodeGenerator.generateNewCode();
            DatabaseRequest request = db.prepareRequest("INSERT INTO invitations values (null, ?, ?, ?)");
            request.setString(name);
            request.setString(email);
            request.setString(linkCode);
            String invitation_id = request.set().toString();
            request = db.prepareRequest("INSERT INTO invitationsAndGroupsMap VALUES (null, ?, ?);");
            request.setInt(invitation_id);
            request.setInt(groupId);
            request.set();
          } 
          i++; 
          break; 
        default: 
        } 
      } 
    } 
  } 
}