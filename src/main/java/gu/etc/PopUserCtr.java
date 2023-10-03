package gu.etc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.admin.organ.DeptSvc;
import gu.admin.organ.UserSvc;
import gu.common.SearchVO;
import gu.common.TreeMaker;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PopUserCtr {

    @Autowired
    private DeptSvc deptSvc;
    
    @Autowired
    private UserSvc userSvc;
    
    /**
     * 부서리스트.
     */
    @RequestMapping(value = "/popupDept", method = RequestMethod.POST)
       public String popupDept(ModelMap modelMap) {
        List<?> listview   = deptSvc.selectDepartment();

        TreeMaker tm = new TreeMaker();
        String treeStr = tm.makeTreeByHierarchy(listview);
        
        modelMap.addAttribute("treeStr", treeStr);
        
        return "etc/popupDept";
    }

    /**
     *  부서리스트 for 사용자.
     */
    @RequestMapping(value = "/popupUser", method = RequestMethod.POST)
    public String popupUser(ModelMap modelMap) {
        List<?> listview   = deptSvc.selectDepartment();

        TreeMaker tm = new TreeMaker();
        String treeStr = tm.makeTreeByHierarchy(listview);
        
        modelMap.addAttribute("treeStr", treeStr);
        
        return "etc/popupUser";
    }
    
    /**
     * 선택된 부서의 User 리스트.
     */
    @RequestMapping(value = "/popupUsersByDept", method = RequestMethod.POST)
    public String popupUsersByDept(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        String deptno = request.getParameter("deptno");
        searchVO.setSearchExt1(deptno);
        
        List<?> listview  = userSvc.selectUserListWithDept(searchVO);
        
        modelMap.addAttribute("listview", listview);
        
        return "etc/popupUsersByDept";
    }
    
    /**
     *  부서리스트 for 사용자들.
     */
    @RequestMapping(value = "/popupUsers", method = RequestMethod.POST)
    public String popupUsers(ModelMap modelMap) {
        popupUser(modelMap);
        
        return "etc/popupUsers";
    }

    /**
     *  부서리스트 for 사용자들 - 결재 경로 지정용.
     */
    @RequestMapping(value = "/popupUsers4SignPath", method = RequestMethod.POST)
    public String popupUsers4SignPath(ModelMap modelMap) {
        popupUser(modelMap);
        
        return "etc/popupUsers4SignPath";
    }
    
    /**
     * User 리스트  for 사용자들.
     */
    @RequestMapping(value = "/popupUsers4Users", method = RequestMethod.POST)
    public String popupUsers4Users(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        popupUsersByDept(request, searchVO, modelMap);
        
        return "etc/popupUsers4Users";
    }    
}
