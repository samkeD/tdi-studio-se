// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.projectsetting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.repository.preference.ProjectSettingPage;

/**
 * DOC achen class global comment. Detailled comment
 */
public class JobSettingProjectSettingPage extends ProjectSettingPage {

    Button implicitBtn, statBtn;

    private Composite mComposite;

    private Composite createPageLayout(Composite parent) {
        Composite main = new Composite(parent, SWT.NULL);
        main.setLayout(new GridLayout());
        main.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        return main;
    }

    protected Composite createImplicitGroup(Composite parent) {

        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setLayout(new GridLayout(3, false));
        Composite composite = new Composite(group, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);
        group.setText(Messages.getString("JobSettingProjectSettingPage.implictLabel")); //$NON-NLS-1$

        implicitBtn = new Button(composite, SWT.CHECK);
        implicitBtn.setText(Messages.getString("JobSettingProjectSettingPage.UseProjectsetting")); //$NON-NLS-1$

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 8;
        composite.setLayout(layout);

        return group;
    }

    protected Composite createStatGroup(Composite parent) {

        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setLayout(new GridLayout(3, false));
        Composite composite = new Composite(group, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);
        group.setText(Messages.getString("JobSettingProjectSettingPage.statAndLog")); //$NON-NLS-1$

        statBtn = new Button(composite, SWT.CHECK);
        statBtn.setText(Messages.getString("JobSettingProjectSettingPage.UseProjectsetting")); //$NON-NLS-1$

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 8;
        composite.setLayout(layout);

        return group;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        mComposite = createPageLayout(parent);
        createImplicitGroup(mComposite);
        createStatGroup(mComposite);
        init();
        return mComposite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        if (mComposite != null) {
            // save the Element's parameters to EMF model
            ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
            if (implicit != null) {
                ElementParameter2ParameterType.setParameterValue(implicit.getParameters(),
                        EParameterName.IMPLICT_DEFAULT_PROJECTSETTING.getName(), Boolean.valueOf(implicitBtn.getSelection()));
            }
            StatAndLogsSettings stat = pro.getEmfProject().getStatAndLogsSettings();
            if (stat != null) {
                ElementParameter2ParameterType.setParameterValue(stat.getParameters(),
                        EParameterName.STATS_DEFAULT_PROJECTSETTING.getName(), Boolean.valueOf(statBtn.getSelection()));
            }

            // save to the memory
            ProjectSettingManager.saveProject();
        }
        return super.performOk();
    }

    private void init() {
        ImplicitContextSettings implicit = pro.getEmfProject().getImplicitContextSettings();
        if (implicit != null) {
            String v = ElementParameter2ParameterType.getParameterValue(implicit.getParameters(),
                    EParameterName.IMPLICT_DEFAULT_PROJECTSETTING.getName());
            if (v != null)
                implicitBtn.setSelection(Boolean.valueOf(v));
            else
                implicitBtn.setSelection(true);
        }
        StatAndLogsSettings stat = pro.getEmfProject().getStatAndLogsSettings();
        if (stat != null) {
            String v = ElementParameter2ParameterType.getParameterValue(stat.getParameters(),
                    EParameterName.STATS_DEFAULT_PROJECTSETTING.getName());
            if (v != null)
                statBtn.setSelection(Boolean.valueOf(v));
            else
                statBtn.setSelection(true);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }
}
