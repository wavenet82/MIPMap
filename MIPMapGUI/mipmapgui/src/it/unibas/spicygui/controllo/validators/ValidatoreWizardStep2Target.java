/*
    Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
    Giansalvatore Mecca - giansalvatore.mecca@unibas.it
    Salvatore Raunich - salrau@gmail.com
    Marcello Buoncristiano - marcello.buoncristiano@yahoo.it

    This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool
    
    ++Spicy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    ++Spicy is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package it.unibas.spicygui.controllo.validators;

import it.unibas.spicygui.Costanti;
import it.unibas.spicygui.commons.Modello;
import it.unibas.spicygui.vista.wizard.pm.CSVConfigurationPM;
import it.unibas.spicygui.vista.wizard.pm.NewMappingTaskPM;
import it.unibas.spicygui.vista.wizard.pm.RelationalConfigurationPM;
import it.unibas.spicygui.vista.wizard.pm.SQLConfigurationPM;
import it.unibas.spicygui.vista.wizard.pm.XMLConfigurationPM;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class ValidatoreWizardStep2Target {

    private static Modello modello = null;

    public static boolean verifica() {
        executeInjection();

        NewMappingTaskPM newMappingTaskPM = (NewMappingTaskPM) modello.getBean(Costanti.NEW_MAPPING_TASK_PM);
        String targetElement = newMappingTaskPM.getTargetElement();
        if (targetElement.equals(NbBundle.getMessage(Costanti.class, Costanti.DATASOURCE_TYPE_RELATIONAL))) {
        // RELATIONAL    
            RelationalConfigurationPM relationalConfigurationPM = (RelationalConfigurationPM) modello.getBean(Costanti.RELATIONAL_CONFIGURATION_TARGET);
            return relationalConfigurationPM.checkFieldsAccessConfiguration();
        } 
        else if(targetElement.equals(NbBundle.getMessage(Costanti.class, Costanti.DATASOURCE_TYPE_XML))){
            //XML
            XMLConfigurationPM xmlConfigurationPM = (XMLConfigurationPM) modello.getBean(Costanti.XML_CONFIGURATION_TARGET);
            return xmlConfigurationPM.checkFieldsForTarget();   
        } 
        else if (targetElement.equals(NbBundle.getMessage(Costanti.class, Costanti.DATASOURCE_TYPE_SQL))){
            //SQL
            SQLConfigurationPM sqlConfigurationPM = (SQLConfigurationPM) modello.getBean(Costanti.SQL_CONFIGURATION_TARGET);
            return (sqlConfigurationPM.checkFileFields() && sqlConfigurationPM.checkDatabaseNameField());
        }          
        else {
            //giannisk CSV (default value)
            //also checks for null Database Name
            CSVConfigurationPM csvConfigurationPM = (CSVConfigurationPM) modello.getBean(Costanti.CSV_CONFIGURATION_TARGET);
            return (csvConfigurationPM.checkFileFields() && csvConfigurationPM.checkDatabaseNameField());
        }
    }

    private static void executeInjection() {
        if (modello == null) {
            modello = Lookup.getDefault().lookup(Modello.class);
        }
    }
}
