package md.manager;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import md.customtype.LogType;
import md.ldap.LDAPConnect;
import md.ldap.LDAPUser;
import md.model.EmployeeInfo;
import md.model.Log;
import md.model.PersonalInfo;
import md.servicedao.GeneralDAO;
import md.session.EmployeeList;
import md.utils.BossUtil;
import md.utils.PropertiesUtils;
import md.utils.SessionUtils;

import org.apache.commons.lang3.StringUtils;

@ManagedBean(name="loginManager")
@RequestScoped
public class LoginManager {
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String loginStaff(String page){
		if(username.trim().length() == 0){
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"��س������ͼ����",""));
			 return null;
		}
		if(password.trim().length() == 0){
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"��س�������ʼ�ҹ",""));
			 return null;
		}
			
		PersonalInfo user = authentication(username, password);
		if(user != null){	
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", user);
			GeneralDAO.saveLog(new Log(null, SessionUtils.getUserFromSession(), Calendar.getInstance().getTime(), "Logged In", LogType.ACCESS));
			
			if(page.equals("d")){
				EmployeeList list = (EmployeeList) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("employeeList");
				list.createEmployeeList();
				return "main?faces-redirect=true";
			} else {
				return "pm:meeting_view";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"���ͼ�����������ʼ�ҹ���١��ͧ",""));
			return null;
		}
		
	}
	
	private PersonalInfo authentication(String username, String password) {
		LDAPConnect connect = new LDAPConnect();
		LDAPUser ldapUser = connect.login(username, password);
		connect.disconnect();
		if(ldapUser != null){
			System.out.println("Getting user from database...");
			PersonalInfo dbUser = null;
			if(ldapUser.getEmployeeCode().equals("999999")){
				dbUser = BossUtil.getBoss();
			} else {
				dbUser = GeneralDAO.getUser(StringUtils.leftPad(ldapUser.getEmployeeCode(), 6, '0'));
			} 
			if(dbUser == null){
				return null;
			} else {
				return dbUser;
			}
		} else {
			return null;
		}
	}
	
	public String logout(){
		GeneralDAO.saveLog(new Log(null, SessionUtils.getUserFromSession(), Calendar.getInstance().getTime(), "Logged Out", LogType.ACCESS));		
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("userSession");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();	 
		return "main?faces-redirect=true";
	}
	
}
