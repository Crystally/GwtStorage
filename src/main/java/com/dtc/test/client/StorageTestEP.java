package com.dtc.test.client;

import com.dtc.test.client.ui.StorageView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class StorageTestEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		Viewport vp = new Viewport();
		vp.add(new StorageView());
		RootPanel.get().add(vp);
	}
}
