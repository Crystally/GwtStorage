package com.dtc.test.client.ui;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.dtc.test.client.Generate;
import com.dtc.test.shared.vo.TestFixtureVO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
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
	
	public TestFixtureView() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
		edit();
	}

	@UiHandler("save")
	void onSave(SelectEvent selectEvent) {
		TestFixtureVO testFixtureVO = driver.flush();
		String key = Generate.byByte(testFixtureVO.getKeyLength());
		String value = Generate.byByte(testFixtureVO.getValueLength());
		if (testFixtureVO.getNumber() != null) {
			storage.clear();
			for (int i = 0; i < testFixtureVO.getNumber(); i++) {
				String keyString=String.valueOf(i);
				if (keyString.length()>testFixtureVO.getKeyLength()) {
					break;
				}
				byte[] b=keyString.getBytes();
				Arrays.fill(b, b.length, testFixtureVO.getKeyLength(), (byte)0);
				storage.setItem(new String(b), value);
			}
		}else {
			storage.setItem(key, value);
		}
		edit();
	}
	
	private void edit() {
		driver.edit(new TestFixtureVO());
	}
}
