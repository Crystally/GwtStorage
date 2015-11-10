package com.dtc.test.client;

import com.dtc.test.client.ui.StorageView;
import com.dtc.test.client.ui.TestFixtureView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class StorageTestEP implements EntryPoint {
	@Override
	public void onModuleLoad() {
		VerticalLayoutContainer verticalLayoutContainer=new VerticalLayoutContainer();
		TestFixtureView testFixtureView=new TestFixtureView();
		StorageView storageView=new StorageView();
		testFixtureView.setStorageView(storageView);
		verticalLayoutContainer.add(storageView);
		verticalLayoutContainer.add(testFixtureView);
		Viewport vp = new Viewport();
		vp.add(verticalLayoutContainer);
		RootPanel.get().add(vp);
	}
}
