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
	@UiField(provided = true)
	@Ignore Grid<StorageVO> storageList;
	
	public StorageView() {
		if (!Storage.isLocalStorageSupported()) {
			CenterLayoutContainer container = new CenterLayoutContainer();
			container.add(new LabelToolItem("Local Storage is not supported"));
			initWidget(container);
			return;
		}

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
		onRefresh(null);	//不是很 readable 的招數，不過很實用  [逃]
		resetEditor();
	}
	
	@UiHandler("saveStorage")
	void onSave(SelectEvent selectEvent){
		StorageVO storageVO=driver.flush();
		
		//維護 local storage 的資料
		storage.setItem(storageVO.getKey(), storageVO.getData());

		//維護畫面上的資料，雖然可以直接用 initList，不過那樣子效率不好
		StorageVO voInStore = storageList.getStore().findModel(storageVO);
		
		if (voInStore == null) {
			storageList.getStore().add(storageVO);
		} else {
			voInStore.setData(storageVO.getData());
			storageList.getView().refresh(false);
		}
		
		resetEditor();
	}
	
	@UiHandler("resetStorage")
	void onReset(SelectEvent s){
		storage.clear();
		storageList.getStore().clear();
	}
	
	@UiHandler("refresh")
	void onRefresh(SelectEvent s){
		storageList.getStore().clear();
		
		for (int i = 0; i < storage.getLength(); i++) {
			String key=storage.key(i);
			storageList.getStore().add(new StorageVO(key, storage.getItem(key)));
		}
	}
	
	private void resetEditor() {
		driver.edit(new StorageVO());
	}
	
	interface StorageProperty extends PropertyAccess<StorageVO> {
		@Path("key")
		ModelKeyProvider<StorageVO> id();

		ValueProvider<StorageVO, String> key();

		ValueProvider<StorageVO, String> data();
	}
}
