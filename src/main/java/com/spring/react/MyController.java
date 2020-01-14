package com.spring.react;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.react.dao.IDao;
import com.spring.react.dto.ContentDto;

@Controller
public class MyController {
 
	@Autowired
	private SqlSession sqlSession;
	
	//CSR�� ���� Index������ ��û
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String page( Model model) {
        model.addAttribute("pageName", "index");
       
        return "page";
    }
	
	//�Խ��� �� ��� ��û
	 @RequestMapping(value = "/read", method = RequestMethod.GET) 
	    public @ResponseBody Object getReadContents() {
	        IDao dao = sqlSession.getMapper(IDao.class);
	        Map<String, Object> retVal = new HashMap<String, Object>();
	        ArrayList<ContentDto> list = new ArrayList<ContentDto>();
	        list = dao.listDao();
	        retVal.put("contents",list);
	       
	        return retVal;
	    }
	 //�Խ����� Ư�� �� ���� ��û
	 @RequestMapping(value = "/read/{id}", method = RequestMethod.GET) 
	    public @ResponseBody Object getReadContent(@PathVariable("id") final String id) {
	        IDao dao = sqlSession.getMapper(IDao.class);
	        Map<String, Object> retVal = new HashMap<String, Object>();
	        ContentDto dto = new ContentDto();
	        dao.upHit(id); // ������ �ҷ����� ���� �ش� �Խñ��� ��ȸ���� �÷��ش�.
	        dto = dao.viewDao(id);
	        
	        retVal.put("post",dto);
	       
	        return retVal;
	    }
	 
	 
	 //�Խ��ǿ� �� �ۼ� ���� ����
	 @RequestMapping(value = "/create", method = RequestMethod.POST)
	 @ResponseBody
	    public void createContent(@RequestBody final ContentDto dto) {
		 IDao dao = sqlSession.getMapper(IDao.class);
		 dao.writeDao(dto.getbName(),dto.getbTitle(),dto.getbContent());

	       
	       
	    }
	 
	 
	
}