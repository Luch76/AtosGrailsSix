package AtosGrailsSix

import com.luch.ApplicationFunctionService
import com.luch.LuchSessionService
import com.luch.ModuleController
import grails.plugin.springsecurity.annotation.Secured

import javax.sql.DataSource

@Secured('ROLE_ADMIN')
class IndexController {

    DataSource dataSource;
    ApplicationFunctionService applicationFunctionService;
    LuchSessionService luchSessionService;

    def okay() {
        render "OK";
    }


    def index() {
        def modules;
        ModuleController moduleController;
        Date today;
        String tempFolder;

        log.info("IndexController, index");

        applicationFunctionService.init(dataSource, session);
        today = applicationFunctionService.currentDateTime();
        modules = [];

        moduleController = new ModuleController();
        moduleController.controller = "fileHeader";
        moduleController.action = "spreadsheetImportIndex";
        moduleController.image = "luch/excel_xp.bmp";
        moduleController.title = "Excel Import";
        modules.add(moduleController);

        /*
        moduleController = new ModuleController();
        moduleController.controller = "xmlImport";
        moduleController.action = "index";
        moduleController.image = "luch/xml.bmp";
        moduleController.title = "XML Import";
        modules.add(moduleController);
        */

        log.info("Index, index, luchSessionService.sessionTempFolder(session): " + luchSessionService.sessionTempFolder(session));

        [modules:modules, today:today];
    }

}
