package org.erlide.engine;

import org.erlide.engine.model.root.IErlModel;
import org.erlide.engine.model.root.IErlModule;
import org.erlide.engine.model.root.IErlProject;
import org.erlide.engine.model.root.IProjectConfiguratorFactory;
import org.erlide.engine.services.ErlangService;
import org.erlide.engine.services.GenericService;
import org.erlide.engine.services.cleanup.CleanupProvider;
import org.erlide.engine.services.codeassist.CompletionService;
import org.erlide.engine.services.codeassist.ContextAssistService;
import org.erlide.engine.services.edoc.EdocExportService;
import org.erlide.engine.services.parsing.ParserService;
import org.erlide.engine.services.parsing.ScannerProviderService;
import org.erlide.engine.services.parsing.SimpleParserService;
import org.erlide.engine.services.parsing.SimpleScannerService;
import org.erlide.engine.services.proclist.ProclistService;
import org.erlide.engine.services.search.ModelFindService;
import org.erlide.engine.services.search.ModelSearcherService;
import org.erlide.engine.services.search.ModelUtilService;
import org.erlide.engine.services.search.SearchServerService;

public interface IErlangEngine {

    boolean isAvailable();

    <T extends ErlangService> T getService(Class<T> type);

    IErlModel getModel();

    String getStateDir();

    // //

    ContextAssistService getContextAssistService();

    SearchServerService getSearchServerService();

    ModelUtilService getModelUtilService();

    CleanupProvider getCleanupProvider();

    ParserService getParserService();

    ScannerProviderService getScannerProviderService();

    EdocExportService getEdocExportService();

    ProclistService getProclistService();

    SimpleScannerService getSimpleScannerService();

    SimpleParserService getSimpleParserService();

    ModelFindService getModelFindService();

    ModelSearcherService getModelSearcherService();

    CompletionService getCompletionService(IErlProject project, IErlModule module,
            String elementBefore);

    // TODO
    GenericService getGenericService();

    // INTERNAL use

    IProjectConfiguratorFactory getProjectConfiguratorFactory();

}
