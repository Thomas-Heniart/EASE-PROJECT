package com.Ease.Utils; 
 
import java.io.File; 
import java.io.FileInputStream; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.Iterator; 
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.xssf.usermodel.XSSFSheet; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
 
import com.Ease.Utils.Crypto.CodeGenerator; 
 
public class XLSXParser { 
 
  protected String infra_id; 
  protected String filePath; 
 
  public XLSXParser(String infra_id, String filePath) throws GeneralException { 
    this.infra_id = infra_id; 
    this.filePath = filePath; 
  } 
 
  public void parseFile(DataBaseConnection db) throws GeneralException { 
    File myFile = new File(filePath); 
    FileInputStream fis = null; 
    XSSFWorkbook myWorkBook = null; 
    XSSFSheet sheet; 
    try { 
      fis = new FileInputStream(myFile); 
      myWorkBook = new XSSFWorkbook(fis); 
      sheet = myWorkBook.getSheetAt(0); 
    } catch (Exception e) { 
      throw new GeneralException(ServletManager.Code.InternError, e); 
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
            ResultSet groupRs = db.get("SELECT id FROM groups WHERE infrastructure_id = " + infra_id 
                + " AND name = '" + groupName + "'"); 
            try { 
              groupRs.next(); 
              groupId = groupRs.getString(1); 
            } catch (SQLException e) { 
              throw new GeneralException(ServletManager.Code.InternError, e); 
            } 
          } else if (i % 4 == 2 && i > 3) { 
            name = cell.getStringCellValue(); 
          } 
          else if (i % 4 == 3 && i > 3) { 
            email = cell.getStringCellValue(); 
            String linkCode = CodeGenerator.generateNewCode(); 
            ResultSet uRs = db.get("SELECT * FROM users WHERE email = '" + email + "';"); 
            try { 
              if (!uRs.next()) { 
                String invitation_id = db.set("INSERT INTO invitations values (null, '" + name + "', '" + email + "', '" + linkCode + "')").toString(); 
                db.set("INSERT INTO invitationsAndGroupsMap VALUES (null, " + invitation_id + ", " + groupId + ");"); 
              } 
            } catch (SQLException e) { 
              throw new GeneralException(ServletManager.Code.InternError, e); 
            } 
          } 
          i++; 
          break; 
        default: 
        } 
      } 
    } 
  } 
}