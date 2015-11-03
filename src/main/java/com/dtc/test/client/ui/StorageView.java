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
	private static ArrayList<StorageVO> list = new ArrayList<>();
	
	interface StorageUiBinder extends UiBinder<Widget, StorageView> {
	}
	
	interface Driver extends SimpleBeanEditorDriver<StorageVO, StorageView>{
	}

	final Storage storage=Storage.getLocalStorageIfSupported();
	final String s=generateString();
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
		storageList = new Grid<StorageVO>(new ListStore<>(properties.id()), cm);
		storageList.getView().setForceFit(true);
		// ========= //
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
		initList();
		reset();
	}
	
	@UiHandler("saveStorage")
	void onSave(SelectEvent selectEvent){
		StorageVO storageVO=driver.flush();
		if (Storage.isLocalStorageSupported()) {
			storage.setItem(storageVO.getKey(), storageVO.getData());
			edit(new StorageVO());
			getList(storageVO);
			reset();
		}
	}
	
	@UiHandler("resetStorage")
	void onRest(SelectEvent s){
		storage.clear();
		list=new ArrayList<>();
		storageList.getStore().clear();
	}
	
	private void getList(StorageVO storageVO) {
		for (StorageVO s : list) {
			if (s.getKey().equals(storageVO.getKey())) {
				s.setData(storageVO.getData());
				return;
			}
		}
		list.add(storageVO);
	}
	
	private void initList(){
		for (int i = 0; i < storage.getLength(); i++) {
			String key=storage.key(i);
			StorageVO storageVO=new StorageVO(key, storage.getItem(key));
			list.add(storageVO);
		}
	}
	
	private String generateString() {
		String a = "";
		for (int i = 0; i < 256; i++) {
			a=a+"12345678";
		}
		return a;
	}
	
	private void reset() {
		storageList.getStore().clear();
		storageList.getStore().addAll(list);
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
