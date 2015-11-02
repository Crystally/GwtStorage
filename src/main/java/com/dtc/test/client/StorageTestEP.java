package com.dtc.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class StorageTestEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		VerticalLayoutContainer verticalLayoutContainer=new VerticalLayoutContainer();
		FramedPanel framedPanel=new FramedPanel();
		framedPanel.setWidth(300);
		final TextField key=new TextField();
		final TextField value=new TextField();
		VerticalLayoutContainer ver=new VerticalLayoutContainer();
		ver.add(new FieldLabel(key,"key"));
		ver.add(new FieldLabel(value,"value"));
		framedPanel.add(ver);
		TextButton saveStorage=new TextButton("保存");
		TextButton getStorage=new TextButton("获取");
		framedPanel.addButton(saveStorage);
		framedPanel.addButton(getStorage);
		final Storage storage=Storage.getLocalStorageIfSupported();
		final FlexTable storageTable=new FlexTable();
		saveStorage.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				// TODO Auto-generated method stub
				if (Storage.isLocalStorageSupported()) {
					storage.setItem(key.getText(), value.getText());
				}
			}
		});
		getStorage.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				// TODO Auto-generated method stub
				if (storage!=null) {
					for (int i = 0; i < storage.getLength(); i++) {
						String key=storage.key(i);
						storageTable.setText(i+1, 0, storage.getItem(key));
					}
				}
			}
		});
		verticalLayoutContainer.add(framedPanel);
		verticalLayoutContainer.add(storageTable);
		RootPanel.get().add(verticalLayoutContainer);
	}
}
