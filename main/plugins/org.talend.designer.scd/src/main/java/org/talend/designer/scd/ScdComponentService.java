// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.scd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.service.IScdComponentService;

/**
 * created by wchen on Jun 15, 2017 Detailled comment
 *
 */
public class ScdComponentService implements IScdComponentService {

    public void updateOutputMetadata(INode node, IMetadataTable table) {
        IExternalNode externalNode = node.getExternalNode();
        if (externalNode instanceof ScdComponent) {
            if (!"REJECT".equals(table.getTableName())) {
                List<String> additionalColumns = new ArrayList<String>();
                IElementParameter surrogateKey = node.getElementParameter(ScdParameterConstants.SURROGATE_KEY);
                if (!StringUtils.isEmpty(String.valueOf(surrogateKey.getValue()))) {
                    additionalColumns.add(String.valueOf(surrogateKey.getValue()));
                }

                IElementParameter elementParameter = node.getElementParameter(ScdParameterConstants.L2_FIELDS_PARAM_NAME);
                List<Map<String, String>> values = (List<Map<String, String>>) elementParameter.getValue();
                List<String> type2Table = convertTableParameterValue(values);
                if (type2Table != null && !type2Table.isEmpty()) {
                    // start date
                    IElementParameter startDate = node.getElementParameter(ScdParameterConstants.L2_STARTDATE_FIELD);
                    additionalColumns.add(String.valueOf(startDate.getValue()));
                    // end date
                    IElementParameter endDate = node.getElementParameter(ScdParameterConstants.L2_ENDDATE_FIELD);
                    additionalColumns.add(String.valueOf(endDate.getValue()));
                    // version
                    IElementParameter versionCheked = node.getElementParameter(ScdParameterConstants.USE_L2_VERSION);
                    if (Boolean.valueOf(String.valueOf(versionCheked.getValue()))) {
                        IElementParameter version = node.getElementParameter(ScdParameterConstants.L2_VERSION_FIELD);
                        additionalColumns.add(String.valueOf(version.getValue()));
                    }
                    // activate
                    IElementParameter activateCheked = node.getElementParameter(ScdParameterConstants.USE_L2_ACTIVE);
                    if (Boolean.valueOf(String.valueOf(activateCheked.getValue()))) {
                        IElementParameter activate = node.getElementParameter(ScdParameterConstants.L2_ACTIVE_FIELD);
                        additionalColumns.add(String.valueOf(activate.getValue()));
                    }
                }
                if (!additionalColumns.isEmpty()) {
                    for (IMetadataColumn column : table.getListColumns()) {
                        if (additionalColumns.contains(column.getLabel())) {
                            column.setCustom(true);
                        }
                    }
                }
            }
        }

    }

    private List<String> convertTableParameterValue(List<Map<String, String>> values) {
        List<String> columns = new ArrayList<String>();
        for (Map<String, String> entry : values) {
            for (String value : entry.values()) {
                if (value != null) {
                    columns.add(value);
                }
            }
        }
        return columns;
    }

}
