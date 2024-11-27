package com.luch.scripts;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class FileSpreadsheetImport {

    Connection connection;
    PreparedStatement preparedStatement;
    CallableStatement callableStatement;
    String fileSpreadsheetInsertString = "\n" +
            "            INSERT INTO FILE_SPREADSHEET (\n" +
            "            \"VERSION\",\n" +
            "             CREATED_BY_ID, DATE_CREATED, LAST_UPDATED_BY_ID, LAST_UPDATED, \"FILE_HEADER_ID\", SHEET_NO, SHEET_NAME, ROW_NO, COLUMN_NO, type_attribute, \"VALUE\"\n" +
            "            ) \n" +
            "            SELECT \n" +
            "                0\n" +
            "                , ?, FH.date_created, ?, FH.date_created, ?, ?, ?, ?, ?, ?, ? \n" +
            "            from FILE_HEADER FH\n" +
            "            WHERE 1=1 \n" +
            "            AND FH.id = ? " +
            "\n";
    Long fileHeaderId;
    long createdBy;
    Timestamp today;
    int sheetNumber;
    String sheetName;
    List<Clob> clobList = new ArrayList<>();

    public FileSpreadsheetImport(Long fileHeaderId, Long userId) {
        this.fileHeaderId = fileHeaderId;
        System.out.println("this.id: " + this.fileHeaderId);
        this.createdBy = userId;
    }

    public void connectToDB(Connection connDataSource) throws Exception {
        String sqlString;
        CallableStatement callableStatement;
        this.connection = connDataSource;

        /*
        sqlString = "{? = call \"CURRENT_DATE\" }";
        callableStatement = this.connection.prepareCall(sqlString);
        callableStatement.registerOutParameter(1, Types.TIMESTAMP);
        callableStatement.execute();
        today = callableStatement.getTimestamp(1);
        */
        System.out.println("connectToDB today: " + today);

        this.connection.setAutoCommit(false);

        preparedStatement = this.connection.prepareStatement(this.fileSpreadsheetInsertString);
    }

    public void commit() throws SAXException {
        System.out.println("ExcelImport commit");
        try {
            preparedStatement.executeBatch();
            connection.commit();
            preparedStatement.close();
            preparedStatement = this.connection.prepareStatement(this.fileSpreadsheetInsertString);

            for (int i = clobList.size() - 1; i >= 0; i--) {
                if (clobList.get(i) != null) {
                    clobList.get(i).free();
                }
                clobList.remove(i);
            }

        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    public void closeStatements() throws Exception {
        try {
            System.out.println("ExcelImport closeStatements");
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void processAllSheets(File file) throws Exception {        ;
        OPCPackage pkg = OPCPackage.open(file);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        sheetNumber = 0;

        if (sheets instanceof XSSFReader.SheetIterator) {
            XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) sheets;
            while (sheetIterator.hasNext()) {
                sheetNumber++;
                System.out.println("Processing new sheet " + sheetNumber + ":\n");
                InputStream sheet = sheetIterator.next();
                sheetName = sheetIterator.getSheetName();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                this.commit();
                sheet.close();
                System.out.println("Sheet completed, no: " + sheetNumber + ", sheetName: " + sheetName + "\n");
            }
        }
        pkg.close();
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        //XMLReader parser = XMLHelper.newXMLReader();
        XMLReader parser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private class SheetHandler extends DefaultHandler {

        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }
        String cellType;
        String columnLetter;
        Integer rowNumber, rowDone = 0, counter = 0;
        Clob myClob;
        String nameParent;
        Stack<String> stack = new Stack<String>();

        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

            if (!(stack.empty())) {
                nameParent = stack.pop();
            }
            stack.push(name);

            if (name.equals("row")) {
                rowNumber = Integer.valueOf(attributes.getValue("r"));
            }

            // c => cell
            if (name.equals("c")) {
                // Print the cell reference
                columnLetter = attributes.getValue("r");
                //System.out.print(columnLetter + rowNumber + " - ");
                // Figure out if the value is an index in the SST
                cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name) throws SAXException {

            //System.out.println("ExcelImport endElement, nameParent: " + nameParent + ", name: " + name);

            counter++;
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }
            // v => contents of a cell
            // Output after we've seen the string contents
            if ((name.equals("v") || ("is".equalsIgnoreCase(nameParent) && name.equals("t"))) && lastContents != null && lastContents.trim().length() > 0) {
                try {
                    preparedStatement.setLong(1, createdBy);
                    preparedStatement.setLong(2, createdBy);
                    preparedStatement.setLong(3, fileHeaderId.longValue());
                    preparedStatement.setInt(4, sheetNumber);
                    if (sheetName == null) { preparedStatement.setNull(5, Types.VARCHAR); } else { preparedStatement.setString(5, sheetName); }
                    preparedStatement.setInt(6, rowNumber.intValue());
                    preparedStatement.setString(7, columnLetter);
                    if (cellType == null) { preparedStatement.setNull(8, Types.VARCHAR); } else { preparedStatement.setString(8, cellType); }

                    //preparedStatement.setNull(8, Types.CLOB);
                    clobList.add(connection.createClob());
                    clobList.get(clobList.size() - 1).setString( 1, lastContents);
                    /*
                    if (myClob != null) { myClob.free(); }
                    myClob = connection.createClob();
                    myClob.setString( 1, lastContents);
                     */
                    preparedStatement.setClob(9, clobList.get(clobList.size() - 1));

                    preparedStatement.setLong(10, fileHeaderId.longValue());
                    preparedStatement.addBatch();


                } catch (Exception e) {
                    throw new SAXException(e);
                }
            }
            if (rowNumber != null && rowNumber % 1000 == 0 && !rowNumber.equals(rowDone)) {
                System.out.println("commit, row: " + rowNumber);
                rowDone = rowNumber;
                commit();
            }
        }

        public void characters(char[] ch, int start, int length) {
            lastContents += new String(ch, start, length);
        }
    }

}
