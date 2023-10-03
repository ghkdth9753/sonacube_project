package gu.admin.board;

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

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardGroupCtr {
    @Autowired
    private BoardGroupSvc boardSvc;

    @Autowired
    private EtcSvc etcSvc;

    /**
     * 리스트 (GET 요청).
     */
    @RequestMapping(value = "/adBoardGroupList", method = RequestMethod.GET)
    public String boardGroupList(HttpServletRequest request, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
        etcSvc.setCommonAttribute(userno, modelMap);

        List<?> listview = boardSvc.selectBoardGroupList();
        TreeMaker tm = new TreeMaker();
        String treeStr = tm.makeTreeByHierarchy(listview);

        modelMap.addAttribute("treeStr", treeStr);

        return "admin/board/BoardGroupList";
    }

    /**
     * 게시판 그룹 쓰기 (POST 요청).
     */
    @RequestMapping(value = "/adBoardGroupSave", method = RequestMethod.POST)
    public void boardGroupSave(HttpServletResponse response, BoardGroupVO bgInfo) {
        boardSvc.insertBoard(bgInfo);
        UtilEtc.responseJsonValue(response, bgInfo);
    }

    /**
     * 게시판 그룹 읽기 (GET 요청).
     */
    @RequestMapping(value = "/adBoardGroupRead", method = RequestMethod.GET)
    public void boardGroupRead(HttpServletRequest request, HttpServletResponse response) {
        String bgno = request.getParameter("bgno");
        BoardGroupVO bgInfo = boardSvc.selectBoardGroupOne(bgno);
        UtilEtc.responseJsonValue(response, bgInfo);
    }

    /**
     * 게시판 그룹 삭제 (GET 요청).
     */
    @RequestMapping(value = "/adBoardGroupDelete", method = RequestMethod.GET)
    public void boardGroupDelete(HttpServletRequest request, HttpServletResponse response) {
        String bgno = request.getParameter("bgno");
        boardSvc.deleteBoardGroup(bgno);
        UtilEtc.responseJsonValue(response, "OK");
    }
}