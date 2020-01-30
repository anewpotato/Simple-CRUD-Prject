package com.spring.react;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.react.dao.IDao;
import com.spring.react.dto.AdminDto;
import com.spring.react.dto.ContentDto;
import com.spring.react.dto.ReplyDto;

@Controller
public class MyController  {
 
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private AdminDto admin; // admin �α��� ������ ������ ��ü�� bean�� ���� �ڵ� ����
	
	//CSR�� ���� Index������ ��û
	@RequestMapping(value = {"/index"})
    public String page( HttpServletRequest request) {       		
        
		// index ������ �湮 �� session�� �����Ͽ� interceptor���� ���͸� ���� �ʰ��Ѵ�.
        HttpSession session = request.getSession();
        session.setAttribute("jud", "Normal connection");
        
        
        return "page";
    }
	
	//Ư�� �ּҸ� ���� ���� �� /index url�� redirection
	@RequestMapping(value = {"/read/{id}","/read","/read/reply/{id}","/update/{id}","/create","/getContent/{id}","/delete/{id}","/create/reply/{id}","/reply/getpw/{id}","/modify/reply/{id}","/delete/reply/{id}","/login"}) 
    public String redirectURL() {
	 	
	 	return "redirect:/index";
       
    }
	
	//�Խ��� �� ��� ��û
	@RequestMapping(value = "/read", method = RequestMethod.GET, produces="application/json") 
    public @ResponseBody Object getReadContents() {
		
        IDao dao = sqlSession.getMapper(IDao.class);       
        Map<String, Object> retVal = new HashMap<String, Object>();
        ArrayList<ContentDto> list = new ArrayList<ContentDto>();
        list = dao.listDao();
        
        retVal.put("contents",list);
        
        return retVal;
       
    }
	
	 
	
	 //�Խ����� Ư�� �� ���� ��û, ���ʿ� �� �� ��� ����Ʈ ��û
	@RequestMapping(value = "/read/{id}", method = RequestMethod.GET, produces="application/json") 
    public @ResponseBody Object getReadContent(@PathVariable("id") final String id) {
	 	
	 	IDao dao = sqlSession.getMapper(IDao.class);
        Map<String, Object> retVal = new HashMap<String, Object>();
        ContentDto dto = new ContentDto();
        ArrayList<ReplyDto> r_dto = new ArrayList<ReplyDto>();
        
        dao.upHit(id); // ������ �ҷ����� ���� �ش� �Խñ��� ��ȸ���� �÷��ش�.
        dto = dao.viewDao(id);
        r_dto = dao.r_listDao(id);
        
        retVal.put("post",dto);
        retVal.put("reply",r_dto);
       
        return retVal;
    }
	
	
	
	 
	 
	 // �Խ����� Ư�� �ۿ� ���� ��� ����Ʈ�� ���� ��û
	@RequestMapping(value = "/read/reply/{id}", method = RequestMethod.GET, produces="application/json") 
    public @ResponseBody Object getContentReply(@PathVariable("id") final String id){
        
	 	IDao dao = sqlSession.getMapper(IDao.class);
        Map<String, Object> retVal = new HashMap<String, Object>();    
        ArrayList<ReplyDto> r_dto = new ArrayList<ReplyDto>(); 
        
        r_dto = dao.r_listDao(id); 
        retVal.put("reply",r_dto);
       
        return retVal;
	 	
    }
	 
	//�Խ����� ������ ���� Ư�� �� ���� ��û
	@RequestMapping(value = "/getContent/{id}", method = RequestMethod.GET, produces="application/json") 
	public @ResponseBody Object getUpdateContent(@PathVariable("id") final String id){
	 	
	 	IDao dao = sqlSession.getMapper(IDao.class);
	    Map<String, Object> retVal = new HashMap<String, Object>();
	    ContentDto dto = new ContentDto();
	    
	    dto = dao.viewDao(id);	    
	    retVal.put("post",dto);
	   
	    return retVal;
	 	
	}
	 
	 
	//�Խ��ǿ� �� �ۼ� ���� ���� �� DB�� �߰�
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces="application/json")	
    public @ResponseBody void createContent(@RequestBody final ContentDto dto){
	  
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.writeDao(dto.getbName(),dto.getbTitle(),dto.getbContent());      
    }
	 
 	//�Խ��ǿ� �� ���� ���� ���� �� DB�� ����
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, produces="application/json")
	@ResponseBody
    public void updateContent(@PathVariable("id") final String id,@RequestBody final ContentDto dto){
	 
		 IDao dao = sqlSession.getMapper(IDao.class);
		 
		 dao.updateDao(dto.getbName(),dto.getbTitle(),dto.getbContent(),id);     
    }
		 
	//������ ���� id�� ���� �� DB���� ����
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces="application/json")
	@ResponseBody
    public void deleteContent(@PathVariable("id") final String id){
	 
		 IDao dao = sqlSession.getMapper(IDao.class);
		 
		 dao.deleteDao(id);   	
    }
		 
	//�Խ����� Ư�� �ۿ� ���� ��� ���� ���� �� DB�� �ۼ�
	@RequestMapping(value = "/create/reply/{id}", method = RequestMethod.POST, produces="application/json") 
    public @ResponseBody void createReply(@PathVariable("id") final String id,@RequestBody final ReplyDto dto){
	 	
	 		IDao dao = sqlSession.getMapper(IDao.class);
	 		
	 		dao.writeReplyDao(dto.getrName(), dto.getrContent(), id,dto.getrPw());     
    }
		 
	// Ư�� �Խñۿ��� ������ ����� ��й�ȣ ������ ����
	@RequestMapping(value = "/reply/getpw/{id}", method = RequestMethod.GET, produces="application/json") 
    public @ResponseBody Map<String, Object> modifyReply(@PathVariable("id") final String id){
	 	 
	 		IDao dao = sqlSession.getMapper(IDao.class);
	 		ReplyDto dto = new ReplyDto();
	 		Map<String, Object> retVal = new HashMap<String, Object>();

	 		dto = dao.modifyReplyDao(id);       
	 		retVal.put("pw",dto);
        
	 		return retVal;      
    }
		 
	// Ư�� �Խñۿ��� ����� ������ �����ϱ� ���� ������ ����
	@RequestMapping(value = "/modify/reply/{id}", method = RequestMethod.PUT, produces="application/json") 
    public @ResponseBody void modifyReply(@PathVariable("id") final String id,@RequestBody final ReplyDto dto){
	 	
	 		IDao dao = sqlSession.getMapper(IDao.class);
	 		
	 		dao.updateReplyDao(dto.getrName(), dto.getrContent(), id);      
    }
		 
	@RequestMapping(value = "/delete/reply/{id}", method = RequestMethod.DELETE, produces="application/json")		 
    public @ResponseBody void deleteReply(@PathVariable("id") final String id){
		 
		 	IDao dao = sqlSession.getMapper(IDao.class);
		 	
		 	dao.deleteReplyDao(id);       
	 }

		 
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces="application/json")		 
    public @ResponseBody Map<String, Object> getLogin() {
		 	
	 	String id = admin.getId();
	 	String pw = admin.getPw();
	 	
        Map<String, Object> retVal = new HashMap<String, Object>();
        
        retVal.put("id",id);
        retVal.put("pw",pw);
               
        return retVal;
    }
	 
	 
	
}