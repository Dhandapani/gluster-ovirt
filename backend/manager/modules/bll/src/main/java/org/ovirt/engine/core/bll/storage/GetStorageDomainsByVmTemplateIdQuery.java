package org.ovirt.engine.core.bll.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ovirt.engine.core.bll.GetVmTemplatesDisksQuery;
import org.ovirt.engine.core.common.businessentities.DiskImage;
import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.core.common.businessentities.storage_domains;
import org.ovirt.engine.core.common.queries.GetStorageDomainsByVmTemplateIdQueryParameters;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dal.dbbroker.DbFacade;

public class GetStorageDomainsByVmTemplateIdQuery<P extends GetStorageDomainsByVmTemplateIdQueryParameters>
        extends GetVmTemplatesDisksQuery<P> {
    public GetStorageDomainsByVmTemplateIdQuery(P parameters) {
        super(parameters);
    }

    @Override
    protected void executeQueryCommand() {
        ArrayList<storage_domains> result = new ArrayList<storage_domains>();
        VmTemplate vmTemplate = DbFacade.getInstance().getVmTemplateDAO()
                .get(getParameters().getId());
        if (vmTemplate != null && vmTemplate.getstorage_pool_id() != null) {
            List<DiskImage> templateDisks = getTemplateDisks();

            if (templateDisks.size() > 0) {

                Set<Guid> domains = new HashSet<Guid>();
                for (DiskImage templateDisk : templateDisks) {
                    domains.addAll(templateDisk.getstorage_ids());
                }

                for (Guid domainId : domains) {
                    storage_domains domain = DbFacade.getInstance().getStorageDomainDAO().getForStoragePool(domainId,
                            vmTemplate.getstorage_pool_id().getValue());
                    if (domain != null) {
                        result.add(domain);
                    }
                }
            }
        }
        getQueryReturnValue().setReturnValue(result);
    }
}
