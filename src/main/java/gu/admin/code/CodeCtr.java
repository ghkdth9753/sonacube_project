package gu.admin.code;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.common.SearchVO;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CodeCtr {

    @Autowired
    private CodeSvc codeSvc;

    static final Logger LOGGER = LoggerFactory.getLogger(CodeCtr.class);

    /**
     * 공통 코드 리스트 (GET 요청).
     */
    @RequestMapping(value = "/adCodeList", method = RequestMethod.GET)
    public String codeList(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        searchVO.pageCalculate(codeSvc.selectCodeCount(searchVO)); // startRow, endRow
        List<?> listview = codeSvc.selectCodeList(searchVO);

        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);

        return "admin/code/CodeList";
    }

    /**
     * 공통 코드 쓰기 (GET 요청).
     */
    @RequestMapping(value = "/adCodeForm", method = RequestMethod.GET)
    public String codeForm(HttpServletRequest request, CodeVO codeInfo, ModelMap modelMap) {
        if (codeInfo.getClassno() != null) {
            codeInfo = codeSvc.selectCodeOne(codeInfo);

            modelMap.addAttribute("codeInfo", codeInfo);
            modelMap.addAttribute("codeFormType", "U");
        }

        return "admin/code/CodeForm";
    }

    /**
     * 공통 코드 저장 (POST 요청).
     */
    @RequestMapping(value = "/adCodeSave", method = RequestMethod.POST)
    public String codeSave(HttpServletRequest request, CodeVO codeInfo, ModelMap modelMap) {
        String codeFormType = request.getParameter("codeFormType");

        if (!"U".equals(codeFormType)) { // insert
            CodeVO cvo = codeSvc.selectCodeOne(codeInfo);
            if (cvo != null) {
                modelMap.addAttribute("msg", "이미 사용중인 코드입니다.");
                return "common/message";
            }
        }
        codeSvc.insertCode(codeFormType, codeInfo);

        return "redirect:/adCodeList";
    }

    /**
     * 공통 코드 읽기 (GET 요청).
     */
    @RequestMapping(value = "/adCodeRead", method = RequestMethod.GET)
    public String codeRead(HttpServletRequest request, CodeVO codeVO, ModelMap modelMap) {

        CodeVO codeInfo = codeSvc.selectCodeOne(codeVO);

        modelMap.addAttribute("codeInfo", codeInfo);

        return "admin/code/CodeRead";
    }

    /**
     * 공통 코드 삭제 (GET 요청).
     */
    @RequestMapping(value = "/adCodeDelete", method = RequestMethod.GET)
    public String codeDelete(HttpServletRequest request, CodeVO codeVO) {

        codeSvc.deleteCodeOne(codeVO);

        return "redirect:/adCodeList";
    }

}
