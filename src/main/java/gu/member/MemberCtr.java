package gu.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.admin.organ.UserSvc;
import gu.common.FileUtil;
import gu.common.FileVO;
import gu.common.SearchVO;
import gu.common.UtilEtc;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberCtr {
    @Autowired
    private UserSvc userSvc;

    @Autowired
    private MemberSvc memberSvc;

    /**
     * 내정보.
     */
    @RequestMapping(value = "/memberForm", method = RequestMethod.GET)
    public String memberForm(HttpServletRequest request, ModelMap modelMap) {
        String save = request.getParameter("save");

        String userno = request.getSession().getAttribute("userno").toString();
        
        UserVO userInfo = userSvc.selectUserOne(userno);
        
        modelMap.addAttribute("userInfo", userInfo);
        modelMap.addAttribute("save", save);
        
        return "member/memberForm";
    }


    /**
     * 사용자 저장.
     */
    @RequestMapping(value = "/userSave", method = RequestMethod.POST)
    public String userSave(HttpServletRequest request, ModelMap modelMap, UserVO userInfo) {
        String userno = request.getSession().getAttribute("userno").toString();
        userInfo.setUserno(userno);
        
        FileUtil fs = new FileUtil();
        FileVO fileInfo = fs.saveImage(userInfo.getPhotofile());
        if (fileInfo != null) {
            userInfo.setPhoto(fileInfo.getRealname());
        }
        userSvc.updateUserByMe(userInfo);

        return "redirect:/memberForm?save=OK";
    }
    
    /**
     * 비밀번호 변경.
     */
    @RequestMapping(value = "/changePWSave", method = RequestMethod.POST)
    public void changePWSave(HttpServletRequest request, HttpServletResponse response, UserVO userInfo) {
        String userno = request.getSession().getAttribute("userno").toString();
        userInfo.setUserno(userno);
        
        userSvc.updateUserPassword(userInfo);

        UtilEtc.responseJsonValue(response,"OK");
    }

    
    /**
     * 직원조회.
     */
    @RequestMapping(value = "/searchMember", method = RequestMethod.GET)
    public String searchMember(SearchVO searchVO, ModelMap modelMap) {
        
        if (searchVO.getSearchKeyword() != null & !"".equals(searchVO.getSearchKeyword())) {
            searchVO.pageCalculate( memberSvc.selectSearchMemberCount(searchVO) ); // startRow, endRow
            
            List<?> listview = memberSvc.selectSearchMemberList(searchVO);
        
            modelMap.addAttribute("listview", listview);
        }
        modelMap.addAttribute("searchVO", searchVO);
        return "member/searchMember";
    }
}
