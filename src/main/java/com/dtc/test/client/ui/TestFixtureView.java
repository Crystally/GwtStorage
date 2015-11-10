package com.dtc.test.client.ui;

import com.dtc.test.client.Generate;
import com.dtc.test.shared.vo.StorageVO;
import com.dtc.test.shared.vo.TestFixtureVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.IntegerField;

public class TestFixtureView extends Composite implements Editor<TestFixtureVO> {

	private static TestFixtureViewUiBinder uiBinder = GWT.create(TestFixtureViewUiBinder.class);

	interface TestFixtureViewUiBinder extends UiBinder<Widget, TestFixtureView> {
	}

	interface Driver extends SimpleBeanEditorDriver<TestFixtureVO, TestFixtureView> {
	}

	final Storage storage = Storage.getLocalStorageIfSupported();
	Driver driver = GWT.create(Driver.class);

	@UiField IntegerField number;
	@UiField IntegerField keyLength;
	@UiField IntegerField valueLength;
	@UiField IntegerField key;
	@UiField IntegerField value;
	@Ignore StorageView storageView;

	public void setStorageView(StorageView view) {
		this.storageView = view;
	}

	public TestFixtureView() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
		edit();
	}

	@UiHandler("save")
	void onSave(SelectEvent selectEvent) {
		storage.clear();
		storageView.storageList.getStore().clear();
		TestFixtureVO testFixtureVO = driver.flush();
		String key = Generate.byByte(testFixtureVO.getKeyLength());
		NumberFormat numberFormat=NumberFormat.getFormat(key);
		for (int i = 0; i < testFixtureVO.getNumber(); i++) {
			String keyString=numberFormat.format(Integer.parseInt(key) + i);
			if (keyString.length()>testFixtureVO.getKeyLength()) {
				break;
			}
			String dataString = Generate.byByte(testFixtureVO.getValueLength());
			storage.setItem(keyString, dataString);
			storageView.storageList.getStore().add(new StorageVO(keyString, dataString));
		}
		storageView.displayCapacity();
		edit();
	}
	
	@UiHandler("specifySave")
	void onSpecifySave(SelectEvent selectEvent){
		TestFixtureVO testFixtureVO = driver.flush();
		String key = Generate.byByte(testFixtureVO.getKey());
		String value = Generate.byByte(testFixtureVO.getValue());
		storage.setItem(key, value);
		StorageVO storageVO=new StorageVO(key, value);
		StorageVO voInStore = storageView.storageList.getStore().findModel(storageVO);
		if (voInStore == null) {
			storageView.storageList.getStore().add(storageVO);
		} else {
			voInStore.setData(value);
			storageView.storageList.getView().refresh(false);
		}
		storageView.displayCapacity();
		edit();
	}
	
	private void edit() {
		driver.edit(new TestFixtureVO());
	}
}
