/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibas.spicy.persistence.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import it.unibas.spicy.utility.SpicyEngineConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ChangeDelimiterCSV {
    
    public void changeDelimiter(File file, String oldDelimiterString, String oldQuotesString, boolean quotesOnTarget) throws FileNotFoundException, IOException{
        char oldDelimiter = mapDelimiter(oldDelimiterString);
        char oldQuotes = mapQuotes(oldQuotesString);
        CSVReader reader = new CSVReader(new FileReader(file), oldDelimiter, oldQuotes);
        String folderPath = file.getParent();
        String fileName = file.getName();
        //exclude filename extension
        if (fileName.indexOf(".") > 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        List<String[]> dataCsv = reader.readAll();
        reader.close();   
        File file2 = new File(folderPath+File.separator+fileName+"_withChangedDelimiter.csv");
        CSVWriter writer;
        if (quotesOnTarget)
            writer = new CSVWriter(new FileWriter(file2), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER);
        else
            writer = new CSVWriter(new FileWriter(file2), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER);
        writer.writeAll(dataCsv);
        writer.close();    
    }
    
    private char mapDelimiter(String oldDelimiterString){
        switch (oldDelimiterString) {
            case SpicyEngineConstants.SEMI_COLON_DELIMITER:
                return ';';
            case SpicyEngineConstants.COLON_DELIMITER:
                return ':';
                //tab
            case SpicyEngineConstants.TAB_DELIMITER:
                return '\t';
        }
        return ' ';
    }
    
    private char mapQuotes(String oldQuotesString){
        switch (oldQuotesString) {
            case SpicyEngineConstants.DOUBLE_QUOTES_OPTION:
                return '"';
            case SpicyEngineConstants.SINGLE_QUOTES_OPTION:
                return '\'';
        }
        return ' ';
    }
    
}
