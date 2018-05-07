package tool;

import exception.EventQueueException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Desciption:
 * Author: jasonhan.
 * Creation time: 2018/5/7 4:37 PM.
 * © Copyright 2013-2018.
 */
public class ExcelHelper {

    private ExcelHelper() {}

    public static Workbook loadExcel(String nmFile) throws IOException, EventQueueException {
        String[] filePathArr = nmFile.split("[.]");
        if (filePathArr.length < 2) {
            throw new EventQueueException("", "excel's path is error");
        }
        String tailStr = filePathArr[1];
        if (tailStr.equals("xls")) {
            return loadHSSFExcel(nmFile);
        } else {
            return loadXSSFExcel(nmFile);
        }
    }

    public static HSSFWorkbook loadHSSFExcel(String nmFile) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        ArrayList<String> result = new ArrayList<String>();

        if (null != nmFile) {
            InputStream inputStream = getInputStream(nmFile);
            hssfWorkbook = new HSSFWorkbook(inputStream);
            if (null!=inputStream) {
                inputStream.close();
            }
        }

        return hssfWorkbook;
    }

    public static XSSFWorkbook loadXSSFExcel(String nmFile) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        ArrayList<String> result = new ArrayList<String>();

        if (null != nmFile) {
            InputStream inputStream = getInputStream(nmFile);
            xssfWorkbook = new XSSFWorkbook(inputStream);
            if (null!=inputStream) {
                inputStream.close();
            }
        }

        return xssfWorkbook;
    }

    public static InputStream getInputStream(String nmFile) throws FileNotFoundException {
        //return this.getClass().getClassLoader().getResourceAsStream(nmFile);
        return new FileInputStream(nmFile);
    }

    public static ArrayList<HashMap> loadValueFromExcel(Workbook workbook, int sheetId) {
        ArrayList<HashMap> result = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(sheetId);
        for (Row rowStep : sheet) {
            HashMap<String, Object> step = new HashMap();

            int column = 0;
            for (Cell cellStep : rowStep) {
                switch (cellStep.getCellType()) {
                    case HSSFCell.CELL_TYPE_BLANK:
                        step.put(String.valueOf(column++), "");
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        step.put(String.valueOf(column++), cellStep.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        step.put(String.valueOf(column++), cellStep.getErrorCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        step.put(String.valueOf(column++), cellStep.getCellFormula());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        step.put(String.valueOf(column++), cellStep.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        step.put(String.valueOf(column++), cellStep.getStringCellValue());
                        break;
                    default:
                        break;
                }
            }
            result.add(step);
        }

        return result;
    }
}
