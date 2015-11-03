package com.dtc.test.client.ui;

import java.util.ArrayList;

import com.dtc.test.shared.vo.StorageVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

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
	@UiField(provided = true)
	@Ignore Grid<StorageVO> storageList;
	
	public StorageView() {
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
		initList();
	}
	
	@UiHandler("saveStorage")
	void onSave(SelectEvent selectEvent){
		StorageVO storageVO=driver.flush();
		if (storage!=null) {
			storage.setItem(storageVO.getKey(), storageVO.getData());
			edit(new StorageVO());
			setList(storageVO);
		}
	}
	
	@UiHandler("resetStorage")
	void onReset(SelectEvent s){
		storage.clear();
		storageList.getStore().clear();
	}
	
	private void setList(StorageVO storageVO) {
		for (StorageVO s : storageList.getStore().getAll()) {
			if (s.getKey().equals(storageVO.getKey())) {
				storageList.getStore().clear();
				initList();
				return;
			}
		}
		storageList.getStore().add(storageVO);
	}
	
	private void initList(){
		for (int i = 0; i < storage.getLength(); i++) {
			String key=storage.key(i);
			storageList.getStore().add(new StorageVO(key, storage.getItem(key)));
		}
	}
	
	public void edit(StorageVO storageVO) {
		driver.edit(storageVO);
	}

	interface StorageProperty extends PropertyAccess<StorageVO> {
		@Path("key")
		ModelKeyProvider<StorageVO> id();

		ValueProvider<StorageVO, String> key();

		ValueProvider<StorageVO, String> data();
	}
}
