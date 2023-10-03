package gu.admin.organ;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.common.TreeMaker;
import gu.common.UtilEtc;
import gu.etc.EtcSvc;

// 추가한 import문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeptCtr {
    @Autowired
    private DeptSvc deptSvc;

    @Autowired
    private EtcSvc etcSvc;

    /**
     * 리스트.
     */
    @RequestMapping(value = "/adDepartment", method = RequestMethod.GET)
    public String departmentList(HttpServletRequest request, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
        etcSvc.setCommonAttribute(userno, modelMap);

        List<?> listview = deptSvc.selectDepartment();

        TreeMaker tm = new TreeMaker();
        String treeStr = tm.makeTreeByHierarchy(listview);

        modelMap.addAttribute("treeStr", treeStr);

        return "admin/organ/Department";
    }

    /**
     * 부서 등록 (POST 요청).
     */
    @RequestMapping(value = "/adDepartmentSave", method = RequestMethod.POST)
    public void departmentSave(HttpServletResponse response, DepartmentVO deptInfo) {

        deptSvc.insertDepartment(deptInfo);

        UtilEtc.responseJsonValue(response, deptInfo);
    }

    /**
     * 부서 정보(하나) (GET 요청).
     */
    @RequestMapping(value = "/adDepartmentRead", method = RequestMethod.GET)
    public void departmentRead(HttpServletRequest request, HttpServletResponse response) {

        String deptno = request.getParameter("deptno");

        DepartmentVO deptInfo = deptSvc.selectDepartmentOne(deptno);

        UtilEtc.responseJsonValue(response, deptInfo);
    }

    /**
     * 부서 삭제 (GET 요청).
     */
    @RequestMapping(value = "/adDepartmentDelete", method = RequestMethod.GET)
    public void departmentDelete(HttpServletRequest request, HttpServletResponse response) {

        String deptno = request.getParameter("deptno");

        deptSvc.deleteDepartment(deptno);

        UtilEtc.responseJsonValue(response, "OK");
    }
}
