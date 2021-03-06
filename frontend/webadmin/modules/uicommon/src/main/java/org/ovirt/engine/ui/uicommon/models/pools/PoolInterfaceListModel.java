package org.ovirt.engine.ui.uicommon.models.pools;
import java.util.Collections;
import org.ovirt.engine.core.compat.*;
import org.ovirt.engine.ui.uicompat.*;
import org.ovirt.engine.core.common.businessentities.*;
import org.ovirt.engine.core.common.vdscommands.*;
import org.ovirt.engine.core.common.queries.*;
import org.ovirt.engine.core.common.action.*;
import org.ovirt.engine.ui.frontend.*;
import org.ovirt.engine.ui.uicommon.*;
import org.ovirt.engine.ui.uicommon.models.*;
import org.ovirt.engine.core.common.*;

import org.ovirt.engine.ui.uicommon.dataprovider.*;
import org.ovirt.engine.ui.uicommon.models.*;
import org.ovirt.engine.core.common.businessentities.*;

import org.ovirt.engine.ui.uicommon.*;

@SuppressWarnings("unused")
public class PoolInterfaceListModel extends SearchableListModel
{
	public PoolInterfaceListModel()
	{
		setTitle("Network Interfaces");
	}

	@Override
	protected void OnEntityChanged()
	{
		super.OnEntityChanged();

		vm_pools pool = (vm_pools)getEntity();
		if (pool != null)
		{
			AsyncQuery _asyncQuery = new AsyncQuery();
			_asyncQuery.setModel(this);
			_asyncQuery.asyncCallback = new INewAsyncCallback() { public void OnSuccess(Object model, Object result)
											{
												VM vm = (VM)result;
												if (vm != null)
												{
													PoolInterfaceListModel poolInterfaceListModel = (PoolInterfaceListModel) model;
													poolInterfaceListModel.SyncSearch(VdcQueryType.GetVmInterfacesByVmId, new GetVmByVmIdParameters(vm.getId()));
												}
											}};
			AsyncDataProvider.GetAnyVm(_asyncQuery, pool.getvm_pool_name());
		}
	}
}