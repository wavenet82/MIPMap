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
 
package it.unibas.spicygui.controllo.mapping;


import it.unibas.spicy.model.exceptions.IllegalMappingTaskException;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicygui.Costanti;
import it.unibas.spicygui.commons.LastActionBean;
import it.unibas.spicygui.commons.Modello;
import it.unibas.spicygui.controllo.Scenario;
import it.unibas.spicygui.vista.TransformationTopComponent;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.lookup.Lookups;

public class ActionGenerateTransformations extends CallableSystemAction implements Observer {

    private static Log logger = LogFactory.getLog(ActionGenerateTransformations.class);
    private ActionViewTransformation viewTransformationAction;
    private Modello modello;
    private LastActionBean lastActionBean;

    public ActionGenerateTransformations() {
        executeInjection();
        this.putValue(SHORT_DESCRIPTION, NbBundle.getMessage(Costanti.class, Costanti.ACTION_SOLVING_MAPPING_TOOLTIP));
        this.setEnabled(false);
        registraAzione();
    }

    private void enableActions() {
        lastActionBean.setLastAction(LastActionBean.SOLVE);
    }

    private void executeInjection() {
        if (this.modello == null) {
            this.modello = Lookup.getDefault().lookup(Modello.class);
        }
        if (this.viewTransformationAction == null) {
            this.viewTransformationAction = Lookups.forPath("Azione").lookup(ActionViewTransformation.class);
        }
    }

    private void openWindows() {
        Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        TransformationTopComponent transformationTopComponent = scenario.getTransformationTopComponent();
        if (transformationTopComponent == null) {
            transformationTopComponent = new TransformationTopComponent(scenario);
            scenario.setTransformationTopComponent(transformationTopComponent);
        }
        transformationTopComponent.clear();
        this.viewTransformationAction.performAction();
    }

    public void update(Observable o, Object stato) {
        if (stato.equals(LastActionBean.CLOSE) || (stato.equals(LastActionBean.NO_SCENARIO_SELECTED))) {
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }

    private void registraAzione() {
        lastActionBean = (LastActionBean) modello.getBean(Costanti.LAST_ACTION_BEAN);
        lastActionBean.addObserver(this);
    }

    @Override
    public void performAction() {
        Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        MappingTask mappingTask = scenario.getMappingTask();
        if (mappingTask.getValueCorrespondences().size() == 0){// && mappingTask.getMappingData().getSTTgds().isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(NbBundle.getMessage(Costanti.class, Costanti.EMPTY_CORRESPONDENCES), DialogDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
            return;
        }
        try {
            openWindows();
            enableActions();
        } catch (Exception ex) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(ex.getMessage(), DialogDescriptor.ERROR_MESSAGE));
        }

        Lookups.forPath("Azione").lookup(ActionViewTGDs.class).myPerformAction(scenario);
        Lookups.forPath("Azione").lookup(ActionViewTGD.class).myPerformAction(scenario);

    }

    @Override
    public String getName() {
        return NbBundle.getMessage(Costanti.class, Costanti.ACTION_SOLVING_MAPPING);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return Costanti.ICONA_GENERATE_TRANSFORMATIONS;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
