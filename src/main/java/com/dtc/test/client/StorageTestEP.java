package com.dtc.test.client;

import com.dtc.test.client.ui.StorageView;
import com.dtc.test.shared.vo.StorageVO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class StorageTestEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		StorageView storage=new StorageView();
		StorageVO storageVO=new StorageVO();
		storage.edit(storageVO);
		RootPanel.get().add(storage);
	}

}
