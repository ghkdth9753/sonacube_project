package gu.crud;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.common.SearchVO;
import gu.etc.EtcSvc;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
public class CrudCtr {

    @Autowired
    private CrudSvc crudSvc;
    
    @Autowired
    private EtcSvc etcSvc; 
    
    static final Logger LOGGER = LoggerFactory.getLogger(CrudCtr.class);
    
    /**
     * 리스트.
     */
    @RequestMapping(value = "/crudList", method = RequestMethod.GET)
    public String crudList(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();
        
        etcSvc.setCommonAttribute(userno, modelMap);
    	
        // CRUD 관련
        searchVO.pageCalculate( crudSvc.selectCrudCount(searchVO) ); // startRow, endRow
        List<?> listview  = crudSvc.selectCrudList(searchVO);
        
        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);
        
        return "crud/CrudList";
    }
    
    /** 
     * 쓰기. 
     */
    @RequestMapping(value = "/crudForm", method = RequestMethod.GET)
    public String crudForm(HttpServletRequest request, CrudVO crudInfo, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();
        
        etcSvc.setCommonAttribute(userno, modelMap);
    	
        // CRUD 관련
        if (crudInfo.getCrno() != null) {
            crudInfo = crudSvc.selectCrudOne(crudInfo);
        
            modelMap.addAttribute("crudInfo", crudInfo);
        }
        
        return "crud/CrudForm";
    }
    
    /**
     * 저장.
     */
    @RequestMapping(value = "/crudSave", method = RequestMethod.POST)
    public String crudSave(HttpServletRequest request, CrudVO crudInfo, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
    	crudInfo.setUserno(userno);
    	
        crudSvc.insertCrud(crudInfo);

        return "redirect:/crudList";
    }

    /**
     * 읽기.
     */
    @RequestMapping(value = "/crudRead", method = RequestMethod.GET)
    public String crudRead(HttpServletRequest request, CrudVO crudVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();
        
        etcSvc.setCommonAttribute(userno, modelMap);
    	
        // CRUD 관련
        
        CrudVO crudInfo = crudSvc.selectCrudOne(crudVO);

        modelMap.addAttribute("crudInfo", crudInfo);
        
        return "crud/CrudRead";
    }
    
    /**
     * 삭제.
     */
    @RequestMapping(value = "/crudDelete", method = RequestMethod.GET)
    public String crudDelete(HttpServletRequest request, CrudVO crudVO) {

        crudSvc.deleteCrud(crudVO);
        
        return "redirect:/crudList";
    }
   
}
