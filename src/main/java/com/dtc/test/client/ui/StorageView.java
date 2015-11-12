package com.dtc.test.client.ui;

import java.util.ArrayList;

import com.dtc.test.shared.vo.StorageVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.storage.client.StorageEvent.Handler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

public class StorageView extends Composite implements Editor<StorageVO>{

	private static StorageUiBinder uiBinder = GWT.create(StorageUiBinder.class);
	private static StorageProperty properties = GWT.create(StorageProperty.class);
	
	interface StorageUiBinder extends UiBinder<Widget, StorageView> {
	}
	
	interface Driver extends SimpleBeanEditorDriver<StorageVO, StorageView>{
	}

	final Storage storage=Storage.getLocalStorageIfSupported();
	Driver driver=GWT.create(Driver.class);
	
	@UiField TextField key;
	@UiField TextField data;
	@Ignore
	@UiField Label keyByteLabel;
	@Ignore
	@UiField Label valueByteLabel;
	@Ignore
	@UiField Label totalByteLabel;
	@UiField(provided = true)
	@Ignore Grid<StorageVO> storageList;
	
	public StorageView() {
		if (!Storage.isLocalStorageSupported()) {
			CenterLayoutContainer container = new CenterLayoutContainer();
			container.add(new LabelToolItem("Local Storage is not supported"));
			initWidget(container);
			return;
		}
		
		Storage.addStorageEventHandler(new Handler() {
			@Override
			public void onStorageChange(StorageEvent event) {
				//不是 storage 產生的 event 就忽略不管
				if (storage != event.getStorageArea()) { return ;}
				
				//顧及 storage.clear() 的狀況，所以直接要求 storageList 清空重建
				refresh();
			}
		});

		// ==== init grid ==== //
		ArrayList<ColumnConfig<StorageVO, ?>> ccList = new ArrayList<>();
		ccList.add(new ColumnConfig<StorageVO, String>(properties.key(), 100,"key"));
		ccList.add(new ColumnConfig<StorageVO, String>(properties.data(), 100,"data"));
		ColumnModel<StorageVO> cm = new ColumnModel<StorageVO>(ccList);
		storageList = new Grid<StorageVO>(new ListStore<StorageVO>(properties.id()), cm);
		storageList.getView().setForceFit(true);
		// ========= //
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
		refresh();
		resetEditor();
	}
	
	@UiHandler("saveStorage")
	void onSave(SelectEvent selectEvent){
		StorageVO storageVO=driver.flush();
		
		//維護 local storage 的資料
		storage.setItem(storageVO.getKey(), storageVO.getData());
		resetEditor();
	}
	
	@UiHandler("resetStorage")
	void onReset(SelectEvent s){
		storage.clear();
	}
	
	private void refresh(){
		storageList.getStore().clear();
		
		for (int i = 0; i < storage.getLength(); i++) {
			String key=storage.key(i);
			storageList.getStore().add(new StorageVO(key, storage.getItem(key)));
		}
		
		displayCapacity();
	}
	
	private void resetEditor() {
		driver.edit(new StorageVO());
	}
	
	private void displayCapacity(){
		int keyByte=0;
		int valueByte=0;
		for (int i = 0; i < storage.getLength(); i++) {
			String key=storage.key(i);
			keyByte += key.getBytes().length;
			valueByte += storage.getItem(key).length();
		}
		keyByteLabel.setText(calculateByte(keyByte));
		valueByteLabel.setText(calculateByte(valueByte));
		totalByteLabel.setText(calculateByte(keyByte+valueByte));
	}
	
	private String calculateByte(int b) {
		StringBuilder s=new StringBuilder();
		int i;
		s.append(b/1048576+"MB ");
		i=b%1048576;
		s.append(i/1024+"KB ");
		s.append(i%1024+"Byte");
		return s.toString();
	}
	
	interface StorageProperty extends PropertyAccess<StorageVO> {
		@Path("key")
		ModelKeyProvider<StorageVO> id();

		ValueProvider<StorageVO, String> key();

		ValueProvider<StorageVO, String> data();
	}
}
