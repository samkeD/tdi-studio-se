// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.mapper.ui.dnd;

import java.util.ArrayList;

import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.ui.visualmap.table.DataMapTableView;

/**
 * Used to manage drag and drop on tables of MetadataColumns and DataMapTableEntries.
 * 
 * <br/>
 * 
 * $Id$
 * 
 */
public class DragNDrop {

    private DragSource dragSource;

    private DropTarget dropTarget;

    private Table draggableTable;

    private MapperManager mapperManager;

    protected int dropDefaultOperation = DND.DROP_LINK;

    /**
     * Set source and target listeners.
     * 
     * @param mapperManager
     * @param draggedTable
     */
    public DragNDrop(MapperManager mapperManager, Table draggedTable) {
        this(mapperManager, draggedTable, true, true);
    }

    /**
     * 
     * @param mapperManager
     * @param draggedTable
     * @param canBeSourceOfDragging
     * @param canBeTargetOfDragging
     */
    public DragNDrop(MapperManager mapperManager, Table draggedTable, boolean canBeSourceOfDragging,
            boolean canBeTargetOfDragging) {
        super();
        this.mapperManager = mapperManager;
        this.draggableTable = draggedTable;

        // DelegatingDragAdapter dragAdapter = new DelegatingDragAdapter();
        // dragAdapter.addDragSourceListener(dragSourceListener);
        //
        // DelegatingDropAdapter dropAdapter = new DelegatingDropAdapter();
        // dropAdapter.addDropTargetListener(dropTargetListener);

        // createDragSource(dragAdapter);
        // createDropTarget(dropAdapter);

        if (canBeSourceOfDragging) {
            createDragSource(dragSourceListener);
        }
        if (canBeTargetOfDragging) {
            CompleteDropTargetListener completeDropTargetListener = new CompleteDropTargetListener(mapperManager,
                    draggedTable);
            createDropTarget(completeDropTargetListener);
        }

    }

    TransferDragSourceListener dragSourceListener = new TransferDragSourceListener() {

        public void dragFinished(DragSourceEvent event) {
        }

        public void dragSetData(DragSourceEvent event) {
            // System.out.println("\n>>dragSetData");
            // System.out.println(event);
            // if (TableEntriesTransfer.getInstance().isSupportedType(event.dataType)) {
            // }
        }

        public void dragStart(DragSourceEvent event) {
            // System.out.println("\n>>dragStart");
            // System.out.println(event);
            TableItem[] items = draggableTable.getSelection();
            if (items.length == 0) {
                event.doit = false;
            } else {
                DataMapTableView dataMapTableViewSource = mapperManager.retrieveDataMapTableView(draggableTable);

                TableItem tableItemSource = draggableTable.getItem(new Point(event.x, event.y));

                if (dataMapTableViewSource != null) {
                    DraggedData draggedData = new DraggedData();

                    ArrayList<DataMapTableView> list = new ArrayList<DataMapTableView>(mapperManager.getUiManager()
                            .getVarsTablesView());
                    list.addAll(mapperManager.getUiManager().getInputsTablesView());
                    list.addAll(mapperManager.getUiManager().getOutputsTablesView());

                    for (DataMapTableView dataMapTableView : list) {
                        Table table = dataMapTableView.getTableViewerCreatorForColumns().getTable();
                        TableItem[] tableItems = table.getSelection();
                        for (int i = 0; i < tableItems.length; i++) {
                            TableItem item = tableItems[i];
                            ITableEntry dataMapTableEntry = (ITableEntry) item.getData();
                            if (dataMapTableEntry instanceof AbstractInOutTableEntry) {
                                draggedData.addEntry(dataMapTableEntry, ((AbstractInOutTableEntry) dataMapTableEntry)
                                        .getMetadataColumn(), dataMapTableView.getZone());
                            } else {
                                draggedData.addEntry(dataMapTableEntry, null, dataMapTableView.getZone());
                            }
                        }
                    }
                    draggedData.setDataMapTableViewSource(dataMapTableViewSource);
                    draggedData.setTableItemSource(tableItemSource);
                    TableEntriesTransfer.getInstance().setDraggedData(draggedData);
                    int countEntries = draggedData.getTransferableEntryList().size();
                    UIManager uiManager = mapperManager.getUiManager();
                    DraggingInfosPopup draggingInfosPopup = uiManager.getDraggingInfosPopup();
                    draggingInfosPopup.setCountDraggingEntries(countEntries);
                    draggingInfosPopup.setMapOneToOneMode(uiManager.isShiftPressed(), true);
                    draggingInfosPopup.setOverwriteMode(uiManager.isCtrlPressed());
                    draggingInfosPopup.setInsertionEntryContext(false);
                    draggingInfosPopup.setExpressionContext(false);
                    uiManager.setDragging(true);
                }

            }
        }

        public Transfer getTransfer() {
            return TableEntriesTransfer.getInstance();
        }
    };

    /**
     * 
     * DOC amaumont Comment method "createDragSource".
     * 
     * @param sourceListener
     */
    private void createDragSource(DragSourceListener sourceListener) {
        if (dragSource != null) {
            dragSource.dispose();
        }
        dragSource = new DragSource(draggableTable, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        dragSource.setTransfer(new Transfer[] { TableEntriesTransfer.getInstance() });
        dragSource.addDragListener(sourceListener);
    }

    /**
     * 
     * create DropTarget.
     */
    private void createDropTarget(DropTargetListener targetListener) {

        if (dropTarget != null) {
            dropTarget.dispose();
        }
        dropTarget = new DropTarget(draggableTable, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        dropTarget.setTransfer(new Transfer[] { TableEntriesTransfer.getInstance() });
        dropTarget.addDropListener(targetListener);
    }

}
