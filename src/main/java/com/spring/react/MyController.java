package com.spring.react;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.spring.react.dto.AdminDto;
import com.spring.react.dto.ContentDto;
import com.spring.react.dto.ReplyDto;

@Controller
public class MyController  {
 
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private AdminDto admin;
	
	//CSR�� ���� Index������ ��û
	@RequestMapping(value = "/index", method = RequestMethod.GET)
    public String page( Model model,HttpServletRequest request) {
        model.addAttribute("pageName", "index");
        
        HttpSession session = request.getSession();

        session.setAttribute("jud", "Normal connection");


        return "page";
    }
	
	//�Խ��� �� ��� ��û
	 @RequestMapping(value = "/read", method = RequestMethod.GET) 
	    public @ResponseBody Object getReadContents(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 	

		 	HttpSession session = request.getSession();
		 	
		 	if(session == null || session.getAttribute("read_jud")==null) {
		 	session.setAttribute("read_jud", "true");
	        IDao dao = sqlSession.getMapper(IDao.class);
	        Map<String, Object> retVal = new HashMap<String, Object>();
	        ArrayList<ContentDto> list = new ArrayList<ContentDto>();
	        list = dao.listDao();
	        retVal.put("contents",list);
	        return retVal;
		 	}else {
		 		session.removeAttribute("read_jud");
		 		response.sendRedirect(request.getContextPath()+"/index");
		 		return null;
		 	}
	        
	        
	    }
	 
	
	 //�Խ����� Ư�� �� ���� ��û, ���ʿ� �� �� ��� ����Ʈ ��û
	 @RequestMapping(value = "/read/{id}", method = RequestMethod.GET) 
	    public @ResponseBody Object getReadContent(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException {
		 	HttpSession session = request.getSession();
		 	
		 	if(session == null || session.getAttribute("read_jud")==null){ 
		 	session.setAttribute("read_jud", "true");
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
		 	}else {
		 		session.removeAttribute("read_jud");
		 		response.sendRedirect(request.getContextPath()+"/index");
		 		return null;
		 	}
	    }
	 
	 
	 // �Խ����� Ư�� �ۿ� ���� ��� ����Ʈ�� ���� ��û
	 @RequestMapping(value = "/read/reply/{id}", method = RequestMethod.GET) 
	    public @ResponseBody Object getContentReply(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException {
	        
		 	HttpSession session = request.getSession();
		 	
		 	if(session == null || session.getAttribute("read_jud")==null){ 
		 	session.setAttribute("read_jud", "true");
		 	IDao dao = sqlSession.getMapper(IDao.class);
	        Map<String, Object> retVal = new HashMap<String, Object>();
	        
	        ArrayList<ReplyDto> r_dto = new ArrayList<ReplyDto>();
	       
	       
	        r_dto = dao.r_listDao(id);
	      
	        retVal.put("reply",r_dto);
	       
	        return retVal;
		 	}else {
		 		session.removeAttribute("read_jud");
		 		response.sendRedirect(request.getContextPath()+"/index");
		 		return null;
		 	}
	    }
	 
	 	//�Խ����� ������ ���� Ư�� �� ���� ��û
		 @RequestMapping(value = "/update/{id}", method = RequestMethod.GET) 
		    public @ResponseBody Object getUpdateContent(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 	HttpSession session = request.getSession();
			 	
			 	if(session == null || session.getAttribute("read_jud")==null){
			 	IDao dao = sqlSession.getMapper(IDao.class);
		        Map<String, Object> retVal = new HashMap<String, Object>();
		        ContentDto dto = new ContentDto();
		        dto = dao.viewDao(id);
		        
		        retVal.put("post",dto);
		       
		        return retVal;
			 	}else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 		return null;
			 	}
		    }
	 
	 
	 //�Խ��ǿ� �� �ۼ� ���� ���� �� DB�� �߰�
	 @RequestMapping(value = "/create", method = RequestMethod.POST)
	 @ResponseBody
	    public void createContent(@RequestBody final ContentDto dto,HttpServletRequest request, HttpServletResponse response) throws IOException{
		 HttpSession session = request.getSession();
		 	
		 if(session == null || session.getAttribute("read_jud")==null){
		 IDao dao = sqlSession.getMapper(IDao.class);
		 dao.writeDao(dto.getbName(),dto.getbTitle(),dto.getbContent());
		 }else {
		 		session.removeAttribute("read_jud");
		 		response.sendRedirect(request.getContextPath()+"/index");
		 }
	       
	       
	    }
	 
	 	//�Խ��ǿ� �� ���� ���� ���� �� DB�� ����
		 @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
		 @ResponseBody
		    public void updateContent(@PathVariable("id") final String id,@RequestBody final ContentDto dto,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 HttpSession session = request.getSession();
			 	
			 if(session == null || session.getAttribute("read_jud")==null){
			 IDao dao = sqlSession.getMapper(IDao.class);
			 dao.updateDao(dto.getbName(),dto.getbTitle(),dto.getbContent(),id);
			 }else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 }
		       
		       
		    }
		 
		//������ ���� id�� ���� �� DB���� ����
		 @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
		 @ResponseBody
		    public void deleteContent(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 HttpSession session = request.getSession();
			 	
			 if(session == null || session.getAttribute("read_jud")==null){
			 IDao dao = sqlSession.getMapper(IDao.class);
			 dao.deleteDao(id);   
			 }else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 }
		       
		    }
		 
		 //�Խ����� Ư�� �ۿ� ���� ��� ���� ���� �� DB�� �ۼ�
		 @RequestMapping(value = "/create/reply/{id}", method = RequestMethod.POST) 
		    public @ResponseBody void createReply(@PathVariable("id") final String id,@RequestBody final ReplyDto dto,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 	HttpSession session = request.getSession();
			 	
			 	if(session == null || session.getAttribute("read_jud")==null){
			 	IDao dao = sqlSession.getMapper(IDao.class);
		        dao.writeReplyDao(dto.getrName(), dto.getrContent(), id,dto.getrPw());
			 	}else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 }
		       
		    }
		 
		 // Ư�� �Խñۿ��� ������ ����� ��й�ȣ ������ ����
		 @RequestMapping(value = "/reply/getpw/{id}", method = RequestMethod.GET) 
		    public @ResponseBody Map<String, Object> modifyReply(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 	HttpSession session = request.getSession();
			 	
			 	if(session == null || session.getAttribute("read_jud")==null){
			 	IDao dao = sqlSession.getMapper(IDao.class);
		        ReplyDto dto = new ReplyDto();
		        Map<String, Object> retVal = new HashMap<String, Object>();

		        dto = dao.modifyReplyDao(id);
		        
		        retVal.put("pw",dto);
		        
		        return retVal;
			 	}else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 		return null;
			 }
		       
		    }
		 
		 // Ư�� �Խñۿ��� ����� ������ �����ϱ� ���� ������ ����
		 @RequestMapping(value = "/modify/reply/{id}", method = RequestMethod.PUT) 
		    public @ResponseBody void modifyReply(@PathVariable("id") final String id,@RequestBody final ReplyDto dto,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 	HttpSession session = request.getSession();
			 	
			 	if(session == null || session.getAttribute("read_jud")==null){
			 	IDao dao = sqlSession.getMapper(IDao.class);
		        dao.updateReplyDao(dto.getrName(), dto.getrContent(), id);
			 	}else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 }
		       
		    }
		 
		 @RequestMapping(value = "/delete/reply/{id}", method = RequestMethod.DELETE)
		 @ResponseBody
		    public void deleteReply(@PathVariable("id") final String id,HttpServletRequest request, HttpServletResponse response) throws IOException{
			 HttpSession session = request.getSession();
			 	
			 if(session == null || session.getAttribute("read_jud")==null){
			 
			 IDao dao = sqlSession.getMapper(IDao.class);
			 dao.deleteReplyDao(id);
			 }else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 }
		       
		 }

		 
		 @RequestMapping(value = "/login", method = RequestMethod.GET)
		 @ResponseBody
		    public Map<String, Object> getLogin(HttpServletRequest request, HttpServletResponse response) throws IOException{
			 	HttpSession session = request.getSession();
			 	
			 	if(session == null || session.getAttribute("read_jud")==null){
			 
			 
			 	String id = admin.getId();
			 	String pw = admin.getPw();
			 	
		        Map<String, Object> retVal = new HashMap<String, Object>();
		        
		        retVal.put("id",id);
		        retVal.put("pw",pw);
		        
		        
		        return retVal;
			 	}else {
			 		session.removeAttribute("read_jud");
			 		response.sendRedirect(request.getContextPath()+"/index");
			 		return null;
			 	}
	
		    }
	 
	 
	
}